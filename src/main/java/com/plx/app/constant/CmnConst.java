package com.plx.app.constant;

import java.util.Arrays;
import java.util.List;

public class CmnConst {

	public static final String LOG_SUCCESS = "SUCCESS";
	public static final String LOG_FAIL = "FAIL";

	public static final String LAYER_CONTROLLER = "CONTROLLER";
	public static final String LAYER_SERVICE = "SERVICE";
	public static final String LAYER_DAO = "MAPPER";

	public static final String LOG_ATTRIBUTE_NAME = "SERVICE_LOG_INFO";
	public static final String LOG_STOP_WATCH_ATTRIBUTE_NAME = "STOP_WATCH";

	public static final String UPLOAD_MAP_PATH = "/map/";
	public static final String UPLOAD_ICON_PATH = "/icon/";
	public static final String UPLOAD_FILE_PATH = "/upload/";

	public static final String LOCATION_URL_ADMIN = "/adm/main/index.do";
	public static final String LOCATION_URL_MANAGER = "/dashboard/index.do";
	public static final String LOCATION_URL_USER = "/viewer/index.do?mapNo=";
	public static final String LOCATION_URL_API = "/adm/main/index.do";
	public static final String LOCATION_URL_MUSER = "/viewer/m/index.do?mapNo=";
	public static final String LOCATION_URL_ERROR = "/error/auth";
	public static final String LOCATION_URL_2D = "/viewer/2d/index.do";

	public static final List<String> EXT_LIST = Arrays.asList(new String[] {
			"txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "hwp", "pdf", "bmp", "jpg", "jpeg", "tif", "png", "gif", "zip", "fbx", "sbm", "gpl", "xml", "obj", "fbx", "mtl", "gml"
			, "mp3","aac","alac","flac","wma","aiff","wav","ogg","mqa","dsd","m4a"
	});

	public static final List<String> IMG_EXT_LIST = Arrays.asList(new String[] {
			"jpg", "jpeg", "bmp", "png", "gif"
	});

	public static final List<String> SBM_FILE_EXT_LIST = Arrays.asList(new String[] {
			"gpl", "jpg", "jpeg", "xml", "sbm", "zip"
	});

	public static final List<String> FBX_FILE_EXT_LIST = Arrays.asList(new String[] {
			"fbx", "gltf", "3ds", "obj", "gml"
	});

	/**
	 * 로그 URL 제외 패턴
	 */
	public static final String[] NOT_LOG_URI = {
			"/resources/",
			"/map/",
			"/icon/",
			"/upload/"
	};

	/**
	 * 뷰어 URL
	 */
	public static final String[] VIEWER_URI = {
			"/viewer/index.do",
			"/viewer/m/index.do",
			"/adm/map/viewer.do"
	};

}
