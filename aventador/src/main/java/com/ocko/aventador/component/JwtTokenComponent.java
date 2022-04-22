package com.ocko.aventador.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenComponent {
	
	/*
	 *  JWT(JSon Web Token)란 JSon 포맷을 이용하여 사용자에 대한 속성을 지정하는 Claim 기반의 Web Token이다.
	 *  JWT는 토큰 자체를 정보로 사용하는 Self-Contained 방식으로 정보를 안전하게 전달한다.
	 */

	/**
	 * 토큰 생성
	 * @return
	 */
	public String createToken() {
		/*
		 * HEADER 부분 설정
		 * 헤더는 typ와 alg 두 가지 정보로 구성된다.
		 * alg는 헤더를 암호화 하는 것이 아니고, Signature를 해싱하기 위한 알고리즘을 지정하는 것이다.
		 * typ: 토큰의 타입을 지정
		 * alg: 알고리즘 방식을 지정하며, 서명(Signature) 및 토큰 검증에 사용
		 */
		Map<String, Object> headers = new HashMap<String, Object>();
		// Type 설정
		headers.put("typ", "JWT");
		// 알고리즘 설정
		headers.put("alg", "HS256");
		
		/*
		 * PAYLOAD 부분 설정
		 * 토큰의 페이로드에는 토큰에서 사용할 정보의 조각들인 클레임(Claim)이 담겨있다.
		 * 클레임은 총 3가지로 나누어지며,
		 * JSON 형태로 다수의 정보를 넣을 수 있다.
		 * Header와 Payload에는 기밀정보는 넣으면 안된다.
		 */
		Map<String, Object> payloads = new HashMap<String, Object>();
		payloads.put("apiKey", "abcdefghi");
		
		// 토큰 유효 시간(2시간)
		Long expiredTime = 1000 * 60L * 60L * 2L;
		
		// 토큰 만료 시간
		Date ext = new Date();
		ext.setTime(ext.getTime() + expiredTime);
		
		/*
		 * 토큰 Builder
		 * 
		 * 등록된 클레임은 토큰 정보를 표현하기 위해 이미 정해진 종류의 데이터들로,
		 * 모두 선택적으로 작성이 가능하며 사용할 것을 권장한다.
		 * 또한 JWT를 간결하기 위해 key는 모두 길이 3의 String이다.
		 * 여기서 subject로는 unique 한 값을 사용한다.
		 * 
		 * iss: 토킅 발급자(issuer)
		 * sub: 토큰 제목(subject)
		 * aud: 토큰 대상자(audience)
		 * ext: 토큰 만료 시간(expiration), NumbericDate 형식으로 되어 있어야 함 ex) 1480849147370
		 * nbf: 토큰 활성 날짜(not before), 이 날이 지나기 전의 토큰은 활성화되지 않음
		 * jat: 토큰 발급 시간(issued at), 토큰 발급 이후의 경과 시간을 알 수 있음
		 * jti: JWT 토큰 식별자(JWT ID), 중복 방지를 위해 사용하며, 일회용 토큰(Access Token)등에 사용
		 * 
		 * Signature(서명)
		 * 서명은 토큰을 인코딩하거나 유효성 검증을 할 때 사용하는 고유한 암호화 코드이다.
		 * 서명은 위에서 만든 헤더와 페이로드의 값을 각각 Base64로 인코딩하고,
		 * 인코딩한 값을 비밀 키를 이용해 헤더에서 정의한 알고리즘으로 해싱을 하고,
		 * 이 값을 다시 Base64로 인코딩하여 생성한다.
		 */
		
		String jwt = Jwts.builder()
				.setHeader(headers)
				.setClaims(payloads)
				.setSubject("user-auth")
				.setExpiration(ext)
				.signWith(SignatureAlgorithm.HS256, "secretKey".getBytes())
				.compact();
		
		return jwt;
	}
	
	public Map<String, Object> verifyJwt(String accessToken){
		Map<String, Object> claimMap = null;
		
		try {
			Claims claims = Jwts.parser()
					.setSigningKey("secretKey".getBytes("UTF-8"))
					.parseClaimsJws(accessToken)
					.getBody();
			
			claimMap = claims;
		} catch (ExpiredJwtException e) {
			System.out.println("토큰 만료");
			System.out.println(e);
		} catch (Exception e) {
			System.out.println("기타 오류");
			System.out.println(e);
		}
		
		return claimMap;
	}
}
