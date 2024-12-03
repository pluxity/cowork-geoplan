package com.plx.app.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.mapper.MapCategoryMapper;
import com.plx.app.admin.vo.MapCategoryVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *  @Project KNIS
 *  @Class MapCategoryServiceImpl
 *  @since 2null19. 9. 4.
 *  @author redmoonk
 *  @Description : 맵카테고리 정보
 */
@Service
public class MapCategoryServiceImpl implements MapCategoryService {

	/**
	 * 맵카테고리 정보
	 */
	@Autowired
	MapCategoryMapper mapCategoryMapper;


	public List<MapCategoryVO> selectMapCategoryList(MapCategoryVO pMapCategoryVO) throws Exception {

		List<MapCategoryVO> mapCategoryList = new ArrayList<MapCategoryVO>();

		if(pMapCategoryVO.getCategory1No() == null && pMapCategoryVO.getCategory2No() == null) {
			mapCategoryList = mapCategoryMapper.selectMapCategoryList1(pMapCategoryVO);
		} else if(pMapCategoryVO.getCategory1No() != null) {
			mapCategoryList = mapCategoryMapper.selectMapCategoryList2(pMapCategoryVO);
		}  else if(pMapCategoryVO.getCategory2No() != null) {
			mapCategoryList = mapCategoryMapper.selectMapCategoryList3(pMapCategoryVO);
		}

		return mapCategoryList;
	}

	/**
	 * 맵카테고리 detail
	 */
	@Override
	public MapCategoryVO selectMapCategory(MapCategoryVO pMapCategoryVO) throws Exception {

		if(pMapCategoryVO.getCategory1No() != null) {
			pMapCategoryVO = mapCategoryMapper.selectMapCategory1(pMapCategoryVO);
		} else if(pMapCategoryVO.getCategory2No() != null) {
			pMapCategoryVO = mapCategoryMapper.selectMapCategory2(pMapCategoryVO);
		}  else if(pMapCategoryVO.getCategory3No() != null) {
			pMapCategoryVO = mapCategoryMapper.selectMapCategory3(pMapCategoryVO);
		}
		return pMapCategoryVO;
	}

	/**
	 * 맵카테고리 update
	 */
	@Override
	public Integer updateMapCategory(MapCategoryVO pMapCategoryVO) throws Exception {

		Integer result = null;
		String categoryType = pMapCategoryVO.getCategoryType();

		if("category1".equals(categoryType)) {
			result = mapCategoryMapper.updateMapCategory1(pMapCategoryVO);
		} else if("category2".equals(categoryType)) {
			result = mapCategoryMapper.updateMapCategory2(pMapCategoryVO);
		}  else if("category3".equals(categoryType)) {
			result = mapCategoryMapper.updateMapCategory3(pMapCategoryVO);
		}

		return result;
	}

	/**
	 * 맵카테고리 insert
	 */
	@Override
	public Integer insertMapCategory(MapCategoryVO pMapCategoryVO) throws Exception {

		Integer result = null;
		String categoryType = pMapCategoryVO.getCategoryType();

		if("category1".equals(categoryType)) {
			result = mapCategoryMapper.insertMapCategory1(pMapCategoryVO);
		} else if("category2".equals(categoryType)) {
			result = mapCategoryMapper.insertMapCategory2(pMapCategoryVO);
		}  else if ("category3".equals(categoryType)){
			result = mapCategoryMapper.insertMapCategory3(pMapCategoryVO);
		}
		return result;
	}

