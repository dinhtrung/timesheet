package com.ft.web.rest;

import com.ft.TimesheetApp;

import com.ft.domain.Timesheet;
import com.ft.domain.User;
import com.ft.domain.User;
import com.ft.repository.TimesheetRepository;
import com.ft.service.TimesheetService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.TimesheetCriteria;
import com.ft.service.TimesheetQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.ft.web.rest.TestUtil.sameInstant;
import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ft.domain.enumeration.ReviewState;
/**
 * Test class for the TimesheetResource REST controller.
 *
 * @see TimesheetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class TimesheetResourceIntTest {

    private static final ZonedDateTime DEFAULT_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_APPROVAL_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_NOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 9999;
    private static final Integer UPDATED_YEAR = 9998;

    private static final Integer DEFAULT_WEEK = 1;
    private static final Integer UPDATED_WEEK = 2;

    private static final ReviewState DEFAULT_STATE = ReviewState.REJECTED;
    private static final ReviewState UPDATED_STATE = ReviewState.PENDING;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private TimesheetQueryService timesheetQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimesheetMockMvc;

    private Timesheet timesheet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimesheetResource timesheetResource = new TimesheetResource(timesheetService, timesheetQueryService);
        this.restTimesheetMockMvc = MockMvcBuilders.standaloneSetup(timesheetResource)
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
    public static Timesheet createEntity(EntityManager em) {
        Timesheet timesheet = new Timesheet()
            .approvedAt(DEFAULT_APPROVED_AT)
            .approvalNote(DEFAULT_APPROVAL_NOTE)
            .year(DEFAULT_YEAR)
            .week(DEFAULT_WEEK)
            .state(DEFAULT_STATE)
            .comment(DEFAULT_COMMENT);
        return timesheet;
    }

    @Before
    public void initTest() {
        timesheet = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimesheet() throws Exception {
        int databaseSizeBeforeCreate = timesheetRepository.findAll().size();

        // Create the Timesheet
        restTimesheetMockMvc.perform(post("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isCreated());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeCreate + 1);
        Timesheet testTimesheet = timesheetList.get(timesheetList.size() - 1);
        assertThat(testTimesheet.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testTimesheet.getApprovalNote()).isEqualTo(DEFAULT_APPROVAL_NOTE);
        assertThat(testTimesheet.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testTimesheet.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testTimesheet.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testTimesheet.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createTimesheetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timesheetRepository.findAll().size();

        // Create the Timesheet with an existing ID
        timesheet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimesheetMockMvc.perform(post("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isBadRequest());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTimesheets() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList
        restTimesheetMockMvc.perform(get("/api/timesheets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].approvalNote").value(hasItem(DEFAULT_APPROVAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", timesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timesheet.getId().intValue()))
            .andExpect(jsonPath("$.approvedAt").value(sameInstant(DEFAULT_APPROVED_AT)))
            .andExpect(jsonPath("$.approvalNote").value(DEFAULT_APPROVAL_NOTE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllTimesheetsByApprovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt equals to DEFAULT_APPROVED_AT
        defaultTimesheetShouldBeFound("approvedAt.equals=" + DEFAULT_APPROVED_AT);

        // Get all the timesheetList where approvedAt equals to UPDATED_APPROVED_AT
        defaultTimesheetShouldNotBeFound("approvedAt.equals=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByApprovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt in DEFAULT_APPROVED_AT or UPDATED_APPROVED_AT
        defaultTimesheetShouldBeFound("approvedAt.in=" + DEFAULT_APPROVED_AT + "," + UPDATED_APPROVED_AT);

        // Get all the timesheetList where approvedAt equals to UPDATED_APPROVED_AT
        defaultTimesheetShouldNotBeFound("approvedAt.in=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByApprovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt is not null
        defaultTimesheetShouldBeFound("approvedAt.specified=true");

        // Get all the timesheetList where approvedAt is null
        defaultTimesheetShouldNotBeFound("approvedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimesheetsByApprovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt greater than or equals to DEFAULT_APPROVED_AT
        defaultTimesheetShouldBeFound("approvedAt.greaterOrEqualThan=" + DEFAULT_APPROVED_AT);

        // Get all the timesheetList where approvedAt greater than or equals to UPDATED_APPROVED_AT
        defaultTimesheetShouldNotBeFound("approvedAt.greaterOrEqualThan=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByApprovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt less than or equals to DEFAULT_APPROVED_AT
        defaultTimesheetShouldNotBeFound("approvedAt.lessThan=" + DEFAULT_APPROVED_AT);

        // Get all the timesheetList where approvedAt less than or equals to UPDATED_APPROVED_AT
        defaultTimesheetShouldBeFound("approvedAt.lessThan=" + UPDATED_APPROVED_AT);
    }


    @Test
    @Transactional
    public void getAllTimesheetsByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where year equals to DEFAULT_YEAR
        defaultTimesheetShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the timesheetList where year equals to UPDATED_YEAR
        defaultTimesheetShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByYearIsInShouldWork() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultTimesheetShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the timesheetList where year equals to UPDATED_YEAR
        defaultTimesheetShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where year is not null
        defaultTimesheetShouldBeFound("year.specified=true");

        // Get all the timesheetList where year is null
        defaultTimesheetShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimesheetsByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where year greater than or equals to DEFAULT_YEAR
        defaultTimesheetShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the timesheetList where year greater than or equals to (DEFAULT_YEAR + 1)
        defaultTimesheetShouldNotBeFound("year.greaterOrEqualThan=" + (DEFAULT_YEAR + 1));
    }

    @Test
    @Transactional
    public void getAllTimesheetsByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where year less than or equals to DEFAULT_YEAR
        defaultTimesheetShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the timesheetList where year less than or equals to (DEFAULT_YEAR + 1)
        defaultTimesheetShouldBeFound("year.lessThan=" + (DEFAULT_YEAR + 1));
    }


    @Test
    @Transactional
    public void getAllTimesheetsByWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where week equals to DEFAULT_WEEK
        defaultTimesheetShouldBeFound("week.equals=" + DEFAULT_WEEK);

        // Get all the timesheetList where week equals to UPDATED_WEEK
        defaultTimesheetShouldNotBeFound("week.equals=" + UPDATED_WEEK);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByWeekIsInShouldWork() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where week in DEFAULT_WEEK or UPDATED_WEEK
        defaultTimesheetShouldBeFound("week.in=" + DEFAULT_WEEK + "," + UPDATED_WEEK);

        // Get all the timesheetList where week equals to UPDATED_WEEK
        defaultTimesheetShouldNotBeFound("week.in=" + UPDATED_WEEK);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where week is not null
        defaultTimesheetShouldBeFound("week.specified=true");

        // Get all the timesheetList where week is null
        defaultTimesheetShouldNotBeFound("week.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimesheetsByWeekIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where week greater than or equals to DEFAULT_WEEK
        defaultTimesheetShouldBeFound("week.greaterOrEqualThan=" + DEFAULT_WEEK);

        // Get all the timesheetList where week greater than or equals to UPDATED_WEEK
        defaultTimesheetShouldNotBeFound("week.greaterOrEqualThan=" + UPDATED_WEEK);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByWeekIsLessThanSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where week less than or equals to DEFAULT_WEEK
        defaultTimesheetShouldNotBeFound("week.lessThan=" + DEFAULT_WEEK);

        // Get all the timesheetList where week less than or equals to UPDATED_WEEK
        defaultTimesheetShouldBeFound("week.lessThan=" + UPDATED_WEEK);
    }


    @Test
    @Transactional
    public void getAllTimesheetsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where state equals to DEFAULT_STATE
        defaultTimesheetShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the timesheetList where state equals to UPDATED_STATE
        defaultTimesheetShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where state in DEFAULT_STATE or UPDATED_STATE
        defaultTimesheetShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the timesheetList where state equals to UPDATED_STATE
        defaultTimesheetShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllTimesheetsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where state is not null
        defaultTimesheetShouldBeFound("state.specified=true");

        // Get all the timesheetList where state is null
        defaultTimesheetShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllTimesheetsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        timesheet.setOwner(owner);
        timesheetRepository.saveAndFlush(timesheet);
        Long ownerId = owner.getId();

        // Get all the timesheetList where owner equals to ownerId
        defaultTimesheetShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the timesheetList where owner equals to ownerId + 1
        defaultTimesheetShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }


    @Test
    @Transactional
    public void getAllTimesheetsByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        User approvedBy = UserResourceIntTest.createEntity(em);
        em.persist(approvedBy);
        em.flush();
        timesheet.setApprovedBy(approvedBy);
        timesheetRepository.saveAndFlush(timesheet);
        Long approvedById = approvedBy.getId();

        // Get all the timesheetList where approvedBy equals to approvedById
        defaultTimesheetShouldBeFound("approvedById.equals=" + approvedById);

        // Get all the timesheetList where approvedBy equals to approvedById + 1
        defaultTimesheetShouldNotBeFound("approvedById.equals=" + (approvedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTimesheetShouldBeFound(String filter) throws Exception {
        restTimesheetMockMvc.perform(get("/api/timesheets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].approvalNote").value(hasItem(DEFAULT_APPROVAL_NOTE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTimesheetShouldNotBeFound(String filter) throws Exception {
        restTimesheetMockMvc.perform(get("/api/timesheets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTimesheet() throws Exception {
        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimesheet() throws Exception {
        // Initialize the database
        timesheetService.save(timesheet);

        int databaseSizeBeforeUpdate = timesheetRepository.findAll().size();

        // Update the timesheet
        Timesheet updatedTimesheet = timesheetRepository.findOne(timesheet.getId());
        // Disconnect from session so that the updates on updatedTimesheet are not directly saved in db
        em.detach(updatedTimesheet);
        updatedTimesheet
            .approvedAt(UPDATED_APPROVED_AT)
            .approvalNote(UPDATED_APPROVAL_NOTE)
            .year(UPDATED_YEAR)
            .week(UPDATED_WEEK)
            .state(UPDATED_STATE)
            .comment(UPDATED_COMMENT);

        restTimesheetMockMvc.perform(put("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimesheet)))
            .andExpect(status().isOk());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeUpdate);
        Timesheet testTimesheet = timesheetList.get(timesheetList.size() - 1);
        assertThat(testTimesheet.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testTimesheet.getApprovalNote()).isEqualTo(UPDATED_APPROVAL_NOTE);
        assertThat(testTimesheet.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testTimesheet.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testTimesheet.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testTimesheet.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingTimesheet() throws Exception {
        int databaseSizeBeforeUpdate = timesheetRepository.findAll().size();

        // Create the Timesheet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimesheetMockMvc.perform(put("/api/timesheets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timesheet)))
            .andExpect(status().isCreated());

        // Validate the Timesheet in the database
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimesheet() throws Exception {
        // Initialize the database
        timesheetService.save(timesheet);

        int databaseSizeBeforeDelete = timesheetRepository.findAll().size();

        // Get the timesheet
        restTimesheetMockMvc.perform(delete("/api/timesheets/{id}", timesheet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Timesheet> timesheetList = timesheetRepository.findAll();
        assertThat(timesheetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timesheet.class);
        Timesheet timesheet1 = new Timesheet();
        timesheet1.setId(1L);
        Timesheet timesheet2 = new Timesheet();
        timesheet2.setId(timesheet1.getId());
        assertThat(timesheet1).isEqualTo(timesheet2);
        timesheet2.setId(2L);
        assertThat(timesheet1).isNotEqualTo(timesheet2);
        timesheet1.setId(null);
        assertThat(timesheet1).isNotEqualTo(timesheet2);
    }
}
