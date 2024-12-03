package com.plx.app.admin.service;

import com.plx.app.admin.mapper.NoticeMapper;
import com.plx.app.admin.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	/**
	 * 층정보 mapper
	 */
	private final NoticeMapper noticeMapper;

	@Override
	public Map<String, Object> selectNoticeList(NoticeVO pNoticeVO) throws Exception {
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// SOP 카테고리 목록
		List<NoticeVO> list = noticeMapper.selectNoticeList(pNoticeVO);
		int total = noticeMapper.selectNoticeListCnt(pNoticeVO);

		resultMap.put("list", list);
		resultMap.put("total", total);

		return resultMap;
		
	}

	@Override
	public NoticeVO selectNotice(NoticeVO pNoticeVO) throws Exception {
		return noticeMapper.selectNotice(pNoticeVO);
	}

	@Override
	public int insertNotice(NoticeVO pNoticeVO) throws Exception {

		int result = noticeMapper.insertNotice(pNoticeVO);

		return result;
	}

	@Override
	public int updateNotice(NoticeVO pNoticeVO) throws Exception {
		return noticeMapper.updateNotice(pNoticeVO);
	}
	
	@Override
	public int updateNoticeActive(NoticeVO pNoticeVO) throws Exception {
		
		int result = noticeMapper.updateNoticeActive(pNoticeVO);

		return result;
	}

	@Override
	public int deleteNotice(NoticeVO pNoticeVO) throws Exception {
		return noticeMapper.deleteNotice(pNoticeVO);
	}

	@Override
	public List<LinkedHashMap<String, Object>> selectMaptreeByGrpNo(Map<String, Object> paramMap) throws Exception {
		return noticeMapper.selectMaptreeByGrpNo(paramMap);
	}



}
