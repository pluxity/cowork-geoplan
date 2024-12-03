package com.plx.app.cmn.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plx.app.cmn.vo.FileInfoVO;


/**
 *  @Project KNIS
 *  @Class FileInfoService
 *  @since 2019. 9. 6.
 *  @author redmoonk
 *  @Description :
 */
public interface FileInfoService {

	public FileInfoVO setUploadFile(FileInfoVO fileInfoVO, MultipartFile thisFile) throws Exception;

	public Map<String, FileInfoVO> setMultiFileUpload(FileInfoVO configVO, MultipartHttpServletRequest request) throws Exception;

	//public void setUploadMultiFiles(AttachInfoVO attachInfoVO, MultipartHttpServletRequest request) throws Exception;

	public int saveFileInfo(FileInfoVO pFileInfoVO) throws Exception;

	public List<FileInfoVO> getFileInfoList(FileInfoVO pFileInfoVO) throws Exception;

    public FileInfoVO getFileInfoInfo(FileInfoVO pFileInfoVO) throws Exception;

    public int deleteFileInfo(FileInfoVO pFileInfoVO) throws Exception;

}
