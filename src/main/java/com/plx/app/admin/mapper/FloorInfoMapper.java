package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.FloorInfoVO;

/**
 *  @Project KNIS
 *  @Class FloorInfoMapper
 *  @since 2019. 9. 15.
 *  @author 류중규
 *  @Description : 층정보
 */
public interface FloorInfoMapper {

	public int insertFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception;

	public int updateFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception;

	public int deleteFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception;

	public FloorInfoVO selectFloorInfo(FloorInfoVO pFloorInfoVO) throws Exception;

	public List<FloorInfoVO> selectFloorInfoList(FloorInfoVO pFloorInfoVO) throws Exception;

}
