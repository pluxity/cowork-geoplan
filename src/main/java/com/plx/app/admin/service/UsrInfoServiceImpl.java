package com.plx.app.admin.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.mapper.UsrInfoMapper;
import com.plx.app.admin.vo.UsrInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *  @Project KNIS
 *  @Class UsrInfoServiceImpl
 *  @since 2019. 12. 17.
 *  @author 류중규
 *  @Description : 사용자 정보
 */
@Service
@Configurable
public class UsrInfoServiceImpl implements UsrInfoService {

	/**
	 * 사용자정보 mapper
	 */
	@Autowired
	UsrInfoMapper usrInfoMapper;

	/**
	 * 사용자 목록
	 */
	public Map<String, Object> selectUsrInfoList(UsrInfoVO pUsrInfoVO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// 사용자 목록
		List<UsrInfoVO> list = usrInfoMapper.selectUsrInfoList(pUsrInfoVO);
		int total = usrInfoMapper.selectUsrInfoTotal(pUsrInfoVO);

		map.put("list", list);
		map.put("total", total);

		return map;
	}

	/**
	 * 사용자 정보
	 */
	public UsrInfoVO selectUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {
		return usrInfoMapper.selectUsrInfo(pUsrInfoVO);
	}

	public int insertUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {
		return usrInfoMapper.insertUsrInfo(pUsrInfoVO);
	}

	public int updateUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {
		if(!StringUtils.isEmpty(pUsrInfoVO.getUsrPwd()) || pUsrInfoVO.isChangePwdFlag()){			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pUsrInfoVO.setLastPwdChangeDt(sdf.format(new Date()));
		}
		return usrInfoMapper.updateUsrInfo(pUsrInfoVO);
	}

	public int deleteUsrInfo(UsrInfoVO pUsrInfoVO) throws Exception {
		return usrInfoMapper.deleteUsrInfo(pUsrInfoVO);
	}

}
