package com.samy.socialnetwork.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.samy.socialnetwork.domain.Follow} entity. This class is used
 * in {@link com.samy.socialnetwork.web.rest.FollowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /follows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FollowCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter followDate;

    private LongFilter userProfileId;

    private LongFilter followerId;

    public FollowCriteria() {
    }

    public FollowCriteria(FollowCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.followDate = other.followDate == null ? null : other.followDate.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
        this.followerId = other.followerId == null ? null : other.followerId.copy();
    }

    @Override
    public FollowCriteria copy() {
        return new FollowCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getFollowDate() {
        return followDate;
    }

    public void setFollowDate(InstantFilter followDate) {
        this.followDate = followDate;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getFollowerId() {
        return followerId;
    }

    public void setFollowerId(LongFilter followerId) {
        this.followerId = followerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FollowCriteria that = (FollowCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(followDate, that.followDate) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(followerId, that.followerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        followDate,
        userProfileId,
        followerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FollowCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (followDate != null ? "followDate=" + followDate + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
                (followerId != null ? "followerId=" + followerId + ", " : "") +
            "}";
    }

}
