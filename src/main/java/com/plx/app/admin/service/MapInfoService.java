package com.plx.app.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plx.app.admin.vo.MapHstVO;
import com.plx.app.admin.vo.MapInfoVO;

/**
 *  @Project KNIS
 *  @Class MapInfoService
 *  @since 2019. 9. 15.
 *  @author 류중규
 *  @Description : 도면정보
 */
public interface MapInfoService {

	/**
	 * @Method insertMapInfo
	 * @since  2019. 9. 15.
	 * @author 류중규
	 * @return int
	 * @param pMapInfoVO
	 * @return
	 * @throws Exception
	 * @description 도면정보 등록
	 */
	public int insertMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	/**
	 * @Method updateMapInfo
	 * @since  2019. 9. 17.
	 * @author 류중규
	 * @return int
	 * @param pMapInfoVO
	 * @return
	 * @throws Exception
	 * @description 도면정보 수정
	 */
	public int updateMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	/**
	 * @Method selectMapInfo
	 * @since  2019. 9. 18.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pMapInfoVO
	 * @return
	 * @throws Exception
	 * @description 도면목록
	 */
	public Map<String, Object> selectMapInfoList(MapInfoVO pMapInfoVO) throws Exception;

	/**
	 * @Method insertMapHst
	 * @since  2019. 9. 15.
	 * @author 류중규
	 * @return int
	 * @param pMapHstVO
	 * @return
	 * @throws Exception
	 * @description 도면이력 등록
	 */
	public int insertMapHst(MapHstVO pMapHstVO) throws Exception;

	/**
	 * @Method mapFileUpload
	 * @since  2019. 9. 15.
	 * @author 류중규
	 * @return int
	 * @param pMapInfoVO
	 * @param request
	 * @param pMapHstVO
	 * @return
	 * @throws Exception
	 * @description 도면파일 업로드 및 등록
	 */
	public int mapFileUpload(MapInfoVO pMapInfoVO, MultipartHttpServletRequest request, MapHstVO pMapHstVO) throws Exception;

	/**
	 * @Method selectMapInfo
	 * @since  2019. 9. 20.
	 * @author 류중규
	 * @return MapInfoVO
	 * @param pMapInfoVO
	 * @return
	 * @throws Exception
	 * @description 도면 정보
	 */
	public MapInfoVO selectMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	/**
	 * @Method deleteMapInfo
	 * @since  2019. 9. 23.
	 * @author 류중규
	 * @return int
	 * @param pMapInfoVO
	 * @return
	 * @throws Exception
	 * @description 도면 삭제
	 */
	public int deleteMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	/**
	 * @Method selectMapHstList
	 * @since  2019. 9. 22.
	 * @author 류중규
	 * @return List<MapHstVO>
	 * @param pMapHstVO
	 * @return
	 * @throws Exception
	 * @description 도면 등록 이력
	 */
	public List<MapHstVO> selectMapHstList(MapHstVO pMapHstVO) throws Exception;

	/**
	 * @Method selectMapHst
	 * @since  2019. 9. 22.
	 * @author 류중규
	 * @return MapHstVO
	 * @param pMapHstVo
	 * @return
	 * @throws Exception
	 * @description 도면등록 이력 정보
	 */
	public MapHstVO selectMapHst(MapHstVO pMapHstVo) throws Exception;

	/**
	 * @Method deleteMapHst
	 * @since  2019. 9. 22.
	 * @author 류중규
	 * @return int
	 * @param pMapHstVo
	 * @return
	 * @throws Exception
	 * @description 도면 이력 삭제
	 */
	public int deleteMapHst(MapHstVO pMapHstVO) throws Exception;

}
