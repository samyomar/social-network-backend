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
 * Criteria class for the {@link com.samy.socialnetwork.domain.Post} entity. This class is used
 * in {@link com.samy.socialnetwork.web.rest.PostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter postDate;

    private StringFilter postContent;

    private LongFilter userProfileId;

    public PostCriteria() {
    }

    public PostCriteria(PostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.postDate = other.postDate == null ? null : other.postDate.copy();
        this.postContent = other.postContent == null ? null : other.postContent.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
    }

    @Override
    public PostCriteria copy() {
        return new PostCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getPostDate() {
        return postDate;
    }

    public void setPostDate(InstantFilter postDate) {
        this.postDate = postDate;
    }

    public StringFilter getPostContent() {
        return postContent;
    }

    public void setPostContent(StringFilter postContent) {
        this.postContent = postContent;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PostCriteria that = (PostCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(postDate, that.postDate) &&
            Objects.equals(postContent, that.postContent) &&
            Objects.equals(userProfileId, that.userProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        postDate,
        postContent,
        userProfileId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (postDate != null ? "postDate=" + postDate + ", " : "") +
                (postContent != null ? "postContent=" + postContent + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
            "}";
    }

}
