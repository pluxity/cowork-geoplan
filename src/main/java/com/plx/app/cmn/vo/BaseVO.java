package com.plx.app.cmn.vo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 *  @Project KNIS
 *  @Class BaseVO
 *  @since 2019. 9. 5.
 *  @author redmoonk
 *  @Description : 기본 공통 VO
 */
@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseVO implements Serializable {

//	public Class getVOClass() throws Exception {
//		return this.getClass();
//	}
	@JsonIgnore
	public HashMap<String, Object> getMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			for (Class<?> c = this.getClass(); c != null; c = c.getSuperclass()) {
				Field fList[] = c.getDeclaredFields();
				for (Field field : fList) {
					if (field.getModifiers() < Modifier.STATIC) {
						field.setAccessible(true);
						map.put(field.getName(), field.get(this));
					}
				}
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
		return map;
	}
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String regUsr = "";			/* 등록자 아이디 */
	//@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)	
	private String regDt;					/* 등록일 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String updUsr = "";			/* 수정자 아이디 */
	//@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String updDt;					/* 수정일 */

	/* 페이징 검색 관련 */
//	@JsonIgnore
	private int page = 1;				/* 페이지 인덱스 */
//	@JsonIgnore
    private int pageSize = 12;			/* 페이지당 게시물수 */
    @JsonIgnore
    private int offset;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String searchType = "";
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String searchKeyword = "";
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String sortType = ""; //ASC, DESC
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String sortBy = ""; //sorting 항목

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int grpNo;

	public String getRegUsr() {
		return regUsr;
	}
	public void setRegUsr(String regUsr) {
		this.regUsr = regUsr;
	}
	public String getUpdUsr() {
		return updUsr;
	}
	public void setUpdUsr(String updUsr) {
		this.updUsr = updUsr;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getSortBy(){
		return sortBy;
	}
	public void setSortBy(String sortBy){
		this.sortBy = sortBy;
	}
	public String getSortType(){
		return sortType;
	}
	public void setSortType(String sortType){
		this.sortType = sortType;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUpdDt() {
		return updDt;
	}
	public void setUpdDt(String updDt) {
		this.updDt = updDt;
	}
	public int getGrpNo() {
		return grpNo;
	}
	public void setGrpNo(int grpNo) {
		this.grpNo = grpNo;
	}

}
