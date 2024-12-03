package com.plx.app.admin.service;

import java.util.List;

import com.plx.app.admin.mapper.CamPosMapper;
import com.plx.app.admin.vo.CamPosVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CamPosServiceImpl implements CamPosService {

    @Autowired
    CamPosMapper camPosMapper;

    public List<CamPosVO> selectCamPosList(CamPosVO pCamPosVO) throws Exception {
        return camPosMapper.selectCamPosList(pCamPosVO);
    }

    public CamPosVO selectCamPosInfo(CamPosVO pCamPosVO) throws Exception {
        return camPosMapper.selectCamPosInfo(pCamPosVO);
    }

    public int selectCamPosTotal(CamPosVO pCamPosVO) throws Exception{
        return camPosMapper.selectCamPosTotal(pCamPosVO);
    }

    public int updateCamPos(CamPosVO pCamPosVO) throws Exception{
        return camPosMapper.updateCamPos(pCamPosVO);
    }

    public int insertCamPos(CamPosVO pCamPosVO) throws Exception{
        return camPosMapper.insertCamPos(pCamPosVO);
    }
}
