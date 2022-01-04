/**
 * 
 */
package com.ocko.aventador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ocko.aventador.model.setting.MemberSettingDetail;
import com.ocko.aventador.service.MemberSettingService;

/**
 * @author ok
 *
 */
@SpringBootTest
public class MemberSettingTest {
	
	@Autowired
	private MemberSettingService settingService;
	
	@Test
	public void setMemberSetting() {
		MemberSettingDetail setting = settingService.getInitMemberSetting();
		settingService.upsertMemberSetting(1, setting);
	}

}
