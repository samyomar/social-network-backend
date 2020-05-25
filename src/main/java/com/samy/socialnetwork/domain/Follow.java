package com.samy.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Follow.
 */
@Entity
@Table(name = "follow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "follow_date", nullable = false)
    private Instant followDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "followings", allowSetters = true)
    private UserProfile userProfile;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "followers", allowSetters = true)
    private UserProfile follower;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFollowDate() {
        return followDate;
    }

    public Follow followDate(Instant followDate) {
        this.followDate = followDate;
        return this;
    }

    public void setFollowDate(Instant followDate) {
        this.followDate = followDate;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public Follow userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getFollower() {
        return follower;
    }

    public Follow follower(UserProfile userProfile) {
        this.follower = userProfile;
        return this;
    }

    public void setFollower(UserProfile userProfile) {
        this.follower = userProfile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Follow)) {
            return false;
        }
        return id != null && id.equals(((Follow) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Follow{" +
            "id=" + getId() +
            ", followDate='" + getFollowDate() + "'" +
            "}";
    }
}
