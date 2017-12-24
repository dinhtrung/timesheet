package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Meeting;
import com.ft.service.MeetingService;
import com.ft.service.UserService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.MeetingCriteria;
import com.ft.service.MeetingQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing Meeting.
 */
@RestController
@RequestMapping("/api")
public class MeetingResource {

    private final Logger log = LoggerFactory.getLogger(MeetingResource.class);

    private static final String ENTITY_NAME = "meeting";

    private final MeetingService meetingService;

    private final MeetingQueryService meetingQueryService;

    @Autowired
    private UserService userService;

    public MeetingResource(MeetingService meetingService, MeetingQueryService meetingQueryService) {
        this.meetingService = meetingService;
        this.meetingQueryService = meetingQueryService;
    }

    /**
     * POST  /meetings : Create a new meeting.
     *
     * @param meeting the meeting to create
     * @return the ResponseEntity with status 201 (Created) and with body the new meeting, or with status 400 (Bad Request) if the meeting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/meetings")
    @Timed
    public ResponseEntity<Meeting> createMeeting(@RequestBody Meeting meeting) throws URISyntaxException {
        log.debug("REST request to save Meeting : {}", meeting);
        if (meeting.getId() != null) {
            throw new BadRequestAlertException("A new meeting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Meeting result = meetingService.save(meeting);
        return ResponseEntity.created(new URI("/api/meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meetings : Updates an existing meeting.
     *
     * @param meeting the meeting to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated meeting,
     * or with status 400 (Bad Request) if the meeting is not valid,
     * or with status 500 (Internal Server Error) if the meeting couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/meetings")
    @Timed
    public ResponseEntity<Meeting> updateMeeting(@RequestBody Meeting meeting) throws URISyntaxException {
        log.debug("REST request to update Meeting : {}", meeting);
        if (meeting.getId() == null) {
            return createMeeting(meeting);
        }
        Meeting result = meetingService.save(meeting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, meeting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meetings : get all the meetings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of meetings in body
     */
    @GetMapping("/meetings")
    @Timed
    public ResponseEntity<List<Meeting>> getAllMeetings(MeetingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Meetings by criteria: {}", criteria);
        Page<Meeting> page = meetingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meetings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meetings/:id : get the "id" meeting.
     *
     * @param id the id of the meeting to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the meeting, or with status 404 (Not Found)
     */
    @GetMapping("/meetings/{id}")
    @Timed
    public ResponseEntity<Meeting> getMeeting(@PathVariable Long id) {
        log.debug("REST request to get Meeting : {}", id);
        Meeting meeting = meetingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(meeting));
    }

    /**
     * DELETE  /meetings/:id : delete the "id" meeting.
     *
     * @param id the id of the meeting to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/meetings/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        log.debug("REST request to delete Meeting : {}", id);
        meetingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
