package com.ft.service;

import com.ft.domain.Timesheet;
import com.ft.repository.TimesheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Timesheet.
 */
@Service
@Transactional
public class TimesheetService {

    private final Logger log = LoggerFactory.getLogger(TimesheetService.class);

    private final TimesheetRepository timesheetRepository;

    public TimesheetService(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    /**
     * Save a timesheet.
     *
     * @param timesheet the entity to save
     * @return the persisted entity
     */
    public Timesheet save(Timesheet timesheet) {
        log.debug("Request to save Timesheet : {}", timesheet);
        return timesheetRepository.save(timesheet);
    }

    /**
     * Get all the timesheets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Timesheet> findAll(Pageable pageable) {
        log.debug("Request to get all Timesheets");
        return timesheetRepository.findAll(pageable);
    }

    /**
     * Get one timesheet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Timesheet findOne(Long id) {
        log.debug("Request to get Timesheet : {}", id);
        return timesheetRepository.findOne(id);
    }

    /**
     * Delete the timesheet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Timesheet : {}", id);
        timesheetRepository.delete(id);
    }
}
