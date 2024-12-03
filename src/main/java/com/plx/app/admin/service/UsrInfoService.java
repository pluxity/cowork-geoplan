package com.plx.app.admin.service;

import java.util.Map;

import com.plx.app.admin.vo.UsrInfoVO;

/**
 *  @Project KNIS
 *  @Class UsrInfoService
 *  @since 2019. 12. 17.
 *  @author 류중규
 *  @Description : 사용자 정보
 */
public interface UsrInfoService {

	/**
	 * @Method selectUsrInfoList
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 목록
	 */
	public Map<String, Object> selectUsrInfoList(UsrInfoVO pUsrInfoVO) throws Exception;

	/**
	 * @Method selectUsrInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return UsrInfoVO
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 정보
	 */
	public UsrInfoVO selectUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

	/**
	 * @Method insertUsrInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return int
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 등록
	 */
	public int insertUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

	/**
	 * @Method updateUsrInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return int
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 수정
	 */
	public int updateUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

	/**
	 * @Method deleteUsrInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return int
	 * @param pUsrInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자 삭제
	 */
	public int deleteUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;


}
