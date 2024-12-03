package com.plx.app.admin.mapper;

import com.plx.app.admin.vo.SystemInfoVO;

/**
 * @Project KNIS
 * @Class SystemInfoMapper
 * @since 2020. 07. 23.
 * @author 이유리
 * @Description : 시스템 정보
 */
public interface SystemInfoMapper {

	public SystemInfoVO selectSystemInfo() throws Exception;

	public int insertSystemInfo(SystemInfoVO pSystemInfoVO) throws Exception;

	public int updateSystemInfo(SystemInfoVO pSystemInfoVO) throws Exception;

}
