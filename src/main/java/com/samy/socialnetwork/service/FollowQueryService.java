package com.samy.socialnetwork.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.samy.socialnetwork.domain.Follow;
import com.samy.socialnetwork.domain.*; // for static metamodels
import com.samy.socialnetwork.repository.FollowRepository;
import com.samy.socialnetwork.service.dto.FollowCriteria;

/**
 * Service for executing complex queries for {@link Follow} entities in the database.
 * The main input is a {@link FollowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Follow} or a {@link Page} of {@link Follow} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FollowQueryService extends QueryService<Follow> {

    private final Logger log = LoggerFactory.getLogger(FollowQueryService.class);

    private final FollowRepository followRepository;

    public FollowQueryService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    /**
     * Return a {@link List} of {@link Follow} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Follow> findByCriteria(FollowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Follow> specification = createSpecification(criteria);
        return followRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Follow} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Follow> findByCriteria(FollowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Follow> specification = createSpecification(criteria);
        return followRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FollowCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Follow> specification = createSpecification(criteria);
        return followRepository.count(specification);
    }

    /**
     * Function to convert {@link FollowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Follow> createSpecification(FollowCriteria criteria) {
        Specification<Follow> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Follow_.id));
            }
            if (criteria.getFollowDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFollowDate(), Follow_.followDate));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserProfileId(),
                    root -> root.join(Follow_.userProfile, JoinType.LEFT).get(UserProfile_.id)));
            }
            if (criteria.getFollowerId() != null) {
                specification = specification.and(buildSpecification(criteria.getFollowerId(),
                    root -> root.join(Follow_.follower, JoinType.LEFT).get(UserProfile_.id)));
            }
        }
        return specification;
    }
}
