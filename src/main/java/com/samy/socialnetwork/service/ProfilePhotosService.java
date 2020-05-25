package com.samy.socialnetwork.service;

import com.samy.socialnetwork.domain.ProfilePhotos;
import com.samy.socialnetwork.repository.ProfilePhotosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ProfilePhotos}.
 */
@Service
@Transactional
public class ProfilePhotosService {

    private final Logger log = LoggerFactory.getLogger(ProfilePhotosService.class);

    private final ProfilePhotosRepository profilePhotosRepository;

    public ProfilePhotosService(ProfilePhotosRepository profilePhotosRepository) {
        this.profilePhotosRepository = profilePhotosRepository;
    }

    /**
     * Save a profilePhotos.
     *
     * @param profilePhotos the entity to save.
     * @return the persisted entity.
     */
    public ProfilePhotos save(ProfilePhotos profilePhotos) {
        log.debug("Request to save ProfilePhotos : {}", profilePhotos);
        return profilePhotosRepository.save(profilePhotos);
    }

    /**
     * Get all the profilePhotos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProfilePhotos> findAll() {
        log.debug("Request to get all ProfilePhotos");
        return profilePhotosRepository.findAll();
    }


    /**
     * Get one profilePhotos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfilePhotos> findOne(Long id) {
        log.debug("Request to get ProfilePhotos : {}", id);
        return profilePhotosRepository.findById(id);
    }

    /**
     * Delete the profilePhotos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProfilePhotos : {}", id);

        profilePhotosRepository.deleteById(id);
    }
}
