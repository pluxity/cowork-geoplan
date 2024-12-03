package com.plx.app.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.mapper.POIInfoMapper;
import com.plx.app.admin.vo.POICategoryVO;
import com.plx.app.admin.vo.POIInfoVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

/**
 * @Project KNIS
 * @Class POIInfoServiceImpl
 * @since 2019. 10. 1.
 * @author 류중규
 * @Description : POI 정보
 */
@Service
@RequiredArgsConstructor
public class POIInfoServiceImpl implements POIInfoService {

	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * poi mapper
	 */
	private final POIInfoMapper poiInfoMapper;

	/**
	 * poi 등록
	 */
	public int insertPOIInfo(POIInfoVO pPOIInfoVO) throws Exception {
		return poiInfoMapper.insertPOIInfo(pPOIInfoVO);
	}

	/**
	 * poi 목록
	 */
	public Map<String, Object> selectPOIInfoList(POIInfoVO pPOIInfoVO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// poi 목록
		List<POIInfoVO> list = poiInfoMapper.selectPOIInfoList(pPOIInfoVO);
		int total = poiInfoMapper.selectPOIInfoTotal(pPOIInfoVO);

		map.put("list", list);
		map.put("total", total);

		return map;
	}

	/**
	 * poi 정보
	 */
	public POIInfoVO selectPOIInfo(POIInfoVO pPOIInfoVO) throws Exception {
		return poiInfoMapper.selectPOIInfo(pPOIInfoVO);
	}

	/**
	 * poi 수정
	 */
	public int updatePOIInfo(POIInfoVO pPOIInfoVO) throws Exception {
		return poiInfoMapper.updatePOIInfo(pPOIInfoVO);
	}

	/**
	 * poi 삭제
	 */
	public int deletePOIInfo(POIInfoVO pPOIInfoVO) throws Exception {
		return poiInfoMapper.deletePOIInfo(pPOIInfoVO);
	}

	@Transactional
	public Map<String, Object> insertExcelFile(MultipartFile[] excelFile, List<POICategoryVO> poiCategory1List,
			List<POICategoryVO> poiCategory2List, POIInfoVO pPOIInfoVO) throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook(excelFile[0].getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);

		int errCnt = 0;
		int resCnt = 0;
		int rowCnt = sheet.getPhysicalNumberOfRows();

		for (int r = 1; r < rowCnt; r++) {
			XSSFRow row = sheet.getRow(r);

			// try {
			int cellCnt = row.getPhysicalNumberOfCells();

			if (cellCnt >= 3) {
				// POI명
				String poiNm = getCellValue(row.getCell(0));

				// 대분류 카테고리번호 생성
				String category1Nm = getCellValue(row.getCell(1));

				// 중분류 카테고리번호 생성
				String category2Nm = getCellValue(row.getCell(2));

				if(StringUtils.isAllEmpty(poiNm, category1Nm, category2Nm)) {
					break;
				}

				Integer category1No = null;
				for (POICategoryVO item : poiCategory1List) {
					if (category1Nm.trim().equals(item.getCategory1Nm().trim())) {
						category1No = item.getCategory1No();
						break;
					}
				}

				Integer category2No = null;
				for (POICategoryVO item : poiCategory2List) {
					if (category1No.equals(item.getCategory1No())
							&& category2Nm.trim().equals(item.getCategory2Nm().trim())) {
						category2No = item.getCategory2No();
					}
				}

				// 객체코드
				String dvcCd = getCellValue(row.getCell(3));

				// POSITION X
				String posX = getCellValue(row.getCell(4));

				// POSITION Y
				String posY = getCellValue(row.getCell(5));

				// POSITION Z
				String posZ = getCellValue(row.getCell(6));

				// 카테고리 없을 경우 처리안함
				if (category1No == null || category2No == null) {
					System.out.println("poiNm:" + poiNm);
					throw new RuntimeException();
				}

				// POI 등록
				pPOIInfoVO.setCategory1No(category1No);
				pPOIInfoVO.setCategory2No(category2No);
				pPOIInfoVO.setPoiNm(poiNm);
				pPOIInfoVO.setDvcCd(dvcCd);
				pPOIInfoVO.setPosX(posX);
				pPOIInfoVO.setPosY(posY);
				pPOIInfoVO.setPosZ(posZ);

				int insCnt = poiInfoMapper.insertPOIInfo(pPOIInfoVO);
				if (insCnt == 0) {
					errCnt++;
				} else {
					resCnt++;
				}
			}
			// } catch (Exception e) {
			// logger_error.error("Exception", e);
			// errCnt++;
			// }
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("errCnt", errCnt);
		resultMap.put("resCnt", resCnt);

		return resultMap;
	}

	private String getCellValue(XSSFCell cell) {
		String value = "";

		if (cell != null) {
			switch (cell.getCellType()) {
			case FORMULA:
				value = cell.getCellFormula();
				break;
			case NUMERIC:
				value = new Integer(new Double(cell.getNumericCellValue()).intValue()).toString();
				break;
			case STRING:
				value = cell.getStringCellValue();
				break;
			case BLANK:
				value = null;
				break;
			case BOOLEAN:
				value = new Boolean(cell.getBooleanCellValue()).toString();
				break;
			case ERROR:
				value = new Byte(cell.getErrorCellValue()).toString();
				break;
			default:
			}
		}
		return value;
	}

	// POI 이미지 삭제시 이미지 파일 넘버 0으로 업데이트 처리
	public int updatePOIImgDelete(POIInfoVO pPOIInfoVO) throws Exception {

		return poiInfoMapper.updatePOIImgDelete(pPOIInfoVO);
	}
}
