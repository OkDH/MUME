package com.ocko.aventador.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocko.aventador.dao.model.aventador.MemberSetting;
import com.ocko.aventador.dao.model.aventador.MemberSettingExample;
import com.ocko.aventador.dao.persistence.aventador.MemberSettingMapper;
import com.ocko.aventador.model.setting.CommonSetting;
import com.ocko.aventador.model.setting.InfiniteAccountSetting;
import com.ocko.aventador.model.setting.InfiniteSetting;
import com.ocko.aventador.model.setting.MemberSettingDetail;

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
     * TODO : 설정 관련 작업 안됨
     * 사용자 설정 초기값 만들기(회원가입 시 호출)
     * @param memberId
     * @return
     */
    public MemberSettingDetail getInitMemberSetting() {
    	MemberSettingDetail settingDetail = new MemberSettingDetail();
    	// TODO : 디폴트 설정값들이 있다면 추가 할 것
    	
    	// 기본설정
    	CommonSetting commonSetting = new CommonSetting();
    	settingDetail.setCommonSetting(commonSetting);
    	
    	// 무한매수 설정
    	InfiniteSetting infiniteSetting = new InfiniteSetting();
    	settingDetail.setInfiniteSetting(infiniteSetting);
    	return settingDetail;
    }
    
    /**
     * 회원 설정 조회
     * @param memberId
     * @return
     */
    public MemberSetting getMemberSetting(Integer memberId){
        return memberSettingMapper.selectByPrimaryKey(memberId);
    }

    /**
     * 회원 설정 변경
     * @param param
     * @return
     */
    public String setMemberSetting(Map<String, String> params) throws JsonProcessingException {
        MemberSettingExample example = new MemberSettingExample();
        example.createCriteria().andMemberIdEqualTo(Integer.parseInt(params.get("memberId").toString()));
        MemberSetting memberSetting = (MemberSetting) memberSettingMapper.selectByExample(example);
        if(memberSetting != null) {
            ObjectMapper om = new ObjectMapper();
            MemberSettingDetail memberSettingDetail = om.readValue(memberSetting.getSettingDetail(), MemberSettingDetail.class);

            /*accountId가 존재할 경우*/
//            if (memberSettingDetail.getInfinitySetting().getAccountSettingList().containsKey(param.get("accountId"))) {
//
//            } else {
//
//            }
        return om.writeValueAsString(memberSettingDetail);
        }
        return null;
    }
}