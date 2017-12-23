package com.ft.web.rest;

import com.ft.TimesheetApp;

import com.ft.domain.MeetingInvitation;
import com.ft.domain.Meeting;
import com.ft.repository.MeetingInvitationRepository;
import com.ft.service.MeetingInvitationService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.MeetingInvitationCriteria;
import com.ft.service.MeetingInvitationQueryService;

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

/**
 * Test class for the MeetingInvitationResource REST controller.
 *
 * @see MeetingInvitationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class MeetingInvitationResourceIntTest {

    private static final ZonedDateTime DEFAULT_DECIDED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DECIDED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    @Autowired
    private MeetingInvitationRepository meetingInvitationRepository;

    @Autowired
    private MeetingInvitationService meetingInvitationService;

    @Autowired
    private MeetingInvitationQueryService meetingInvitationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMeetingInvitationMockMvc;

    private MeetingInvitation meetingInvitation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeetingInvitationResource meetingInvitationResource = new MeetingInvitationResource(meetingInvitationService, meetingInvitationQueryService);
        this.restMeetingInvitationMockMvc = MockMvcBuilders.standaloneSetup(meetingInvitationResource)
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
    public static MeetingInvitation createEntity(EntityManager em) {
        MeetingInvitation meetingInvitation = new MeetingInvitation()
            .decidedAt(DEFAULT_DECIDED_AT)
            .accepted(DEFAULT_ACCEPTED);
        return meetingInvitation;
    }

    @Before
    public void initTest() {
        meetingInvitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeetingInvitation() throws Exception {
        int databaseSizeBeforeCreate = meetingInvitationRepository.findAll().size();

        // Create the MeetingInvitation
        restMeetingInvitationMockMvc.perform(post("/api/meeting-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingInvitation)))
            .andExpect(status().isCreated());

        // Validate the MeetingInvitation in the database
        List<MeetingInvitation> meetingInvitationList = meetingInvitationRepository.findAll();
        assertThat(meetingInvitationList).hasSize(databaseSizeBeforeCreate + 1);
        MeetingInvitation testMeetingInvitation = meetingInvitationList.get(meetingInvitationList.size() - 1);
        assertThat(testMeetingInvitation.getDecidedAt()).isEqualTo(DEFAULT_DECIDED_AT);
        assertThat(testMeetingInvitation.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
    }

    @Test
    @Transactional
    public void createMeetingInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = meetingInvitationRepository.findAll().size();

        // Create the MeetingInvitation with an existing ID
        meetingInvitation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingInvitationMockMvc.perform(post("/api/meeting-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingInvitation)))
            .andExpect(status().isBadRequest());

        // Validate the MeetingInvitation in the database
        List<MeetingInvitation> meetingInvitationList = meetingInvitationRepository.findAll();
        assertThat(meetingInvitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeetingInvitations() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList
        restMeetingInvitationMockMvc.perform(get("/api/meeting-invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetingInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].decidedAt").value(hasItem(sameInstant(DEFAULT_DECIDED_AT))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMeetingInvitation() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get the meetingInvitation
        restMeetingInvitationMockMvc.perform(get("/api/meeting-invitations/{id}", meetingInvitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meetingInvitation.getId().intValue()))
            .andExpect(jsonPath("$.decidedAt").value(sameInstant(DEFAULT_DECIDED_AT)))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByDecidedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where decidedAt equals to DEFAULT_DECIDED_AT
        defaultMeetingInvitationShouldBeFound("decidedAt.equals=" + DEFAULT_DECIDED_AT);

        // Get all the meetingInvitationList where decidedAt equals to UPDATED_DECIDED_AT
        defaultMeetingInvitationShouldNotBeFound("decidedAt.equals=" + UPDATED_DECIDED_AT);
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByDecidedAtIsInShouldWork() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where decidedAt in DEFAULT_DECIDED_AT or UPDATED_DECIDED_AT
        defaultMeetingInvitationShouldBeFound("decidedAt.in=" + DEFAULT_DECIDED_AT + "," + UPDATED_DECIDED_AT);

        // Get all the meetingInvitationList where decidedAt equals to UPDATED_DECIDED_AT
        defaultMeetingInvitationShouldNotBeFound("decidedAt.in=" + UPDATED_DECIDED_AT);
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByDecidedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where decidedAt is not null
        defaultMeetingInvitationShouldBeFound("decidedAt.specified=true");

        // Get all the meetingInvitationList where decidedAt is null
        defaultMeetingInvitationShouldNotBeFound("decidedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByDecidedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where decidedAt greater than or equals to DEFAULT_DECIDED_AT
        defaultMeetingInvitationShouldBeFound("decidedAt.greaterOrEqualThan=" + DEFAULT_DECIDED_AT);

        // Get all the meetingInvitationList where decidedAt greater than or equals to UPDATED_DECIDED_AT
        defaultMeetingInvitationShouldNotBeFound("decidedAt.greaterOrEqualThan=" + UPDATED_DECIDED_AT);
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByDecidedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where decidedAt less than or equals to DEFAULT_DECIDED_AT
        defaultMeetingInvitationShouldNotBeFound("decidedAt.lessThan=" + DEFAULT_DECIDED_AT);

        // Get all the meetingInvitationList where decidedAt less than or equals to UPDATED_DECIDED_AT
        defaultMeetingInvitationShouldBeFound("decidedAt.lessThan=" + UPDATED_DECIDED_AT);
    }


    @Test
    @Transactional
    public void getAllMeetingInvitationsByAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where accepted equals to DEFAULT_ACCEPTED
        defaultMeetingInvitationShouldBeFound("accepted.equals=" + DEFAULT_ACCEPTED);

        // Get all the meetingInvitationList where accepted equals to UPDATED_ACCEPTED
        defaultMeetingInvitationShouldNotBeFound("accepted.equals=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where accepted in DEFAULT_ACCEPTED or UPDATED_ACCEPTED
        defaultMeetingInvitationShouldBeFound("accepted.in=" + DEFAULT_ACCEPTED + "," + UPDATED_ACCEPTED);

        // Get all the meetingInvitationList where accepted equals to UPDATED_ACCEPTED
        defaultMeetingInvitationShouldNotBeFound("accepted.in=" + UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        meetingInvitationRepository.saveAndFlush(meetingInvitation);

        // Get all the meetingInvitationList where accepted is not null
        defaultMeetingInvitationShouldBeFound("accepted.specified=true");

        // Get all the meetingInvitationList where accepted is null
        defaultMeetingInvitationShouldNotBeFound("accepted.specified=false");
    }

    @Test
    @Transactional
    public void getAllMeetingInvitationsByMeetingIsEqualToSomething() throws Exception {
        // Initialize the database
        Meeting meeting = MeetingResourceIntTest.createEntity(em);
        em.persist(meeting);
        em.flush();
        meetingInvitation.setMeeting(meeting);
        meetingInvitationRepository.saveAndFlush(meetingInvitation);
        Long meetingId = meeting.getId();

        // Get all the meetingInvitationList where meeting equals to meetingId
        defaultMeetingInvitationShouldBeFound("meetingId.equals=" + meetingId);

        // Get all the meetingInvitationList where meeting equals to meetingId + 1
        defaultMeetingInvitationShouldNotBeFound("meetingId.equals=" + (meetingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMeetingInvitationShouldBeFound(String filter) throws Exception {
        restMeetingInvitationMockMvc.perform(get("/api/meeting-invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetingInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].decidedAt").value(hasItem(sameInstant(DEFAULT_DECIDED_AT))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMeetingInvitationShouldNotBeFound(String filter) throws Exception {
        restMeetingInvitationMockMvc.perform(get("/api/meeting-invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMeetingInvitation() throws Exception {
        // Get the meetingInvitation
        restMeetingInvitationMockMvc.perform(get("/api/meeting-invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeetingInvitation() throws Exception {
        // Initialize the database
        meetingInvitationService.save(meetingInvitation);

        int databaseSizeBeforeUpdate = meetingInvitationRepository.findAll().size();

        // Update the meetingInvitation
        MeetingInvitation updatedMeetingInvitation = meetingInvitationRepository.findOne(meetingInvitation.getId());
        // Disconnect from session so that the updates on updatedMeetingInvitation are not directly saved in db
        em.detach(updatedMeetingInvitation);
        updatedMeetingInvitation
            .decidedAt(UPDATED_DECIDED_AT)
            .accepted(UPDATED_ACCEPTED);

        restMeetingInvitationMockMvc.perform(put("/api/meeting-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeetingInvitation)))
            .andExpect(status().isOk());

        // Validate the MeetingInvitation in the database
        List<MeetingInvitation> meetingInvitationList = meetingInvitationRepository.findAll();
        assertThat(meetingInvitationList).hasSize(databaseSizeBeforeUpdate);
        MeetingInvitation testMeetingInvitation = meetingInvitationList.get(meetingInvitationList.size() - 1);
        assertThat(testMeetingInvitation.getDecidedAt()).isEqualTo(UPDATED_DECIDED_AT);
        assertThat(testMeetingInvitation.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
    }

    @Test
    @Transactional
    public void updateNonExistingMeetingInvitation() throws Exception {
        int databaseSizeBeforeUpdate = meetingInvitationRepository.findAll().size();

        // Create the MeetingInvitation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeetingInvitationMockMvc.perform(put("/api/meeting-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meetingInvitation)))
            .andExpect(status().isCreated());

        // Validate the MeetingInvitation in the database
        List<MeetingInvitation> meetingInvitationList = meetingInvitationRepository.findAll();
        assertThat(meetingInvitationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeetingInvitation() throws Exception {
        // Initialize the database
        meetingInvitationService.save(meetingInvitation);

        int databaseSizeBeforeDelete = meetingInvitationRepository.findAll().size();

        // Get the meetingInvitation
        restMeetingInvitationMockMvc.perform(delete("/api/meeting-invitations/{id}", meetingInvitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MeetingInvitation> meetingInvitationList = meetingInvitationRepository.findAll();
        assertThat(meetingInvitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeetingInvitation.class);
        MeetingInvitation meetingInvitation1 = new MeetingInvitation();
        meetingInvitation1.setId(1L);
        MeetingInvitation meetingInvitation2 = new MeetingInvitation();
        meetingInvitation2.setId(meetingInvitation1.getId());
        assertThat(meetingInvitation1).isEqualTo(meetingInvitation2);
        meetingInvitation2.setId(2L);
        assertThat(meetingInvitation1).isNotEqualTo(meetingInvitation2);
        meetingInvitation1.setId(null);
        assertThat(meetingInvitation1).isNotEqualTo(meetingInvitation2);
    }
}
