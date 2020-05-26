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

import com.samy.socialnetwork.domain.UserProfile;
import com.samy.socialnetwork.domain.*; // for static metamodels
import com.samy.socialnetwork.repository.UserProfileRepository;
import com.samy.socialnetwork.service.dto.UserProfileCriteria;

/**
 * Service for executing complex queries for {@link UserProfile} entities in the database.
 * The main input is a {@link UserProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserProfile} or a {@link Page} of {@link UserProfile} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserProfileQueryService extends QueryService<UserProfile> {

    private final Logger log = LoggerFactory.getLogger(UserProfileQueryService.class);

    private final UserProfileRepository userProfileRepository;

    public UserProfileQueryService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Return a {@link List} of {@link UserProfile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserProfile> findByCriteria(UserProfileCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserProfile} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserProfile> findByCriteria(UserProfileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserProfileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link UserProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserProfile> createSpecification(UserProfileCriteria criteria) {
        Specification<UserProfile> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserProfile_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), UserProfile_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), UserProfile_.lastName));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), UserProfile_.age));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), UserProfile_.gender));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), UserProfile_.country));
            }
            if (criteria.getNativeLang() != null) {
                specification = specification.and(buildSpecification(criteria.getNativeLang(), UserProfile_.nativeLang));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(UserProfile_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getFollowingsId() != null) {
                specification = specification.and(buildSpecification(criteria.getFollowingsId(),
                    root -> root.join(UserProfile_.followings, JoinType.LEFT).get(Follow_.id)));
            }
            if (criteria.getFollowersId() != null) {
                specification = specification.and(buildSpecification(criteria.getFollowersId(),
                    root -> root.join(UserProfile_.followers, JoinType.LEFT).get(Follow_.id)));
            }
            if (criteria.getPostsId() != null) {
                specification = specification.and(buildSpecification(criteria.getPostsId(),
                    root -> root.join(UserProfile_.posts, JoinType.LEFT).get(Post_.id)));
            }
            if (criteria.getPhotosId() != null) {
                specification = specification.and(buildSpecification(criteria.getPhotosId(),
                    root -> root.join(UserProfile_.photos, JoinType.LEFT).get(ProfilePhotos_.id)));
            }
        }
        return specification;
    }
}
