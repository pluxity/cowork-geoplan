package com.plx.app.admin.service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import com.plx.app.admin.mapper.POICategoryMapper;
import com.plx.app.admin.mapper.POIIconsetMapper;
import com.plx.app.admin.mapper.POIInfoMapper;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POIIconsetVO;
import com.plx.app.admin.vo.POIInfoVO;
import com.plx.app.cmn.mapper.FileInfoMapper;
import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.util.WebUtils;
import com.plx.app.util.ZipFile;


/**
 *  @Project KNIS
 *  @Class POICategoryServiceImpl
 *  @since 2019. 9. 23.
 *  @author newbie
 *  @Description :
 */

@Service
public class POICategoryServiceImpl implements POICategoryService {

	/**
	 * 도면 정보 mapper 채나연 일시키기
	 */
	@Autowired
	POICategoryMapper POICategoryMapper;


	/**
	 * 도면 정보 mapper
	 */
	@Autowired
	POIIconsetMapper POIIconsetMapper;


	/**
	 * 업로드 파일 DB 처리
	 */
	@Autowired
	FileInfoMapper fileInfoMapper;


	/**
	 * 업로드파일처리
	 */
	@Autowired
	FileInfoService fileInfoService;

	/**
	 * poi mapper
	 */
	@Autowired
	POIInfoMapper poiInfoMapper;


	/**
	 * properties 파일 처리
	 */
	@Resource(name="messageSourceAccessor")
    protected MessageSourceAccessor messageSourceAccessor;

	/**
	 * 파일 업로드 최상위 경로
	 */
	@Value("#{globalProp['upload.root.path']}")
	private String uploadRootPath;


	@Override
	public Map<String, Object> selectPOIIconsetList(POIIconsetVO pPOIIconsetVO) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		int total = POIIconsetMapper.selectPOIIconsetTotal(pPOIIconsetVO);
		List<POIIconsetVO>result = POIIconsetMapper.selectPOIIconsetList(pPOIIconsetVO);

		map.put("result", result);
		map.put("total", total);

