package com.samy.socialnetwork.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.samy.socialnetwork.domain.enumeration.Gender;

import com.samy.socialnetwork.domain.enumeration.Language;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Min(value = 12)
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "native_lang", nullable = false)
    private Language nativeLang;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProfilePhotos> photos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserProfile firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserProfile lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public UserProfile age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public UserProfile gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public UserProfile country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Language getNativeLang() {
        return nativeLang;
    }

    public UserProfile nativeLang(Language nativeLang) {
        this.nativeLang = nativeLang;
        return this;
    }

    public void setNativeLang(Language nativeLang) {
        this.nativeLang = nativeLang;
    }

    public User getUser() {
        return user;
    }

    public UserProfile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Follow> getFollowings() {
        return followings;
    }

    public UserProfile followings(Set<Follow> follows) {
        this.followings = follows;
        return this;
    }

    public UserProfile addFollowings(Follow follow) {
        this.followings.add(follow);
        follow.setUserProfile(this);
        return this;
    }

    public UserProfile removeFollowings(Follow follow) {
        this.followings.remove(follow);
        follow.setUserProfile(null);
        return this;
    }

    public void setFollowings(Set<Follow> follows) {
        this.followings = follows;
    }

    public Set<Follow> getFollowers() {
        return followers;
    }

    public UserProfile followers(Set<Follow> follows) {
        this.followers = follows;
        return this;
    }

    public UserProfile addFollowers(Follow follow) {
        this.followers.add(follow);
        follow.setFollower(this);
        return this;
    }

    public UserProfile removeFollowers(Follow follow) {
        this.followers.remove(follow);
        follow.setFollower(null);
        return this;
    }

    public void setFollowers(Set<Follow> follows) {
        this.followers = follows;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public UserProfile posts(Set<Post> posts) {
        this.posts = posts;
        return this;
    }

    public UserProfile addPosts(Post post) {
        this.posts.add(post);
        post.setUserProfile(this);
        return this;
    }

    public UserProfile removePosts(Post post) {
        this.posts.remove(post);
        post.setUserProfile(null);
        return this;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<ProfilePhotos> getPhotos() {
        return photos;
    }

    public UserProfile photos(Set<ProfilePhotos> profilePhotos) {
        this.photos = profilePhotos;
        return this;
    }

    public UserProfile addPhotos(ProfilePhotos profilePhotos) {
        this.photos.add(profilePhotos);
        profilePhotos.setUserProfile(this);
        return this;
    }

    public UserProfile removePhotos(ProfilePhotos profilePhotos) {
        this.photos.remove(profilePhotos);
        profilePhotos.setUserProfile(null);
        return this;
    }

    public void setPhotos(Set<ProfilePhotos> profilePhotos) {
        this.photos = profilePhotos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return id != null && id.equals(((UserProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", age=" + getAge() +
            ", gender='" + getGender() + "'" +
            ", country='" + getCountry() + "'" +
            ", nativeLang='" + getNativeLang() + "'" +
            "}";
    }
}
