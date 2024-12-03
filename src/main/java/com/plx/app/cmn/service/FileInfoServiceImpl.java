package com.plx.app.cmn.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plx.app.cmn.mapper.FileInfoMapper;
import com.plx.app.cmn.vo.FileInfoVO;
import com.plx.app.constant.CmnConst;
import com.plx.app.util.ZipFile;


/**
 *  @Project KNIS
 *  @Class FileInfoServiceImpl
 *  @since 2019. 9. 6.
 *  @author redmoonk
 *  @Description :
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * 업로드 파일 DB 처리
	 */
	@Autowired
	FileInfoMapper fileInfoMapper;

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

	public FileInfoVO setUploadFile(FileInfoVO fileInfoVO, MultipartFile thisFile) throws Exception {

    	if(!StringUtils.isEmpty(thisFile) && thisFile.getSize() > 0) {
      		// 파일 실제 풀 경로 생성
    		File dir = new File(uploadRootPath + fileInfoVO.getFilePath());
            if(dir.exists() == false){
                if(dir.mkdirs()) {
                	logger.info("디렉토리 생성:" + dir.getName());
                } else {
                	logger.error("디렉토리 생성 실패");
                	throw new Exception("디렉토리 생성 실패");
                }
            }

    		String realFileName = thisFile.getOriginalFilename();
    		if( !("".equals(realFileName)) && realFileName != null ) {
	    		String ext = realFileName.substring(realFileName.lastIndexOf(".")+1).toLowerCase();
	    		if(!CmnConst.EXT_LIST.contains(ext)) throw new Exception("[" + ext + "]"+"허용되지 않은 파일 형식입니다.");
	    		String saveFileName = UUID.randomUUID().toString() + "." + ext;

	    		File file = new File(dir, saveFileName);
                thisFile.transferTo(file);

                if("zip".equals(ext)) {
	                ZipFile zipFile = new ZipFile();
	    			boolean zipResult = zipFile.decompressChk(file);
	    			if(!zipResult) throw new Exception("허용되지 않은 파일이 존재합니다.");
                }

                //파일 정보 저장
                fileInfoVO.setFileRealNm(realFileName);
                fileInfoVO.setFileSaveNm(saveFileName);

                saveFileInfo(fileInfoVO);
    		}
	    }

    	return fileInfoVO;
    }

	public Map<String,FileInfoVO> setMultiFileUpload(FileInfoVO configVO, MultipartHttpServletRequest request) throws Exception {

		Map<String,MultipartFile>fileMap = request.getFileMap();
		Map<String,FileInfoVO> fileVOMap = new HashMap<String,FileInfoVO>();

		for(String key : fileMap.keySet()) {

			if(fileMap.get(key).getSize() == 0) continue;	//공백파일은 일단 처리 안하기로.

			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFilePath(configVO.getFilePath());
			fileInfoVO.setFileType(configVO.getFileType());
			fileInfoVO = setUploadFile(fileInfoVO, fileMap.get(key));
			if(fileInfoVO.getFileNo() != 0)	fileVOMap.put(key, fileInfoVO);
		}
    	return fileVOMap;
    }

	/* (non-Javadoc)
	 * @see skipc.cmmn.service.AttachInfoService#setUploadFiles(skipc.cmmn.service.AttachInfoVO, org.springframework.web.multipart.MultipartHttpServletRequest)
	 */
	/*
	public void setUploadMultiFiles(AttachInfoVO attachInfoVO, MultipartHttpServletRequest request) throws Exception {
    	List<MultipartFile> uploadfile =  request.getFiles("uploadfile");
    	if(!StringUtils.isEmpty(uploadfile)){
    		List<String> atchCd = Arrays.asList(request.getParameterValues("atchcd"));
    		attachInfoVO.setAtchPath(attachInfoVO.getAtchPath() + "/");
    		//폴더 생성
    		File file = new File(attachInfoVO.getAtchPath());
            if(file.exists() == false){
                file.mkdirs();
            }

	    	for(int i=0; i<uploadfile.size(); i++){
	    		MultipartFile thisFile = uploadfile.get(i);
	    		String realFileName = thisFile.getOriginalFilename();
	    		if( !("".equals(realFileName)) && realFileName != null ) {
		    		String ext = realFileName.substring(realFileName.lastIndexOf(".")+1).toLowerCase();
		    		if(!extList.contains(ext)) throw new Exception("허용되지 않은 파일 형식입니다.");

		    		String saveFileName = UUID.randomUUID().toString();

		    		file = new File(attachInfoVO.getAtchPath() + saveFileName);
	                thisFile.transferTo(file);

	                //파일 정보 저장
	                attachInfoVO.setAtchCd(atchCd.get(i));
	                attachInfoVO.setAtchRealNm(realFileName);
	                attachInfoVO.setAtchSaveNm(saveFileName);

	                saveAttachInfo(attachInfoVO);
	    		}
	    	}
	    }
    }
    */


	/* (non-Javadoc)
	 * @see skipc.cmmn.service.AttachInfoService#saveAttachInfo(skipc.cmmn.service.AttachInfoVO)
	 */
	public int saveFileInfo(FileInfoVO pFileInfoVO) throws Exception {
		return fileInfoMapper.saveFileInfo(pFileInfoVO);
	}

	public List<FileInfoVO> getFileInfoList(FileInfoVO pFileInfoVO) throws Exception {
        return fileInfoMapper.selectFileInfoList(pFileInfoVO);
    }

    public FileInfoVO getFileInfoInfo(FileInfoVO pFileInfoVO) throws Exception {
        return fileInfoMapper.selectFileInfo(pFileInfoVO);
    }

    public int deleteFileInfo(FileInfoVO pFileInfoVO) throws Exception {
    	FileInfoVO rFileInfoVO = fileInfoMapper.selectFileInfo(pFileInfoVO);
    	int result = fileInfoMapper.deleteFileInfo(pFileInfoVO);
    	if(result > 0) {
    		// 실제파일 삭제 로직 추가
    		String filePath = rFileInfoVO.getFilePath();
    		String fileSaveNm = rFileInfoVO.getFileSaveNm();
    		if(!StringUtils.isEmpty(filePath) && !StringUtils.isEmpty(fileSaveNm)) {
    			File delFile = new File(uploadRootPath + filePath + fileSaveNm);
        		if(delFile.delete()) {
        			logger.info("파일 삭제:" + delFile.getName());
        		} else {
        			logger.error("파일 삭제 실패");
        		}
    		}
		}

		return result;
    }

}
