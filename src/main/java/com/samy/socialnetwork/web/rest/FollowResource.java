package com.samy.socialnetwork.web.rest;

import com.samy.socialnetwork.domain.Follow;
import com.samy.socialnetwork.service.FollowService;
import com.samy.socialnetwork.web.rest.errors.BadRequestAlertException;
import com.samy.socialnetwork.service.dto.FollowCriteria;
import com.samy.socialnetwork.service.FollowQueryService;

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
 * REST controller for managing {@link com.samy.socialnetwork.domain.Follow}.
 */
@RestController
@RequestMapping("/api")
public class FollowResource {

    private final Logger log = LoggerFactory.getLogger(FollowResource.class);

    private static final String ENTITY_NAME = "follow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FollowService followService;

    private final FollowQueryService followQueryService;

    public FollowResource(FollowService followService, FollowQueryService followQueryService) {
        this.followService = followService;
        this.followQueryService = followQueryService;
    }

    /**
     * {@code POST  /follows} : Create a new follow.
     *
     * @param follow the follow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new follow, or with status {@code 400 (Bad Request)} if the follow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/follows")
    public ResponseEntity<Follow> createFollow(@Valid @RequestBody Follow follow) throws URISyntaxException {
        log.debug("REST request to save Follow : {}", follow);
        if (follow.getId() != null) {
            throw new BadRequestAlertException("A new follow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Follow result = followService.save(follow);
        return ResponseEntity.created(new URI("/api/follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /follows} : Updates an existing follow.
     *
     * @param follow the follow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated follow,
     * or with status {@code 400 (Bad Request)} if the follow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the follow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/follows")
    public ResponseEntity<Follow> updateFollow(@Valid @RequestBody Follow follow) throws URISyntaxException {
        log.debug("REST request to update Follow : {}", follow);
        if (follow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Follow result = followService.save(follow);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, follow.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /follows} : get all the follows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of follows in body.
     */
    @GetMapping("/follows")
    public ResponseEntity<List<Follow>> getAllFollows(FollowCriteria criteria) {
        log.debug("REST request to get Follows by criteria: {}", criteria);
        List<Follow> entityList = followQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /follows/count} : count all the follows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/follows/count")
    public ResponseEntity<Long> countFollows(FollowCriteria criteria) {
        log.debug("REST request to count Follows by criteria: {}", criteria);
        return ResponseEntity.ok().body(followQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /follows/:id} : get the "id" follow.
     *
     * @param id the id of the follow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the follow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/follows/{id}")
    public ResponseEntity<Follow> getFollow(@PathVariable Long id) {
        log.debug("REST request to get Follow : {}", id);
        Optional<Follow> follow = followService.findOne(id);
        return ResponseUtil.wrapOrNotFound(follow);
    }

    /**
     * {@code DELETE  /follows/:id} : delete the "id" follow.
     *
     * @param id the id of the follow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/follows/{id}")
    public ResponseEntity<Void> deleteFollow(@PathVariable Long id) {
        log.debug("REST request to delete Follow : {}", id);

        followService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
