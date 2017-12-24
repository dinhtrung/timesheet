package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Authority;
import com.ft.domain.TimeEntry;
import com.ft.domain.Timesheet;
import com.ft.domain.User;
import com.ft.domain.enumeration.ReviewState;
import com.ft.security.AuthoritiesConstants;
import com.ft.service.TimeEntryService;
import com.ft.service.TimesheetService;
import com.ft.service.UserService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.TimeEntryCriteria;
import com.ft.service.TimeEntryQueryService;
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
 * REST controller for managing TimeEntry.
 */
@RestController
@RequestMapping("/api")
public class TimeEntryResource {

    private final Logger log = LoggerFactory.getLogger(TimeEntryResource.class);

    private static final String ENTITY_NAME = "timeEntry";

    private final TimeEntryService timeEntryService;

    private final TimeEntryQueryService timeEntryQueryService;

    @Autowired
    private UserService userService;

    @Autowired
    private TimesheetService timesheetService;

    public TimeEntryResource(TimeEntryService timeEntryService, TimeEntryQueryService timeEntryQueryService) {
        this.timeEntryService = timeEntryService;
        this.timeEntryQueryService = timeEntryQueryService;
    }

    /**
     * POST  /time-entries : Create a new timeEntry.
     *
     * @param timeEntry the timeEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeEntry, or with status 400 (Bad Request) if the timeEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-entries")
    @Timed
    public ResponseEntity<TimeEntry> createTimeEntry(@RequestBody TimeEntry timeEntry) throws Exception {
        log.debug("REST request to save TimeEntry : {}", timeEntry);
        if (timeEntry.getId() != null) {
            throw new BadRequestAlertException("A new timeEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User currentUser = userService.getUserWithAuthorities().get();
        Timesheet timesheet = timesheetService.findOrCreateTimesheet(timeEntry.getDate(), currentUser);
    	if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
    		if ((timesheet.getState() != null ) && timesheet.getState().equals(ReviewState.APPROVED)) throw new Exception("You are not allowed to update an Approved timesheet");
    		timeEntry.timesheet(timesheet);
    	}
        TimeEntry result = timeEntryService.save(timeEntry);
        return ResponseEntity.created(new URI("/api/time-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-entries : Updates an existing timeEntry.
     *
     * @param timeEntry the timeEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeEntry,
     * or with status 400 (Bad Request) if the timeEntry is not valid,
     * or with status 500 (Internal Server Error) if the timeEntry couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-entries")
    @Timed
    public ResponseEntity<TimeEntry> updateTimeEntry(@RequestBody TimeEntry timeEntry) throws Exception {
        log.debug("REST request to update TimeEntry : {}", timeEntry);
        if (timeEntry.getId() == null) {
            return createTimeEntry(timeEntry);
        }
        User currentUser = userService.getUserWithAuthorities().get();
        log.debug("Only list entries of user " + currentUser);
        Timesheet timesheet = timesheetService.findOrCreateTimesheet(timeEntry.getDate(), currentUser);
    	if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
    		if (timesheet.getState().equals(ReviewState.APPROVED)) throw new Exception("You are not allowed to update an Approved timesheet");
    		timeEntry.timesheet(timesheet);
    	}
        TimeEntry result = timeEntryService.save(timeEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-entries : get all the timeEntries.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of timeEntries in body
     */
    @GetMapping("/time-entries")
    @Timed
    public ResponseEntity<List<TimeEntry>> getAllTimeEntries(TimeEntryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TimeEntries by criteria: {}", criteria);
        User currentUser = userService.getUserWithAuthorities().get();
        if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
        	criteria.getTimesheetOwner().setEquals(currentUser);
        }
        Page<TimeEntry> page = timeEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /time-entries/:id : get the "id" timeEntry.
     *
     * @param id the id of the timeEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeEntry, or with status 404 (Not Found)
     */
    @GetMapping("/time-entries/{id}")
    @Timed
    public ResponseEntity<TimeEntry> getTimeEntry(@PathVariable Long id) throws Exception {
        log.debug("REST request to get TimeEntry : {}", id);
        TimeEntry timeEntry = timeEntryService.findOne(id);
        User currentUser = userService.getUserWithAuthorities().get();
        if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
        	if (timeEntry.getTimesheet().getOwner().getId() != currentUser.getId()) throw new Exception("You are not allowed to delete other member timesheet");
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timeEntry));
    }

    /**
     * DELETE  /time-entries/:id : delete the "id" timeEntry.
     *
     * @param id the id of the timeEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-entries/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimeEntry(@PathVariable Long id) throws Exception {
        log.debug("REST request to delete TimeEntry : {}", id);
        User currentUser = userService.getUserWithAuthorities().get();
        if (!currentUser.getAuthorities().contains(new Authority(AuthoritiesConstants.ADMIN))) {
        	TimeEntry timeEntry = timeEntryService.findOne(id);
        	if (timeEntry.getTimesheet().getOwner().getId() != currentUser.getId()) throw new Exception("You are not allowed to delete other member timesheet");
        }
        timeEntryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