		return map;
	}

	@Override
	public POIIconsetVO selectPOIIconsetDetail(POIIconsetVO pPOIIconsetVO) throws Exception {
		return POIIconsetMapper.selectPOIIconsetDetail(pPOIIconsetVO);
	}

	@Override
	public int insertPOIIconset(POIIconsetVO pPOIIconsetVO) throws Exception {
		return POIIconsetMapper.insertPOIIconset(pPOIIconsetVO);
	}

	@Override
	public int updatePOIIconset(POIIconsetVO pPOIIconsetVO, Map<String,FileInfoVO> fileVOMap) throws Exception {

		if(fileVOMap.get("poi2d_0") != null) pPOIIconsetVO.setIconset2d0(fileVOMap.get("poi2d_0").getFileNo());
		if(fileVOMap.get("poi2d_1") != null) pPOIIconsetVO.setIconset2d1(fileVOMap.get("poi2d_1").getFileNo());
		if(fileVOMap.get("poi2d_2") != null) pPOIIconsetVO.setIconset2d2(fileVOMap.get("poi2d_2").getFileNo());
		if(fileVOMap.get("poi2d_3") != null) pPOIIconsetVO.setIconset2d3(fileVOMap.get("poi2d_3").getFileNo());
		if(fileVOMap.get("poi2d_4") != null) pPOIIconsetVO.setIconset2d4(fileVOMap.get("poi2d_4").getFileNo());
		if(fileVOMap.get("poi3d_thumb") != null) pPOIIconsetVO.setIconset3dThumb(fileVOMap.get("poi3d_thumb").getFileNo());

		if(fileVOMap.get("poi3d") != null) {
			//압축 해제
			String zipFileDir = uploadRootPath + fileVOMap.get("poi3d").getFilePath();
			File mapZipFile = new File(zipFileDir, fileVOMap.get("poi3d").getFileSaveNm()); // 업로드 압축파일
			if(mapZipFile.isFile()) {
				ZipFile zipFile = new ZipFile();
				String type = "3d/";

				String unnzipPath = zipFileDir + type; // 압축해제 경로
				WebUtils.deleteDir(unnzipPath);

				zipFile.decompress(mapZipFile, unnzipPath); // 압축해제

				// 도면 압축해제 파일중 xml 파일 찾기

				File dir = new File(unnzipPath);
				File[] fileList = dir.listFiles();
				if(fileList != null) {
					for(int i = 0 ; i < fileList.length ; i++) {
						File file = fileList[i];
						if(file.isFile()) {
							if(file.getName().toLowerCase().contains(".obj") || file.getName().toLowerCase().contains(".fbx")) {
								pPOIIconsetVO.setIconset3d(fileVOMap.get("poi3d").getFilePath()+type+file.getName());
							}
							if(file.getName().contains("thumbnail") && pPOIIconsetVO.getIconset3dThumb() == 0) {
								pPOIIconsetVO.setIconset3dThumbFilePath(fileVOMap.get("poi3d").getFilePath()+type+file.getName());
								pPOIIconsetVO.setIconset3dThumb(-1);	//사실 이곳은 따로 Thumbnail 올릴때 사용하려고 만든 컬럼이다. 일단은 있으면 -1 처리함
							}
						}
					}
				}
			}
		}
		return POIIconsetMapper.updatePOIIconset(pPOIIconsetVO);
	}

	@Override
	public int deletePOIIconset(POIIconsetVO pPOIIconsetVO) throws Exception {

		//pPOIIconsetVO = selectPOIIconsetDetail (pPOIIconsetVO);

		if(pPOIIconsetVO.getIconsetNo() != null) {
			deletePOIIconsetFile(pPOIIconsetVO);
		} else {
			List<POIIconsetVO> pPOIIconsetVOList = pPOIIconsetVO.getIconsetList();

			for(POIIconsetVO iconsetVO : pPOIIconsetVOList) {
				deletePOIIconsetFile(iconsetVO);
			}
		}

		int result = POIIconsetMapper.deletePoIIconset(pPOIIconsetVO);
		return result;
	}

	/**
	 * 파일삭제
	 * @Method deletePOIIconsetFile
	 * @since  2019. 9. 30.
	 * @author NEWBIE
	 * @return void
	 * @param pPOIIconsetVO
	 * @throws Exception
	 * @description
	 */
	public void deletePOIIconsetFile(POIIconsetVO pPOIIconsetVO) throws Exception  {
		FileInfoVO pFileInfoVO = new FileInfoVO();
		pFileInfoVO.setFilePath("/icon/"+pPOIIconsetVO.getIconsetNo()+"/");
		WebUtils.deleteDir(uploadRootPath+pFileInfoVO.getFilePath());
		fileInfoMapper.deleteFileInfo(pFileInfoVO);
	}

	@Override
	public List<POICategoryVO> selectPOICategoryList(POICategoryVO pPOICategoryVO) throws Exception {

		if(pPOICategoryVO.getCategory1No() == null && pPOICategoryVO.getCategory1Nm() == null ) {
			return POICategoryMapper.selectPOICategory1List(pPOICategoryVO);
		}


		return POICategoryMapper.selectPOICategory2List(pPOICategoryVO);
	}

	@Override
	public List<POICategoryVO> selectPOICategory2List(POICategoryVO pPOICategoryVO) throws Exception {
		return POICategoryMapper.selectPOICategory2List(pPOICategoryVO);
	}

	@Override
	public POICategoryVO selectPOICategoryDetail(POICategoryVO pPOICategoryVO) throws Exception {

		if(pPOICategoryVO.getCategory1No() != null || !StringUtils.isEmpty(pPOICategoryVO.getCategory1Nm())) {
			pPOICategoryVO = POICategoryMapper.selectPOICategory1Detail(pPOICategoryVO);
		} else if(pPOICategoryVO.getCategory2No() != null) {
			pPOICategoryVO = POICategoryMapper.selectPOICategory2Detail(pPOICategoryVO);
		}

		return pPOICategoryVO;
	}

	@Override
	public List<POICategoryVO> selectPOICategoryDetailList(POICategoryVO pPOICategoryVO) throws Exception {

		return POICategoryMapper.selectPOICategoryDetailList(pPOICategoryVO);
	}

	@Override
	public int insertPOICategory(POICategoryVO pPOICategoryVO) throws Exception {

		String categoryType = pPOICategoryVO.getCategoryType();
		int result = 0;

		if("category1".equals(categoryType)) {
			result = POICategoryMapper.insertPOICategory1(pPOICategoryVO);
		} else if("category2".equals(categoryType)) {
			result = POICategoryMapper.insertPOICategory2(pPOICategoryVO);
		}

		return result;
	}

	@Override
	public int updatePOICategory(POICategoryVO pPOICategoryVO) throws Exception {

		String categoryType = pPOICategoryVO.getCategoryType();
		int result = 0;

		if("category1".equals(categoryType)) {
			result = POICategoryMapper.updatePOICategory1(pPOICategoryVO);
		} else if("category2".equals(categoryType)) {
			result = POICategoryMapper.updatePOICategory2(pPOICategoryVO);
		}

		return result;
	}

	@Override
	public Integer deletePOICategory(POICategoryVO pPOICategoryVO) throws Exception {

		String categoryType = pPOICategoryVO.getCategoryType();
		Integer result = null;

		POIInfoVO pPOIInfoVO = new POIInfoVO();
		pPOIInfoVO.setPoiCategory(pPOICategoryVO);

		int poiCounts = poiInfoMapper.selectPOIInfoTotal(pPOIInfoVO);

		if (poiCounts != 0) return result;	//poiCounts가 0이 아니면 삭제 하지않음

		if("category1".equals(categoryType)) {
			// 하위 항목 삭제
			List<POICategoryVO> subCateList = POICategoryMapper.selectPOICategory2List(pPOICategoryVO);
			for (POICategoryVO poiCategoryVO : subCateList) {
				POICategoryMapper.deletePOICategory2(poiCategoryVO);
			}
			//해당 항목 삭제
			result = POICategoryMapper.deletePOICategory1(pPOICategoryVO);
		} else if("category2".equals(categoryType)) {
			result = POICategoryMapper.deletePOICategory2(pPOICategoryVO);
		}
		return result;
	}

	/**
	 * 맵 카테고리 두개를 받아서 switch 한다.
	 */
	@Override
	public Integer poiCategorySwitch(POICategoryVO pPOICategoryVO) throws Exception {

		String categoryType = pPOICategoryVO.getCategoryType();

		Integer swap1 = pPOICategoryVO.getPoiCategoryList().get(0).getCategoryNo();
		Integer swap2 = pPOICategoryVO.getPoiCategoryList().get(1).getCategoryNo();
		Integer result = null;

		if("category1".equals(categoryType)) {
			result = POICategoryMapper.swapPOICategoryOrderNo1(swap1, swap2);
		} else if("category2".equals(categoryType)) {
			result = POICategoryMapper.swapPOICategoryOrderNo2(swap1, swap2);
		}

		return result;
	}


	@Override
	public int insertPOICategory2(POICategoryVO pPOICategoryVO) throws Exception {
		return POICategoryMapper.updatePOICategory1(pPOICategoryVO);
	}

	@Override
	public int updatePOICategory2(POICategoryVO pPOICategoryVO) throws Exception {
		return POICategoryMapper.updatePOICategory2(pPOICategoryVO);
	}

	@Override
	public int deletePOICategory2(POICategoryVO pPOICategoryVO) throws Exception {
		return POICategoryMapper.deletePOICategory1(pPOICategoryVO);
	}

	public List<LinkedHashMap<String, Object>> poiCategoryTree() throws Exception {
		return POICategoryMapper.poiCategoryTree();
	}
}
