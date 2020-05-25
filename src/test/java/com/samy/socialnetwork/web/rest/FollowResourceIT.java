package com.samy.socialnetwork.web.rest;

import com.samy.socialnetwork.SocialNetworkBackendApp;
import com.samy.socialnetwork.domain.Follow;
import com.samy.socialnetwork.domain.UserProfile;
import com.samy.socialnetwork.repository.FollowRepository;
import com.samy.socialnetwork.service.FollowService;
import com.samy.socialnetwork.service.dto.FollowCriteria;
import com.samy.socialnetwork.service.FollowQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FollowResource} REST controller.
 */
@SpringBootTest(classes = SocialNetworkBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FollowResourceIT {

    private static final Instant DEFAULT_FOLLOW_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FOLLOW_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowQueryService followQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFollowMockMvc;

    private Follow follow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Follow createEntity(EntityManager em) {
        Follow follow = new Follow()
            .followDate(DEFAULT_FOLLOW_DATE);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        follow.setUserProfile(userProfile);
        // Add required entity
        follow.setFollower(userProfile);
        return follow;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Follow createUpdatedEntity(EntityManager em) {
        Follow follow = new Follow()
            .followDate(UPDATED_FOLLOW_DATE);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        follow.setUserProfile(userProfile);
        // Add required entity
        follow.setFollower(userProfile);
        return follow;
    }

    @BeforeEach
    public void initTest() {
        follow = createEntity(em);
    }

    @Test
    @Transactional
    public void createFollow() throws Exception {
        int databaseSizeBeforeCreate = followRepository.findAll().size();
        // Create the Follow
        restFollowMockMvc.perform(post("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isCreated());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeCreate + 1);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getFollowDate()).isEqualTo(DEFAULT_FOLLOW_DATE);
    }

    @Test
    @Transactional
    public void createFollowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = followRepository.findAll().size();

        // Create the Follow with an existing ID
        follow.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowMockMvc.perform(post("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFollowDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = followRepository.findAll().size();
        // set the field null
        follow.setFollowDate(null);

        // Create the Follow, which fails.


        restFollowMockMvc.perform(post("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isBadRequest());

        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFollows() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList
        restFollowMockMvc.perform(get("/api/follows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(follow.getId().intValue())))
            .andExpect(jsonPath("$.[*].followDate").value(hasItem(DEFAULT_FOLLOW_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getFollow() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get the follow
        restFollowMockMvc.perform(get("/api/follows/{id}", follow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(follow.getId().intValue()))
            .andExpect(jsonPath("$.followDate").value(DEFAULT_FOLLOW_DATE.toString()));
    }


    @Test
    @Transactional
    public void getFollowsByIdFiltering() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        Long id = follow.getId();

        defaultFollowShouldBeFound("id.equals=" + id);
        defaultFollowShouldNotBeFound("id.notEquals=" + id);

        defaultFollowShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFollowShouldNotBeFound("id.greaterThan=" + id);

        defaultFollowShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFollowShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFollowsByFollowDateIsEqualToSomething() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where followDate equals to DEFAULT_FOLLOW_DATE
        defaultFollowShouldBeFound("followDate.equals=" + DEFAULT_FOLLOW_DATE);

        // Get all the followList where followDate equals to UPDATED_FOLLOW_DATE
        defaultFollowShouldNotBeFound("followDate.equals=" + UPDATED_FOLLOW_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowsByFollowDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where followDate not equals to DEFAULT_FOLLOW_DATE
        defaultFollowShouldNotBeFound("followDate.notEquals=" + DEFAULT_FOLLOW_DATE);

        // Get all the followList where followDate not equals to UPDATED_FOLLOW_DATE
        defaultFollowShouldBeFound("followDate.notEquals=" + UPDATED_FOLLOW_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowsByFollowDateIsInShouldWork() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where followDate in DEFAULT_FOLLOW_DATE or UPDATED_FOLLOW_DATE
        defaultFollowShouldBeFound("followDate.in=" + DEFAULT_FOLLOW_DATE + "," + UPDATED_FOLLOW_DATE);

        // Get all the followList where followDate equals to UPDATED_FOLLOW_DATE
        defaultFollowShouldNotBeFound("followDate.in=" + UPDATED_FOLLOW_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowsByFollowDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        followRepository.saveAndFlush(follow);

        // Get all the followList where followDate is not null
        defaultFollowShouldBeFound("followDate.specified=true");

        // Get all the followList where followDate is null
        defaultFollowShouldNotBeFound("followDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowsByUserProfileIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserProfile userProfile = follow.getUserProfile();
        followRepository.saveAndFlush(follow);
        Long userProfileId = userProfile.getId();

        // Get all the followList where userProfile equals to userProfileId
        defaultFollowShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the followList where userProfile equals to userProfileId + 1
        defaultFollowShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }


    @Test
    @Transactional
    public void getAllFollowsByFollowerIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserProfile follower = follow.getFollower();
        followRepository.saveAndFlush(follow);
        Long followerId = follower.getId();

        // Get all the followList where follower equals to followerId
        defaultFollowShouldBeFound("followerId.equals=" + followerId);

        // Get all the followList where follower equals to followerId + 1
        defaultFollowShouldNotBeFound("followerId.equals=" + (followerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFollowShouldBeFound(String filter) throws Exception {
        restFollowMockMvc.perform(get("/api/follows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(follow.getId().intValue())))
            .andExpect(jsonPath("$.[*].followDate").value(hasItem(DEFAULT_FOLLOW_DATE.toString())));

        // Check, that the count call also returns 1
        restFollowMockMvc.perform(get("/api/follows/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFollowShouldNotBeFound(String filter) throws Exception {
        restFollowMockMvc.perform(get("/api/follows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFollowMockMvc.perform(get("/api/follows/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFollow() throws Exception {
        // Get the follow
        restFollowMockMvc.perform(get("/api/follows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollow() throws Exception {
        // Initialize the database
        followService.save(follow);

        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // Update the follow
        Follow updatedFollow = followRepository.findById(follow.getId()).get();
        // Disconnect from session so that the updates on updatedFollow are not directly saved in db
        em.detach(updatedFollow);
        updatedFollow
            .followDate(UPDATED_FOLLOW_DATE);

        restFollowMockMvc.perform(put("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFollow)))
            .andExpect(status().isOk());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
        Follow testFollow = followList.get(followList.size() - 1);
        assertThat(testFollow.getFollowDate()).isEqualTo(UPDATED_FOLLOW_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFollow() throws Exception {
        int databaseSizeBeforeUpdate = followRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFollowMockMvc.perform(put("/api/follows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(follow)))
            .andExpect(status().isBadRequest());

        // Validate the Follow in the database
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFollow() throws Exception {
        // Initialize the database
        followService.save(follow);

        int databaseSizeBeforeDelete = followRepository.findAll().size();

        // Delete the follow
        restFollowMockMvc.perform(delete("/api/follows/{id}", follow.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Follow> followList = followRepository.findAll();
        assertThat(followList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
