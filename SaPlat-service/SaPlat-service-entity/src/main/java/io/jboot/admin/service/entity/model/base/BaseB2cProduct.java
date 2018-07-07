package io.jboot.admin.service.entity.model.base;

import io.jboot.db.model.JbootModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by Jboot, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseB2cProduct<M extends BaseB2cProduct<M>> extends JbootModel<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public void setProdDesc(java.lang.String prodDesc) {
		set("prodDesc", prodDesc);
	}
	
	public java.lang.String getProdDesc() {
		return getStr("prodDesc");
	}

	public void setTotalNum(java.lang.Integer totalNum) {
		set("totalNum", totalNum);
	}
	
	public java.lang.Integer getTotalNum() {
		return getInt("totalNum");
	}

	public void setOrderedNum(java.lang.Integer orderedNum) {
		set("orderedNum", orderedNum);
	}
	
	public java.lang.Integer getOrderedNum() {
		return getInt("orderedNum");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}
	
	public java.lang.String getStatus() {
		return getStr("status");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("createDate", createDate);
	}
	
	public java.util.Date getCreateDate() {
		return get("createDate");
	}

	public void setLastUpdAcct(java.lang.String lastUpdAcct) {
		set("lastUpdAcct", lastUpdAcct);
	}
	
	public java.lang.String getLastUpdAcct() {
		return getStr("lastUpdAcct");
	}

	public void setLastUpdTime(java.util.Date lastUpdTime) {
		set("lastUpdTime", lastUpdTime);
	}
	
	public java.util.Date getLastUpdTime() {
		return get("lastUpdTime");
	}

	public void setNote(java.lang.String note) {
		set("note", note);
	}
	
	public java.lang.String getNote() {
		return getStr("note");
	}

}
