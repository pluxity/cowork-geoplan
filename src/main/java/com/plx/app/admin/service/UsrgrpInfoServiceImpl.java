package com.plx.app.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plx.app.admin.mapper.TopologyInfoMapper;
import com.plx.app.admin.mapper.UsrgrpInfoMapper;
import com.plx.app.admin.vo.UsrgrpInfoVO;
import com.plx.app.admin.vo.UsrgrpMapVO;
import com.plx.app.admin.vo.UsrgrpMenuVO;
import com.plx.app.admin.vo.UsrgrpPoiVO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

/**
 *  @Project KNIS
 *  @Class UsrgrpInfoServiceImpl
 *  @since 2019. 12. 17.
 *  @author 류중규
 *  @Description : 사용자그룹 정보
 */
@Service
@Configurable
@RequiredArgsConstructor
public class UsrgrpInfoServiceImpl implements UsrgrpInfoService {

	/**
	 * 사용자그룹정보 mapper
	 */
	private final UsrgrpInfoMapper usrgrpInfoMapper;

	/**
	 * 사용자그룹 목록
	 */
	public Map<String, Object> selectUsrgrpInfoList(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		// 사용자그룹 목록
		List<UsrgrpInfoVO> list = usrgrpInfoMapper.selectUsrgrpInfoList(pUsrgrpInfoVO);
		int total = usrgrpInfoMapper.selectUsrgrpInfoTotal(pUsrgrpInfoVO);

		map.put("list", list);
		map.put("total", total);

		return map;
	}

	/**
	 * 사용자그룹 정보
	 */
	public UsrgrpInfoVO selectUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {
		return usrgrpInfoMapper.selectUsrgrpInfo(pUsrgrpInfoVO);
	}

	public int insertUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {
		return usrgrpInfoMapper.insertUsrgrpInfo(pUsrgrpInfoVO);
	}

	public int updateUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {
		return usrgrpInfoMapper.updateUsrgrpInfo(pUsrgrpInfoVO);
	}

	public int deleteUsrgrpInfo(UsrgrpInfoVO pUsrgrpInfoVO) throws Exception {
		return usrgrpInfoMapper.deleteUsrgrpInfo(pUsrgrpInfoVO);
	}

	public List<UsrgrpMapVO> selectUsrgrpMapList(UsrgrpMapVO pUsrgrpMapVO) throws Exception {
		return usrgrpInfoMapper.selectUsrgrpMapList(pUsrgrpMapVO);
	}

	public int insertUsrgrpMap(UsrgrpMapVO pUsrgrpMapVO) throws Exception {
		return usrgrpInfoMapper.insertUsrgrpMap(pUsrgrpMapVO);
	}

	public int deleteUsrgrpMap(UsrgrpMapVO pUsrgrpMapVO) throws Exception {
		return usrgrpInfoMapper.deleteUsrgrpMap(pUsrgrpMapVO);
	}

	public List<UsrgrpPoiVO> selectUsrgrpPoiList(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception {
		return usrgrpInfoMapper.selectUsrgrpPoiList(pUsrgrpPoiVO);
	}

	public int insertUsrgrpPoi(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception {
		return usrgrpInfoMapper.insertUsrgrpPoi(pUsrgrpPoiVO);
	}

	public int deleteUsrgrpPoi(UsrgrpPoiVO pUsrgrpPoiVO) throws Exception {
		return usrgrpInfoMapper.deleteUsrgrpPoi(pUsrgrpPoiVO);
	}

	public List<UsrgrpMenuVO> selectUsrgrpMenuList(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception {
		return usrgrpInfoMapper.selectUsrgrpMenuList(pUsrgrpMenuVO);
	}

	public int insertUsrgrpMenu(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception {
		return usrgrpInfoMapper.insertUsrgrpMenu(pUsrgrpMenuVO);
	}

	public int deleteUsrgrpMenu(UsrgrpMenuVO pUsrgrpMenuVO) throws Exception {
		return usrgrpInfoMapper.deleteUsrgrpMenu(pUsrgrpMenuVO);
	}

}
