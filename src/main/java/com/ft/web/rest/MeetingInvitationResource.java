package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.MeetingInvitation;
import com.ft.service.MeetingInvitationService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.MeetingInvitationCriteria;
import com.ft.service.MeetingInvitationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MeetingInvitation.
 */
@RestController
@RequestMapping("/api")
public class MeetingInvitationResource {

    private final Logger log = LoggerFactory.getLogger(MeetingInvitationResource.class);

    private static final String ENTITY_NAME = "meetingInvitation";

    private final MeetingInvitationService meetingInvitationService;

    private final MeetingInvitationQueryService meetingInvitationQueryService;

    public MeetingInvitationResource(MeetingInvitationService meetingInvitationService, MeetingInvitationQueryService meetingInvitationQueryService) {
        this.meetingInvitationService = meetingInvitationService;
        this.meetingInvitationQueryService = meetingInvitationQueryService;
    }

    /**
     * POST  /meeting-invitations : Create a new meetingInvitation.
     *
     * @param meetingInvitation the meetingInvitation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meetingInvitation, or with status 400 (Bad Request) if the meetingInvitation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meeting-invitations")
    @Timed
    public ResponseEntity<MeetingInvitation> createMeetingInvitation(@RequestBody MeetingInvitation meetingInvitation) throws URISyntaxException {
        log.debug("REST request to save MeetingInvitation : {}", meetingInvitation);
        if (meetingInvitation.getId() != null) {
            throw new BadRequestAlertException("A new meetingInvitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeetingInvitation result = meetingInvitationService.save(meetingInvitation);
        return ResponseEntity.created(new URI("/api/meeting-invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meeting-invitations : Updates an existing meetingInvitation.
     *
     * @param meetingInvitation the meetingInvitation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meetingInvitation,
     * or with status 400 (Bad Request) if the meetingInvitation is not valid,
     * or with status 500 (Internal Server Error) if the meetingInvitation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meeting-invitations")
    @Timed
    public ResponseEntity<MeetingInvitation> updateMeetingInvitation(@RequestBody MeetingInvitation meetingInvitation) throws URISyntaxException {
        log.debug("REST request to update MeetingInvitation : {}", meetingInvitation);
        if (meetingInvitation.getId() == null) {
            return createMeetingInvitation(meetingInvitation);
        }
        MeetingInvitation result = meetingInvitationService.save(meetingInvitation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meetingInvitation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meeting-invitations : get all the meetingInvitations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of meetingInvitations in body
     */
    @GetMapping("/meeting-invitations")
    @Timed
    public ResponseEntity<List<MeetingInvitation>> getAllMeetingInvitations(MeetingInvitationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MeetingInvitations by criteria: {}", criteria);
        Page<MeetingInvitation> page = meetingInvitationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meeting-invitations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meeting-invitations/:id : get the "id" meetingInvitation.
     *
     * @param id the id of the meetingInvitation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meetingInvitation, or with status 404 (Not Found)
     */
    @GetMapping("/meeting-invitations/{id}")
    @Timed
    public ResponseEntity<MeetingInvitation> getMeetingInvitation(@PathVariable Long id) {
        log.debug("REST request to get MeetingInvitation : {}", id);
        MeetingInvitation meetingInvitation = meetingInvitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(meetingInvitation));
    }

    /**
     * DELETE  /meeting-invitations/:id : delete the "id" meetingInvitation.
     *
     * @param id the id of the meetingInvitation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meeting-invitations/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeetingInvitation(@PathVariable Long id) {
        log.debug("REST request to delete MeetingInvitation : {}", id);
        meetingInvitationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
