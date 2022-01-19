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

	/**
	 * FCM 토큰 등록
	 */
	public Boolean updateFcmToken(Integer memberId, Map<String, Object> params) {
		if(params.get("fcmToken") == null)
			return false;
		
		MemberSetting updateSetting = new MemberSetting();
		updateSetting.setMemberId(memberId);
		updateSetting.setFcmToken(params.get("fcmToken").toString());
		
		memberSettingMapper.updateByPrimaryKeySelective(updateSetting);
		
		return true;
	}

}
