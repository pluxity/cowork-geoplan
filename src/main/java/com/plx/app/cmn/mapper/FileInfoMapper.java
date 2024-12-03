package com.plx.app.cmn.mapper;

import java.util.List;

import com.plx.app.cmn.vo.FileInfoVO;

/**
 *  @Project KNIS
 *  @Class FileInfoMapper
 *  @since 2019. 9. 6.
 *  @author redmoonk
 *  @Description : 첨부파일 관리
 */
public interface FileInfoMapper {

	public List<FileInfoVO> selectFileInfoList(FileInfoVO pFileInfoVO) throws Exception;

	public FileInfoVO selectFileInfo(FileInfoVO pFileInfoVO) throws Exception;

	public int saveFileInfo(FileInfoVO pFileInfoVO) throws Exception;

	public int deleteFileInfo(FileInfoVO pFileInfoVO) throws Exception;

}
