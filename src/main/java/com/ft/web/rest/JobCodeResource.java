package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.JobCode;
import com.ft.service.JobCodeService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.JobCodeCriteria;
import com.ft.service.JobCodeQueryService;
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
 * REST controller for managing JobCode.
 */
@RestController
@RequestMapping("/api")
public class JobCodeResource {

    private final Logger log = LoggerFactory.getLogger(JobCodeResource.class);

    private static final String ENTITY_NAME = "jobCode";

    private final JobCodeService jobCodeService;

    private final JobCodeQueryService jobCodeQueryService;

    public JobCodeResource(JobCodeService jobCodeService, JobCodeQueryService jobCodeQueryService) {
        this.jobCodeService = jobCodeService;
        this.jobCodeQueryService = jobCodeQueryService;
    }

    /**
     * POST  /job-codes : Create a new jobCode.
     *
     * @param jobCode the jobCode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobCode, or with status 400 (Bad Request) if the jobCode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/job-codes")
    @Timed
    public ResponseEntity<JobCode> createJobCode(@RequestBody JobCode jobCode) throws URISyntaxException {
        log.debug("REST request to save JobCode : {}", jobCode);
        if (jobCode.getId() != null) {
            throw new BadRequestAlertException("A new jobCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobCode result = jobCodeService.save(jobCode);
        return ResponseEntity.created(new URI("/api/job-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-codes : Updates an existing jobCode.
     *
     * @param jobCode the jobCode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobCode,
     * or with status 400 (Bad Request) if the jobCode is not valid,
     * or with status 500 (Internal Server Error) if the jobCode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/job-codes")
    @Timed
    public ResponseEntity<JobCode> updateJobCode(@RequestBody JobCode jobCode) throws URISyntaxException {
        log.debug("REST request to update JobCode : {}", jobCode);
        if (jobCode.getId() == null) {
            return createJobCode(jobCode);
        }
        JobCode result = jobCodeService.save(jobCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobCode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-codes : get all the jobCodes.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of jobCodes in body
     */
    @GetMapping("/job-codes")
    @Timed
    public ResponseEntity<List<JobCode>> getAllJobCodes(JobCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobCodes by criteria: {}", criteria);
        Page<JobCode> page = jobCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-codes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-codes/:id : get the "id" jobCode.
     *
     * @param id the id of the jobCode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobCode, or with status 404 (Not Found)
     */
    @GetMapping("/job-codes/{id}")
    @Timed
    public ResponseEntity<JobCode> getJobCode(@PathVariable Long id) {
        log.debug("REST request to get JobCode : {}", id);
        JobCode jobCode = jobCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobCode));
    }

    /**
     * DELETE  /job-codes/:id : delete the "id" jobCode.
     *
     * @param id the id of the jobCode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/job-codes/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobCode(@PathVariable Long id) {
        log.debug("REST request to delete JobCode : {}", id);
        jobCodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
