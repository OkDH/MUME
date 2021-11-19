package com.ocko.aventador.dao.persistence.aventador;

import com.ocko.aventador.dao.model.aventador.MemberAccount;
import java.util.List;
import com.ocko.aventador.dao.model.aventador.MemberAccountExample;
import org.apache.ibatis.annotations.Param;

public interface MemberAccountMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	long countByExample(MemberAccountExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int deleteByExample(MemberAccountExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int deleteByPrimaryKey(Integer memberId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int insert(MemberAccount record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int insertSelective(MemberAccount record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	List<MemberAccount> selectByExample(MemberAccountExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	MemberAccount selectByPrimaryKey(Integer memberId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int updateByExampleSelective(@Param("record") MemberAccount record, @Param("example") MemberAccountExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int updateByExample(@Param("record") MemberAccount record, @Param("example") MemberAccountExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int updateByPrimaryKeySelective(MemberAccount record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.member_account
	 * @mbg.generated  Fri Nov 19 23:46:31 KST 2021
	 */
	int updateByPrimaryKey(MemberAccount record);
}