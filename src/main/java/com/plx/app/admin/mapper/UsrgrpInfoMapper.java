package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.UsrgrpInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.admin.vo.UsrgrpPoiVO;

/**
 *  @Project KNIS
 *  @Class UsrgrpInfoMapper
 *  @since 2019. 12. 17.
 *  @author 류중규
 *  @Description : 사용자그룹 정보
 */
public interface UsrgrpInfoMapper {

	public List<UsrgrpInfoVO> selectUsrgrpInfoList(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	public int selectUsrgrpInfoTotal(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	public UsrgrpInfoVO selectUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	public int insertUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	public int updateUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	public int deleteUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception;

	public List<UsrgrpMapVO> selectUsrgrpMapList(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	public int insertUsrgrpMap(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	public int deleteUsrgrpMap(UsrgrpMapVO pUsrgrpMapVO) throws Exception;

	public List<UsrgrpPoiVO> selectUsrgrpPoiList(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception;

	public int insertUsrgrpPoi(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception;

	public int deleteUsrgrpPoi(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception;

	public List<UsrgrpMenuVO> selectUsrgrpMenuList(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception;

	public int insertUsrgrpMenu(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception;

	public int deleteUsrgrpMenu(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception;

}
