package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.CamPosVO;

public interface CamPosMapper {
    public List<CamPosVO> selectCamPosList(CamPosVO camPosVO) throws Exception;

    public CamPosVO selectCamPosInfo(CamPosVO camPosVO) throws Exception;

    public int selectCamPosTotal(CamPosVO pCamPosVO) throws Exception;

    public int updateCamPos(CamPosVO pCamPosVO) throws Exception;

    public int insertCamPos(CamPosVO pCamPosVO) throws Exception;
}
