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

import com.ft.domain.Feedback;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.FeedbackRepository;
import com.ft.service.dto.FeedbackCriteria;


/**
 * Service for executing complex queries for Feedback entities in the database.
 * The main input is a {@link FeedbackCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Feedback} or a {@link Page} of {@link Feedback} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeedbackQueryService extends QueryService<Feedback> {

    private final Logger log = LoggerFactory.getLogger(FeedbackQueryService.class);


    private final FeedbackRepository feedbackRepository;

    public FeedbackQueryService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Return a {@link List} of {@link Feedback} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Feedback> findByCriteria(FeedbackCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Feedback} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Feedback> findByCriteria(FeedbackCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification, page);
    }

    /**
     * Function to convert FeedbackCriteria to a {@link Specifications}
     */
    private Specifications<Feedback> createSpecification(FeedbackCriteria criteria) {
        Specifications<Feedback> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Feedback_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Feedback_.createdAt));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Feedback_.name));
            }
            if (criteria.getRepliedToId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getRepliedToId(), Feedback_.repliedTo, Feedback_.id));
            }
            if (criteria.getTimesheetId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTimesheetId(), Feedback_.timesheet, Timesheet_.id));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCreatedById(), Feedback_.createdBy, User_.id));
            }
        }
        return specification;
    }

}
