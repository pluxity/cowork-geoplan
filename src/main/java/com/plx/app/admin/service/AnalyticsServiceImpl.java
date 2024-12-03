package com.plx.app.admin.service;

import java.util.List;

import com.plx.app.admin.mapper.AnalyticsMapper;
import com.plx.app.admin.vo.AnalyticsVO;
import com.plx.app.admin.vo.MapCategoryVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
    @Autowired
    AnalyticsMapper analyticsMapper;
    
    public List<AnalyticsVO> selectAnalyticsInfoList(AnalyticsVO pAnalyticsVO) throws Exception {
        return analyticsMapper.selectAnalyticsInfoList(pAnalyticsVO);
    }

    public int insertAnalyticsInfo(AnalyticsVO pAnalyticsVO) throws Exception {
        return analyticsMapper.insertAnalyticsInfo(pAnalyticsVO);
    }

    public List<MapCategoryVO> selectMapCategoryList3(MapCategoryVO pMapCategoryVO) throws Exception {
		return analyticsMapper.selectMapCategoryList3(pMapCategoryVO);
	}
}
