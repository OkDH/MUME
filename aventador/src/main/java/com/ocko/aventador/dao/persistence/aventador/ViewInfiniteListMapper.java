package com.ocko.aventador.dao.persistence.aventador;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

public interface ViewInfiniteListMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	long countByExample(ViewInfiniteListExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_list.
	 * @mbggenerated
	 */
	void truncate();

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	int deleteByExample(ViewInfiniteListExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_list.
	 * @mbggenerated
	 */
	int insertIgnore(ViewInfiniteList record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_list.
	 * @mbggenerated
	 */
	int upsert(ViewInfiniteList record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	int insert(ViewInfiniteList record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	int insertSelective(ViewInfiniteList record);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_list.
	 * @mbggenerated
	 */
	Cursor<ViewInfiniteList> cursorByExample(ViewInfiniteListExample example);

	/**
	 * This method was generated by MyBatis Generator Soov Plugins. This method corresponds to the database table aventador.view_infinite_list.
	 * @mbggenerated
	 */
	List<List<ViewInfiniteList>> selectPksByExample(ViewInfiniteListExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	List<ViewInfiniteList> selectByExample(ViewInfiniteListExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	int updateByExampleSelective(@Param("record") ViewInfiniteList record,
			@Param("example") ViewInfiniteListExample example);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:26:02 KST 2021
	 */
	int updateByExample(@Param("record") ViewInfiniteList record, @Param("example") ViewInfiniteListExample example);

	/**
	 * 무매 종목 ?��
	 * @param query
	 * @return
	 */
	Long countByInfinite(Map<String, Object> query);
	
	/**
	 * 무매 배정 ?��?�� 총합
	 * @param query
	 * @return
	 */
	BigDecimal sumByInfiniteSeed(Map<String, Object> query);
	
	/**
	 * 무매 매입금액 총합
	 * @param example
	 * @return
	 */
	BigDecimal sumByBuyPrice(Map<String, Object> query);
}