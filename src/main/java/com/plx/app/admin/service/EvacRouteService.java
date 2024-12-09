package com.plx.app.admin.service;

import com.plx.app.admin.vo.EvacRouteVO;

/**
 *  @Project SKT_TSOP
 *  @Class EvacRouteService
 *  @since 2020. 3. 2.
 *  @author NEWBIE
 *  @Description : 대피경로 서비스
 */

public interface EvacRouteService {

	public EvacRouteVO selectEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;

	public int insertEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;

	public int updateEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;

	public int upsertEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;

	public int deleteEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;	

	
}
