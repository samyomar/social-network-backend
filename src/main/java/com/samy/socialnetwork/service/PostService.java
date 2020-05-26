package com.samy.socialnetwork.service;

import com.samy.socialnetwork.domain.Post;
import com.samy.socialnetwork.repository.PostRepository;
import com.samy.socialnetwork.security.AuthoritiesConstants;
import com.samy.socialnetwork.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Post}.
 */
@Service
@Transactional
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Save a post.
     *
     * @param post the entity to save.
     * @return the persisted entity.
     */
    public Post save(Post post) {
        log.debug("Request to save Post : {}", post);
        return postRepository.save(post);
    }

    /**
     * Get all the posts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Post> findAll() {
        log.debug("Request to get all Posts");
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
          return postRepository.findAll();
        } else {
            return postRepository.findAllByUserProfileUserLogin(SecurityUtils.getCurrentUserLogin().get());
        }
    }


    /**
     * Get one post by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id);
    }

    /**
     * Delete the post by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);

        postRepository.deleteById(id);
    }
}
