/**
 * 
 */
package com.ocko.aventador.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.model.MemberDetail;
import com.ocko.aventador.model.api.ResponseDto;
import com.ocko.aventador.service.AuthenticationMobileService;
import com.ocko.aventador.service.AuthenticationService;

/**
 * @author ok
 *
 */
@Controller
public class ApiAuthController {
	
	@Autowired private AuthenticationService authenticationService;
	@Autowired private AuthenticationMobileService authMobileService;
	
	/**
	 * WEB
	 * 소셜 인증 성공 후 처리<br>
	 * 소셜 인증 사용자가 본 서비스 회원인지 체크하여<br>
	 * 회원이면 보안 context에 등록<br>
	 * 회원이 아니면 로그인 실패 처리
	 * @param authentication 
	 * @return
	 */
	@RequestMapping(value="/api/auth/check-social", method = { RequestMethod.GET, RequestMethod.POST } )
	public String getAuthCheckSocial(OAuth2AuthenticationToken authentication, HttpServletRequest request) {

		MemberInfo memberInfo = authenticationService.authenticateSocial(authentication);

		if(memberInfo == null) {
			return "redirect:/public/?auth=failed";
		}
		
		MemberDetail memberDetail = new MemberDetail(memberInfo);
		
		// AuthToken
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authToken);
		
		HttpSession session = request.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		
		return "redirect:/private";
	}
	
	/**
	 * 로그아웃
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/api/auth/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/public";
	}
	
	/**
	 * 모바일 앱애서 소셜로그인 성공이후 회원 체크 및 토큰 반환
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/auth/check-social-m", method = RequestMethod.POST)
	public ResponseEntity<Boolean> checkSocialMobile(@RequestBody Map<String, Object> params, HttpServletResponse response) {
		return new ResponseEntity<Boolean>(authMobileService.authenticateSocial(params, response), HttpStatus.OK);
	}
	
	/**
	 * 토큰 재발급
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/auth/refresh", method = RequestMethod.POST)
	public ResponseEntity<Boolean> refresh(HttpServletRequest request, HttpServletResponse response) {
		return new ResponseEntity<Boolean>(authMobileService.refresh(request, response), HttpStatus.OK);
	}
	
	
	/**
	 * 임시체크
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/auth/temp-check", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> temp(HttpServletRequest request) {
		return new ResponseEntity<Map<String, Object> >(authMobileService.temp(request), HttpStatus.OK);
	}
}
