package com.plx.app.viewer.mapper;

import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.viewer.vo.AlarmRequestDTO;
import com.plx.app.viewer.vo.AlarmResponseDTO;
import com.plx.app.viewer.vo.ViewerPOIInfoVO;


/**
 *  @Project KNIS
 *  @Class DeviceInfoMapper
 *  @since 2019. 11. 20.
 *  @author NEWBIE
 *  @Description : viewerMapper
 */
public interface ViewerMapper {

	public List<ViewerPOIInfoVO> selectPOIInfoList(ViewerPOIInfoVO pViewerPOIInfo) throws Exception;

	public ViewerPOIInfoVO selectPOIInfo(ViewerPOIInfoVO pViewerPOIInfo) throws Exception;

	public List<UsrgrpMapVO> selectMapList(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	public void saveAlarm(AlarmRequestDTO dto) throws Exception;

	public List<AlarmResponseDTO> getAlarms(Map<String, Object> params) throws Exception;


}
