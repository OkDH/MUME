package com.ocko.aventador.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.SimpleTokenComponent;
import com.ocko.aventador.dao.model.aventador.MemberSetting;
import com.ocko.aventador.dao.model.aventador.MemberSettingExample;
import com.ocko.aventador.dao.persistence.aventador.MemberSettingMapper;
import com.ocko.aventador.model.api.ResponseDto;

@Service
public class FcmTokenService {
	
	@Autowired private MemberSettingMapper memberSettingMapper;
	@Autowired private SimpleTokenComponent simpleTokenComponent;

	/**
	 * 회원 식별 토큰 발급
	 * @param memberId
	 * @return
	 */
	public String getCheckToken(Integer memberId) {
		
		MemberSetting memberSetting = memberSettingMapper.selectByPrimaryKey(memberId);
		
		// CheckToken 의 유효시간이 유효하다면 토큰 그대로 리턴
		if(memberSetting.getCheckTokenAvailable() != null) {
			if(LocalDateTime.now().isBefore(memberSetting.getCheckTokenAvailable()))
				return memberSetting.getCheckToken();
		}
		// 토큰 생성
		String checkToken = simpleTokenComponent.generatorToken();
		LocalDateTime availableTime = LocalDateTime.now().plusMinutes(10);
		
		MemberSetting updateSetting = new MemberSetting();
		updateSetting.setMemberId(memberId);
		updateSetting.setCheckToken(checkToken);
		updateSetting.setCheckTokenAvailable(availableTime);
		
		memberSettingMapper.updateByPrimaryKeySelective(updateSetting);
		
		return checkToken;
	}
	
	/**
	 * FCM 토큰 등록
	 */
	public ResponseDto updateFcmToken(Map<String, String> params) {
		ResponseDto response = new ResponseDto();
		if(params.get("checkToken") == null || params.get("fcmToken") == null) {
			response.setState("fail");
			response.setMessage("파라미터를 확인하세요.");
			return response;
		}
			
		MemberSettingExample example = new MemberSettingExample();
		example.createCriteria().andCheckTokenEqualTo(params.get("checkToken"));
		
		List<MemberSetting> settings = memberSettingMapper.selectByExample(example);
		
		if(settings.isEmpty()) {
			response.setState("fail");
			response.setMessage("유효하지 않은 토큰입니다.");
		}
		
		if(settings.get(0).getCheckTokenAvailable().isAfter(LocalDateTime.now())){
			response.setState("fail");
			response.setMessage("만료된 토큰입니다.");
		}
		
		MemberSetting updateSetting = new MemberSetting();
		updateSetting.setMemberId(settings.get(0).getMemberId());
		updateSetting.setFcmToken(params.get("fcmToken").toString());
		
		memberSettingMapper.updateByPrimaryKeySelective(updateSetting);
		
		response.setState("success");
		
		return response;
	}

}
