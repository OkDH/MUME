package com.ocko.aventador.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocko.aventador.dao.model.aventador.MemberSetting;
import com.ocko.aventador.dao.model.aventador.MemberSettingExample;
import com.ocko.aventador.dao.persistence.aventador.MemberSettingMapper;
import com.ocko.aventador.model.MemberSettingDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author hmsung
 *
 */
@Service
public class MemberSettingService {

    @Autowired
    private MemberSettingMapper memberSettingMapper;
    
    /**
     * 사용자 설정 upsert
     * @param memberId
     * @param settingDetail
     */
    public void upsertMemberSetting(Integer memberId, MemberSettingDetail settingDetail) {
    	if(memberId == null || settingDetail == null)
    		return;
    	
        ObjectMapper om = new ObjectMapper();
        try {
			String settingJson = om.writeValueAsString(settingDetail);
			
			MemberSetting memberSetting = new MemberSetting();
			memberSetting.setMemberId(memberId);
			memberSetting.setSettingDetail(settingJson);
			
			memberSettingMapper.upsert(memberSetting);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 사용자 설정 초기값 만들기(회원가입 시 호출)
     * @param memberId
     * @return
     */
    public MemberSettingDetail getInitMemberSetting(Integer memberId) {
    	MemberSettingDetail settingDetail = new MemberSettingDetail();
    	
    	return null;
    }

    /**
     * 회원 설정 조회
     * @param param
     * @return
     */
    public String getMemberSetting(Map<String, String> param){
        MemberSettingExample example = new MemberSettingExample();
        example.createCriteria().andMemberIdEqualTo(param.get("memberId"));
        MemberSetting memberSetting = (MemberSetting) memberSettingMapper.selectByExample(example);
        return null;
    }

    /**
     * 회원 설정 변경
     * @param param
     * @return
     */
    public String setMemberSetting(Map<String, String> param) throws JsonProcessingException {
        MemberSettingExample example = new MemberSettingExample();
        example.createCriteria().andMemberIdEqualTo(param.get("memberId"));
        MemberSetting memberSetting = (MemberSetting) memberSettingMapper.selectByExample(example);
        if(memberSetting != null) {
            ObjectMapper om = new ObjectMapper();
            MemberSettingDetail memberSettingDetail = om.readValue(memberSetting.getSettingDetail(), MemberSettingDetail.class);

            /*accountId가 존재할 경우*/
            if (memberSettingDetail.getInfinitySetting().getAccountSettingList().containsKey(param.get("accountId"))) {

            } else {

            }
        return om.writeValueAsString(memberSettingDetail);
        }
        return null;
    }
}