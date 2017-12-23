package com.ft.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ft.domain.JobCode;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.JobCodeRepository;
import com.ft.service.dto.JobCodeCriteria;


/**
 * Service for executing complex queries for JobCode entities in the database.
 * The main input is a {@link JobCodeCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JobCode} or a {@link Page} of {@link JobCode} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JobCodeQueryService extends QueryService<JobCode> {

    private final Logger log = LoggerFactory.getLogger(JobCodeQueryService.class);


    private final JobCodeRepository jobCodeRepository;

    public JobCodeQueryService(JobCodeRepository jobCodeRepository) {
        this.jobCodeRepository = jobCodeRepository;
    }

    /**
     * Return a {@link List} of {@link JobCode} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JobCode> findByCriteria(JobCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<JobCode> specification = createSpecification(criteria);
        return jobCodeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link JobCode} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JobCode> findByCriteria(JobCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<JobCode> specification = createSpecification(criteria);
        return jobCodeRepository.findAll(specification, page);
    }

    /**
     * Function to convert JobCodeCriteria to a {@link Specifications}
     */
    private Specifications<JobCode> createSpecification(JobCodeCriteria criteria) {
        Specifications<JobCode> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), JobCode_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), JobCode_.name));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUsersId(), JobCode_.users, User_.id));
            }
        }
        return specification;
    }

}
