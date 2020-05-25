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

import com.samy.socialnetwork.domain.ProfilePhotos;
import com.samy.socialnetwork.domain.*; // for static metamodels
import com.samy.socialnetwork.repository.ProfilePhotosRepository;
import com.samy.socialnetwork.service.dto.ProfilePhotosCriteria;

/**
 * Service for executing complex queries for {@link ProfilePhotos} entities in the database.
 * The main input is a {@link ProfilePhotosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProfilePhotos} or a {@link Page} of {@link ProfilePhotos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProfilePhotosQueryService extends QueryService<ProfilePhotos> {

    private final Logger log = LoggerFactory.getLogger(ProfilePhotosQueryService.class);

    private final ProfilePhotosRepository profilePhotosRepository;

    public ProfilePhotosQueryService(ProfilePhotosRepository profilePhotosRepository) {
        this.profilePhotosRepository = profilePhotosRepository;
    }

    /**
     * Return a {@link List} of {@link ProfilePhotos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProfilePhotos> findByCriteria(ProfilePhotosCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProfilePhotos> specification = createSpecification(criteria);
        return profilePhotosRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProfilePhotos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfilePhotos> findByCriteria(ProfilePhotosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProfilePhotos> specification = createSpecification(criteria);
        return profilePhotosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProfilePhotosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProfilePhotos> specification = createSpecification(criteria);
        return profilePhotosRepository.count(specification);
    }

    /**
     * Function to convert {@link ProfilePhotosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProfilePhotos> createSpecification(ProfilePhotosCriteria criteria) {
        Specification<ProfilePhotos> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProfilePhotos_.id));
            }
            if (criteria.getUploadedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadedDate(), ProfilePhotos_.uploadedDate));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ProfilePhotos_.title));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserProfileId(),
                    root -> root.join(ProfilePhotos_.userProfile, JoinType.LEFT).get(UserProfile_.id)));
            }
        }
        return specification;
    }
}
