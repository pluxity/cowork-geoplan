package com.plx.app.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.MapCategoryVO;


/**
 *  @Project KNIS
 *  @Class MapCategoryService
 *  @since 2019. 9. 4.
 *  @author redmoonk
 *  @Description : 맵카테고리 정보
 */
public interface MapCategoryService {

	public List<MapCategoryVO> selectMapCategoryList(MapCategoryVO pMapCategoryVO) throws Exception;

	public MapCategoryVO selectMapCategory(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer updateMapCategory(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer insertMapCategory(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer deleteMapCategory(MapCategoryVO pMapCategoryVO) throws Exception;

	public boolean validationMapCategory(MapCategoryVO pMapCategoryVO) throws Exception;

	public Integer mapCategorySwitch(MapCategoryVO pMapCategoryVO) throws Exception;

	public List<LinkedHashMap<String, Object>> mapCategoryTree() throws Exception;

	public Map<String, Object> selectAllMapCategoryList(MapCategoryVO pMapCategoryVO) throws Exception;
}
