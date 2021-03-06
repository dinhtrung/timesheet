package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Authority;
import com.ft.domain.Timesheet;
import com.ft.domain.User;
import com.ft.domain.enumeration.ReviewState;
import com.ft.security.AuthoritiesConstants;
import com.ft.service.TimesheetService;
import com.ft.service.UserService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.TimesheetCriteria;
import com.ft.service.TimesheetQueryService;

import io.github.jhipster.service.filter.LongFilter;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Timesheet.
 */
@RestController
@RequestMapping("/api")
public class TimesheetResource {

    private final Logger log = LoggerFactory.getLogger(TimesheetResource.class);

    private static final String ENTITY_NAME = "timesheet";

    private final TimesheetService timesheetService;

    private final TimesheetQueryService timesheetQueryService;

    @Autowired
    private UserService userService;

    public TimesheetResource(TimesheetService timesheetService, TimesheetQueryService timesheetQueryService) {
        this.timesheetService = timesheetService;
        this.timesheetQueryService = timesheetQueryService;
    }

    /**
     * POST  /timesheets : Create a new timesheet.
     *
     * @param timesheet the timesheet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timesheet, or with status 400 (Bad Request) if the timesheet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/timesheets")
    @Timed
    public ResponseEntity<Timesheet> createTimesheet(@Valid @RequestBody Timesheet timesheet) throws URISyntaxException {
        log.debug("REST request to save Timesheet : {}", timesheet);
        if (timesheet.getId() != null) {
            throw new BadRequestAlertException("A new timesheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (timesheet.getOwner() == null) {
        	User currentUser = userService.getUserWithAuthorities().get();
        	if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
        		timesheet
        		.owner(currentUser);
        	}
        }
        Timesheet result = timesheetService.save(timesheet);
        return ResponseEntity.created(new URI("/api/timesheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timesheets : Updates an existing timesheet.
     *
     * @param timesheet the timesheet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timesheet,
     * or with status 400 (Bad Request) if the timesheet is not valid,
     * or with status 500 (Internal Server Error) if the timesheet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/timesheets")
    @Timed
    public ResponseEntity<Timesheet> updateTimesheet(@Valid @RequestBody Timesheet timesheet) throws URISyntaxException {
        log.debug("REST request to update Timesheet : {}", timesheet);
        if (timesheet.getId() == null) {
            return createTimesheet(timesheet);
        }
        User currentUser = userService.getUserWithAuthorities().get();
    	if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
    		timesheet
    			.owner(currentUser);
    	} else if (timesheet.getApprovedBy() == null) {
    		timesheet.approvedBy(currentUser);
    	}
        Timesheet result = timesheetService.save(timesheet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timesheet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timesheets : get all the timesheets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of timesheets in body
     */
    @GetMapping("/timesheets")
    @Timed
    public ResponseEntity<List<Timesheet>> getAllTimesheets(TimesheetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Timesheets by criteria: {}", criteria);
        User currentUser = userService.getUserWithAuthorities().get();
        log.debug("Only list entries of user " + currentUser);
    	if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
    		criteria.setOwnerId((LongFilter) new LongFilter().setEquals(currentUser.getId()));
    	}
        Page<Timesheet> page = timesheetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/timesheets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /timesheets/:id : get the "id" timesheet.
     *
     * @param id the id of the timesheet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timesheet, or with status 404 (Not Found)
     */
    @GetMapping("/timesheets/{id}")
    @Timed
    public ResponseEntity<Timesheet> getTimesheet(@PathVariable Long id) {
        log.debug("REST request to get Timesheet : {}", id);
        Timesheet timesheet = timesheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timesheet));
    }

    /**
     * DELETE  /timesheets/:id : delete the "id" timesheet.
     *
     * @param id the id of the timesheet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/timesheets/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        log.debug("REST request to delete Timesheet : {}", id);
        timesheetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
