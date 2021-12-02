/**
 * 
 */
package com.ocko.aventador.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.model.MemberDetail;
import com.ocko.aventador.service.AuthenticationService;

/**
 * @author ok
 *
 */
@Controller
public class ApiAuthController {
	
	@Autowired private AuthenticationService authenticationService;
	
	/**
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
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authToken);
		
		HttpSession session = request.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
		
		return "redirect:/private";
	}
	
	@RequestMapping(value="/api/auth/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/public";
	}
}
