package com.plx.app.admin.service;

import com.plx.app.admin.mapper.EvacRouteMapper;
import com.plx.app.admin.vo.EvacRouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *  @Project SKT_TSOP
 *  @Class VirtualPatrolServiceImpl
 *  @since 2019. 10. 31.
 *  @author NEWBIE
 *  @Description :가상순찰 service
 */
@Service
public class EvacRouteServiceImpl implements EvacRouteService {

	/**
	 * 층정보 mapper
	 */
	@Autowired
	EvacRouteMapper evacRouteMapper;

	@Override
	public int insertEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception {
		return evacRouteMapper.insertEvacRoute(pEvacRouteVO);
	}

	@Override
	public int updateEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception {
		return evacRouteMapper.updateEvacRoute(pEvacRouteVO);
	}

	@Override
	public int deleteEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception {
		return evacRouteMapper.deleteEvacRoute(pEvacRouteVO);
	}

	@Override
	public EvacRouteVO selectEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception {
		return evacRouteMapper.selectEvacRoute(pEvacRouteVO);
	}

	@Override
	public int upsertEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception {

		EvacRouteVO route = selectEvacRoute(pEvacRouteVO);
		int res = 0;
		if(route == null){
			res = insertEvacRoute(pEvacRouteVO);
		} else {
			res = updateEvacRoute(pEvacRouteVO);
		}		
	
		return res;
	}

}
