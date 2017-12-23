package com.ft.web.rest;

import com.ft.TimesheetApp;

import com.ft.domain.TimeEntry;
import com.ft.domain.Timesheet;
import com.ft.domain.JobCode;
import com.ft.repository.TimeEntryRepository;
import com.ft.service.TimeEntryService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.TimeEntryCriteria;
import com.ft.service.TimeEntryQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TimeEntryResource REST controller.
 *
 * @see TimeEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class TimeEntryResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_DURATION = 1D;
    private static final Double UPDATED_DURATION = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private TimeEntryService timeEntryService;

    @Autowired
    private TimeEntryQueryService timeEntryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimeEntryMockMvc;

    private TimeEntry timeEntry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimeEntryResource timeEntryResource = new TimeEntryResource(timeEntryService, timeEntryQueryService);
        this.restTimeEntryMockMvc = MockMvcBuilders.standaloneSetup(timeEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeEntry createEntity(EntityManager em) {
        TimeEntry timeEntry = new TimeEntry()
            .date(DEFAULT_DATE)
            .duration(DEFAULT_DURATION)
            .description(DEFAULT_DESCRIPTION);
        return timeEntry;
    }

    @Before
    public void initTest() {
        timeEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimeEntry() throws Exception {
        int databaseSizeBeforeCreate = timeEntryRepository.findAll().size();

        // Create the TimeEntry
        restTimeEntryMockMvc.perform(post("/api/time-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeEntry)))
            .andExpect(status().isCreated());

        // Validate the TimeEntry in the database
        List<TimeEntry> timeEntryList = timeEntryRepository.findAll();
        assertThat(timeEntryList).hasSize(databaseSizeBeforeCreate + 1);
        TimeEntry testTimeEntry = timeEntryList.get(timeEntryList.size() - 1);
        assertThat(testTimeEntry.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTimeEntry.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testTimeEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTimeEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeEntryRepository.findAll().size();

        // Create the TimeEntry with an existing ID
        timeEntry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeEntryMockMvc.perform(post("/api/time-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeEntry)))
            .andExpect(status().isBadRequest());

        // Validate the TimeEntry in the database
        List<TimeEntry> timeEntryList = timeEntryRepository.findAll();
        assertThat(timeEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTimeEntries() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList
        restTimeEntryMockMvc.perform(get("/api/time-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTimeEntry() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get the timeEntry
        restTimeEntryMockMvc.perform(get("/api/time-entries/{id}", timeEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timeEntry.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where date equals to DEFAULT_DATE
        defaultTimeEntryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the timeEntryList where date equals to UPDATED_DATE
        defaultTimeEntryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTimeEntryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the timeEntryList where date equals to UPDATED_DATE
        defaultTimeEntryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where date is not null
        defaultTimeEntryShouldBeFound("date.specified=true");

        // Get all the timeEntryList where date is null
        defaultTimeEntryShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where date greater than or equals to DEFAULT_DATE
        defaultTimeEntryShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the timeEntryList where date greater than or equals to UPDATED_DATE
        defaultTimeEntryShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where date less than or equals to DEFAULT_DATE
        defaultTimeEntryShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the timeEntryList where date less than or equals to UPDATED_DATE
        defaultTimeEntryShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllTimeEntriesByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where duration equals to DEFAULT_DURATION
        defaultTimeEntryShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the timeEntryList where duration equals to UPDATED_DURATION
        defaultTimeEntryShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultTimeEntryShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the timeEntryList where duration equals to UPDATED_DURATION
        defaultTimeEntryShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where duration is not null
        defaultTimeEntryShouldBeFound("duration.specified=true");

        // Get all the timeEntryList where duration is null
        defaultTimeEntryShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimeEntriesByTimesheetIsEqualToSomething() throws Exception {
        // Initialize the database
        Timesheet timesheet = TimesheetResourceIntTest.createEntity(em);
        em.persist(timesheet);
        em.flush();
        timeEntry.setTimesheet(timesheet);
        timeEntryRepository.saveAndFlush(timeEntry);
        Long timesheetId = timesheet.getId();

        // Get all the timeEntryList where timesheet equals to timesheetId
        defaultTimeEntryShouldBeFound("timesheetId.equals=" + timesheetId);

        // Get all the timeEntryList where timesheet equals to timesheetId + 1
        defaultTimeEntryShouldNotBeFound("timesheetId.equals=" + (timesheetId + 1));
    }


    @Test
    @Transactional
    public void getAllTimeEntriesByJobCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        JobCode jobCode = JobCodeResourceIntTest.createEntity(em);
        em.persist(jobCode);
        em.flush();
        timeEntry.setJobCode(jobCode);
        timeEntryRepository.saveAndFlush(timeEntry);
        Long jobCodeId = jobCode.getId();

        // Get all the timeEntryList where jobCode equals to jobCodeId
        defaultTimeEntryShouldBeFound("jobCodeId.equals=" + jobCodeId);

        // Get all the timeEntryList where jobCode equals to jobCodeId + 1
        defaultTimeEntryShouldNotBeFound("jobCodeId.equals=" + (jobCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTimeEntryShouldBeFound(String filter) throws Exception {
        restTimeEntryMockMvc.perform(get("/api/time-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTimeEntryShouldNotBeFound(String filter) throws Exception {
        restTimeEntryMockMvc.perform(get("/api/time-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTimeEntry() throws Exception {
        // Get the timeEntry
        restTimeEntryMockMvc.perform(get("/api/time-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeEntry() throws Exception {
        // Initialize the database
        timeEntryService.save(timeEntry);

        int databaseSizeBeforeUpdate = timeEntryRepository.findAll().size();

        // Update the timeEntry
        TimeEntry updatedTimeEntry = timeEntryRepository.findOne(timeEntry.getId());
        // Disconnect from session so that the updates on updatedTimeEntry are not directly saved in db
        em.detach(updatedTimeEntry);
        updatedTimeEntry
            .date(UPDATED_DATE)
            .duration(UPDATED_DURATION)
            .description(UPDATED_DESCRIPTION);

        restTimeEntryMockMvc.perform(put("/api/time-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimeEntry)))
            .andExpect(status().isOk());

        // Validate the TimeEntry in the database
        List<TimeEntry> timeEntryList = timeEntryRepository.findAll();
        assertThat(timeEntryList).hasSize(databaseSizeBeforeUpdate);
        TimeEntry testTimeEntry = timeEntryList.get(timeEntryList.size() - 1);
        assertThat(testTimeEntry.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTimeEntry.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTimeEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTimeEntry() throws Exception {
        int databaseSizeBeforeUpdate = timeEntryRepository.findAll().size();

        // Create the TimeEntry

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimeEntryMockMvc.perform(put("/api/time-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeEntry)))
            .andExpect(status().isCreated());

        // Validate the TimeEntry in the database
        List<TimeEntry> timeEntryList = timeEntryRepository.findAll();
        assertThat(timeEntryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimeEntry() throws Exception {
        // Initialize the database
        timeEntryService.save(timeEntry);

        int databaseSizeBeforeDelete = timeEntryRepository.findAll().size();

        // Get the timeEntry
        restTimeEntryMockMvc.perform(delete("/api/time-entries/{id}", timeEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TimeEntry> timeEntryList = timeEntryRepository.findAll();
        assertThat(timeEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeEntry.class);
        TimeEntry timeEntry1 = new TimeEntry();
        timeEntry1.setId(1L);
        TimeEntry timeEntry2 = new TimeEntry();
        timeEntry2.setId(timeEntry1.getId());
        assertThat(timeEntry1).isEqualTo(timeEntry2);
        timeEntry2.setId(2L);
        assertThat(timeEntry1).isNotEqualTo(timeEntry2);
        timeEntry1.setId(null);
        assertThat(timeEntry1).isNotEqualTo(timeEntry2);
    }
}
