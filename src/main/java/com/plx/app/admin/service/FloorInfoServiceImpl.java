package com.plx.app.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plx.app.admin.mapper.FloorInfoMapper;
import com.plx.app.admin.vo.FloorInfoVO;

/**
 *  @Project KNIS
 *  @Class FloorInfoServiceImpl
 *  @since 2019. 9. 15.
 *  @author 류중규
 *  @Description : 층정보
 */
@Service
public class FloorInfoServiceImpl implements FloorInfoService {

	/**
	 * 층정보 mapper
	 */
	@Autowired
	FloorInfoMapper floorInfoMapper;

	public int insertFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception {
		return floorInfoMapper.insertFloorInfo(pFloorInfoVO);
	}

	public int updateFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception {
		return floorInfoMapper.updateFloorInfo(pFloorInfoVO);
	}

	public int deleteFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception {
		return floorInfoMapper.deleteFloorInfo(pFloorInfoVO);
	}

	public FloorInfoVO selectFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception {
		return floorInfoMapper.selectFloorInfo(pFloorInfoVO);
	}

	public List<FloorInfoVO> selectFloorInfoList(FloorInfoVO pFloorInfoVO) throws Exception {
		return floorInfoMapper.selectFloorInfoList(pFloorInfoVO);
	}

}
