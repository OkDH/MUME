package com.ocko.aventador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.SimpleTokenComponent;
import com.ocko.aventador.dao.model.aventador.MemberSetting;
import com.ocko.aventador.dao.persistence.aventador.MemberSettingMapper;

@Service
public class ApiService {

	@Autowired private MemberSettingMapper memberSettingMapper;
	@Autowired private SimpleTokenComponent simpleTokenComponent;
	
	/**
	 * 지니 프로그램 연동 API key 가져오기
	 * @param memberId
	 * @return
	 */
	public String getApiKey(Integer memberId) {
		MemberSetting memberSetting = memberSettingMapper.selectByPrimaryKey(memberId);
		
		if(memberSetting.getApiKey() != null)
			return memberSetting.getApiKey();
		
		String apiKey = simpleTokenComponent.generatorToken(memberId);
		
		MemberSetting updateSetting = new MemberSetting();
		updateSetting.setMemberId(memberId);
		updateSetting.setApiKey(apiKey);
		memberSettingMapper.updateByPrimaryKeySelective(updateSetting);
		
		return apiKey;
	}
}
