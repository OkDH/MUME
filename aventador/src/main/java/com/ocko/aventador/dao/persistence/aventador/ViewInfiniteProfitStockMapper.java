package com.ocko.aventador.dao.persistence.aventador;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitStock;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitStockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface ViewInfiniteProfitStockMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	long countByExample(ViewInfiniteProfitStockExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_stock.
	 * @mbggenerated
	 */
	void truncate();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	int deleteByExample(ViewInfiniteProfitStockExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_stock.
	 * @mbggenerated
	 */
	int insertIgnore(ViewInfiniteProfitStock record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_stock.
	 * @mbggenerated
	 */
	int upsert(ViewInfiniteProfitStock record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	int insert(ViewInfiniteProfitStock record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	int insertSelective(ViewInfiniteProfitStock record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_stock.
	 * @mbggenerated
	 */
	Cursor<ViewInfiniteProfitStock> cursorByExample(ViewInfiniteProfitStockExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_profit_stock.
	 * @mbggenerated
	 */
	List<List<ViewInfiniteProfitStock>> selectPksByExample(ViewInfiniteProfitStockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	List<ViewInfiniteProfitStock> selectByExample(ViewInfiniteProfitStockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	int updateByExampleSelective(@Param("record") ViewInfiniteProfitStock record,
			@Param("example") ViewInfiniteProfitStockExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_profit_stock
	 * @mbg.generated  Thu Mar 31 17:04:40 KST 2022
	 */
	int updateByExample(@Param("record") ViewInfiniteProfitStock record,
			@Param("example") ViewInfiniteProfitStockExample example);

	List<ViewInfiniteProfitStock> selectByExampleAll(ViewInfiniteProfitStockExample example);
}