package com.plx.app.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POIInfoVO;

/**
 *  @Project KNIS
 *  @Class POIInfoService
 *  @since 2019. 10. 1.
 *  @author 류중규
 *  @Description : POI 정보
 */
public interface POIInfoService {

	public int insertPOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public Map<String, Object> selectPOIInfoList(POIInfoVO pPOIInfoVO) throws Exception;

	public POIInfoVO selectPOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public int updatePOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public int deletePOIInfo(POIInfoVO pPOIInfoVO) throws Exception;

	public Map<String, Object> insertExcelFile(MultipartFile[] excelFile, List<POICategoryVO> poiCategory1List, List<POICategoryVO> poiCategory2List, POIInfoVO pPOIInfoVO) throws Exception;

	public int updatePOIImgDelete (POIInfoVO pPOIInfoVO) throws Exception;
}
