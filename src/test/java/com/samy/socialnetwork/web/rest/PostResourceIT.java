package com.samy.socialnetwork.web.rest;

import com.samy.socialnetwork.SocialNetworkBackendApp;
import com.samy.socialnetwork.domain.Post;
import com.samy.socialnetwork.domain.UserProfile;
import com.samy.socialnetwork.repository.PostRepository;
import com.samy.socialnetwork.service.PostService;
import com.samy.socialnetwork.service.dto.PostCriteria;
import com.samy.socialnetwork.service.PostQueryService;

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
 * Integration tests for the {@link PostResource} REST controller.
 */
@SpringBootTest(classes = SocialNetworkBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PostResourceIT {

    private static final Instant DEFAULT_POST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_POST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_POST_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_POST_CONTENT = "BBBBBBBBBB";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostMockMvc;

    private Post post;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createEntity(EntityManager em) {
        Post post = new Post()
            .postDate(DEFAULT_POST_DATE)
            .postContent(DEFAULT_POST_CONTENT);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        post.setUserProfile(userProfile);
        return post;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createUpdatedEntity(EntityManager em) {
        Post post = new Post()
            .postDate(UPDATED_POST_DATE)
            .postContent(UPDATED_POST_CONTENT);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity(em);
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        post.setUserProfile(userProfile);
        return post;
    }

    @BeforeEach
    public void initTest() {
        post = createEntity(em);
    }

    @Test
    @Transactional
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();
        // Create the Post
        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getPostDate()).isEqualTo(DEFAULT_POST_DATE);
        assertThat(testPost.getPostContent()).isEqualTo(DEFAULT_POST_CONTENT);
    }

    @Test
    @Transactional
    public void createPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post with an existing ID
        post.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPostDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postRepository.findAll().size();
        // set the field null
        post.setPostDate(null);

        // Create the Post, which fails.


        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isBadRequest());

        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = postRepository.findAll().size();
        // set the field null
        post.setPostContent(null);

        // Create the Post, which fails.


        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isBadRequest());

        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].postContent").value(hasItem(DEFAULT_POST_CONTENT)));
    }
    
    @Test
    @Transactional
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.postDate").value(DEFAULT_POST_DATE.toString()))
            .andExpect(jsonPath("$.postContent").value(DEFAULT_POST_CONTENT));
    }


    @Test
    @Transactional
    public void getPostsByIdFiltering() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        Long id = post.getId();

        defaultPostShouldBeFound("id.equals=" + id);
        defaultPostShouldNotBeFound("id.notEquals=" + id);

        defaultPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPostShouldNotBeFound("id.greaterThan=" + id);

        defaultPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPostShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPostsByPostDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postDate equals to DEFAULT_POST_DATE
        defaultPostShouldBeFound("postDate.equals=" + DEFAULT_POST_DATE);

        // Get all the postList where postDate equals to UPDATED_POST_DATE
        defaultPostShouldNotBeFound("postDate.equals=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByPostDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postDate not equals to DEFAULT_POST_DATE
        defaultPostShouldNotBeFound("postDate.notEquals=" + DEFAULT_POST_DATE);

        // Get all the postList where postDate not equals to UPDATED_POST_DATE
        defaultPostShouldBeFound("postDate.notEquals=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByPostDateIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postDate in DEFAULT_POST_DATE or UPDATED_POST_DATE
        defaultPostShouldBeFound("postDate.in=" + DEFAULT_POST_DATE + "," + UPDATED_POST_DATE);

        // Get all the postList where postDate equals to UPDATED_POST_DATE
        defaultPostShouldNotBeFound("postDate.in=" + UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByPostDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postDate is not null
        defaultPostShouldBeFound("postDate.specified=true");

        // Get all the postList where postDate is null
        defaultPostShouldNotBeFound("postDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByPostContentIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postContent equals to DEFAULT_POST_CONTENT
        defaultPostShouldBeFound("postContent.equals=" + DEFAULT_POST_CONTENT);

        // Get all the postList where postContent equals to UPDATED_POST_CONTENT
        defaultPostShouldNotBeFound("postContent.equals=" + UPDATED_POST_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByPostContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postContent not equals to DEFAULT_POST_CONTENT
        defaultPostShouldNotBeFound("postContent.notEquals=" + DEFAULT_POST_CONTENT);

        // Get all the postList where postContent not equals to UPDATED_POST_CONTENT
        defaultPostShouldBeFound("postContent.notEquals=" + UPDATED_POST_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByPostContentIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postContent in DEFAULT_POST_CONTENT or UPDATED_POST_CONTENT
        defaultPostShouldBeFound("postContent.in=" + DEFAULT_POST_CONTENT + "," + UPDATED_POST_CONTENT);

        // Get all the postList where postContent equals to UPDATED_POST_CONTENT
        defaultPostShouldNotBeFound("postContent.in=" + UPDATED_POST_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByPostContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postContent is not null
        defaultPostShouldBeFound("postContent.specified=true");

        // Get all the postList where postContent is null
        defaultPostShouldNotBeFound("postContent.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByPostContentContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postContent contains DEFAULT_POST_CONTENT
        defaultPostShouldBeFound("postContent.contains=" + DEFAULT_POST_CONTENT);

        // Get all the postList where postContent contains UPDATED_POST_CONTENT
        defaultPostShouldNotBeFound("postContent.contains=" + UPDATED_POST_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByPostContentNotContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postContent does not contain DEFAULT_POST_CONTENT
        defaultPostShouldNotBeFound("postContent.doesNotContain=" + DEFAULT_POST_CONTENT);

        // Get all the postList where postContent does not contain UPDATED_POST_CONTENT
        defaultPostShouldBeFound("postContent.doesNotContain=" + UPDATED_POST_CONTENT);
    }


    @Test
    @Transactional
    public void getAllPostsByUserProfileIsEqualToSomething() throws Exception {
        // Get already existing entity
        UserProfile userProfile = post.getUserProfile();
        postRepository.saveAndFlush(post);
        Long userProfileId = userProfile.getId();

        // Get all the postList where userProfile equals to userProfileId
        defaultPostShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the postList where userProfile equals to userProfileId + 1
        defaultPostShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostShouldBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE.toString())))
            .andExpect(jsonPath("$.[*].postContent").value(hasItem(DEFAULT_POST_CONTENT)));

        // Check, that the count call also returns 1
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostShouldNotBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePost() throws Exception {
        // Initialize the database
        postService.save(post);

        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = postRepository.findById(post.getId()).get();
        // Disconnect from session so that the updates on updatedPost are not directly saved in db
        em.detach(updatedPost);
        updatedPost
            .postDate(UPDATED_POST_DATE)
            .postContent(UPDATED_POST_CONTENT);

        restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPost)))
            .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getPostDate()).isEqualTo(UPDATED_POST_DATE);
        assertThat(testPost.getPostContent()).isEqualTo(UPDATED_POST_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPost() throws Exception {
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePost() throws Exception {
        // Initialize the database
        postService.save(post);

        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Delete the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
