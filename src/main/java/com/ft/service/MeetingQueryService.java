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

import com.ft.domain.Meeting;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.MeetingRepository;
import com.ft.service.dto.MeetingCriteria;


/**
 * Service for executing complex queries for Meeting entities in the database.
 * The main input is a {@link MeetingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Meeting} or a {@link Page} of {@link Meeting} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeetingQueryService extends QueryService<Meeting> {

    private final Logger log = LoggerFactory.getLogger(MeetingQueryService.class);


    private final MeetingRepository meetingRepository;

    public MeetingQueryService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    /**
     * Return a {@link List} of {@link Meeting} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Meeting> findByCriteria(MeetingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Meeting> specification = createSpecification(criteria);
        return meetingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Meeting} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Meeting> findByCriteria(MeetingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Meeting> specification = createSpecification(criteria);
        return meetingRepository.findAll(specification, page);
    }

    /**
     * Function to convert MeetingCriteria to a {@link Specifications}
     */
    private Specifications<Meeting> createSpecification(MeetingCriteria criteria) {
        Specifications<Meeting> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Meeting_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Meeting_.name));
            }
            if (criteria.getAllDay() != null) {
                specification = specification.and(buildSpecification(criteria.getAllDay(), Meeting_.allDay));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Meeting_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Meeting_.endDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Meeting_.user, User_.id));
            }
        }
        return specification;
    }

}
