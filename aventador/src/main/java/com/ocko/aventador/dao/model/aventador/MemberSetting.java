package com.ocko.aventador.dao.model.aventador;

public class MemberSetting {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.member_setting.member_id
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	private Integer memberId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.member_setting.setting_detail
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	private String settingDetail;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.member_setting.api_key
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	private String apiKey;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.member_setting.member_id
	 * @return  the value of aventador.member_setting.member_id
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	public Integer getMemberId() {
		return memberId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.member_setting.member_id
	 * @param memberId  the value for aventador.member_setting.member_id
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.member_setting.setting_detail
	 * @return  the value of aventador.member_setting.setting_detail
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	public String getSettingDetail() {
		return settingDetail;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.member_setting.setting_detail
	 * @param settingDetail  the value for aventador.member_setting.setting_detail
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	public void setSettingDetail(String settingDetail) {
		this.settingDetail = settingDetail;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.member_setting.api_key
	 * @return  the value of aventador.member_setting.api_key
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.member_setting.api_key
	 * @param apiKey  the value for aventador.member_setting.api_key
	 * @mbg.generated  Tue Jan 04 16:25:38 KST 2022
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}