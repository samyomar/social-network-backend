package com.samy.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ProfilePhotos.
 */
@Entity
@Table(name = "profile_photos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProfilePhotos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @NotNull
    @Column(name = "uploaded_date", nullable = false)
    private Instant uploadedDate;

    @Column(name = "title")
    private String title;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "photos", allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public ProfilePhotos image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public ProfilePhotos imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Instant getUploadedDate() {
        return uploadedDate;
    }

    public ProfilePhotos uploadedDate(Instant uploadedDate) {
        this.uploadedDate = uploadedDate;
        return this;
    }

    public void setUploadedDate(Instant uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getTitle() {
        return title;
    }

    public ProfilePhotos title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public ProfilePhotos userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfilePhotos)) {
            return false;
        }
        return id != null && id.equals(((ProfilePhotos) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilePhotos{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", uploadedDate='" + getUploadedDate() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
