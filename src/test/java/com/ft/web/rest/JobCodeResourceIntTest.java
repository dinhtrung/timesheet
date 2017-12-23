package com.ft.web.rest;

import com.ft.TimesheetApp;

import com.ft.domain.JobCode;
import com.ft.domain.User;
import com.ft.repository.JobCodeRepository;
import com.ft.service.JobCodeService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.JobCodeCriteria;
import com.ft.service.JobCodeQueryService;

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
import java.util.List;

import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobCodeResource REST controller.
 *
 * @see JobCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TimesheetApp.class)
public class JobCodeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private JobCodeRepository jobCodeRepository;

    @Autowired
    private JobCodeService jobCodeService;

    @Autowired
    private JobCodeQueryService jobCodeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobCodeMockMvc;

    private JobCode jobCode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JobCodeResource jobCodeResource = new JobCodeResource(jobCodeService, jobCodeQueryService);
        this.restJobCodeMockMvc = MockMvcBuilders.standaloneSetup(jobCodeResource)
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
    public static JobCode createEntity(EntityManager em) {
        JobCode jobCode = new JobCode()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return jobCode;
    }

    @Before
    public void initTest() {
        jobCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobCode() throws Exception {
        int databaseSizeBeforeCreate = jobCodeRepository.findAll().size();

        // Create the JobCode
        restJobCodeMockMvc.perform(post("/api/job-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCode)))
            .andExpect(status().isCreated());

        // Validate the JobCode in the database
        List<JobCode> jobCodeList = jobCodeRepository.findAll();
        assertThat(jobCodeList).hasSize(databaseSizeBeforeCreate + 1);
        JobCode testJobCode = jobCodeList.get(jobCodeList.size() - 1);
        assertThat(testJobCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testJobCode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createJobCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobCodeRepository.findAll().size();

        // Create the JobCode with an existing ID
        jobCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobCodeMockMvc.perform(post("/api/job-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCode)))
            .andExpect(status().isBadRequest());

        // Validate the JobCode in the database
        List<JobCode> jobCodeList = jobCodeRepository.findAll();
        assertThat(jobCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJobCodes() throws Exception {
        // Initialize the database
        jobCodeRepository.saveAndFlush(jobCode);

        // Get all the jobCodeList
        restJobCodeMockMvc.perform(get("/api/job-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getJobCode() throws Exception {
        // Initialize the database
        jobCodeRepository.saveAndFlush(jobCode);

        // Get the jobCode
        restJobCodeMockMvc.perform(get("/api/job-codes/{id}", jobCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobCode.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllJobCodesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        jobCodeRepository.saveAndFlush(jobCode);

        // Get all the jobCodeList where name equals to DEFAULT_NAME
        defaultJobCodeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the jobCodeList where name equals to UPDATED_NAME
        defaultJobCodeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJobCodesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        jobCodeRepository.saveAndFlush(jobCode);

        // Get all the jobCodeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultJobCodeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the jobCodeList where name equals to UPDATED_NAME
        defaultJobCodeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllJobCodesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobCodeRepository.saveAndFlush(jobCode);

        // Get all the jobCodeList where name is not null
        defaultJobCodeShouldBeFound("name.specified=true");

        // Get all the jobCodeList where name is null
        defaultJobCodeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllJobCodesByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        User users = UserResourceIntTest.createEntity(em);
        em.persist(users);
        em.flush();
        jobCode.addUsers(users);
        jobCodeRepository.saveAndFlush(jobCode);
        Long usersId = users.getId();

        // Get all the jobCodeList where users equals to usersId
        defaultJobCodeShouldBeFound("usersId.equals=" + usersId);

        // Get all the jobCodeList where users equals to usersId + 1
        defaultJobCodeShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultJobCodeShouldBeFound(String filter) throws Exception {
        restJobCodeMockMvc.perform(get("/api/job-codes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultJobCodeShouldNotBeFound(String filter) throws Exception {
        restJobCodeMockMvc.perform(get("/api/job-codes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingJobCode() throws Exception {
        // Get the jobCode
        restJobCodeMockMvc.perform(get("/api/job-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobCode() throws Exception {
        // Initialize the database
        jobCodeService.save(jobCode);

        int databaseSizeBeforeUpdate = jobCodeRepository.findAll().size();

        // Update the jobCode
        JobCode updatedJobCode = jobCodeRepository.findOne(jobCode.getId());
        // Disconnect from session so that the updates on updatedJobCode are not directly saved in db
        em.detach(updatedJobCode);
        updatedJobCode
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restJobCodeMockMvc.perform(put("/api/job-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobCode)))
            .andExpect(status().isOk());

        // Validate the JobCode in the database
        List<JobCode> jobCodeList = jobCodeRepository.findAll();
        assertThat(jobCodeList).hasSize(databaseSizeBeforeUpdate);
        JobCode testJobCode = jobCodeList.get(jobCodeList.size() - 1);
        assertThat(testJobCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testJobCode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingJobCode() throws Exception {
        int databaseSizeBeforeUpdate = jobCodeRepository.findAll().size();

        // Create the JobCode

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobCodeMockMvc.perform(put("/api/job-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobCode)))
            .andExpect(status().isCreated());

        // Validate the JobCode in the database
        List<JobCode> jobCodeList = jobCodeRepository.findAll();
        assertThat(jobCodeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobCode() throws Exception {
        // Initialize the database
        jobCodeService.save(jobCode);

        int databaseSizeBeforeDelete = jobCodeRepository.findAll().size();

        // Get the jobCode
        restJobCodeMockMvc.perform(delete("/api/job-codes/{id}", jobCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<JobCode> jobCodeList = jobCodeRepository.findAll();
        assertThat(jobCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobCode.class);
        JobCode jobCode1 = new JobCode();
        jobCode1.setId(1L);
        JobCode jobCode2 = new JobCode();
        jobCode2.setId(jobCode1.getId());
        assertThat(jobCode1).isEqualTo(jobCode2);
        jobCode2.setId(2L);
        assertThat(jobCode1).isNotEqualTo(jobCode2);
        jobCode1.setId(null);
        assertThat(jobCode1).isNotEqualTo(jobCode2);
    }
}
