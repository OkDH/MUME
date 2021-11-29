package com.ocko.aventador.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocko.aventador.constant.MemberStatus;
import com.ocko.aventador.constant.SocialType;
import com.ocko.aventador.dao.model.aventador.MemberAccount;
import com.ocko.aventador.dao.model.aventador.MemberAccountExample;
import com.ocko.aventador.dao.model.aventador.SocialAuthentication;
import com.ocko.aventador.dao.model.aventador.SocialAuthenticationExample;
import com.ocko.aventador.dao.persistence.aventador.MemberAccountMapper;
import com.ocko.aventador.dao.persistence.aventador.SocialAuthenticationMapper;
import com.ocko.aventador.model.MemberDetail;

@Service
public class AuthenticationService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Autowired private MemberAccountMapper memberAccountMapper;
	@Autowired private SocialAuthenticationMapper socialAuthenticationMapper;
	@Autowired private OAuth2AuthorizedClientService authorizedClientService;
	
	/**
	 * 
	 * @param username member_email
	 * @return 
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("userId: {}", username);

		MemberAccountExample example = new MemberAccountExample();
		example.createCriteria()
				.andMemberEmailEqualTo(username);
		List<MemberAccount> list = memberAccountMapper.selectByExample(example);
		if(list.size() == 0) {
			throw new UsernameNotFoundException("Member email is not found.");
		}
		
		MemberDetail memberDetail = new MemberDetail(list.get(0));
		
		if(!memberDetail.isEnabled()){
			// 미승인 상태 회원의 로그인 처리
			throw new UsernameNotFoundException("Member account is disabled.");
		}else if(!memberDetail.isAccountNonLocked()){
			// TODO 휴면 상태 회원의 로그인 처리
		}else if(!memberDetail.isAccountNonExpired()){
			// 탈퇴 회원의 로그인 처리
			throw new UsernameNotFoundException("Member account is expired.");
		}else if(!memberDetail.isCredentialsNonExpired()){
			// TODO 비밀번호 만료 회원의 로그인 처리
		}
		return memberDetail;
	}
	
	/**
	 * 현재 로그인된 회원 계정 정보를 반환
	 * @return 로그인 없으면 null
	 */
	public MemberAccount getCurrentMember() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if(principal instanceof MemberDetail) {
				MemberDetail member = (MemberDetail)principal;
				return member.getMemberAccount();
			}
		}
		return null;
	}
	
	/**
	 * 현재 회원의 로그아웃 처리
	 */
	public void logout() {
		SecurityContextHolder.clearContext();
	}
	
	/**
     * 소셜 회원 정보를 조회하기 위한 URL<br>
     * 소셜 종류에 따라 URL 구성 변경
     * @param site {@link SocialType}
     * @return
     */
    private String buildUrlForSocialMemberInfo(String site, String baseUrl){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        if(site.equals(SocialType.NAVER)){
            uriBuilder.queryParam("fields", "name,email");
        }
        
        return uriBuilder.toUriString();
    }
    
    private ObjectMapper jsonMapper = new ObjectMapper();
    
    
    private String toString(Object object) {
    	if(object == null)
    		return null;
    	return object.toString();
    }
    
    
    /**
	 * 소셜 인증 사용자가 현재 회원인지 여부를 체크<br>
	 * (1) 소셜 인증을 통한 가입 회원<br>
	 * (2) 실패
	 * @param authentication
	 * @return (1) 성공시 {@link MemberAccount}, (2) 실패시 <code>null</code>
	 */
	public MemberAccount authenticateSocial(OAuth2AuthenticationToken authentication) {
		// 1. 소셜 회원의 상세 정보를 조회
		OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName()
                );
		
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();
        
        if (StringUtils.isEmpty(userInfoEndpointUri)) {
        	return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
        
        HttpEntity<HttpHeaders> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String,Object>> response = restTemplate.exchange(
        		buildUrlForSocialMemberInfo(authentication.getAuthorizedClientRegistrationId(), userInfoEndpointUri), 
        		HttpMethod.GET, entity, 
        		new ParameterizedTypeReference<Map<String,Object>>() {});
        Map<String,Object> userAttributes = response.getBody();
        
        Map<String,String> mapMemberInfo = new HashMap<>();
        if(client.getClientRegistration().getRegistrationId().equals(SocialType.NAVER)){
        	@SuppressWarnings("unchecked")
			Map<String,String> map = (Map<String,String>)userAttributes.get("response");
        	mapMemberInfo.put("name", map.get("name"));
        	mapMemberInfo.put("email", map.get("email"));
        	mapMemberInfo.put("id", map.get("id"));
        	mapMemberInfo.put("social", SocialType.NAVER);
        	try {
				mapMemberInfo.put("etc", jsonMapper.writeValueAsString(map));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
        }
        
        MemberAccount memberAccount = null;
        // (1) 소셜 인증을 통한 가입 회원
        {
        	
        	SocialAuthenticationExample example = new SocialAuthenticationExample();
        	example.createCriteria()
        			.andSocialTypeEqualTo(client.getClientRegistration().getRegistrationId())
        			.andSocialIdEqualTo(String.valueOf(mapMemberInfo.get("id")));
        	if(socialAuthenticationMapper.countByExample(example) > 0) {
        		int memberId = socialAuthenticationMapper.selectByExample(example).get(0).getMemberId();
        		memberAccount = memberAccountMapper.selectByPrimaryKey(memberId);
        	} else {
        		// 회원가입
        		memberAccount = new MemberAccount();
        		memberAccount.setMemberName(mapMemberInfo.get("name"));
        		memberAccount.setMemberEmail(mapMemberInfo.get("email"));
        		memberAccount.setMemberRoles("ROLE_USER");
        		memberAccount.setMemberStatus(MemberStatus.ACTIVE.name());
        		memberAccount.setSubscriptionDate(LocalDateTime.now());
        		memberAccountMapper.insert(memberAccount);
        		
        		// 소셜 등록
        		SocialAuthentication socialAuthentication = new SocialAuthentication();
        		socialAuthentication.setMemberId(memberAccount.getMemberId());
        		socialAuthentication.setSocialType(client.getClientRegistration().getRegistrationId());
        		socialAuthentication.setSocialId(mapMemberInfo.get("id"));
        		socialAuthenticationMapper.insert(socialAuthentication);
        	}
        }
        
        return memberAccount;
	}

}
