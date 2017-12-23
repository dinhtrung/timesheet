package com.ft.web.rest;

import com.ft.TimesheetApp;

import com.ft.domain.LeaveRequest;
import com.ft.domain.User;
import com.ft.domain.User;
import com.ft.repository.LeaveRequestRepository;
import com.ft.service.LeaveRequestService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.LeaveRequestCriteria;
import com.ft.service.LeaveRequestQueryService;

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
 * Test class for the LeaveRequestResource REST controller.
 *
 * @see LeaveRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class LeaveRequestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ALL_DAY = false;
    private static final Boolean UPDATED_ALL_DAY = true;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final ReviewState DEFAULT_STATE = ReviewState.REJECTED;
    private static final ReviewState UPDATED_STATE = ReviewState.PENDING;

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_APPROVAL_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_APPROVAL_NOTE = "BBBBBBBBBB";

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private LeaveRequestQueryService leaveRequestQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeaveRequestMockMvc;

    private LeaveRequest leaveRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeaveRequestResource leaveRequestResource = new LeaveRequestResource(leaveRequestService, leaveRequestQueryService);
        this.restLeaveRequestMockMvc = MockMvcBuilders.standaloneSetup(leaveRequestResource)
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
    public static LeaveRequest createEntity(EntityManager em) {
        LeaveRequest leaveRequest = new LeaveRequest()
            .name(DEFAULT_NAME)
            .note(DEFAULT_NOTE)
            .allDay(DEFAULT_ALL_DAY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .number(DEFAULT_NUMBER)
            .state(DEFAULT_STATE)
            .updatedAt(DEFAULT_UPDATED_AT)
            .approvalNote(DEFAULT_APPROVAL_NOTE);
        return leaveRequest;
    }

    @Before
    public void initTest() {
        leaveRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveRequest() throws Exception {
        int databaseSizeBeforeCreate = leaveRequestRepository.findAll().size();

        // Create the LeaveRequest
        restLeaveRequestMockMvc.perform(post("/api/leave-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRequest)))
            .andExpect(status().isCreated());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveRequest testLeaveRequest = leaveRequestList.get(leaveRequestList.size() - 1);
        assertThat(testLeaveRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLeaveRequest.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testLeaveRequest.isAllDay()).isEqualTo(DEFAULT_ALL_DAY);
        assertThat(testLeaveRequest.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaveRequest.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeaveRequest.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testLeaveRequest.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testLeaveRequest.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testLeaveRequest.getApprovalNote()).isEqualTo(DEFAULT_APPROVAL_NOTE);
    }

    @Test
    @Transactional
    public void createLeaveRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveRequestRepository.findAll().size();

        // Create the LeaveRequest with an existing ID
        leaveRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveRequestMockMvc.perform(post("/api/leave-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRequest)))
            .andExpect(status().isBadRequest());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLeaveRequests() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList
        restLeaveRequestMockMvc.perform(get("/api/leave-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].approvalNote").value(hasItem(DEFAULT_APPROVAL_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getLeaveRequest() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get the leaveRequest
        restLeaveRequestMockMvc.perform(get("/api/leave-requests/{id}", leaveRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leaveRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.allDay").value(DEFAULT_ALL_DAY.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.approvalNote").value(DEFAULT_APPROVAL_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where name equals to DEFAULT_NAME
        defaultLeaveRequestShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the leaveRequestList where name equals to UPDATED_NAME
        defaultLeaveRequestShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLeaveRequestShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the leaveRequestList where name equals to UPDATED_NAME
        defaultLeaveRequestShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where name is not null
        defaultLeaveRequestShouldBeFound("name.specified=true");

        // Get all the leaveRequestList where name is null
        defaultLeaveRequestShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByAllDayIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where allDay equals to DEFAULT_ALL_DAY
        defaultLeaveRequestShouldBeFound("allDay.equals=" + DEFAULT_ALL_DAY);

        // Get all the leaveRequestList where allDay equals to UPDATED_ALL_DAY
        defaultLeaveRequestShouldNotBeFound("allDay.equals=" + UPDATED_ALL_DAY);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByAllDayIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where allDay in DEFAULT_ALL_DAY or UPDATED_ALL_DAY
        defaultLeaveRequestShouldBeFound("allDay.in=" + DEFAULT_ALL_DAY + "," + UPDATED_ALL_DAY);

        // Get all the leaveRequestList where allDay equals to UPDATED_ALL_DAY
        defaultLeaveRequestShouldNotBeFound("allDay.in=" + UPDATED_ALL_DAY);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByAllDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where allDay is not null
        defaultLeaveRequestShouldBeFound("allDay.specified=true");

        // Get all the leaveRequestList where allDay is null
        defaultLeaveRequestShouldNotBeFound("allDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where startDate equals to DEFAULT_START_DATE
        defaultLeaveRequestShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the leaveRequestList where startDate equals to UPDATED_START_DATE
        defaultLeaveRequestShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultLeaveRequestShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the leaveRequestList where startDate equals to UPDATED_START_DATE
        defaultLeaveRequestShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where startDate is not null
        defaultLeaveRequestShouldBeFound("startDate.specified=true");

        // Get all the leaveRequestList where startDate is null
        defaultLeaveRequestShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where startDate greater than or equals to DEFAULT_START_DATE
        defaultLeaveRequestShouldBeFound("startDate.greaterOrEqualThan=" + DEFAULT_START_DATE);

        // Get all the leaveRequestList where startDate greater than or equals to UPDATED_START_DATE
        defaultLeaveRequestShouldNotBeFound("startDate.greaterOrEqualThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where startDate less than or equals to DEFAULT_START_DATE
        defaultLeaveRequestShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the leaveRequestList where startDate less than or equals to UPDATED_START_DATE
        defaultLeaveRequestShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }


    @Test
    @Transactional
    public void getAllLeaveRequestsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where endDate equals to DEFAULT_END_DATE
        defaultLeaveRequestShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the leaveRequestList where endDate equals to UPDATED_END_DATE
        defaultLeaveRequestShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultLeaveRequestShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the leaveRequestList where endDate equals to UPDATED_END_DATE
        defaultLeaveRequestShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where endDate is not null
        defaultLeaveRequestShouldBeFound("endDate.specified=true");

        // Get all the leaveRequestList where endDate is null
        defaultLeaveRequestShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where endDate greater than or equals to DEFAULT_END_DATE
        defaultLeaveRequestShouldBeFound("endDate.greaterOrEqualThan=" + DEFAULT_END_DATE);

        // Get all the leaveRequestList where endDate greater than or equals to UPDATED_END_DATE
        defaultLeaveRequestShouldNotBeFound("endDate.greaterOrEqualThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where endDate less than or equals to DEFAULT_END_DATE
        defaultLeaveRequestShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the leaveRequestList where endDate less than or equals to UPDATED_END_DATE
        defaultLeaveRequestShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }


    @Test
    @Transactional
    public void getAllLeaveRequestsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where number equals to DEFAULT_NUMBER
        defaultLeaveRequestShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the leaveRequestList where number equals to UPDATED_NUMBER
        defaultLeaveRequestShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultLeaveRequestShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the leaveRequestList where number equals to UPDATED_NUMBER
        defaultLeaveRequestShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where number is not null
        defaultLeaveRequestShouldBeFound("number.specified=true");

        // Get all the leaveRequestList where number is null
        defaultLeaveRequestShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where number greater than or equals to DEFAULT_NUMBER
        defaultLeaveRequestShouldBeFound("number.greaterOrEqualThan=" + DEFAULT_NUMBER);

        // Get all the leaveRequestList where number greater than or equals to UPDATED_NUMBER
        defaultLeaveRequestShouldNotBeFound("number.greaterOrEqualThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where number less than or equals to DEFAULT_NUMBER
        defaultLeaveRequestShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the leaveRequestList where number less than or equals to UPDATED_NUMBER
        defaultLeaveRequestShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLeaveRequestsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where state equals to DEFAULT_STATE
        defaultLeaveRequestShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the leaveRequestList where state equals to UPDATED_STATE
        defaultLeaveRequestShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where state in DEFAULT_STATE or UPDATED_STATE
        defaultLeaveRequestShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the leaveRequestList where state equals to UPDATED_STATE
        defaultLeaveRequestShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where state is not null
        defaultLeaveRequestShouldBeFound("state.specified=true");

        // Get all the leaveRequestList where state is null
        defaultLeaveRequestShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultLeaveRequestShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the leaveRequestList where updatedAt equals to UPDATED_UPDATED_AT
        defaultLeaveRequestShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultLeaveRequestShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the leaveRequestList where updatedAt equals to UPDATED_UPDATED_AT
        defaultLeaveRequestShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where updatedAt is not null
        defaultLeaveRequestShouldBeFound("updatedAt.specified=true");

        // Get all the leaveRequestList where updatedAt is null
        defaultLeaveRequestShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where updatedAt greater than or equals to DEFAULT_UPDATED_AT
        defaultLeaveRequestShouldBeFound("updatedAt.greaterOrEqualThan=" + DEFAULT_UPDATED_AT);

        // Get all the leaveRequestList where updatedAt greater than or equals to UPDATED_UPDATED_AT
        defaultLeaveRequestShouldNotBeFound("updatedAt.greaterOrEqualThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllLeaveRequestsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRequestRepository.saveAndFlush(leaveRequest);

        // Get all the leaveRequestList where updatedAt less than or equals to DEFAULT_UPDATED_AT
        defaultLeaveRequestShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the leaveRequestList where updatedAt less than or equals to UPDATED_UPDATED_AT
        defaultLeaveRequestShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }


    @Test
    @Transactional
    public void getAllLeaveRequestsByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        leaveRequest.setOwner(owner);
        leaveRequestRepository.saveAndFlush(leaveRequest);
        Long ownerId = owner.getId();

        // Get all the leaveRequestList where owner equals to ownerId
        defaultLeaveRequestShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the leaveRequestList where owner equals to ownerId + 1
        defaultLeaveRequestShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }


    @Test
    @Transactional
    public void getAllLeaveRequestsByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        User approvedBy = UserResourceIntTest.createEntity(em);
        em.persist(approvedBy);
        em.flush();
        leaveRequest.setApprovedBy(approvedBy);
        leaveRequestRepository.saveAndFlush(leaveRequest);
        Long approvedById = approvedBy.getId();

        // Get all the leaveRequestList where approvedBy equals to approvedById
        defaultLeaveRequestShouldBeFound("approvedById.equals=" + approvedById);

        // Get all the leaveRequestList where approvedBy equals to approvedById + 1
        defaultLeaveRequestShouldNotBeFound("approvedById.equals=" + (approvedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLeaveRequestShouldBeFound(String filter) throws Exception {
        restLeaveRequestMockMvc.perform(get("/api/leave-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].approvalNote").value(hasItem(DEFAULT_APPROVAL_NOTE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLeaveRequestShouldNotBeFound(String filter) throws Exception {
        restLeaveRequestMockMvc.perform(get("/api/leave-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingLeaveRequest() throws Exception {
        // Get the leaveRequest
        restLeaveRequestMockMvc.perform(get("/api/leave-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveRequest() throws Exception {
        // Initialize the database
        leaveRequestService.save(leaveRequest);

        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();

        // Update the leaveRequest
        LeaveRequest updatedLeaveRequest = leaveRequestRepository.findOne(leaveRequest.getId());
        // Disconnect from session so that the updates on updatedLeaveRequest are not directly saved in db
        em.detach(updatedLeaveRequest);
        updatedLeaveRequest
            .name(UPDATED_NAME)
            .note(UPDATED_NOTE)
            .allDay(UPDATED_ALL_DAY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .number(UPDATED_NUMBER)
            .state(UPDATED_STATE)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvalNote(UPDATED_APPROVAL_NOTE);

        restLeaveRequestMockMvc.perform(put("/api/leave-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLeaveRequest)))
            .andExpect(status().isOk());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate);
        LeaveRequest testLeaveRequest = leaveRequestList.get(leaveRequestList.size() - 1);
        assertThat(testLeaveRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLeaveRequest.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testLeaveRequest.isAllDay()).isEqualTo(UPDATED_ALL_DAY);
        assertThat(testLeaveRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaveRequest.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaveRequest.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testLeaveRequest.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testLeaveRequest.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testLeaveRequest.getApprovalNote()).isEqualTo(UPDATED_APPROVAL_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveRequest() throws Exception {
        int databaseSizeBeforeUpdate = leaveRequestRepository.findAll().size();

        // Create the LeaveRequest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeaveRequestMockMvc.perform(put("/api/leave-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRequest)))
            .andExpect(status().isCreated());

        // Validate the LeaveRequest in the database
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLeaveRequest() throws Exception {
        // Initialize the database
        leaveRequestService.save(leaveRequest);

        int databaseSizeBeforeDelete = leaveRequestRepository.findAll().size();

        // Get the leaveRequest
        restLeaveRequestMockMvc.perform(delete("/api/leave-requests/{id}", leaveRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LeaveRequest> leaveRequestList = leaveRequestRepository.findAll();
        assertThat(leaveRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveRequest.class);
        LeaveRequest leaveRequest1 = new LeaveRequest();
        leaveRequest1.setId(1L);
        LeaveRequest leaveRequest2 = new LeaveRequest();
        leaveRequest2.setId(leaveRequest1.getId());
        assertThat(leaveRequest1).isEqualTo(leaveRequest2);
        leaveRequest2.setId(2L);
        assertThat(leaveRequest1).isNotEqualTo(leaveRequest2);
        leaveRequest1.setId(null);
        assertThat(leaveRequest1).isNotEqualTo(leaveRequest2);
    }
}
