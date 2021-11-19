package com.ocko.aventador.config;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import com.ocko.aventador.constant.SocialType;

public enum CustomOAuth2Provider {
	KAKAO {
		@Override 
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL);
			builder.scope("profile");
			builder.authorizationUri("https://kauth.kakao.com/oauth/authorize");
			builder.tokenUri("https://kauth.kakao.com/oauth/token");
			builder.userInfoUri("https://kapi.kakao.com/v2/user/me");
			builder.userNameAttributeName("id");
			builder.clientName(SocialType.KAKAO);
			return builder;
		}
	},
	NAVER {
		@Override
		public Builder getBuilder(String registrationId) {
			ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.POST, DEFAULT_REDIRECT_URL);
			builder.scope("profile");
			builder.authorizationUri("https://nid.naver.com/oauth2.0/authorize");
			builder.tokenUri("https://nid.naver.com/oauth2.0/token");
			builder.userInfoUri("https://openapi.naver.com/v1/nid/me");
			builder.userNameAttributeName("response");
			builder.clientName(SocialType.NAVER);
			return builder;
		}
	};
	
	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/api/auth/oauth2/code/{registrationId}";
	
	protected final ClientRegistration.Builder getBuilder(String registrationId,
			ClientAuthenticationMethod method, String redirectUri) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUriTemplate(redirectUri);
		return builder;
	}
	
	public abstract ClientRegistration.Builder getBuilder(String registrationId);
}
