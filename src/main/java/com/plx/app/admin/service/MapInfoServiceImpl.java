package com.plx.app.admin.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.plx.app.admin.mapper.MapInfoMapper;
import com.plx.app.admin.mapper.NoticeMapper;
import com.plx.app.admin.vo.FloorInfoVO;
import com.plx.app.admin.vo.MapHstVO;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.admin.vo.POIInfoVO;
import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.constant.CmnConst;
import com.plx.app.exception.IncorrectFileTypeException;
import com.plx.app.util.WebUtils;
import com.plx.app.util.ZipFile;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  @Project KNIS
 *  @Class MapInfoServiceImpl
 *  @since 2019. 9. 10.
 *  @author 류중규
 *  @Description : 도면정보
 */
/**
 *  @Project KNIS
 *  @Class MapInfoServiceImpl
 *  @since 2019. 9. 15.
 *  @author 류중규
 *  @Description :
 */
@Service
@RequiredArgsConstructor
public class MapInfoServiceImpl implements MapInfoService {

	/**
	 * 도면 정보 mapper
	 */
	private final MapInfoMapper mapInfoMapper;

	/**
	 * 업로드파일처리
	 */
	private final FileInfoService fileInfoService;

	/**
	 * properties 파일 처리
	 */
	@Resource(name="messageSourceAccessor")
    protected MessageSourceAccessor messageSourceAccessor;


	/**
	 * 층정보 serivce
	 */
	private final FloorInfoService floorInfoService;

	/**
	 * poi service
	 */
	private final POIInfoService poiInfoService;

	/**
	 * poi lod
	 */
	private final POILodInfoService poiLodInfoService;


	/**
	 * 파일 업로드 최상위 경로
	 */
	@Value("#{globalProp['upload.root.path']}")
	private String uploadRootPath;


	/**
	 * 도면정보 등록
	 */
	public int insertMapInfo(MapInfoVO pMapInfoVO) throws Exception {
		return mapInfoMapper.insertMapInfo(pMapInfoVO);
	}

	/**
	 * 도면정보 수정
	 */
	public int updateMapInfo(MapInfoVO pMapInfoVO) throws Exception {
		return mapInfoMapper.updateMapInfo(pMapInfoVO);
	}

	/**
	 * 도면 목록
	 */
	public Map<String, Object> selectMapInfoList(MapInfoVO pMapInfoVO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// 도면목록
		List<MapInfoVO> list = mapInfoMapper.selectMapInfoList(pMapInfoVO);
		int total = mapInfoMapper.selectMapInfoTotal(pMapInfoVO);

		map.put("list", list);
		map.put("total", total);

		return map;
	}

	/**
	 * 도면이력 등록
	 */
	public int insertMapHst(MapHstVO pMapHstVO) throws Exception {
		return mapInfoMapper.insertMapHst(pMapHstVO);
	}

