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

import com.ft.domain.MeetingInvitation;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.MeetingInvitationRepository;
import com.ft.service.dto.MeetingInvitationCriteria;


/**
 * Service for executing complex queries for MeetingInvitation entities in the database.
 * The main input is a {@link MeetingInvitationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MeetingInvitation} or a {@link Page} of {@link MeetingInvitation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MeetingInvitationQueryService extends QueryService<MeetingInvitation> {

    private final Logger log = LoggerFactory.getLogger(MeetingInvitationQueryService.class);


    private final MeetingInvitationRepository meetingInvitationRepository;

    public MeetingInvitationQueryService(MeetingInvitationRepository meetingInvitationRepository) {
        this.meetingInvitationRepository = meetingInvitationRepository;
    }

    /**
     * Return a {@link List} of {@link MeetingInvitation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MeetingInvitation> findByCriteria(MeetingInvitationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MeetingInvitation> specification = createSpecification(criteria);
        return meetingInvitationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MeetingInvitation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MeetingInvitation> findByCriteria(MeetingInvitationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MeetingInvitation> specification = createSpecification(criteria);
        return meetingInvitationRepository.findAll(specification, page);
    }

    /**
     * Function to convert MeetingInvitationCriteria to a {@link Specifications}
     */
    private Specifications<MeetingInvitation> createSpecification(MeetingInvitationCriteria criteria) {
        Specifications<MeetingInvitation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MeetingInvitation_.id));
            }
            if (criteria.getDecidedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecidedAt(), MeetingInvitation_.decidedAt));
            }
            if (criteria.getAccepted() != null) {
                specification = specification.and(buildSpecification(criteria.getAccepted(), MeetingInvitation_.accepted));
            }
            if (criteria.getMeetingId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMeetingId(), MeetingInvitation_.meeting, Meeting_.id));
            }
        }
        return specification;
    }

}
