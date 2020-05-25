package com.samy.socialnetwork.repository;

import com.samy.socialnetwork.domain.ProfilePhotos;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProfilePhotos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilePhotosRepository extends JpaRepository<ProfilePhotos, Long>, JpaSpecificationExecutor<ProfilePhotos> {
}
