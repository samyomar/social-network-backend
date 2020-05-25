package com.samy.socialnetwork.web.rest;

import com.samy.socialnetwork.domain.ProfilePhotos;
import com.samy.socialnetwork.service.ProfilePhotosService;
import com.samy.socialnetwork.web.rest.errors.BadRequestAlertException;
import com.samy.socialnetwork.service.dto.ProfilePhotosCriteria;
import com.samy.socialnetwork.service.ProfilePhotosQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.samy.socialnetwork.domain.ProfilePhotos}.
 */
@RestController
@RequestMapping("/api")
public class ProfilePhotosResource {

    private final Logger log = LoggerFactory.getLogger(ProfilePhotosResource.class);

    private static final String ENTITY_NAME = "profilePhotos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilePhotosService profilePhotosService;

    private final ProfilePhotosQueryService profilePhotosQueryService;

    public ProfilePhotosResource(ProfilePhotosService profilePhotosService, ProfilePhotosQueryService profilePhotosQueryService) {
        this.profilePhotosService = profilePhotosService;
        this.profilePhotosQueryService = profilePhotosQueryService;
    }

    /**
     * {@code POST  /profile-photos} : Create a new profilePhotos.
     *
     * @param profilePhotos the profilePhotos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profilePhotos, or with status {@code 400 (Bad Request)} if the profilePhotos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profile-photos")
    public ResponseEntity<ProfilePhotos> createProfilePhotos(@Valid @RequestBody ProfilePhotos profilePhotos) throws URISyntaxException {
        log.debug("REST request to save ProfilePhotos : {}", profilePhotos);
        if (profilePhotos.getId() != null) {
            throw new BadRequestAlertException("A new profilePhotos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfilePhotos result = profilePhotosService.save(profilePhotos);
        return ResponseEntity.created(new URI("/api/profile-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profile-photos} : Updates an existing profilePhotos.
     *
     * @param profilePhotos the profilePhotos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilePhotos,
     * or with status {@code 400 (Bad Request)} if the profilePhotos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profilePhotos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profile-photos")
    public ResponseEntity<ProfilePhotos> updateProfilePhotos(@Valid @RequestBody ProfilePhotos profilePhotos) throws URISyntaxException {
        log.debug("REST request to update ProfilePhotos : {}", profilePhotos);
        if (profilePhotos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfilePhotos result = profilePhotosService.save(profilePhotos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profilePhotos.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profile-photos} : get all the profilePhotos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profilePhotos in body.
     */
    @GetMapping("/profile-photos")
    public ResponseEntity<List<ProfilePhotos>> getAllProfilePhotos(ProfilePhotosCriteria criteria) {
        log.debug("REST request to get ProfilePhotos by criteria: {}", criteria);
        List<ProfilePhotos> entityList = profilePhotosQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /profile-photos/count} : count all the profilePhotos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/profile-photos/count")
    public ResponseEntity<Long> countProfilePhotos(ProfilePhotosCriteria criteria) {
        log.debug("REST request to count ProfilePhotos by criteria: {}", criteria);
        return ResponseEntity.ok().body(profilePhotosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /profile-photos/:id} : get the "id" profilePhotos.
     *
     * @param id the id of the profilePhotos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profilePhotos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profile-photos/{id}")
    public ResponseEntity<ProfilePhotos> getProfilePhotos(@PathVariable Long id) {
        log.debug("REST request to get ProfilePhotos : {}", id);
        Optional<ProfilePhotos> profilePhotos = profilePhotosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profilePhotos);
    }

    /**
     * {@code DELETE  /profile-photos/:id} : delete the "id" profilePhotos.
     *
     * @param id the id of the profilePhotos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profile-photos/{id}")
    public ResponseEntity<Void> deleteProfilePhotos(@PathVariable Long id) {
        log.debug("REST request to delete ProfilePhotos : {}", id);

        profilePhotosService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
