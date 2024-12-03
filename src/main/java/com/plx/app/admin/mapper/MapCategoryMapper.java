package com.plx.app.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import com.plx.app.admin.vo.MapCategoryVO;


/**
 *  @Project KNIS
 *  @Class MapCategoryMapper
 *  @since 2019. 9. 4.
 *  @author redmoonk
 *  @Description : 맵카테고리 정보
 */
public interface MapCategoryMapper {

	public List<MapCategoryVO> selectMapCategoryList1(MapCategoryVO pMapCategoryVO) throws Exception;

	public List<MapCategoryVO> selectMapCategoryList2(MapCategoryVO pMapCategoryVO) throws Exception;

	public List<MapCategoryVO> selectMapCategoryList3(MapCategoryVO pMapCategoryVO) throws Exception;

	public MapCategoryVO selectMapCategory1(MapCategoryVO pMapCategoryVO) throws Exception;

	public MapCategoryVO selectMapCategory2(MapCategoryVO pMapCategoryVO) throws Exception;

	public MapCategoryVO selectMapCategory3(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer updateMapCategory1(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer updateMapCategory2(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer updateMapCategory3(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer insertMapCategory1(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer insertMapCategory2(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer insertMapCategory3(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer deleteMapCategory1(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer deleteMapCategory2(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer deleteMapCategory3(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer validationMapCategory(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer swapMapCategoryOrderNo1 (Integer swap1, Integer swap2) throws Exception;
	public Integer swapMapCategoryOrderNo2 (Integer swap1, Integer swap2) throws Exception;
	public Integer swapMapCategoryOrderNo3 (Integer swap1, Integer swap2) throws Exception;

	public List<LinkedHashMap<String, Object>> mapCategoryTree() throws Exception;
}
