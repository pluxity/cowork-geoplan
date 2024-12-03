package com.plx.app.admin.mapper;

import com.plx.app.admin.vo.TopologyInfoVO;

import java.util.List;

public interface TopologyInfoMapper {

    public List<TopologyInfoVO> selectTopologyList(int mapNo) throws Exception;

    public TopologyInfoVO selectTopologyInfo(TopologyInfoVO pMapInfoVO) throws Exception;

    public int updateTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception;

}
