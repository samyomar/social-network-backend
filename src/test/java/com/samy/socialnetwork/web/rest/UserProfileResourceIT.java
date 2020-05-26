package com.samy.socialnetwork.web.rest;

import com.samy.socialnetwork.SocialNetworkBackendApp;
import com.samy.socialnetwork.domain.UserProfile;
import com.samy.socialnetwork.domain.User;
import com.samy.socialnetwork.domain.Follow;
import com.samy.socialnetwork.domain.Post;
import com.samy.socialnetwork.domain.ProfilePhotos;
import com.samy.socialnetwork.repository.UserProfileRepository;
import com.samy.socialnetwork.service.UserProfileService;
import com.samy.socialnetwork.service.dto.UserProfileCriteria;
import com.samy.socialnetwork.service.UserProfileQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.samy.socialnetwork.domain.enumeration.Gender;
import com.samy.socialnetwork.domain.enumeration.Language;
/**
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@SpringBootTest(classes = SocialNetworkBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserProfileResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 12;
    private static final Integer UPDATED_AGE = 13;
    private static final Integer SMALLER_AGE = 12 - 1;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Language DEFAULT_NATIVE_LANG = Language.FRENCH;
    private static final Language UPDATED_NATIVE_LANG = Language.ENGLISH;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileQueryService userProfileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER)
            .country(DEFAULT_COUNTRY)
            .nativeLang(DEFAULT_NATIVE_LANG);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        return userProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .country(UPDATED_COUNTRY)
            .nativeLang(UPDATED_NATIVE_LANG);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        return userProfile;
    }

    @BeforeEach
    public void initTest() {
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();
        // Create the UserProfile
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserProfile.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testUserProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserProfile.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserProfile.getNativeLang()).isEqualTo(DEFAULT_NATIVE_LANG);
    }

    @Test
    @Transactional
    public void createUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile with an existing ID
        userProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setFirstName(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setAge(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setGender(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setCountry(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNativeLangIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setNativeLang(null);

        // Create the UserProfile, which fails.


        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].nativeLang").value(hasItem(DEFAULT_NATIVE_LANG.toString())));
    }
    
    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.nativeLang").value(DEFAULT_NATIVE_LANG.toString()));
    }


    @Test
    @Transactional
    public void getUserProfilesByIdFiltering() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        Long id = userProfile.getId();

        defaultUserProfileShouldBeFound("id.equals=" + id);
        defaultUserProfileShouldNotBeFound("id.notEquals=" + id);

        defaultUserProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultUserProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserProfileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName equals to DEFAULT_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName equals to UPDATED_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName not equals to DEFAULT_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName not equals to UPDATED_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the userProfileList where firstName equals to UPDATED_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName is not null
        defaultUserProfileShouldBeFound("firstName.specified=true");

        // Get all the userProfileList where firstName is null
        defaultUserProfileShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName contains DEFAULT_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName contains UPDATED_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName does not contain UPDATED_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName equals to DEFAULT_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName equals to UPDATED_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName not equals to DEFAULT_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName not equals to UPDATED_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the userProfileList where lastName equals to UPDATED_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName is not null
        defaultUserProfileShouldBeFound("lastName.specified=true");

        // Get all the userProfileList where lastName is null
        defaultUserProfileShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName contains DEFAULT_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName contains UPDATED_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName does not contain DEFAULT_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName does not contain UPDATED_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age equals to DEFAULT_AGE
        defaultUserProfileShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the userProfileList where age equals to UPDATED_AGE
        defaultUserProfileShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age not equals to DEFAULT_AGE
        defaultUserProfileShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the userProfileList where age not equals to UPDATED_AGE
        defaultUserProfileShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age in DEFAULT_AGE or UPDATED_AGE
        defaultUserProfileShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the userProfileList where age equals to UPDATED_AGE
        defaultUserProfileShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age is not null
        defaultUserProfileShouldBeFound("age.specified=true");

        // Get all the userProfileList where age is null
        defaultUserProfileShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age is greater than or equal to DEFAULT_AGE
        defaultUserProfileShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the userProfileList where age is greater than or equal to UPDATED_AGE
        defaultUserProfileShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age is less than or equal to DEFAULT_AGE
        defaultUserProfileShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the userProfileList where age is less than or equal to SMALLER_AGE
        defaultUserProfileShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age is less than DEFAULT_AGE
        defaultUserProfileShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the userProfileList where age is less than UPDATED_AGE
        defaultUserProfileShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where age is greater than DEFAULT_AGE
        defaultUserProfileShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the userProfileList where age is greater than SMALLER_AGE
        defaultUserProfileShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender equals to DEFAULT_GENDER
        defaultUserProfileShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the userProfileList where gender equals to UPDATED_GENDER
        defaultUserProfileShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender not equals to DEFAULT_GENDER
        defaultUserProfileShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the userProfileList where gender not equals to UPDATED_GENDER
        defaultUserProfileShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultUserProfileShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the userProfileList where gender equals to UPDATED_GENDER
        defaultUserProfileShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where gender is not null
        defaultUserProfileShouldBeFound("gender.specified=true");

        // Get all the userProfileList where gender is null
        defaultUserProfileShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country equals to DEFAULT_COUNTRY
        defaultUserProfileShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country equals to UPDATED_COUNTRY
        defaultUserProfileShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country not equals to DEFAULT_COUNTRY
        defaultUserProfileShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country not equals to UPDATED_COUNTRY
        defaultUserProfileShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultUserProfileShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the userProfileList where country equals to UPDATED_COUNTRY
        defaultUserProfileShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country is not null
        defaultUserProfileShouldBeFound("country.specified=true");

        // Get all the userProfileList where country is null
        defaultUserProfileShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserProfilesByCountryContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country contains DEFAULT_COUNTRY
        defaultUserProfileShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country contains UPDATED_COUNTRY
        defaultUserProfileShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country does not contain DEFAULT_COUNTRY
        defaultUserProfileShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country does not contain UPDATED_COUNTRY
        defaultUserProfileShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByNativeLangIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where nativeLang equals to DEFAULT_NATIVE_LANG
        defaultUserProfileShouldBeFound("nativeLang.equals=" + DEFAULT_NATIVE_LANG);

        // Get all the userProfileList where nativeLang equals to UPDATED_NATIVE_LANG
        defaultUserProfileShouldNotBeFound("nativeLang.equals=" + UPDATED_NATIVE_LANG);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByNativeLangIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where nativeLang not equals to DEFAULT_NATIVE_LANG
        defaultUserProfileShouldNotBeFound("nativeLang.notEquals=" + DEFAULT_NATIVE_LANG);

        // Get all the userProfileList where nativeLang not equals to UPDATED_NATIVE_LANG
        defaultUserProfileShouldBeFound("nativeLang.notEquals=" + UPDATED_NATIVE_LANG);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByNativeLangIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where nativeLang in DEFAULT_NATIVE_LANG or UPDATED_NATIVE_LANG
        defaultUserProfileShouldBeFound("nativeLang.in=" + DEFAULT_NATIVE_LANG + "," + UPDATED_NATIVE_LANG);

        // Get all the userProfileList where nativeLang equals to UPDATED_NATIVE_LANG
        defaultUserProfileShouldNotBeFound("nativeLang.in=" + UPDATED_NATIVE_LANG);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByNativeLangIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where nativeLang is not null
        defaultUserProfileShouldBeFound("nativeLang.specified=true");

        // Get all the userProfileList where nativeLang is null
        defaultUserProfileShouldNotBeFound("nativeLang.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userProfile.getUser();
        userProfileRepository.saveAndFlush(userProfile);
        Long userId = user.getId();

        // Get all the userProfileList where user equals to userId
        defaultUserProfileShouldBeFound("userId.equals=" + userId);

        // Get all the userProfileList where user equals to userId + 1
        defaultUserProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllUserProfilesByFollowingsIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        Follow followings = FollowResourceIT.createEntity(em);
        em.persist(followings);
        em.flush();
        userProfile.addFollowings(followings);
        userProfileRepository.saveAndFlush(userProfile);
        Long followingsId = followings.getId();

        // Get all the userProfileList where followings equals to followingsId
        defaultUserProfileShouldBeFound("followingsId.equals=" + followingsId);

        // Get all the userProfileList where followings equals to followingsId + 1
        defaultUserProfileShouldNotBeFound("followingsId.equals=" + (followingsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserProfilesByFollowersIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        Follow followers = FollowResourceIT.createEntity(em);
        em.persist(followers);
        em.flush();
        userProfile.addFollowers(followers);
        userProfileRepository.saveAndFlush(userProfile);
        Long followersId = followers.getId();

        // Get all the userProfileList where followers equals to followersId
        defaultUserProfileShouldBeFound("followersId.equals=" + followersId);

        // Get all the userProfileList where followers equals to followersId + 1
        defaultUserProfileShouldNotBeFound("followersId.equals=" + (followersId + 1));
    }


    @Test
    @Transactional
    public void getAllUserProfilesByPostsIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        Post posts = PostResourceIT.createEntity(em);
        em.persist(posts);
        em.flush();
        userProfile.addPosts(posts);
        userProfileRepository.saveAndFlush(userProfile);
        Long postsId = posts.getId();

        // Get all the userProfileList where posts equals to postsId
        defaultUserProfileShouldBeFound("postsId.equals=" + postsId);

        // Get all the userProfileList where posts equals to postsId + 1
        defaultUserProfileShouldNotBeFound("postsId.equals=" + (postsId + 1));
    }


    @Test
    @Transactional
    public void getAllUserProfilesByPhotosIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);
        ProfilePhotos photos = ProfilePhotosResourceIT.createEntity(em);
        em.persist(photos);
        em.flush();
        userProfile.addPhotos(photos);
        userProfileRepository.saveAndFlush(userProfile);
        Long photosId = photos.getId();

        // Get all the userProfileList where photos equals to photosId
        defaultUserProfileShouldBeFound("photosId.equals=" + photosId);

        // Get all the userProfileList where photos equals to photosId + 1
        defaultUserProfileShouldNotBeFound("photosId.equals=" + (photosId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].nativeLang").value(hasItem(DEFAULT_NATIVE_LANG.toString())));

        // Check, that the count call also returns 1
        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileService.save(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).get();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .country(UPDATED_COUNTRY)
            .nativeLang(UPDATED_NATIVE_LANG);

        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserProfile)))
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserProfile.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testUserProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserProfile.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserProfile.getNativeLang()).isEqualTo(UPDATED_NATIVE_LANG);
    }

    @Test
    @Transactional
    public void updateNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userProfile)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileService.save(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Delete the userProfile
        restUserProfileMockMvc.perform(delete("/api/user-profiles/{id}", userProfile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
