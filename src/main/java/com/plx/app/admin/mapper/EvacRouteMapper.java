package com.plx.app.admin.mapper;

import com.plx.app.admin.vo.EvacRouteVO;

/**
 *  @Project SKT_TSOP
 *  @Class VirtualPatrolMapper
 *  @since 2019. 10. 31.
 *  @author NEWBIE
 *  @Description : 가상순찰 정보
 */
public interface EvacRouteMapper {

	public EvacRouteVO selectEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;
	public int insertEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;
	public int updateEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;
	public int deleteEvacRoute(EvacRouteVO pEvacRouteVO) throws Exception;

}