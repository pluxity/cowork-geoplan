package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.POIInfoVO;

/**
 *  @Project KNIS
 *  @Class POIInfoMapper
 *  @since 2019. 10. 1.
 *  @author 류중규
 *  @Description : POI 정보
 */
public interface POIInfoMapper {

	public int insertPOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public List<POIInfoVO> selectPOIInfoList(POIInfoVO pPOIInfoVO) throws Exception;

	public int selectPOIInfoTotal(POIInfoVO pPOIInfoVO) throws Exception;

	public POIInfoVO selectPOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public int updatePOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public int deletePOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public int updatePOIImgDelete (POIInfoVO pPOIInfoVO) throws Exception;
	
}
