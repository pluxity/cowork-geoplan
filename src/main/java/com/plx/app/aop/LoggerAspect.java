package com.plx.app.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

import com.plx.app.constant.CmnConst;
import com.plx.app.util.StringUtil;


@Aspect
public class LoggerAspect {
	protected final Log info_logger = LogFactory.getLog("INFO_LOG");
	protected final Log error_logger = LogFactory.getLog("ERROR_LOG");

	@Around(value = "execution(* com.plx.app..*Service.*(..)) or execution(* com.plx.app..*Mapper.*(..))")
	public Object doLayerLoggingAround(ProceedingJoinPoint pjp) throws Throwable {
		return doLoggingAround(pjp);
	}

	public Object doLoggingAround(ProceedingJoinPoint pjp) throws Throwable {
		String target = pjp.getTarget().getClass().getSimpleName();// className
		String packageName = pjp.getTarget().getClass().getPackage().getName();
		String sigName = pjp.getSignature().getName();// method Name
		Object[] obj = pjp.getArgs();
		String objNames = StringUtil.getAgumentNames(obj);
		Object retVal = null;

		StringBuffer sb = new StringBuffer();
		StopWatch stopWatch = new StopWatch();
		String layerType = CmnConst.LAYER_SERVICE;
		String className = packageName + "." + target + "." + sigName + "(" + objNames + ")";

		stopWatch.start();
		retVal = pjp.proceed();
		stopWatch.stop();

		sb.append("[").append("LAYER").append("=").append(layerType);
		sb.append("|").append("CLASS_NAME").append("=").append(className);
		sb.append("|").append("ELAPSED_TIME").append("=").append(stopWatch.toString());
		sb.append("|").append("S/F").append("=").append(CmnConst.LOG_SUCCESS);
		sb.append("]");
		info_logger.info(sb.toString());

		return retVal;
	}

}
