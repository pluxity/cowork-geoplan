package com.plx.app.viewer.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;



/**
 *  @Project KNIS
 *  @Class ScheduleTask
 *  @since 2021. 2. 16.
 *  @author 류중규
 *  @Description : 스케쥴 작업
 */
@Component
public class ScheduleTask {

	protected Log logger_error = LogFactory.getLog("ERROR_LOG");

//	@Scheduled(cron = "0 0 0 * * *")
    public void initPoiStatusTask() throws Exception {

	}

}
