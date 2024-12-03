package com.plx.app.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plx.app.admin.mapper.POILodInfoMapper;
import com.plx.app.admin.vo.POILodInfoVO;
import com.plx.app.admin.vo.POILodTypeVO;

/**
 *  @Project KNIS
 *  @Class POILodInfoServiceImpl
 *  @since 2019. 11. 11.
 *  @author 류중규
 *  @Description : POI LOD 정보
 */
@Service
public class POILodInfoServiceImpl implements POILodInfoService {

	@Autowired
	POILodInfoMapper poiLodInfoMapper;

	/**
	 *  poi lod 정보 등록
	 */
	public int insertPoiLodInfo(POILodTypeVO pPOILodTypeVO) throws Exception {
		int result = 0;
		// lod 기본정보
		result = poiLodInfoMapper.insertPoiLodType(pPOILodTypeVO);
		if(result > 0) {
			// 단계별 lod 정보
			for(POILodInfoVO vo : pPOILodTypeVO.getPoiLodInfoList()) {
				vo.setRegUsr(pPOILodTypeVO.getRegUsr());
				vo.setUpdUsr(pPOILodTypeVO.getUpdUsr());
				poiLodInfoMapper.insertPoiLodInfo(vo);
			}
		}

		return result;
	}

	/**
	 * poi lod 삭제
	 */
	public int deletePoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception {
		return poiLodInfoMapper.deletePoiLodType(pPOILodTypeVO);
	}

	/**
	 * poi lod 정보 호출
	 */
	public POILodTypeVO selectPoiLodInfo(POILodTypeVO pPOILodTypeVO) throws Exception {
		POILodTypeVO poiLodType = poiLodInfoMapper.selectPoiLodType(pPOILodTypeVO);

		if(null != poiLodType) {
			POILodInfoVO pPOILodInfoVO = new POILodInfoVO();
			pPOILodInfoVO.setMapNo(pPOILodTypeVO.getMapNo());
			List<POILodInfoVO> poiLodInfoList = poiLodInfoMapper.selectPoiLodInfoList(pPOILodInfoVO);
			if(null != poiLodInfoList) {
				poiLodType.setPoiLodInfoList(poiLodInfoList);
			}
		}

		return poiLodType;
	}

//	public int updatePoiLodType(POILodTypeVO pPOILodTypeVO) throws Exception {
//	return poiLodInfoMapper.updatePoiLodType(pPOILodTypeVO);
//}

//	public int updatePoiLodInfo(POILodInfoVO pPOILodInfoVO) throws Exception {
//	return poiLodInfoMapper.updatePoiLodInfo(pPOILodInfoVO);
//}

}
