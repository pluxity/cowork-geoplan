package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.AnalyticsVO;
import com.plx.app.admin.vo.MapCategoryVO;

public interface AnalyticsMapper {
    public List<AnalyticsVO> selectAnalyticsInfoList(AnalyticsVO pAnalyticsVO) throws Exception;

    public int insertAnalyticsInfo(AnalyticsVO pAnalyticsVO) throws Exception;

    public List<MapCategoryVO> selectMapCategoryList3(MapCategoryVO pMapCategoryVO) throws Exception;
}
