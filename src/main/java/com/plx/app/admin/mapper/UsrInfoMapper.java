package com.plx.app.admin.mapper;

import java.util.List;

import com.plx.app.admin.vo.SystemInfoVO;
import com.plx.app.admin.vo.UsrInfoVO;

/**
 *  @Project KNIS
 *  @Class UsrInfoMapper
 *  @since 2019. 12. 17.
 *  @author 류중규
 *  @Description : 사용자 정보
 */
public interface UsrInfoMapper {

	public List<UsrInfoVO> selectUsrInfoList(UsrInfoVO pUsrInfoVO) throws Exception;

	public int selectUsrInfoTotal(UsrInfoVO pUsrInfoVO) throws Exception;

	public UsrInfoVO selectUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

	public int insertUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

	public int updateUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

	public int deleteUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception;

}
