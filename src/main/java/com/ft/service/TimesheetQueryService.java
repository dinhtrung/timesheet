package com.ft.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ft.domain.Timesheet;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.TimesheetRepository;
import com.ft.service.dto.TimesheetCriteria;

import com.ft.domain.enumeration.ReviewState;

/**
 * Service for executing complex queries for Timesheet entities in the database.
 * The main input is a {@link TimesheetCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Timesheet} or a {@link Page} of {@link Timesheet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimesheetQueryService extends QueryService<Timesheet> {

    private final Logger log = LoggerFactory.getLogger(TimesheetQueryService.class);


    private final TimesheetRepository timesheetRepository;

    public TimesheetQueryService(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    /**
     * Return a {@link List} of {@link Timesheet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Timesheet> findByCriteria(TimesheetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Timesheet> specification = createSpecification(criteria);
        return timesheetRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Timesheet} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Timesheet> findByCriteria(TimesheetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Timesheet> specification = createSpecification(criteria);
        return timesheetRepository.findAll(specification, page);
    }

    /**
     * Function to convert TimesheetCriteria to a {@link Specifications}
     */
    private Specifications<Timesheet> createSpecification(TimesheetCriteria criteria) {
        Specifications<Timesheet> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Timesheet_.id));
            }
            if (criteria.getApprovedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedAt(), Timesheet_.approvedAt));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), Timesheet_.year));
            }
            if (criteria.getWeek() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeek(), Timesheet_.week));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), Timesheet_.state));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOwnerId(), Timesheet_.owner, User_.id));
            }
            if (criteria.getApprovedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getApprovedById(), Timesheet_.approvedBy, User_.id));
            }
        }
        return specification;
    }

}
