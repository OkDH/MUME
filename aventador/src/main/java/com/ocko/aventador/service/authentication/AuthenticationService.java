/**
 * 
 */
package com.ocko.aventador.service.authentication;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.JwtTokenComponent;
import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.dao.persistence.aventador.MemberInfoMapper;
import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;

/**
 * @author ok
 *	API 요청중인 사용자 처리 servcie
 */
@Service
public class AuthenticationService {
	
	@Autowired private AuthenticationWebService authWebService;
	@Autowired private MemberInfoMapper memberInfoMapper;
	@Autowired private JwtTokenComponent jwtTokenComponent;
	
	/**
	 * 사용자 검사
	 * 1. Header에 ACCESS_TOKEN이 있는지 체크
	 * 2. 있으면, 유효성 검사 후 유효하면 member_info 테이블에서 조회
	 * 3. 없으면, 웹에서 요청한 경우이니 세션에 있는 사용자 가져옴.
	 * @param request
	 * @return
	 */
	public MemberInfo getCurrentMember(HttpServletRequest request) {
		
		MemberInfo memberInfo = null;
		
		// Header에 있는 ACCESS_TOKEN 추출
		String accessToken = request.getHeader("ACCESS_TOKEN");
		
		// Header에 ACCESS_TOKEN이 있다면 휴효성 검사 
		if(accessToken != null) {
			Map<String, Object> claims = jwtTokenComponent.verifyAccessToken(accessToken);
			memberInfo = memberInfoMapper.selectByPrimaryKey(Integer.parseInt(claims.get("memberId").toString()));
		} else {
			memberInfo = authWebService.getCurrentMember();
		}
		
		if(memberInfo == null)
			throw new MyAccessDeniedException();
		
		return memberInfo;
	}

}
