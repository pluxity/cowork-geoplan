package com.plx.app.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.NoticeVO;

public interface NoticeService {

	public Map<String, Object> selectNoticeList(NoticeVO pNoticeVO) throws Exception;
	
	public NoticeVO selectNotice(NoticeVO pNoticeVO) throws Exception;
	
	public int insertNotice(NoticeVO pNoticeVO) throws Exception;
	
	public int updateNotice(NoticeVO pNoticeVO) throws Exception;

	public int updateNoticeActive(NoticeVO pNoticeVO) throws Exception;

	public int deleteNotice(NoticeVO pNoticeVO) throws Exception;
	
	public List<LinkedHashMap<String, Object>> selectMaptreeByGrpNo(Map<String, Object> paramMap) throws Exception;
}
