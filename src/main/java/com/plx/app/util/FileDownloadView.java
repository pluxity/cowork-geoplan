package com.plx.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.plx.app.cmn.vo.FileInfoVO;

/**
 *  @Class Name : FileDownloadView
 *  @Project Name : skipc
 *  @since 2018. 2. 28.
 *  @Description : 파일 다운로드 뷰
 */
public class FileDownloadView extends AbstractView {

    public FileDownloadView() {
        // 객체가 생성될 때 Content Type을 다음과 같이 변경
        setContentType("application/download; charset=utf-8");
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response
            ) throws Exception {

    	FileInfoVO fileInfo = (FileInfoVO) model.get("downloadFile"); // 넘겨받은 모델(파일 정보)

        String fileUploadPath = (String) fileInfo.getFilePath();
        String fileLogicName = (String) fileInfo.getFileRealNm();
        String filePhysicName = (String) fileInfo.getFileSaveNm();

        File file = new File(WebUtils.filePathBlackList(fileUploadPath), WebUtils.filePathBlackList(filePhysicName));

		String ua = request.getHeader("User-Agent");
		if(ua.matches(".*MSIE [789][.]0.*") || ua.matches(".*Trident.*"))
			fileLogicName = URLEncoder.encode(fileLogicName, "UTF-8").replaceAll("\\+", "%20");
		else
			fileLogicName = URLEncoder.encode(fileLogicName,"UTF-8");

        response.setContentType(getContentType());
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileLogicName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		OutputStream os = null;
        FileInputStream fis = null;
        try {
        	fis = new FileInputStream(file);
        	os = response.getOutputStream();
        	FileCopyUtils.copy(fis, os);

        } catch (IOException e) {
            logger.error("FileDownload IOException");
        } finally {
           	if(fis != null) {
           		try {
           			fis.close();
           		} catch(IOException e) {
           			logger.error(e.getMessage());
           		}
           	}
            if (os != null) {
            	try {
            		os.close();
            	} catch(IOException e) {
            		logger.error(e.getMessage());
            	}
            }
        }
    }
}