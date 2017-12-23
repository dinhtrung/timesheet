package com.ft.service;

import com.ft.domain.JobCode;
import com.ft.repository.JobCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing JobCode.
 */
@Service
@Transactional
public class JobCodeService {

    private final Logger log = LoggerFactory.getLogger(JobCodeService.class);

    private final JobCodeRepository jobCodeRepository;

    public JobCodeService(JobCodeRepository jobCodeRepository) {
        this.jobCodeRepository = jobCodeRepository;
    }

    /**
     * Save a jobCode.
     *
     * @param jobCode the entity to save
     * @return the persisted entity
     */
    public JobCode save(JobCode jobCode) {
        log.debug("Request to save JobCode : {}", jobCode);
        return jobCodeRepository.save(jobCode);
    }

    /**
     * Get all the jobCodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<JobCode> findAll(Pageable pageable) {
        log.debug("Request to get all JobCodes");
        return jobCodeRepository.findAll(pageable);
    }

    /**
     * Get one jobCode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public JobCode findOne(Long id) {
        log.debug("Request to get JobCode : {}", id);
        return jobCodeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the jobCode by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete JobCode : {}", id);
        jobCodeRepository.delete(id);
    }
}
