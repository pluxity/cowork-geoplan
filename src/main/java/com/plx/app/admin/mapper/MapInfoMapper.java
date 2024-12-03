package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.MapHstVO;
import com.plx.app.admin.vo.MapInfoVO;



/**
 *  @Project KNIS
 *  @Class MapInfoMapper
 *  @since 2019. 9. 10.
 *  @author 류중규
 *  @Description : 도면정보
 */
public interface MapInfoMapper {

	public int insertMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	public int updateMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	public List<MapInfoVO> selectMapInfoList(MapInfoVO pMapInfoVO) throws Exception;

	public int selectMapInfoTotal(MapInfoVO pMapInfoVO) throws Exception;

	public MapInfoVO selectMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	public int deleteMapInfo(MapInfoVO pMapInfoVO) throws Exception;

	public int insertMapHst(MapHstVO pMapHstVO) throws Exception;

	public List<MapHstVO> selectMapHstList(MapHstVO pMapHstVO) throws Exception;

	public MapHstVO selectMapHst(MapHstVO pMapHstVO) throws Exception;

	public int deleteMapHst(MapHstVO pMapHstVO) throws Exception;

}
