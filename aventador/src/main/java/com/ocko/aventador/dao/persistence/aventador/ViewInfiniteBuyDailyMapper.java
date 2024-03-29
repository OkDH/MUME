package com.ocko.aventador.dao.persistence.aventador;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyDaily;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyDailyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface ViewInfiniteBuyDailyMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	long countByExample(ViewInfiniteBuyDailyExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_buy_daily.
	 * @mbggenerated
	 */
	void truncate();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	int deleteByExample(ViewInfiniteBuyDailyExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_buy_daily.
	 * @mbggenerated
	 */
	int insertIgnore(ViewInfiniteBuyDaily record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_buy_daily.
	 * @mbggenerated
	 */
	int upsert(ViewInfiniteBuyDaily record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	int insert(ViewInfiniteBuyDaily record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	int insertSelective(ViewInfiniteBuyDaily record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_buy_daily.
	 * @mbggenerated
	 */
	Cursor<ViewInfiniteBuyDaily> cursorByExample(ViewInfiniteBuyDailyExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_buy_daily.
	 * @mbggenerated
	 */
	List<List<ViewInfiniteBuyDaily>> selectPksByExample(ViewInfiniteBuyDailyExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	List<ViewInfiniteBuyDaily> selectByExample(ViewInfiniteBuyDailyExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	int updateByExampleSelective(@Param("record") ViewInfiniteBuyDaily record,
			@Param("example") ViewInfiniteBuyDailyExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_buy_daily
	 * @mbg.generated  Tue Feb 08 21:20:29 KST 2022
	 */
	int updateByExample(@Param("record") ViewInfiniteBuyDaily record,
			@Param("example") ViewInfiniteBuyDailyExample example);
}