package com.plx.app.viewer.service;

import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.viewer.vo.AlarmRequestDTO;
import com.plx.app.viewer.vo.AlarmResponseDTO;
import com.plx.app.viewer.vo.ViewerPOIInfoVO;

/**
 *  @Project KNIS
 *  @Class DeviceInfoService
 *  @since 2019. 11. 21.
 *  @author NEWBIE
 *  @Description : 뷰어
 */
public interface ViewerService {

	/**
	 * @Method selectPOIInfoList
	 * @since  2020. 08. 18.
	 * @author 류중규
	 * @return List<ViewerPOIInfoVO>
	 * @param pViewerPOIInfoVO
	 * @throws Exception
	 * @description 뷰어 POIINFO LIST
	 */
	public List<ViewerPOIInfoVO> selectPOIInfoList(ViewerPOIInfoVO pViewerPOIInfoVO) throws Exception;

	/**
	 * @Method selectPOIInfo
	 * @since  2020. 08. 18.
	 * @author 유경식
	 * @return List<ViewerPOIInfoVO>
	 * @param pViewerPOIInfoVO
	 * @throws Exception
	 * @description 뷰어 POIINFO LIST
	 */
	public ViewerPOIInfoVO selectPOIInfo(ViewerPOIInfoVO pViewerPOIInfoVO) throws Exception;

	public List<UsrgrpMapVO> selectMapList(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	public void saveAlarm(AlarmRequestDTO dto) throws Exception;

	public List<AlarmResponseDTO> getAlarms(Map<String, Object> params) throws Exception;



}


