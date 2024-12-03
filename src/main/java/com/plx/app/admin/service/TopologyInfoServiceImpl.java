package com.plx.app.admin.service;

import com.plx.app.admin.mapper.TopologyInfoMapper;
import com.plx.app.admin.vo.TopologyInfoVO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopologyInfoServiceImpl implements TopologyInfoService{
    /**
	 * Topology 정보 mapper
	 */
    private final TopologyInfoMapper topologyInfoMapper;

    @Override
    public List<TopologyInfoVO> selectTopologyList(int mapNo) throws Exception {
        return topologyInfoMapper.selectTopologyList(mapNo);
    }

    @Override
    public TopologyInfoVO selectTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception {
        return topologyInfoMapper.selectTopologyInfo(pTopologyInfoVO);
    }

    @Override
    public int updateTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception {
        return topologyInfoMapper.updateTopologyInfo(pTopologyInfoVO);
    }
}
