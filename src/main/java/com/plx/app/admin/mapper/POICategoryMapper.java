package com.plx.app.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import com.plx.app.admin.vo.POICategoryVO;

/**
 *  @Project KNIS
 *  @Class POICategoryMapper
 *  @since 2019. 9. 23.
 *  @author newbie
 *  @Description : POI 카테고리
 */
public interface POICategoryMapper {

	public List<POICategoryVO> selectPOICategory1List(POICategoryVO pPOICategoryVO) throws Exception;

	public List<POICategoryVO> selectPOICategory2List(POICategoryVO pPOICategoryVO) throws Exception;

	public POICategoryVO selectPOICategory1Detail(POICategoryVO pPOICategoryVO) throws Exception;

	public POICategoryVO selectPOICategory2Detail(POICategoryVO pPOICategoryVO) throws Exception;

	public int insertPOICategory1(POICategoryVO pPOICategoryVO) throws Exception;

	public int updatePOICategory1(POICategoryVO pPOICategoryVO) throws Exception;

	public int deletePOICategory1(POICategoryVO pPOICategoryVO) throws Exception;

	public int insertPOICategory2(POICategoryVO pPOICategoryVO) throws Exception;

	public int updatePOICategory2(POICategoryVO pPOICategoryVO) throws Exception;

	public int deletePOICategory2(POICategoryVO pPOICategoryVO) throws Exception;

	public Integer swapPOICategoryOrderNo1 (Integer swap1, Integer swap2) throws Exception;

	public Integer swapPOICategoryOrderNo2 (Integer swap1, Integer swap2) throws Exception;

	public List<POICategoryVO> selectPOICategoryDetailList(POICategoryVO pPOICategoryVO) throws Exception;

	public List<LinkedHashMap<String, Object>> poiCategoryTree() throws Exception;


}
