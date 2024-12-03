package com.plx.app.admin.service;

import java.util.List;

import com.plx.app.admin.vo.CamPosVO;

public interface CamPosService {
    public List<CamPosVO> selectCamPosList(CamPosVO pCamPosVO) throws Exception;

    public CamPosVO selectCamPosInfo(CamPosVO pCamPosVO) throws Exception;

    public int selectCamPosTotal(CamPosVO pCamPosVO) throws Exception;

    public int updateCamPos(CamPosVO pCamPosVO) throws Exception;

    public int insertCamPos(CamPosVO pCamPosVO) throws Exception;
}
