package com.ocko.aventador.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.MemberSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *
 * @author hmsung
 *
 */
@Controller
public class MemberSettingController {

    @Autowired private MemberSettingService memberSettingService;
    @Autowired private AuthenticationService authenticationService;

    /**
     * 회원 설정 조회
     * @return
     */
    @RequestMapping(value = "/api/infinite/setting/get", method = RequestMethod.POST)
    public @ResponseBody
    String getMemberSetting(@RequestBody Map<String, String> param){
        return memberSettingService.getMemberSetting(param);
    }

    /**
     * 회원 설정 변경
     * @return
     */
    @RequestMapping(value = "/api/infinite/setting/set", method = RequestMethod.POST)
    public @ResponseBody
    String setMemberSetting(@RequestBody Map<String, String> param) throws JsonProcessingException {
        return memberSettingService.setMemberSetting(param);
    }

}
