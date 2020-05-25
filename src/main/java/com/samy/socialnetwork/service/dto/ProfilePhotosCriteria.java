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
 * Criteria class for the {@link com.samy.socialnetwork.domain.ProfilePhotos} entity. This class is used
 * in {@link com.samy.socialnetwork.web.rest.ProfilePhotosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /profile-photos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfilePhotosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter uploadedDate;

    private StringFilter title;

    private LongFilter userProfileId;

    public ProfilePhotosCriteria() {
    }

    public ProfilePhotosCriteria(ProfilePhotosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uploadedDate = other.uploadedDate == null ? null : other.uploadedDate.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
    }

    @Override
    public ProfilePhotosCriteria copy() {
        return new ProfilePhotosCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(InstantFilter uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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
        final ProfilePhotosCriteria that = (ProfilePhotosCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uploadedDate, that.uploadedDate) &&
            Objects.equals(title, that.title) &&
            Objects.equals(userProfileId, that.userProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uploadedDate,
        title,
        userProfileId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilePhotosCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uploadedDate != null ? "uploadedDate=" + uploadedDate + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
            "}";
    }

}
