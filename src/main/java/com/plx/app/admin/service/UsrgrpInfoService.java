package com.plx.app.admin.service;

import java.util.List;
import java.util.Map;

import com.plx.app.admin.vo.UsrgrpInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.admin.vo.UsrgrpPoiVO;

/**
 *  @Project KNIS
 *  @Class UsrgrpInfoService
 *  @since 2019. 12. 17.
 *  @author 류중규
 *  @Description : 사용자그룹 정보
 */
public interface UsrgrpInfoService {

	/**
	 * @Method selectUsrgrpInfoList
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return Map<String,Object>
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 목록
	 */
	public Map<String, Object> selectUsrgrpInfoList(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	/**
	 * @Method selectUsrgrpInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return UsrgrpInfoVO
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 정보
	 */
	public UsrgrpInfoVO selectUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	/**
	 * @Method insertUsrgrpInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 등록
	 */
	public int insertUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	/**
	 * @Method updateUsrgrpInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 수정
	 */
	public int updateUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	/**
	 * @Method deleteUsrgrpInfo
	 * @since  2019. 12. 17.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpInfoVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 삭제
	 */
	public int deleteUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	/**
	 * @Method selectUsrgrpMapList
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return List<UsrgrpMapVO>
	 * @param pUsrgrpMapVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 도면권한 목록
	 */
	public List<UsrgrpMapVO> selectUsrgrpMapList(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	/**
	 * @Method insertUsrgrpMap
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpMapVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 도면권한 등록
	 */
	public int insertUsrgrpMap(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	/**
	 * @Method deleteUsrgrpMap
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpMapVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 도면권한 삭제
	 */
	public int deleteUsrgrpMap(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	/**
	 * @Method selectUsrgrpPoiList
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return List<UsrgrpPoiVO>
	 * @param pUsrgrpPoiVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 poi 권한 목록
	 */
	public List<UsrgrpPoiVO> selectUsrgrpPoiList(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception;

	/**
	 * @Method insertUsrgrpPoi
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpPoiVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 poi 권한 등록
	 */
	public int insertUsrgrpPoi(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception;

	/**
	 * @Method deleteUsrgrpPoi
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpPoiVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 poi 권한 삭제
	 */
	public int deleteUsrgrpPoi(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception;

	/**
	 * @Method selectUsrgrpMenuList
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return List<UsrgrpMenuVO>
	 * @param pUsrgrpMenuVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 메뉴 목록
	 */
	public List<UsrgrpMenuVO> selectUsrgrpMenuList(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception;

	/**
	 * @Method insertUsrgrpMenu
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpMenuVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 메뉴 등록
	 */
	public int insertUsrgrpMenu(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception;

	/**
	 * @Method deleteUsrgrpMenu
	 * @since  2020. 1. 3.
	 * @author 류중규
	 * @return int
	 * @param pUsrgrpMenuVO
	 * @return
	 * @throws Exception
	 * @description 사용자그룹 메뉴 삭제
	 */
	public int deleteUsrgrpMenu(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception;

}
