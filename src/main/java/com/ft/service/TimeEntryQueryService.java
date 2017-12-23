package com.ft.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ft.domain.TimeEntry;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.TimeEntryRepository;
import com.ft.service.dto.TimeEntryCriteria;


/**
 * Service for executing complex queries for TimeEntry entities in the database.
 * The main input is a {@link TimeEntryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TimeEntry} or a {@link Page} of {@link TimeEntry} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimeEntryQueryService extends QueryService<TimeEntry> {

    private final Logger log = LoggerFactory.getLogger(TimeEntryQueryService.class);


    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryQueryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    /**
     * Return a {@link List} of {@link TimeEntry} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TimeEntry> findByCriteria(TimeEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TimeEntry> specification = createSpecification(criteria);
        return timeEntryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TimeEntry} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TimeEntry> findByCriteria(TimeEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TimeEntry> specification = createSpecification(criteria);
        return timeEntryRepository.findAll(specification, page);
    }

    /**
     * Function to convert TimeEntryCriteria to a {@link Specifications}
     */
    private Specifications<TimeEntry> createSpecification(TimeEntryCriteria criteria) {
        Specifications<TimeEntry> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TimeEntry_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), TimeEntry_.date));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), TimeEntry_.duration));
            }
            if (criteria.getTimesheetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTimesheetId(), TimeEntry_.timesheet, Timesheet_.id));
            }
            if (criteria.getJobCodeId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getJobCodeId(), TimeEntry_.jobCode, JobCode_.id));
            }
        }
        return specification;
    }

}