	/**
	 * 맵카테고리 delete
	 */
	@Override
	public Integer deleteMapCategory(MapCategoryVO pMapCategoryVO) throws Exception {

		Integer result = null;
		String categoryType = pMapCategoryVO.getCategoryType();

		if(validationMapCategory(pMapCategoryVO)) return -1;
		List<MapCategoryVO> subCateList = null;
		
		if("category1".equals(categoryType)) {
			result = mapCategoryMapper.deleteMapCategory1(pMapCategoryVO);
			//하위 카테고리 삭제하기 위해
			subCateList = mapCategoryMapper.selectMapCategoryList2(pMapCategoryVO);
			for (MapCategoryVO mapCategoryVO : subCateList) {
				mapCategoryVO.setCategoryType("category2");
				this.deleteMapCategory(mapCategoryVO);
			}
		} else if("category2".equals(categoryType)) {
			result = mapCategoryMapper.deleteMapCategory2(pMapCategoryVO);
			//하위 카테고리 삭제하기 위해
			subCateList = mapCategoryMapper.selectMapCategoryList3(pMapCategoryVO);
			for (MapCategoryVO mapCategoryVO : subCateList) {
				mapCategoryMapper.deleteMapCategory3(mapCategoryVO);
			}
		}  else if("category3".equals(categoryType)) {
			result = mapCategoryMapper.deleteMapCategory3(pMapCategoryVO);
		}
		
		//하위 카테고리 삭제
		if (subCateList != null) {
			for (MapCategoryVO mapCategoryVO : subCateList) {
				this.deleteMapCategory(mapCategoryVO);
			}
		}

		return result;
	}

	/**
	 * 맵카테고리에 속한 도면이 있으면 true 아니면 false
	 */
	@Override
	public boolean validationMapCategory(MapCategoryVO pMapCategoryVO) throws Exception {
		Integer count = mapCategoryMapper.validationMapCategory(pMapCategoryVO);
		if(count != 0) return true;
		return false;

	}

	/**
	 * 맵 카테고리 두개를 받아서 switch 한다.
	 */
	@Override
	public Integer mapCategorySwitch(MapCategoryVO pMapCategoryVO) throws Exception {

		String categoryType = pMapCategoryVO.getCategoryType();

		Integer swap1 = pMapCategoryVO.getMapCategoryList().get(0).getCategoryNo();
		Integer swap2 = pMapCategoryVO.getMapCategoryList().get(1).getCategoryNo();
		Integer result = null;

		if("category1".equals(categoryType)) {
			result = mapCategoryMapper.swapMapCategoryOrderNo1(swap1, swap2);
		} else if("category2".equals(categoryType)) {
			result = mapCategoryMapper.swapMapCategoryOrderNo2(swap1, swap2);
		}  else if("category3".equals(categoryType)) {
			result = mapCategoryMapper.swapMapCategoryOrderNo3(swap1, swap2);
		}

		return result;
	}

	/**
	 * 도면카테고리 트리
	 */
	public List<LinkedHashMap<String, Object>> mapCategoryTree() throws Exception {
		return mapCategoryMapper.mapCategoryTree();
	}
	
	/**
	 * 도면카테고리1,2,3을 모두 불러와서 No를 Key, Nm을 value로 갖는 Map으로 리턴해줌
	 */
	public Map<String, Object> selectAllMapCategoryList(MapCategoryVO pMapCategoryVO) throws Exception{
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, String> categoryMap1 = new HashMap<String, String>();
		Map<String, String> categoryMap2 = new HashMap<String, String>();
		Map<String, String> categoryMap3 = new HashMap<String, String>();
		
		for (MapCategoryVO mapCategoryVO : mapCategoryMapper.selectMapCategoryList1(pMapCategoryVO)) {
			categoryMap1.put(String.valueOf(mapCategoryVO.getCategory1No()), mapCategoryVO.getCategory1Nm());
		}
		returnMap.put("categoryMap1", categoryMap1);
		
		for (MapCategoryVO mapCategoryVO : mapCategoryMapper.selectMapCategoryList2(pMapCategoryVO)) {
			categoryMap2.put(String.valueOf(mapCategoryVO.getCategory2No()), mapCategoryVO.getCategory2Nm());
		}
		returnMap.put("categoryMap2", categoryMap2);
		
		for (MapCategoryVO mapCategoryVO : mapCategoryMapper.selectMapCategoryList3(pMapCategoryVO)) {
			categoryMap3.put(String.valueOf(mapCategoryVO.getCategory3No()), mapCategoryVO.getCategory3Nm());
		}
		returnMap.put("categoryMap3", categoryMap3);
		
		return returnMap;
	}
}
