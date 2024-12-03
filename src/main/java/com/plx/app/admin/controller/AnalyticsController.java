package com.plx.app.admin.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.service.AnalyticsService;
import com.plx.app.admin.service.MapCategoryService;
import com.plx.app.admin.service.MapInfoService;
import com.plx.app.admin.vo.AnalyticsVO;
import com.plx.app.admin.vo.MapCategoryVO;
import com.plx.app.admin.vo.MapInfoVO;
import com.plx.app.cmn.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/adm/analytics")
@Transactional
public class AnalyticsController extends BaseController{
    @Autowired
    AnalyticsService analyticsService;

    @Autowired
    MapInfoService mapInfoService;

    @Autowired
    MapCategoryService mapCategoryService;

    @RequestMapping(value="/analytics.do")
    public String admAnalytics(Model model) throws Exception {
        // List<AnalyticsVO> analyticsList = analyticsService.selectAnalyticsInfoList();
        // model.addAttribute("analyticsList", analyticsList);
        MapInfoVO pMapInfoVO = new MapInfoVO();
        pMapInfoVO.setPageSize(0);
        Map<String, Object> mapInfoList = mapInfoService.selectMapInfoList(pMapInfoVO);

        MapCategoryVO pMapCategoryVO = new MapCategoryVO();
        List<MapCategoryVO> mapCategory3List = analyticsService.selectMapCategoryList3(pMapCategoryVO);

        model.addAttribute("mapInfoList", mapInfoList.get("list"));
        model.addAttribute("mapCategory3List", mapCategory3List);

        return "adm/analytics/analytics";
    }

    @RequestMapping(value="/getAnalyticsList.json", method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> selectAnalyticsInfoList(@RequestBody Map<String, Object> param) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";
        // String sDate = (String)param.get("sDate");
        // String eDate = (String)param.get("eDate");

        AnalyticsVO analyticsVO = new AnalyticsVO();
        analyticsVO.setSDate((String)param.get("sDate"));
        analyticsVO.setEDate((String)param.get("eDate"));
        if(param.get("mapNo") != null) {
            analyticsVO.setMapNo((int)param.get("mapNo"));
        }

        if(param.get("category3No") != null) {
            analyticsVO.setCategory3No((int)param.get("category3No"));
        }
        try {
            List<AnalyticsVO> resultList = analyticsService.selectAnalyticsInfoList(analyticsVO);
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

    @RequestMapping(value="/analyticsRegist.json")
    @ResponseBody
    public Map<String, Object> registanalytics(AnalyticsVO pAnalyticsVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String resultCd = "success";
        String resultMsg = "";
        
        try {
            int result = 0;
            result = analyticsService.insertAnalyticsInfo(pAnalyticsVO);
        
            if(result <= 0) {
                resultCd = "fail";
                resultMsg = messageSourceAccessor.getMessage("insert.fail");
            } else {
                resultMsg = messageSourceAccessor.getMessage("insert.success");
            }

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
