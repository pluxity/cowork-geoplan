package com.plx.app.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POIIconsetVO;
import com.plx.app.cmn.vo.FileInfoVO;

/**
 *  @Project KNIS
 *  @Class MapInfoService
 *  @since 2019. 9. 15.
 *  @author 류중규
 *  @Description : 도면정보
 */
public interface POICategoryService {


	public Map<String, Object> selectPOIIconsetList(POIIconsetVO pPOIIconsetVO) throws Exception;

	public POIIconsetVO selectPOIIconsetDetail(POIIconsetVO pPOIIconsetVO) throws Exception;

	public int insertPOIIconset(POIIconsetVO pPOIIconsetVO) throws Exception;

	public int updatePOIIconset(POIIconsetVO pPOIIconsetVO, Map<String,FileInfoVO> fileVOMap) throws Exception;

	public int deletePOIIconset(POIIconsetVO pPOIIconsetVO) throws Exception;


	public List<POICategoryVO> selectPOICategoryList(POICategoryVO pPOICategoryVO) throws Exception;

	public List<POICategoryVO> selectPOICategory2List(POICategoryVO pPOICategoryVO) throws Exception;

	public POICategoryVO selectPOICategoryDetail(POICategoryVO pPOICategoryVO) throws Exception;

	public List<POICategoryVO> selectPOICategoryDetailList(POICategoryVO pPOICategoryVO) throws Exception;

	public int insertPOICategory(POICategoryVO pPOICategoryVO) throws Exception;

	public int updatePOICategory(POICategoryVO pPOICategoryVO) throws Exception;

	public Integer deletePOICategory(POICategoryVO pPOICategoryVO) throws Exception;

	public Integer poiCategorySwitch(POICategoryVO pPOICategoryVO) throws Exception;


	public int insertPOICategory2(POICategoryVO pPOICategoryVO) throws Exception;

	public int updatePOICategory2(POICategoryVO pPOICategoryVO) throws Exception;

	public int deletePOICategory2(POICategoryVO pPOICategoryVO) throws Exception;

	/**
	 * @Method poiCategoryTree
	 * @since  2019. 10. 3.
	 * @author 류중규
	 * @return List<LinkedHashMap<String,Object>>
	 * @return
	 * @throws Exception
	 * @description poi카테고리 트리
	 */
	public List<LinkedHashMap<String, Object>> poiCategoryTree() throws Exception;


}
