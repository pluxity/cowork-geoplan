/**
 *
 */
package com.plx.app.admin.vo;

import java.io.Serializable;

import com.plx.app.cmn.vo.BaseVO;

import lombok.Data;

/**
 * @Project KNIS
 * @Class SystemInfoVO
 * @since 2020. 07. 21.
 * @author 이유리
 * @Description : 사용자 설정 vo
 */
@Data
@SuppressWarnings("serial")
public class SystemInfoVO extends BaseVO implements Serializable  {

	/**
	 * Constructor
	 */
	public SystemInfoVO() {
	}

	/**
	 * Field
	 */
	private Integer poiLength;
	private Integer poiIconRatio;
	private Integer poiTextRatio;
}
