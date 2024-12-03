package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.service.TopologyInfoService;
import com.plx.app.admin.vo.TopologyInfoVO;
import com.plx.app.cmn.controller.BaseController;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/adm/topology")
@Transactional
@RequiredArgsConstructor
public class TopologyInfoController extends BaseController{

    private final TopologyInfoService topologyInfoService;

    @RequestMapping(value="/getTopologyList.json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> SelectTopologyList(int mapNo) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";
        String resultMsg = "";

        try {
            List<TopologyInfoVO> resultList = topologyInfoService.selectTopologyList(mapNo);
            resultMap.put("list", resultList);
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

    @RequestMapping(value="/getTopologyInfo.json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> SelectTopologyInfo(TopologyInfoVO pTopologyInfoVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";
        String resultMsg = "";

        try {
            TopologyInfoVO topologyInfo = new TopologyInfoVO();
            
            topologyInfo.setMapNo(pTopologyInfoVO.getMapNo());
            topologyInfo.setTopoType(pTopologyInfoVO.getTopoType());

            topologyInfo = topologyInfoService.selectTopologyInfo(topologyInfo);
            
            resultMap.put("topologyInfo", topologyInfo);
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

    @RequestMapping(value="/updateTopologyInfo.json", method = RequestMethod.PATCH)
    @ResponseBody
    public Map<String, Object> UpdateTopologyInfo(
            @RequestBody TopologyInfoVO pTopologyInfoVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";
        String resultMsg = "";

        try {
            importLoginSession(pTopologyInfoVO);

            int result = 0;
            result = topologyInfoService.updateTopologyInfo(pTopologyInfoVO);

            if(result <= 0) {
                resultCd = "fail";
                resultMsg = messageSourceAccessor.getMessage("update.fail");
            } else {
                resultMsg = messageSourceAccessor.getMessage("update.success");
            }

            resultMap.put("result", result);
        } catch(SQLException se) {
            resultCd = "fail";
            logger_error.error("SQLException", se);
        } catch(Exception e) {
            resultCd = "fail";
            logger_error.error("Exception", e);
        }

        resultMap.put("resultCd", resultCd);
        resultMap.put("resultMsg", resultMsg);

        return resultMap;
    }
}
