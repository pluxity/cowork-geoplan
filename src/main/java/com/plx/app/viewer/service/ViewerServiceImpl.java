package com.plx.app.viewer.service;

import java.util.ArrayList;
import java.util.List;

import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.viewer.mapper.ViewerMapper;
import com.plx.app.viewer.vo.ViewerPOIInfoVO;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @Project KNIS
 * @Class ViewerServiceImpl
 * @since 2019. 11. 21.
 * @author NEWBIE
 * @Description : 뷰어 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class ViewerServiceImpl implements ViewerService {

	private final ViewerMapper viewerMapper;

	@Override
	public List<ViewerPOIInfoVO> selectPOIInfoList(ViewerPOIInfoVO pViewerPOIInfoVO) throws Exception {

		List<ViewerPOIInfoVO> result = new ArrayList<ViewerPOIInfoVO>();
		result = viewerMapper.selectPOIInfoList(pViewerPOIInfoVO);

		return result;
	}

	@Override
	public ViewerPOIInfoVO selectPOIInfo(ViewerPOIInfoVO pViewerPOIInfoVO) throws Exception {
		ViewerPOIInfoVO result = viewerMapper.selectPOIInfo(pViewerPOIInfoVO);

		return result;
	}
	
	@Override
	public List<UsrgrpMapVO> selectMapList(UsrgrpMapVO pUsrgrpMapVO) throws Exception {
		return viewerMapper.selectMapList(pUsrgrpMapVO);
	}

}