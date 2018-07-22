package io.jboot.admin.service.entity.model.base;

import io.jboot.db.model.JbootModel;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by Jboot, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseImpTeam<M extends BaseImpTeam<M>> extends JbootModel<M> implements IBean {

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

    public void setSpell(java.lang.String spell) {
        set("spell", spell);
    }

    public java.lang.String getSpell() {
        return getStr("spell");
    }

    public void setProjectID(java.lang.Long projectID) {
        set("projectID", projectID);
    }

    public java.lang.Long getProjectID() {
        return getLong("projectID");
    }

    public void setLeaderID(java.lang.Long leaderID) {
        set("leaderID", leaderID);
    }

    public java.lang.Long getLeaderID() {
        return getLong("leaderID");
    }

    public void setAssLeaderIDs(java.lang.String assLeaderIDs) {
        set("assLeaderIDs", assLeaderIDs);
    }

    public java.lang.String getAssLeaderIDs() {
        return getStr("assLeaderIDs");
    }

    public void setExpertGroupIDs(java.lang.String expertGroupIDs) {
        set("expertGroupIDs", expertGroupIDs);
    }

    public java.lang.String getExpertGroupIDs() {
        return getStr("expertGroupIDs");
    }

    public void setInvTeamIDs(java.lang.String invTeamIDs) {
        set("invTeamIDs", invTeamIDs);
    }

    public java.lang.String getInvTeamIDs() {
        return getStr("invTeamIDs");
    }

    public void setRepTeamIDs(java.lang.String repTeamIDs) {
        set("repTeamIDs", repTeamIDs);
    }

    public java.lang.String getRepTeamIDs() {
        return getStr("repTeamIDs");
    }

    public void setCreateUserID(java.lang.Long createUserID) {
        set("createUserID", createUserID);
    }

    public java.lang.Long getCreateUserID() {
        return getLong("createUserID");
    }

    public void setCreateTime(java.util.Date createTime) {
        set("createTime", createTime);
    }

    public java.util.Date getCreateTime() {
        return get("createTime");
    }

    public void setLastUpdateUserID(java.lang.Long lastUpdateUserID) {
        set("lastUpdateUserID", lastUpdateUserID);
    }

    public java.lang.Long getLastUpdateUserID() {
        return getLong("lastUpdateUserID");
    }

    public void setLastAccessTime(java.util.Date lastAccessTime) {
        set("lastAccessTime", lastAccessTime);
    }

    public java.util.Date getLastAccessTime() {
        return get("lastAccessTime");
    }

    public void setSort(java.lang.Integer sort) {
        set("sort", sort);
    }

    public java.lang.Integer getSort() {
        return getInt("sort");
    }

    public void setRemark(java.lang.String remark) {
        set("remark", remark);
    }

    public java.lang.String getRemark() {
        return getStr("remark");
    }

    public void setIsEnable(java.lang.Boolean isEnable) {
        set("isEnable", isEnable);
    }

    public java.lang.Boolean getIsEnable() {
        return get("isEnable");
    }

}
