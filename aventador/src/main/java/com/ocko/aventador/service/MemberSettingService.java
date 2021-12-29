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