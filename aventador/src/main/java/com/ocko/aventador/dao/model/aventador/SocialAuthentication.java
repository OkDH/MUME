package com.ocko.aventador.dao.model.aventador;

public class SocialAuthentication {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.social_authentication.id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.social_authentication.member_id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	private Integer memberId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.social_authentication.social_type
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	private String socialType;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.social_authentication.social_id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	private String socialId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column aventador.social_authentication.social_info
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	private Object socialInfo;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.social_authentication.id
	 * @return  the value of aventador.social_authentication.id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.social_authentication.id
	 * @param id  the value for aventador.social_authentication.id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.social_authentication.member_id
	 * @return  the value of aventador.social_authentication.member_id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public Integer getMemberId() {
		return memberId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.social_authentication.member_id
	 * @param memberId  the value for aventador.social_authentication.member_id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.social_authentication.social_type
	 * @return  the value of aventador.social_authentication.social_type
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public String getSocialType() {
		return socialType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.social_authentication.social_type
	 * @param socialType  the value for aventador.social_authentication.social_type
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.social_authentication.social_id
	 * @return  the value of aventador.social_authentication.social_id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public String getSocialId() {
		return socialId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.social_authentication.social_id
	 * @param socialId  the value for aventador.social_authentication.social_id
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column aventador.social_authentication.social_info
	 * @return  the value of aventador.social_authentication.social_info
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public Object getSocialInfo() {
		return socialInfo;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column aventador.social_authentication.social_info
	 * @param socialInfo  the value for aventador.social_authentication.social_info
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	public void setSocialInfo(Object socialInfo) {
		this.socialInfo = socialInfo;
	}
}