	/**
	 * 도면파일 업로드 및 등록
	 */
	@Transactional
	public int mapFileUpload(MapInfoVO pMapInfoVO, MultipartHttpServletRequest request, MapHstVO pMapHstVO) throws Exception {
		int resultErr = 0; // 에러체크
		List<FloorInfoVO> floorInfoList = new ArrayList<FloorInfoVO>(); // 층정보 등록
		int mapNo = pMapInfoVO.getMapNo();

		// 도면 파일 경로
		String mapPath = CmnConst.UPLOAD_MAP_PATH + mapNo + "/";

		// 도면 버전
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String mapVer = sdf.format(new Date()); // 도면버전

		// 도면압축파일 업로드 등록
		FileInfoVO pFileInfoVO2 = new FileInfoVO();
		pFileInfoVO2.setFilePath(mapPath);
		pFileInfoVO2.setFileType("mapZip");
		MultipartFile mapZip =  request.getFile("mapZip");
		FileInfoVO mapZipFileInfo = fileInfoService.setUploadFile(pFileInfoVO2, mapZip); // 도면압축파일 업로드 처리
		int mapFileNo = mapZipFileInfo.getFileNo();
		if(mapFileNo > 0) {
			pMapHstVO.setMapFileNo(mapZipFileInfo.getFileNo()); // 도면파일 번호
		} else {
			resultErr++;
			return resultErr;
		}

		// 압축해제
		String zipFileDir = uploadRootPath + mapZipFileInfo.getFilePath();
		File mapZipFile = new File(zipFileDir, mapZipFileInfo.getFileSaveNm()); // 업로드 압축파일
		if(mapZipFile.isFile()) {
			ZipFile zipFile = new ZipFile();
	        pMapInfoVO.setMapVer(mapVer);
	        pMapHstVO.setMapVer(mapVer);
			String mapUnzipPath = zipFileDir + mapVer; // 압축해제 경로
			zipFile.decompress(mapZipFile, mapUnzipPath); // 압축해제


			if(pMapInfoVO.getFileType().equals("sbm")) {
				// 도면 압축해제 파일중 xml 파일 찾기
				String xmlFileNm = "";
				File dir = new File(mapUnzipPath);
				File[] fileList = dir.listFiles();
				if(fileList != null) {
					for(int i = 0 ; i < fileList.length ; i++) {
						File file = fileList[i];
						if(file.isFile()) {
							String fileNm = file.getName();
							String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1);

							if(CmnConst.FBX_FILE_EXT_LIST.contains(ext.toLowerCase())) {
								new IncorrectFileTypeException(new String("Modeling Filetype Exception"));
							}

							if("xml".equals(ext)) {
								xmlFileNm = file.getName();
								break;
							}
						}
					}
				}
				
				// 도면 xml 파일 파싱
				File fXmlFile = new File(mapUnzipPath + "/" + xmlFileNm);
				if(fXmlFile.isFile()) {
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();
					// 도면 기본층 정의
					int defaultFloor = Integer.parseInt(doc.getElementsByTagName("DefaultFloor").item(0).getTextContent());
					pMapInfoVO.setDefaultFloor(defaultFloor);
					
					Element fElement = (Element) doc.getElementsByTagName("Floors").item(0);
					NodeList fNodeList = fElement.getElementsByTagName("Floor");
					Element sElement = (Element) doc.getElementsByTagName("SpaceInfo").item(0);
					NodeList sNodeList = sElement.getElementsByTagName("Floor");
					
					// xml 파일 층정보 파싱
					for (int temp = 0; temp < fNodeList.getLength(); temp++) {
						FloorInfoVO floorInfo = new FloorInfoVO();
						Node fNode = fNodeList.item(temp);
						if (fNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) fNode;
							// <Floor id="0" name="LEVEL6" baseFloor="1" groupID="0" isMain="True">
							Element eElement2 = (Element) eElement.getElementsByTagName("FileSource").item(0);
							
							floorInfo.setMapNo(pMapInfoVO.getMapNo());
							floorInfo.setMapVer(mapVer);
							floorInfo.setFloorNo(Integer.parseInt(eElement.getAttribute("id")));	// 층번호
							floorInfo.setFloorGroup(Integer.parseInt(eElement.getAttribute("groupID"))); // 층그룹
							floorInfo.setFloorBase(Integer.parseInt(eElement.getAttribute("baseFloor"))); // 실제층
							floorInfo.setFloorNm(eElement.getAttribute("name")); // 층이름
							floorInfo.setFloorFileNm(eElement2.getAttribute("name").replace(".\\\\", "")); // sbm파일명
							floorInfo.setFloorFileType(pMapInfoVO.getFileType()); //파일 타입
							floorInfo.setIsMain(eElement.getAttribute("isMain")); // 층메인
							floorInfo.setRegUsr(pMapInfoVO.getRegUsr());
						}
						
						Node sNode = sNodeList.item(temp);
						if (sNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) sNode;
							// <Floor id="0" name="LEVEL6" longname="2121764074" baseFloor="1" level="1500.000000" />
							floorInfo.setFloorId(eElement.getAttribute("longname")); // floor id
							BigDecimal floorLvl = new BigDecimal(eElement.getAttribute("level")); // 층 level
							floorInfo.setFloorLvl(floorLvl);
						}
						
						floorInfoList.add(floorInfo);
					}
				} 
			} else {
				File dir = new File(mapUnzipPath);
				File[] fileList = dir.listFiles();
				
				if(fileList != null) {
					for(int i = 0 ; i < fileList.length ; i++) {
						File file = fileList[i];
						if(file.isFile()) {
							String fileNm = file.getName();
							String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1);

							if(CmnConst.SBM_FILE_EXT_LIST.contains(ext.toLowerCase())) {
								new IncorrectFileTypeException(new String("Modeling Filetype Exception"));
							}

							FloorInfoVO floorInfo = new FloorInfoVO();
							// 그룹번호_층명_파일네이밍
							String[] fileNames = file.getName().split("_");
							int floorNo = Integer.parseInt(fileNames[0]);

							floorInfo.setMapNo(pMapInfoVO.getMapNo());
							floorInfo.setMapVer(mapVer);
							floorInfo.setFloorNo(floorNo);
							floorInfo.setFloorGroup(floorNo);
							floorInfo.setFloorBase(floorNo);
							floorInfo.setFloorId(fileNames[0]);
							floorInfo.setFloorNm(fileNames[1]);
							floorInfo.setFloorFileNm(fileNm);
							floorInfo.setFloorFileType(pMapInfoVO.getFileType());
							floorInfo.setRegUsr(pMapInfoVO.getRegUsr());

							floorInfoList.add(floorInfo);
						}
					}
				}
			}
		} else {
			resultErr++;
			return resultErr;
		}

		// 층정보 삭제
		if(floorInfoList.size() > 0) {
//			FloorInfoVO pFloorInfoVO = new FloorInfoVO();
//			pFloorInfoVO.setMapNo(mapNo);
//			floorInfoService.deleteFloorInfo(pFloorInfoVO);

			// 층정보 등록
			for(FloorInfoVO fl :floorInfoList) {
				floorInfoService.insertFloorInfo(fl);
			}
		}

		// 도면이력 등록
		pMapHstVO.setMapNo(mapNo);
		pMapHstVO.setRegUsr(pMapInfoVO.getRegUsr());
		int updMapHstRes = mapInfoMapper.insertMapHst(pMapHstVO);
		if(updMapHstRes < 1) {
			resultErr++;
		} else {
			// 도면정보 업데이트
			int updMapRes = mapInfoMapper.updateMapInfo(pMapInfoVO);
			if(updMapRes < 1) {
				resultErr++;
			}
		}

		return resultErr;
	}


	/**
	 * 도면 정보
	 */
	public MapInfoVO selectMapInfo(MapInfoVO pMapInfoVO) throws Exception {
		return mapInfoMapper.selectMapInfo(pMapInfoVO);
	}

	/**
	 * 도면 삭제
	 */
	@Transactional
	public int deleteMapInfo(MapInfoVO pMapInfoVO) throws Exception {
		// 도면 정보 삭제
		int result = mapInfoMapper.deleteMapInfo(pMapInfoVO);
		int mapNo = pMapInfoVO.getMapNo();
		if(result > 0) {
			// 도면 이력 삭제 -> FK로 삭제 변경
			// MapHstVO pMapHstVO = new MapHstVO();
			// pMapHstVO.setMapNo(mapNo);
			// mapInfoMapper.deleteMapHst(pMapHstVO);

			// 층정보 삭제 -> FK로 삭제 변경
			// FloorInfoVO pFloorInfoVO = new FloorInfoVO();
			// pFloorInfoVO.setMapNo(mapNo);
			// floorInfoService.deleteFloorInfo(pFloorInfoVO);

			// poi lod 삭제
			// POILodTypeVO pPOILodTypeVO = new POILodTypeVO();
			// pPOILodTypeVO.setMapNo(mapNo);
			// poiLodInfoService.deletePoiLodType(pPOILodTypeVO);

			// 도면 디렉토리 삭제
			if(mapNo > 0) {
				String delDir = uploadRootPath + CmnConst.UPLOAD_MAP_PATH + mapNo + "/";
				WebUtils.deleteDir(delDir);
			}

			// poi 삭제
			POIInfoVO pPOIInfoVO = new POIInfoVO();
			pPOIInfoVO.setMapNo(mapNo);
			//poiInfoService.deletePOIInfo(pPOIInfoVO);
		}

		return result;
	}

	/**
	 * 도면 이력 리스트
	 */
	public List<MapHstVO> selectMapHstList(MapHstVO pMapHstVO) throws Exception {
		return mapInfoMapper.selectMapHstList(pMapHstVO);
	}

	/**
	 * 도면 이력 정보
	 */
	public MapHstVO selectMapHst(MapHstVO pMapHstVO) throws Exception {
		return mapInfoMapper.selectMapHst(pMapHstVO);
	}

	/**
	 * 도면 이력 삭제
	 */
	@Transactional
	public int deleteMapHst(MapHstVO pMapHstVO) throws Exception {
		// 도면 이력 삭제
		int result = mapInfoMapper.deleteMapHst(pMapHstVO);
		if(result > 0) {
			// 층정보 삭제
			FloorInfoVO pFloorInfoVO= new FloorInfoVO();
			pFloorInfoVO.setMapNo(pMapHstVO.getMapNo());
			pFloorInfoVO.setMapVer(pMapHstVO.getMapVer());
			floorInfoService.deleteFloorInfo(pFloorInfoVO);

			// 파일 DB, 실제 파일 삭제 처리
			FileInfoVO pFileInfoVO = new FileInfoVO();
			pFileInfoVO.setFileNo(pMapHstVO.getMapFileNo());
			FileInfoVO fileInfo = fileInfoService.getFileInfoInfo(pFileInfoVO);
			fileInfoService.deleteFileInfo(pFileInfoVO);

			// 버전 디렉토리 삭제
			if(!StringUtils.isEmpty(fileInfo.getFilePath()) && !StringUtils.isEmpty(pMapHstVO.getMapVer())) {
				String delDir = uploadRootPath + fileInfo.getFilePath() + "/" + pMapHstVO.getMapVer() + "/";
				WebUtils.deleteDir(delDir);
			}
		}

		return result;
	}

}
