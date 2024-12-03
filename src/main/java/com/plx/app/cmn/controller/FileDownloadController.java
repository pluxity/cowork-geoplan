package com.plx.app.cmn.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.plx.app.cmn.service.FileInfoService;
import com.plx.app.cmn.vo.FileInfoVO;


/**
 *  @Project KNIS
 *  @Class FileDownloadController
 *  @since 2019. 9. 16.
 *  @author 류중규
 *  @Description :
 */
@Controller
@RequestMapping(value="/download")
public class FileDownloadController extends BaseController {

	/**
	 * 업로드 파일 처리
	 */
	@Autowired
	FileInfoService fileInfoService;

    /**
	 * 파일 업로드 최상위 경로
	 */
	@Value("#{globalProp['upload.root.path']}")
	private String uploadRootPath;

	/**
	 * @Method Name : fileDownload
	 * @since 2019. 9. 16.
	 * @description 파일 다운로드(인덱스번호)
	 * @param model
	 * @param pFileInfoVO
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value="/file")
	public String fileDownload(Model model, FileInfoVO pFileInfoVO) throws Exception {
		try {
			FileInfoVO downloadFile = fileInfoService.getFileInfoInfo(pFileInfoVO);
			String downloadPath = uploadRootPath + downloadFile.getFilePath();
			downloadFile.setFilePath(downloadPath);
			model.addAttribute("downloadFile", downloadFile);

		} catch(SQLException se) {
			logger_error.error("SQLException", se);
		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		return "fileDownloadView";
	}

	/**
	 * @Method mapSbmDownload
	 * @since  2019. 9. 24.
	 * @author 류중규
	 * @return String
	 * @param model
	 * @param mapNo
	 * @param mapVer
	 * @param fileNm
	 * @return
	 * @throws Exception
	 * @description 도면 sbm 다운로드
	 */
	/*
	@GetMapping(value="/map/{mapNo}/{mapVer}")
	public String mapSbmDownload(Model model, @PathVariable("mapNo")int mapNo,
			@PathVariable("mapVer")String mapVer,
			@RequestParam("fileNm")String fileNm) throws Exception {
		try {
			fileNm = WebUtils.filePathBlackList(fileNm);
			List<String> extList = Arrays.asList(new String[] {"mtl", "obj", "fbx", "sbm", "gpl"});
			String ext = fileNm.substring(fileNm.lastIndexOf(".")+1).toLowerCase();
			if(!extList.contains(ext)) throw new Exception("허용되지 않은 파일 형식입니다.");
			else {
				FileInfoVO downloadFile = new FileInfoVO();
				downloadFile.setFilePath(CmnConst.UPLOAD_MAP_PATH + mapNo + "/" + mapVer);
				downloadFile.setFileRealNm(fileNm);
				downloadFile.setFileSaveNm(fileNm);

				model.addAttribute("downloadFile", downloadFile);
			}

		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		return "fileDownloadView";
	}
	*/


	/**
	 * @Method mapImglDownload
	 * @since  2019. 9. 24.
	 * @author 류중규
	 * @return String
	 * @param model
	 * @param mapNo
	 * @param mapVer
	 * @param imgType
	 * @param fileNm
	 * @return
	 * @throws Exception
	 * @description 도면 텍스쳐 이미지 다운로드
	 */
	/*
	@GetMapping(value="/map/{mapNo}/{mapVer}/{imgType}/{fileNm:.+}")
	public String mapImglDownload(Model model,
			@PathVariable("mapNo")int mapNo,
			@PathVariable("mapVer")String mapVer,
			@PathVariable("imgType")String imgType,
			@PathVariable("fileNm")String fileNm) throws Exception {
		try {
			fileNm = WebUtils.filePathBlackList(fileNm);

			List<String> extList = Arrays.asList(new String[] {"bmp", "jpg", "jpeg", "png", "gif"});
			String ext = fileNm.substring(fileNm.lastIndexOf(".")+1).toLowerCase();
			if(!extList.contains(ext)) throw new Exception("허용되지 않은 파일 형식입니다.");
			else {
				FileInfoVO downloadFile = new FileInfoVO();
				downloadFile.setFilePath(CmnConst.UPLOAD_MAP_PATH + mapNo + "/" + mapVer + "/" + imgType);
				downloadFile.setFileRealNm(fileNm);
				downloadFile.setFileSaveNm(fileNm);

				model.addAttribute("downloadFile", downloadFile);
			}

		} catch(Exception e) {
			logger_error.error("Exception", e);
		}

		return "fileDownloadView";
	}
	*/


}
