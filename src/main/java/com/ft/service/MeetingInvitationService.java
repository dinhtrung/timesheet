package com.ft.service;

import com.ft.domain.MeetingInvitation;
import com.ft.repository.MeetingInvitationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MeetingInvitation.
 */
@Service
@Transactional
public class MeetingInvitationService {

    private final Logger log = LoggerFactory.getLogger(MeetingInvitationService.class);

    private final MeetingInvitationRepository meetingInvitationRepository;

    public MeetingInvitationService(MeetingInvitationRepository meetingInvitationRepository) {
        this.meetingInvitationRepository = meetingInvitationRepository;
    }

    /**
     * Save a meetingInvitation.
     *
     * @param meetingInvitation the entity to save
     * @return the persisted entity
     */
    public MeetingInvitation save(MeetingInvitation meetingInvitation) {
        log.debug("Request to save MeetingInvitation : {}", meetingInvitation);
        return meetingInvitationRepository.save(meetingInvitation);
    }

    /**
     * Get all the meetingInvitations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MeetingInvitation> findAll(Pageable pageable) {
        log.debug("Request to get all MeetingInvitations");
        return meetingInvitationRepository.findAll(pageable);
    }

    /**
     * Get one meetingInvitation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MeetingInvitation findOne(Long id) {
        log.debug("Request to get MeetingInvitation : {}", id);
        return meetingInvitationRepository.findOne(id);
    }

    /**
     * Delete the meetingInvitation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeetingInvitation : {}", id);
        meetingInvitationRepository.delete(id);
    }
}
