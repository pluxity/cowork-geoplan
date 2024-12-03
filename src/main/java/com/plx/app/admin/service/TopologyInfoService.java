package com.plx.app.admin.service;

import com.plx.app.admin.vo.TopologyInfoVO;

import java.util.List;

public interface TopologyInfoService {

    public List<TopologyInfoVO> selectTopologyList(int mapNo) throws Exception;

    public TopologyInfoVO selectTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception;

    public int updateTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception;
}
