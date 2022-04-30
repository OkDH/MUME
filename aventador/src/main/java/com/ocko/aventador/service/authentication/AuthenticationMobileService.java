package com.ocko.aventador.service.authentication;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.JwtTokenComponent;
import com.ocko.aventador.constant.MemberStatus;
import com.ocko.aventador.constant.SocialType;
import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.dao.model.aventador.MemberSetting;
import com.ocko.aventador.dao.model.aventador.SocialAuthentication;
import com.ocko.aventador.dao.model.aventador.SocialAuthenticationExample;
import com.ocko.aventador.dao.persistence.aventador.MemberInfoMapper;
import com.ocko.aventador.dao.persistence.aventador.MemberSettingMapper;
import com.ocko.aventador.dao.persistence.aventador.SocialAuthenticationMapper;
import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.exception.AccessTokenExpiredException;


@Service
public class AuthenticationMobileService {

	@Autowired private MemberInfoMapper memberInfoMapper;
	@Autowired private MemberSettingMapper memberSettingMapper;
	@Autowired private SocialAuthenticationMapper socialAuthMapper;
	@Autowired private JwtTokenComponent jwtTokenComponent;
	
	/**
	 * 소셜 회원 체크 및 토큰 반환
	 * @param params social_type 소셜 타입, social_id 소셜 사용자 유니크 키
	 * @return
	 */
	public Boolean authenticateSocial(Map<String, Object> params, HttpServletResponse response) {
		if(params.get("socialType") == null || params.get("socialId") == null)
			throw new MyArgumentException();
		
		String socialType = params.get("socialType").toString();
		String socialId = params.get("socialId").toString();
		
		switch (socialType) {
		case SocialType.NAVER:
		case SocialType.GOOGLE:
		case SocialType.APPLE:
			break;
		default:
			return false;
		}
		
		SocialAuthenticationExample example = new SocialAuthenticationExample();
		example.createCriteria()
			.andSocialTypeEqualTo(socialType)
			.andSocialIdEqualTo(socialId);
		
		List<SocialAuthentication> list = socialAuthMapper.selectByExample(example);
		
		MemberInfo memberInfo = null;
		MemberSetting memberSetting = null;
		if(list.isEmpty()) { // 조회되지 않으면 회원가입
			// 회원가입
			memberInfo = new MemberInfo();
    		memberInfo.setMemberRoles("ROLE_USER");
    		memberInfo.setMemberStatus(MemberStatus.ACTIVE.name());
    		memberInfo.setSubscriptionDate(LocalDateTime.now());
    		memberInfoMapper.insert(memberInfo);
    		
    		// 소셜 등록
    		SocialAuthentication socialAuthentication = new SocialAuthentication();
    		socialAuthentication.setMemberId(memberInfo.getMemberId());
    		socialAuthentication.setSocialType(socialType);
    		socialAuthentication.setSocialId(socialId);
    		socialAuthMapper.insert(socialAuthentication);
    		
    		// 회원 설정
    		memberSetting = new MemberSetting();
    		memberSetting.setMemberId(memberInfo.getMemberId());
    		memberSettingMapper.insert(memberSetting);
		} else {
			memberInfo = memberInfoMapper.selectByPrimaryKey(list.get(0).getMemberId());
			memberSetting = memberSettingMapper.selectByPrimaryKey(list.get(0).getMemberId());
		}
		
		// 마지막 로그인 일자 기록
    	memberInfo.setLastLoginDate(LocalDateTime.now());
    	memberInfoMapper.updateByPrimaryKey(memberInfo);

		// 토큰 발급
		String accessToken = jwtTokenComponent.createAccessToken(memberInfo.getMemberId());
		String refreshToken = jwtTokenComponent.createRefreshToken(memberInfo.getMemberId());
		
		memberSetting.setRefreshToken(refreshToken);
		memberSettingMapper.updateByPrimaryKeySelective(memberSetting);
		
		response.setHeader("ACCESS_TOKEN", accessToken);
		response.setHeader("REFRESH_TOKEN", refreshToken);
		
		return true;
	}
	
	/**
	 * 토큰 refresh
	 * @param params
	 * @return
	 */
	public Boolean refresh(HttpServletRequest request, HttpServletResponse response) {
		if(request.getHeader("REFRESH_TOKEN") == null)
			throw new MyArgumentException();
		
		Map<String, Object> claims = jwtTokenComponent.verifyRefreshToken(request.getHeader("REFRESH_TOKEN"));
		
		Integer memberId = (Integer) claims.get("memberId");
		MemberSetting memberSetting = memberSettingMapper.selectByPrimaryKey(memberId);
		
		if(memberSetting != null && memberSetting.getRefreshToken() != null 
				&& memberSetting.getRefreshToken().equals(request.getHeader("REFRESH_TOKEN"))) {
			
			// 토큰 발급
			String accessToken = jwtTokenComponent.createAccessToken(memberId);
			String refreshToken = jwtTokenComponent.createRefreshToken(memberId);
			
			memberSetting.setRefreshToken(refreshToken);
			memberSettingMapper.updateByPrimaryKeySelective(memberSetting);
			
			response.setHeader("ACCESS_TOKEN", accessToken);
			response.setHeader("REFRESH_TOKEN", refreshToken);	 
			
			return true;
		}
		
		return false;
	}
	
	public Map<String, Object> temp(HttpServletRequest request) {
		String accessToken = request.getHeader("ACCESS_TOKEN");
		return jwtTokenComponent.verifyAccessToken(accessToken);
	}

}
