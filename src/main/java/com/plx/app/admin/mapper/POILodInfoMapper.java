package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.POILodInfoVO;
import com.plx.app.admin.vo.POILodTypeVO;

/**
 *  @Project KNIS
 *  @Class POILodInfoMapper
 *  @since 2019. 11. 11.
 *  @author 류중규
 *  @Description : POI LOD 정보
 */
public interface POILodInfoMapper {

	public int insertPoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception;

	//public int updatePoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception;

	public int deletePoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception;

	public POILodTypeVO selectPoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception;

	public int insertPoiLodInfo(POILodInfoVO pPOILodInfoVO) throws Exception;

	//public int updatePoiLodInfo(POILodInfoVO pPOILodInfoVO) throws Exception;

	public List<POILodInfoVO> selectPoiLodInfoList(POILodInfoVO pPOILodInfoVO) throws Exception;

}
