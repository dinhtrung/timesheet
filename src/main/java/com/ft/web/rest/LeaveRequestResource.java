package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.LeaveRequest;
import com.ft.service.LeaveRequestService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.LeaveRequestCriteria;
import com.ft.service.LeaveRequestQueryService;
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
 * REST controller for managing LeaveRequest.
 */
@RestController
@RequestMapping("/api")
public class LeaveRequestResource {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestResource.class);

    private static final String ENTITY_NAME = "leaveRequest";

    private final LeaveRequestService leaveRequestService;

    private final LeaveRequestQueryService leaveRequestQueryService;

    public LeaveRequestResource(LeaveRequestService leaveRequestService, LeaveRequestQueryService leaveRequestQueryService) {
        this.leaveRequestService = leaveRequestService;
        this.leaveRequestQueryService = leaveRequestQueryService;
    }

    /**
     * POST  /leave-requests : Create a new leaveRequest.
     *
     * @param leaveRequest the leaveRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leaveRequest, or with status 400 (Bad Request) if the leaveRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leave-requests")
    @Timed
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) throws URISyntaxException {
        log.debug("REST request to save LeaveRequest : {}", leaveRequest);
        if (leaveRequest.getId() != null) {
            throw new BadRequestAlertException("A new leaveRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveRequest result = leaveRequestService.save(leaveRequest);
        return ResponseEntity.created(new URI("/api/leave-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leave-requests : Updates an existing leaveRequest.
     *
     * @param leaveRequest the leaveRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leaveRequest,
     * or with status 400 (Bad Request) if the leaveRequest is not valid,
     * or with status 500 (Internal Server Error) if the leaveRequest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leave-requests")
    @Timed
    public ResponseEntity<LeaveRequest> updateLeaveRequest(@RequestBody LeaveRequest leaveRequest) throws URISyntaxException {
        log.debug("REST request to update LeaveRequest : {}", leaveRequest);
        if (leaveRequest.getId() == null) {
            return createLeaveRequest(leaveRequest);
        }
        LeaveRequest result = leaveRequestService.save(leaveRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leaveRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leave-requests : get all the leaveRequests.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of leaveRequests in body
     */
    @GetMapping("/leave-requests")
    @Timed
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests(LeaveRequestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaveRequests by criteria: {}", criteria);
        Page<LeaveRequest> page = leaveRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leave-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /leave-requests/:id : get the "id" leaveRequest.
     *
     * @param id the id of the leaveRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leaveRequest, or with status 404 (Not Found)
     */
    @GetMapping("/leave-requests/{id}")
    @Timed
    public ResponseEntity<LeaveRequest> getLeaveRequest(@PathVariable Long id) {
        log.debug("REST request to get LeaveRequest : {}", id);
        LeaveRequest leaveRequest = leaveRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leaveRequest));
    }

    /**
     * DELETE  /leave-requests/:id : delete the "id" leaveRequest.
     *
     * @param id the id of the leaveRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leave-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        log.debug("REST request to delete LeaveRequest : {}", id);
        leaveRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
