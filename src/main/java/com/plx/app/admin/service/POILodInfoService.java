package com.plx.app.admin.service;

import com.plx.app.admin.vo.POILodTypeVO;

/**
 *  @Project KNIS
 *  @Class POILodInfoService
 *  @since 2019. 11. 11.
 *  @author 류중규
 *  @Description : POI LOD 정보
 */
public interface POILodInfoService {

	public int insertPoiLodInfo(POILodTypeVO pPOILodTypeVO) throws Exception;

	public int deletePoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception;

	public POILodTypeVO selectPoiLodInfo(POILodTypeVO pPOILodTypeVO) throws Exception;

	//public int updatePoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception;

	//public int updatePoiLodInfo(POILodInfoVO pPOILodInfoVO) throws Exception;



}
