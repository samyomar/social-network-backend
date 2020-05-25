package com.samy.socialnetwork.service;

import com.samy.socialnetwork.domain.Follow;
import com.samy.socialnetwork.repository.FollowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Follow}.
 */
@Service
@Transactional
public class FollowService {

    private final Logger log = LoggerFactory.getLogger(FollowService.class);

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    /**
     * Save a follow.
     *
     * @param follow the entity to save.
     * @return the persisted entity.
     */
    public Follow save(Follow follow) {
        log.debug("Request to save Follow : {}", follow);
        return followRepository.save(follow);
    }

    /**
     * Get all the follows.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Follow> findAll() {
        log.debug("Request to get all Follows");
        return followRepository.findAll();
    }


    /**
     * Get one follow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Follow> findOne(Long id) {
        log.debug("Request to get Follow : {}", id);
        return followRepository.findById(id);
    }

    /**
     * Delete the follow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Follow : {}", id);

        followRepository.deleteById(id);
    }
}
