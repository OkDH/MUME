package com.ocko.aventador.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.ocko.aventador.constant.SocialType;
import com.ocko.aventador.service.AuthenticationService;

/**
 * spring security 설정<br>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired private AuthenticationService authenticationService;
	
	@Autowired private AuthFailureHandler authFailureHandler;
	
	public SecurityConfig(
			@Value("${spring.security.oauth2.client.registration.NAVER.client-id}") String naverClientId,
			@Value("${spring.security.oauth2.client.registration.NAVER.client-secret}") String naverClientSecret
			) {
		this.naverClientId = naverClientId;
		this.naverClientSecret = naverClientSecret;
	}
	
	private String naverClientId;
	private String naverClientSecret;
	
	/**
	 * CSRF 토큰 저장소 빈
	 * @return
	 */
	@Bean
	public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
		CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
		cookieCsrfTokenRepository.setCookieHttpOnly(false);
		return cookieCsrfTokenRepository;
	}
	
	/**
	 * 비밀번호 인코더 빈
	 * @return
	 */
	@Bean 
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	/**
	 * 보안 문맥 구성
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// by default, spring adds "ROLE_" prefix, so the role in the repository must start with "ROLE_".
		http.authorizeRequests()
				.antMatchers(
						"/admin",
						"/admin/**",
						"/api/admin/**"
						).hasAnyRole("ADMIN")
				.antMatchers(
						"/private",
						"/private/**",
						"/api/private/**"
						).access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
				.antMatchers(
						"/public",
						"/public/**",
						"/api/public/**",
						"/api/auth/**",
						"/api/auth/oauth2/**", // social redirect
						"/tpl/**"
						).permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/**").permitAll()
				.and()
				.formLogin()
				.loginPage("/public/#!/login") // 지정해야 custom page 사용 가능, 미인증 사용자 접근시 redirect target
				.and()
			.oauth2Login()
				.loginPage("/public/#!/login") // 지정해야 custom page 사용 가능, 미인증 사용자 접근시 redirect target
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						redirectStrategy.sendRedirect(request, response, "/api/auth/check-social");
					}
				})		
				.failureUrl("/public/?auth=failed")
				.clientRegistrationRepository(clientRegistrationRepository())
				.authorizedClientService(authorizedClientService())
				.authorizationEndpoint()
					.baseUri("/api/auth/oauth2/authorization")
					.authorizationRequestRepository(authorizationRequestRepository())
					.and()
				.redirectionEndpoint()
					.baseUri("/api/auth/oauth2/code/*") // /login/oauth2/code/*
					.and()
				.and()
			.logout()
				.logoutUrl("/api/auth/logout")
				.logoutSuccessUrl("/public")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.and()
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers("/api/public/**");
			 
	}
	
	/**
	 * 기본 인증 구성
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService)
			.passwordEncoder(passwordEncoder());
//		super.configure(auth);
	}
	
	/**
	 * 소셜 인증(OAuth2) 클라이언트<br>
	 * Naver와 Facebook을 구현함
	 * @return
	 */
	@Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> clientRegistrations = new ArrayList<>();
		
		{
			clientRegistrations.add(
				CustomOAuth2Provider.NAVER.getBuilder(SocialType.NAVER)
        		.clientId(naverClientId)
        		.clientSecret(naverClientSecret)
        		.build()
        		);
		}
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }
	
	
	/**
	 * OAuth2 인증 서비스 빈
	 * @return
	 */
	@Bean
    public OAuth2AuthorizedClientService authorizedClientService(){
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}

}
