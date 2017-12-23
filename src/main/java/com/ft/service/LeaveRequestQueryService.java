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

import com.ft.domain.LeaveRequest;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.LeaveRequestRepository;
import com.ft.service.dto.LeaveRequestCriteria;

import com.ft.domain.enumeration.ReviewState;

/**
 * Service for executing complex queries for LeaveRequest entities in the database.
 * The main input is a {@link LeaveRequestCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveRequest} or a {@link Page} of {@link LeaveRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveRequestQueryService extends QueryService<LeaveRequest> {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestQueryService.class);


    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestQueryService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    /**
     * Return a {@link List} of {@link LeaveRequest} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveRequest> findByCriteria(LeaveRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<LeaveRequest> specification = createSpecification(criteria);
        return leaveRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LeaveRequest} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveRequest> findByCriteria(LeaveRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<LeaveRequest> specification = createSpecification(criteria);
        return leaveRequestRepository.findAll(specification, page);
    }

    /**
     * Function to convert LeaveRequestCriteria to a {@link Specifications}
     */
    private Specifications<LeaveRequest> createSpecification(LeaveRequestCriteria criteria) {
        Specifications<LeaveRequest> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LeaveRequest_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), LeaveRequest_.name));
            }
            if (criteria.getAllDay() != null) {
                specification = specification.and(buildSpecification(criteria.getAllDay(), LeaveRequest_.allDay));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), LeaveRequest_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), LeaveRequest_.endDate));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), LeaveRequest_.number));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildSpecification(criteria.getState(), LeaveRequest_.state));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), LeaveRequest_.updatedAt));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOwnerId(), LeaveRequest_.owner, User_.id));
            }
            if (criteria.getApprovedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getApprovedById(), LeaveRequest_.approvedBy, User_.id));
            }
        }
        return specification;
    }

}
