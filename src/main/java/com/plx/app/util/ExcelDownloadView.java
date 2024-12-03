package com.plx.app.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;
//import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.plx.app.cmn.vo.BaseVO;

/**
 *  @Project KNIS
 *  @Class ExcelDownloadView
 *  @since 2020.06.24.
 *  @author 이유리
 *
 *  @Description : 엑셀 파일 다운로드(데이터를 엑셀 파일로 작성해서 출력)
 */
public class ExcelDownloadView extends AbstractView {

	protected Log logger = LogFactory.getLog(getClass());
	protected Log logger_info = LogFactory.getLog("INFO_LOG");
	protected Log logger_error = LogFactory.getLog("ERROR_LOG");

	private int rowNum = 0;

	//Method
	/**
	 * @Method renderMergedOutputModel
	 * @since  2020.06.24.
	 * @author 이유리
	 * @return void
	 * ${tags}
	 * @description
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// vo.properties 로드
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("prop/vo.properties"));
		} catch (IOException e) {
			//e.printStackTrace();
			logger_error.error(e.getMessage());
		}

		rowNum = 0;

		Map<String, Object> categoryMap = (Map<String, Object>) model.get("categoryMap");
		String sheetName = (String)model.get("sheetName");
		String[] fieldNmList = (String[])model.get("fieldNmList");
		@SuppressWarnings("unchecked")
		List<? extends BaseVO> VOList = (List<? extends BaseVO>) model.get("VOList");

		SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet(sheetName);

        // 헤더 셀 생성
        Row headerRow = sheet.createRow(0);
        Cell headerCell = null;
        for (int i=0; i<fieldNmList.length; i++) {
        	headerCell = headerRow.createCell(i);
        	headerCell.setCellValue(prop.getProperty((fieldNmList[i]))); //vo.properties에 필드명 몇 개 한글로 정리해둠. 필요시 추가
		}
        rowNum++;

        // 내용 행 및 셀 생성
		for (BaseVO row : VOList) {
			if (categoryMap != null) {
				this.createRow(sheet, fieldNmList, row.getMap(), categoryMap);
			} else {
				this.createRow(sheet, fieldNmList, row.getMap());
			}
        	//this.createRow(sheet, fieldNmList, row.getMap());
		}

        // 파일명 설정
        String currentDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = sheetName + "_" + currentDt + ".xlsx";

        // 여기서부터는 각 브라우저에 따른 파일이름 인코딩작업
        String browser = request.getHeader("User-Agent");
        if (browser.indexOf("MSIE") > -1) {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.indexOf("Trident") > -1) {       // IE11
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.indexOf("Firefox") > -1) {
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.indexOf("Opera") > -1) {
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.indexOf("Chrome") > -1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
               char c = fileName.charAt(i);
               if (c > '~') {
                     sb.append(URLEncoder.encode("" + c, "UTF-8"));
                       } else {
                             sb.append(c);
                       }
                }
                fileName = sb.toString();
        } else if (browser.indexOf("Safari") > -1){
            fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        } else {
             fileName = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1")+ "\"";
        }

        response.setContentType("application/download;charset=utf-8");
        //response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        //response.setHeader("Content-Transfer-Encoding ", "binary");
        response.setHeader("Transfer-Encoding ", "chunked");

       OutputStream os = null;

       try {
           os = response.getOutputStream();
           // 파일생성
           workbook.write(os);
       } catch (IOException e) {
           //e.printStackTrace();
    	   logger_error.error(e.getMessage());
       } finally {
           if(workbook != null) {
               try {
                   workbook.close();
               } catch (Exception e) {
                   //e.printStackTrace();
            	   logger_error.error(e.getMessage());
               }
           }
           if(os != null) {
               try {
                   os.close();
               } catch (IOException e) {
                   //e.printStackTrace();
            	   logger_error.error(e.getMessage());
               }
           }
       }
	}

	private void createRow(SXSSFSheet sheet, String[] keys, Map<String, Object> data) {
		int cellCnt = 0;
		Row row = sheet.createRow(rowNum);
		for(String key : keys){
			Cell cell = row.createCell(cellCnt);
			cell.setCellValue(String.valueOf(data.get(key)==null?"":data.get(key)));
			cellCnt++;
		}
//		sheet.autoSizeColumn(rowNum);
		rowNum++;
	}

	private void createRow(SXSSFSheet sheet, String[] keys, Map<String, Object> data, Map<String, Object> pMap) {
		int cellCnt = 0;
		Row row = sheet.createRow(rowNum);

		Map<String, String> categoryMap1 = (Map<String, String>) pMap.get("categoryMap1");
		Map<String, String> categoryMap2 = (Map<String, String>) pMap.get("categoryMap2");
		Map<String, String> categoryMap3 = (Map<String, String>) pMap.get("categoryMap3");

		for (String key : keys) {
			Cell cell = row.createCell(cellCnt);
			if (key.contains("category")) {
				if (key.equals("category1No") && categoryMap1 != null) {// category1No를 category1Nm로 바꿔서 셀에 입력
					cell.setCellValue(categoryMap1.get(String.valueOf(data.get(key))));
				} else if (key.equals("category2No") && categoryMap2 != null) {// category2No를 category2Nm로 바꿔서 셀에 입력
					cell.setCellValue(categoryMap2.get(String.valueOf(data.get(key))));
				} else if (key.equals("category3No") && categoryMap3 != null) {// category3No를 category3Nm로 바꿔서 셀에 입력
					cell.setCellValue(categoryMap3.get(String.valueOf(data.get(key))));
				}
			} else {
				cell.setCellValue(String.valueOf(data.get(key) == null ? "" : data.get(key)));
			}
			cellCnt++;
		}
//		sheet.autoSizeColumn(rowNum);
		rowNum++;
	}

	/**
	 * @Method getFieldNmList
	 * @since  2020.06.24.
	 * @author 이유리
	 * @return String[]
	 * ${tags}
	 * @description VO class의 필드명을 뽑아주는 메서드
	 */
	/*
	private String[] getFieldNmList(Class VOClass) throws Exception {

		Field[] fields = VOClass.getDeclaredFields();
		String[] fieldNmList = new String[fields.length];
		String fieldName = null;

		for (int i=0; i<fields.length; i++) {
			fieldName = fields[i].toString();
			fieldName = fieldName.substring(fieldName.lastIndexOf(".")+1);
			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			fieldNmList[i] = fieldName;
		}
		return fieldNmList;
	}
	*/


}
