package com.ft.service;

import com.ft.domain.LeaveRequest;
import com.ft.repository.LeaveRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing LeaveRequest.
 */
@Service
@Transactional
public class LeaveRequestService {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestService.class);

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    /**
     * Save a leaveRequest.
     *
     * @param leaveRequest the entity to save
     * @return the persisted entity
     */
    public LeaveRequest save(LeaveRequest leaveRequest) {
        log.debug("Request to save LeaveRequest : {}", leaveRequest);
        return leaveRequestRepository.save(leaveRequest);
    }

    /**
     * Get all the leaveRequests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LeaveRequest> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveRequests");
        return leaveRequestRepository.findAll(pageable);
    }

    /**
     * Get one leaveRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public LeaveRequest findOne(Long id) {
        log.debug("Request to get LeaveRequest : {}", id);
        return leaveRequestRepository.findOne(id);
    }

    /**
     * Delete the leaveRequest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveRequest : {}", id);
        leaveRequestRepository.delete(id);
    }
}
