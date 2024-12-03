package com.plx.app.admin.service;

import com.plx.app.admin.vo.SystemInfoVO;

/**
 *  @Project KNIS
 *  @Class SystemInfoService
 *  @since 2020. 07. 23.
 *  @author 이유리
 *  @Description : 시스템 정보
 */
public interface SystemInfoService {

	/**
	 * @Method selectSystemInfo
	 * @since  2020. 7. 21.
	 * @author 이유리
	 * @return SystemInfoVO
	 * @param pUsrInfoVO
	 * @throws Exception
	 * @description 시스템 설정 조회
	 */
	public SystemInfoVO selectSystemInfo() throws Exception;

	/**
	 * @Method insertSystemInfo
	 * @since  2020. 7. 21.
	 * @author 이유리
	 * @return int
	 * @param pSystemInfoVO
	 * @throws Exception
	 * @description 시스템 설정 입력
	 */
	public int insertSystemInfo(SystemInfoVO pSystemInfoVO) throws Exception;

	/**
	 * @Method updateSystemInfo
	 * @since  2020. 7. 21.
	 * @author 이유리
	 * @return int
	 * @param pSystemInfoVO
	 * @throws Exception
	 * @description 시스템 설정 수정
	 */
	public int updateSystemInfo(SystemInfoVO pSystemInfoVO) throws Exception;

}
