package com.ft.repository;

import com.ft.domain.JobCode;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the JobCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobCodeRepository extends JpaRepository<JobCode, Long>, JpaSpecificationExecutor<JobCode> {
    @Query("select distinct job_code from JobCode job_code left join fetch job_code.users")
    List<JobCode> findAllWithEagerRelationships();

    @Query("select job_code from JobCode job_code left join fetch job_code.users where job_code.id =:id")
    JobCode findOneWithEagerRelationships(@Param("id") Long id);

}
