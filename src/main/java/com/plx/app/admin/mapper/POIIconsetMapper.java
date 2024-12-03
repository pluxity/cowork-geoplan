package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.POIIconsetVO;

/**
 *  @Project KNIS
 *  @Class POICategoryMapper
 *  @since 2019. 9. 23.
 *  @author newbie
 *  @Description : POI 카테고리
 */
public interface POIIconsetMapper {

	public List<POIIconsetVO> selectPOIIconsetList(POIIconsetVO pPOIIconsetVO) throws Exception;

	public POIIconsetVO selectPOIIconsetDetail(POIIconsetVO pPOIIconsetVO) throws Exception;

	public int selectPOIIconsetTotal(POIIconsetVO pPOIIconsetVO) throws Exception;

	public int insertPOIIconset(POIIconsetVO pPOIIconsetVO) throws Exception;

	public int updatePOIIconset(POIIconsetVO pPOIIconsetVO) throws Exception;

	public int deletePoIIconset(POIIconsetVO pPOIIconsetVO) throws Exception;
}
