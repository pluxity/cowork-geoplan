package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.plx.app.admin.service.CamPosService;
import com.plx.app.admin.vo.CamPosVO;
import com.plx.app.cmn.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/adm/camPos")
@Transactional
public class CamPosController extends BaseController {
    
    @Autowired
    CamPosService camPosService;

    @RequestMapping(value="/camPosInfo.json")
    public Map<String, Object> getCamPosInfo(CamPosVO pCamPosVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";

        try {
            CamPosVO camPosInfo = camPosService.selectCamPosInfo(pCamPosVO);
            resultMap.put("camPosInfo", camPosInfo);
        } catch(SQLException se) {
            resultCd = "fail";
            logger_error.error("SQLException", se);
        } catch(Exception e) {
            resultCd = "fail";
            logger_error.error("Exception", e);
        }
        
        resultMap.put("resultCd", resultCd);

        return resultMap;
    }
    
    @RequestMapping(value="/camPosModify.json")
    @ResponseBody
    public Map<String, Object> ModifyCamPos(CamPosVO pCamPosVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";
        String resultMsg = "";

        try {
            int result = 0;
            int count = camPosService.selectCamPosTotal(pCamPosVO);

            if(count > 0) {
                result = camPosService.updateCamPos(pCamPosVO);
                if(result > 0) {
                    resultMsg = messageSourceAccessor.getMessage("update.success");
                } else {
                    resultCd = "fail";
                    resultMsg = messageSourceAccessor.getMessage("update.fail");
                }
            } else {
                result = camPosService.insertCamPos(pCamPosVO);
                if(result > 0) {
                    resultMsg = messageSourceAccessor.getMessage("insert.success");
                } else {
                    resultCd = "fail";
                    resultMsg = messageSourceAccessor.getMessage("insert.fail");
                }
            }

            resultMap.put("result", result);
        }catch(SQLException se) {
            resultCd = "fail";
            logger_error.error("SQLException", se);
        }catch(Exception e) {
            resultCd = "fail";
            logger_error.error("Exception", e);
        }

        resultMap.put("resultCd", resultCd);
        resultMap.put("resultMsg", resultMsg);

        return resultMap;
    }

}
