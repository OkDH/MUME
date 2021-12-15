package com.ocko.aventador.dao.persistence.aventador;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthly;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthlyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface ViewInfiniteProfitMonthlyMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	long countByExample(ViewInfiniteProfitMonthlyExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_monthly.
	 * @mbggenerated
	 */
	void truncate();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	int deleteByExample(ViewInfiniteProfitMonthlyExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_monthly.
	 * @mbggenerated
	 */
	int insertIgnore(ViewInfiniteProfitMonthly record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_monthly.
	 * @mbggenerated
	 */
	int upsert(ViewInfiniteProfitMonthly record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	int insert(ViewInfiniteProfitMonthly record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	int insertSelective(ViewInfiniteProfitMonthly record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_monthly.
	 * @mbggenerated
	 */
	Cursor<ViewInfiniteProfitMonthly> cursorByExample(ViewInfiniteProfitMonthlyExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_monthly.
	 * @mbggenerated
	 */
	List<List<ViewInfiniteProfitMonthly>> selectPksByExample(ViewInfiniteProfitMonthlyExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	List<ViewInfiniteProfitMonthly> selectByExample(ViewInfiniteProfitMonthlyExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	int updateByExampleSelective(@Param("record") ViewInfiniteProfitMonthly record,
			@Param("example") ViewInfiniteProfitMonthlyExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_monthly
	 * @mbg.generated  Wed Dec 15 13:59:06 KST 2021
	 */
	int updateByExample(@Param("record") ViewInfiniteProfitMonthly record,
			@Param("example") ViewInfiniteProfitMonthlyExample example);
}