package io.jboot.admin.service.entity.model;

import io.jboot.db.annotation.Table;
import io.jboot.admin.service.entity.model.base.BaseReviewGroup;

/**
 * Generated by Jboot.
 */
@Table(tableName = "review_group", primaryKey = "id")
public class ReviewGroup extends BaseReviewGroup<ReviewGroup> {

    private boolean isInvite;

    public void setIsInvite(boolean isInvite) {
        this.isInvite = isInvite;
    }

    public boolean getIsInvite() {
        return this.isInvite;
    }

}
