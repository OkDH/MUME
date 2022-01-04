package com.ocko.aventador.model.setting;

/**
 * 최종 설정 모델
 * @author ok
 *
 */
public class MemberSettingDetail {

    private InfiniteSetting infiniteSetting;
    private CommonSetting commonSetting;
    
	/**
	 * @return {@link #infiniteSetting}
	 */
	public InfiniteSetting getInfiniteSetting() {
		return infiniteSetting;
	}
	/**
	 * @param infiniteSetting {@link #infiniteSetting}
	 */
	public void setInfiniteSetting(InfiniteSetting infiniteSetting) {
		this.infiniteSetting = infiniteSetting;
	}
	/**
	 * @return {@link #commonSetting}
	 */
	public CommonSetting getCommonSetting() {
		return commonSetting;
	}
	/**
	 * @param commonSetting {@link #commonSetting}
	 */
	public void setCommonSetting(CommonSetting commonSetting) {
		this.commonSetting = commonSetting;
	}

}
