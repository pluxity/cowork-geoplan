package com.plx.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plx.app.admin.mapper.SystemInfoMapper;
import com.plx.app.admin.vo.SystemInfoVO;

/**
 *  @Project KNIS
 *  @Class SystemInfoServiceImpl
 *  @since 2020. 07. 23.
 *  @author 이유리
 *  @Description : 시스템 정보
 */
@Service
@RequiredArgsConstructor
public class SystemInfoServiceImpl implements SystemInfoService {

	/**
	 * 시스템 정보 mapper
	 */
	private final SystemInfoMapper systemInfoMapper;

	public SystemInfoVO selectSystemInfo() throws Exception {
		if (systemInfoMapper.selectSystemInfo() == null) {
				SystemInfoVO systemInfoVO = new SystemInfoVO();
				systemInfoVO.setPoiLength(30);
				systemInfoVO.setPoiIconRatio(300);
				systemInfoVO.setPoiTextRatio(150);
				systemInfoVO.setUpdUsr("admin");		

				systemInfoMapper.insertSystemInfo(systemInfoVO);
				return systemInfoVO;
		}
		return systemInfoMapper.selectSystemInfo();
	}

	public int insertSystemInfo(SystemInfoVO pSystemInfoVO) throws Exception{
		return systemInfoMapper.insertSystemInfo(pSystemInfoVO);
	}

	public int updateSystemInfo(SystemInfoVO pSystemInfoVO) throws Exception{
		return systemInfoMapper.updateSystemInfo(pSystemInfoVO);
	}
}
