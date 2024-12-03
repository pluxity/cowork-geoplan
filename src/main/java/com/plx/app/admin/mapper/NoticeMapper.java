package com.plx.app.admin.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.NoticeVO;

/**
 *  @Project SmartStationV3
 *  @Class NoticeMapper
 *  @since 2020. 4. 22.
 *  @author 나동규
 *  @Description : 
 */
public interface NoticeMapper {

	public List<NoticeVO> selectNoticeList(NoticeVO pNoticeVO) throws Exception;
	
	public int selectNoticeListCnt(NoticeVO pNoticeVO) throws Exception;
	
	public NoticeVO selectNotice(NoticeVO pNoticeVO) throws Exception;
	
	public int insertNotice(NoticeVO pNoticeVO) throws Exception;
	
	public int updateNotice(NoticeVO pNoticeVO) throws Exception;

	public int updateNoticeActive(NoticeVO pNoticeVO) throws Exception;	

	public int deleteNotice(NoticeVO pNoticeVO) throws Exception;
	
	public List<LinkedHashMap<String, Object>> selectMaptreeByGrpNo(Map<String, Object> paramMap) throws Exception;
}