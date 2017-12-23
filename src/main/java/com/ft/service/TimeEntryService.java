package com.ft.service;

import com.ft.domain.TimeEntry;
import com.ft.repository.TimeEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TimeEntry.
 */
@Service
@Transactional
public class TimeEntryService {

    private final Logger log = LoggerFactory.getLogger(TimeEntryService.class);

    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryService(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    /**
     * Save a timeEntry.
     *
     * @param timeEntry the entity to save
     * @return the persisted entity
     */
    public TimeEntry save(TimeEntry timeEntry) {
        log.debug("Request to save TimeEntry : {}", timeEntry);
        return timeEntryRepository.save(timeEntry);
    }

    /**
     * Get all the timeEntries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TimeEntry> findAll(Pageable pageable) {
        log.debug("Request to get all TimeEntries");
        return timeEntryRepository.findAll(pageable);
    }

    /**
     * Get one timeEntry by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TimeEntry findOne(Long id) {
        log.debug("Request to get TimeEntry : {}", id);
        return timeEntryRepository.findOne(id);
    }

    /**
     * Delete the timeEntry by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeEntry : {}", id);
        timeEntryRepository.delete(id);
    }
}
