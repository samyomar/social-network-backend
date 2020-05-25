package com.samy.socialnetwork.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.samy.socialnetwork.domain.enumeration.Gender;
import com.samy.socialnetwork.domain.enumeration.Language;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.samy.socialnetwork.domain.UserProfile} entity. This class is used
 * in {@link com.samy.socialnetwork.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserProfileCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }
    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {

        public LanguageFilter() {
        }

        public LanguageFilter(LanguageFilter filter) {
            super(filter);
        }

        @Override
        public LanguageFilter copy() {
            return new LanguageFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private IntegerFilter age;

    private GenderFilter gender;

    private StringFilter country;

    private LanguageFilter nativeLang;

    private LongFilter userId;

    private LongFilter followingsId;

    private LongFilter followersId;

    private LongFilter postsId;

    private LongFilter photosId;

    private LongFilter following2Id;

    private LongFilter follower2Id;

    public UserProfileCriteria() {
    }

    public UserProfileCriteria(UserProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.nativeLang = other.nativeLang == null ? null : other.nativeLang.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.followingsId = other.followingsId == null ? null : other.followingsId.copy();
        this.followersId = other.followersId == null ? null : other.followersId.copy();
        this.postsId = other.postsId == null ? null : other.postsId.copy();
        this.photosId = other.photosId == null ? null : other.photosId.copy();
        this.following2Id = other.following2Id == null ? null : other.following2Id.copy();
        this.follower2Id = other.follower2Id == null ? null : other.follower2Id.copy();
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public LanguageFilter getNativeLang() {
        return nativeLang;
    }

    public void setNativeLang(LanguageFilter nativeLang) {
        this.nativeLang = nativeLang;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getFollowingsId() {
        return followingsId;
    }

    public void setFollowingsId(LongFilter followingsId) {
        this.followingsId = followingsId;
    }

    public LongFilter getFollowersId() {
        return followersId;
    }

    public void setFollowersId(LongFilter followersId) {
        this.followersId = followersId;
    }

    public LongFilter getPostsId() {
        return postsId;
    }

    public void setPostsId(LongFilter postsId) {
        this.postsId = postsId;
    }

    public LongFilter getPhotosId() {
        return photosId;
    }

    public void setPhotosId(LongFilter photosId) {
        this.photosId = photosId;
    }

    public LongFilter getFollowing2Id() {
        return following2Id;
    }

    public void setFollowing2Id(LongFilter following2Id) {
        this.following2Id = following2Id;
    }

    public LongFilter getFollower2Id() {
        return follower2Id;
    }

    public void setFollower2Id(LongFilter follower2Id) {
        this.follower2Id = follower2Id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(age, that.age) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(country, that.country) &&
            Objects.equals(nativeLang, that.nativeLang) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(followingsId, that.followingsId) &&
            Objects.equals(followersId, that.followersId) &&
            Objects.equals(postsId, that.postsId) &&
            Objects.equals(photosId, that.photosId) &&
            Objects.equals(following2Id, that.following2Id) &&
            Objects.equals(follower2Id, that.follower2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        age,
        gender,
        country,
        nativeLang,
        userId,
        followingsId,
        followersId,
        postsId,
        photosId,
        following2Id,
        follower2Id
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (age != null ? "age=" + age + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (nativeLang != null ? "nativeLang=" + nativeLang + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (followingsId != null ? "followingsId=" + followingsId + ", " : "") +
                (followersId != null ? "followersId=" + followersId + ", " : "") +
                (postsId != null ? "postsId=" + postsId + ", " : "") +
                (photosId != null ? "photosId=" + photosId + ", " : "") +
                (following2Id != null ? "following2Id=" + following2Id + ", " : "") +
                (follower2Id != null ? "follower2Id=" + follower2Id + ", " : "") +
            "}";
    }

}
