package com.samy.socialnetwork.web.rest;

import com.samy.socialnetwork.SocialNetworkBackendApp;
import com.samy.socialnetwork.domain.ProfilePhotos;
import com.samy.socialnetwork.domain.UserProfile;
import com.samy.socialnetwork.repository.ProfilePhotosRepository;
import com.samy.socialnetwork.service.ProfilePhotosService;
import com.samy.socialnetwork.service.dto.ProfilePhotosCriteria;
import com.samy.socialnetwork.service.ProfilePhotosQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProfilePhotosResource} REST controller.
 */
@SpringBootTest(classes = SocialNetworkBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfilePhotosResourceIT {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_UPLOADED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private ProfilePhotosRepository profilePhotosRepository;

    @Autowired
    private ProfilePhotosService profilePhotosService;

    @Autowired
    private ProfilePhotosQueryService profilePhotosQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfilePhotosMockMvc;

    private ProfilePhotos profilePhotos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilePhotos createEntity(EntityManager em) {
        ProfilePhotos profilePhotos = new ProfilePhotos()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .uploadedDate(DEFAULT_UPLOADED_DATE)
            .title(DEFAULT_TITLE);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        profilePhotos.setUserProfile(userProfile);
        return profilePhotos;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfilePhotos createUpdatedEntity(EntityManager em) {
        ProfilePhotos profilePhotos = new ProfilePhotos()
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .title(UPDATED_TITLE);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        profilePhotos.setUserProfile(userProfile);
        return profilePhotos;
    }

    @BeforeEach
    public void initTest() {
        profilePhotos = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfilePhotos() throws Exception {
        int databaseSizeBeforeCreate = profilePhotosRepository.findAll().size();
        // Create the ProfilePhotos
        restProfilePhotosMockMvc.perform(post("/api/profile-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilePhotos)))
            .andExpect(status().isCreated());

        // Validate the ProfilePhotos in the database
        List<ProfilePhotos> profilePhotosList = profilePhotosRepository.findAll();
        assertThat(profilePhotosList).hasSize(databaseSizeBeforeCreate + 1);
        ProfilePhotos testProfilePhotos = profilePhotosList.get(profilePhotosList.size() - 1);
        assertThat(testProfilePhotos.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProfilePhotos.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProfilePhotos.getUploadedDate()).isEqualTo(DEFAULT_UPLOADED_DATE);
        assertThat(testProfilePhotos.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createProfilePhotosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profilePhotosRepository.findAll().size();

        // Create the ProfilePhotos with an existing ID
        profilePhotos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfilePhotosMockMvc.perform(post("/api/profile-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilePhotos)))
            .andExpect(status().isBadRequest());

        // Validate the ProfilePhotos in the database
        List<ProfilePhotos> profilePhotosList = profilePhotosRepository.findAll();
        assertThat(profilePhotosList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUploadedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = profilePhotosRepository.findAll().size();
        // set the field null
        profilePhotos.setUploadedDate(null);

        // Create the ProfilePhotos, which fails.


        restProfilePhotosMockMvc.perform(post("/api/profile-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilePhotos)))
            .andExpect(status().isBadRequest());

        List<ProfilePhotos> profilePhotosList = profilePhotosRepository.findAll();
        assertThat(profilePhotosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfilePhotos() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList
        restProfilePhotosMockMvc.perform(get("/api/profile-photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilePhotos.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].uploadedDate").value(hasItem(DEFAULT_UPLOADED_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }
    
    @Test
    @Transactional
    public void getProfilePhotos() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get the profilePhotos
        restProfilePhotosMockMvc.perform(get("/api/profile-photos/{id}", profilePhotos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profilePhotos.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.uploadedDate").value(DEFAULT_UPLOADED_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }


    @Test
    @Transactional
    public void getProfilePhotosByIdFiltering() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        Long id = profilePhotos.getId();

        defaultProfilePhotosShouldBeFound("id.equals=" + id);
        defaultProfilePhotosShouldNotBeFound("id.notEquals=" + id);

        defaultProfilePhotosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfilePhotosShouldNotBeFound("id.greaterThan=" + id);

        defaultProfilePhotosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfilePhotosShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProfilePhotosByUploadedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where uploadedDate equals to DEFAULT_UPLOADED_DATE
        defaultProfilePhotosShouldBeFound("uploadedDate.equals=" + DEFAULT_UPLOADED_DATE);

        // Get all the profilePhotosList where uploadedDate equals to UPDATED_UPLOADED_DATE
        defaultProfilePhotosShouldNotBeFound("uploadedDate.equals=" + UPDATED_UPLOADED_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByUploadedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where uploadedDate not equals to DEFAULT_UPLOADED_DATE
        defaultProfilePhotosShouldNotBeFound("uploadedDate.notEquals=" + DEFAULT_UPLOADED_DATE);

        // Get all the profilePhotosList where uploadedDate not equals to UPDATED_UPLOADED_DATE
        defaultProfilePhotosShouldBeFound("uploadedDate.notEquals=" + UPDATED_UPLOADED_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByUploadedDateIsInShouldWork() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where uploadedDate in DEFAULT_UPLOADED_DATE or UPDATED_UPLOADED_DATE
        defaultProfilePhotosShouldBeFound("uploadedDate.in=" + DEFAULT_UPLOADED_DATE + "," + UPDATED_UPLOADED_DATE);

        // Get all the profilePhotosList where uploadedDate equals to UPDATED_UPLOADED_DATE
        defaultProfilePhotosShouldNotBeFound("uploadedDate.in=" + UPDATED_UPLOADED_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByUploadedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where uploadedDate is not null
        defaultProfilePhotosShouldBeFound("uploadedDate.specified=true");

        // Get all the profilePhotosList where uploadedDate is null
        defaultProfilePhotosShouldNotBeFound("uploadedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where title equals to DEFAULT_TITLE
        defaultProfilePhotosShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the profilePhotosList where title equals to UPDATED_TITLE
        defaultProfilePhotosShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where title not equals to DEFAULT_TITLE
        defaultProfilePhotosShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the profilePhotosList where title not equals to UPDATED_TITLE
        defaultProfilePhotosShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultProfilePhotosShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the profilePhotosList where title equals to UPDATED_TITLE
        defaultProfilePhotosShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where title is not null
        defaultProfilePhotosShouldBeFound("title.specified=true");

        // Get all the profilePhotosList where title is null
        defaultProfilePhotosShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilePhotosByTitleContainsSomething() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where title contains DEFAULT_TITLE
        defaultProfilePhotosShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the profilePhotosList where title contains UPDATED_TITLE
        defaultProfilePhotosShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProfilePhotosByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        profilePhotosRepository.saveAndFlush(profilePhotos);

        // Get all the profilePhotosList where title does not contain DEFAULT_TITLE
        defaultProfilePhotosShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the profilePhotosList where title does not contain UPDATED_TITLE
        defaultProfilePhotosShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllProfilePhotosByUserProfileIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserProfile userProfile = profilePhotos.getUserProfile();
        profilePhotosRepository.saveAndFlush(profilePhotos);
        Long userProfileId = userProfile.getId();

        // Get all the profilePhotosList where userProfile equals to userProfileId
        defaultProfilePhotosShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the profilePhotosList where userProfile equals to userProfileId + 1
        defaultProfilePhotosShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfilePhotosShouldBeFound(String filter) throws Exception {
        restProfilePhotosMockMvc.perform(get("/api/profile-photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profilePhotos.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].uploadedDate").value(hasItem(DEFAULT_UPLOADED_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));

        // Check, that the count call also returns 1
        restProfilePhotosMockMvc.perform(get("/api/profile-photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfilePhotosShouldNotBeFound(String filter) throws Exception {
        restProfilePhotosMockMvc.perform(get("/api/profile-photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfilePhotosMockMvc.perform(get("/api/profile-photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProfilePhotos() throws Exception {
        // Get the profilePhotos
        restProfilePhotosMockMvc.perform(get("/api/profile-photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfilePhotos() throws Exception {
        // Initialize the database
        profilePhotosService.save(profilePhotos);

        int databaseSizeBeforeUpdate = profilePhotosRepository.findAll().size();

        // Update the profilePhotos
        ProfilePhotos updatedProfilePhotos = profilePhotosRepository.findById(profilePhotos.getId()).get();
        // Disconnect from session so that the updates on updatedProfilePhotos are not directly saved in db
        em.detach(updatedProfilePhotos);
        updatedProfilePhotos
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .title(UPDATED_TITLE);

        restProfilePhotosMockMvc.perform(put("/api/profile-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfilePhotos)))
            .andExpect(status().isOk());

        // Validate the ProfilePhotos in the database
        List<ProfilePhotos> profilePhotosList = profilePhotosRepository.findAll();
        assertThat(profilePhotosList).hasSize(databaseSizeBeforeUpdate);
        ProfilePhotos testProfilePhotos = profilePhotosList.get(profilePhotosList.size() - 1);
        assertThat(testProfilePhotos.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProfilePhotos.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProfilePhotos.getUploadedDate()).isEqualTo(UPDATED_UPLOADED_DATE);
        assertThat(testProfilePhotos.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingProfilePhotos() throws Exception {
        int databaseSizeBeforeUpdate = profilePhotosRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfilePhotosMockMvc.perform(put("/api/profile-photos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profilePhotos)))
            .andExpect(status().isBadRequest());

        // Validate the ProfilePhotos in the database
        List<ProfilePhotos> profilePhotosList = profilePhotosRepository.findAll();
        assertThat(profilePhotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfilePhotos() throws Exception {
        // Initialize the database
        profilePhotosService.save(profilePhotos);

        int databaseSizeBeforeDelete = profilePhotosRepository.findAll().size();

        // Delete the profilePhotos
        restProfilePhotosMockMvc.perform(delete("/api/profile-photos/{id}", profilePhotos.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProfilePhotos> profilePhotosList = profilePhotosRepository.findAll();
        assertThat(profilePhotosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
