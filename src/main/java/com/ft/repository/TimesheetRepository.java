package com.ft.repository;

import com.ft.domain.Timesheet;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Timesheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long>, JpaSpecificationExecutor<Timesheet> {

    @Query("select timesheet from Timesheet timesheet where timesheet.owner.login = ?#{principal.username}")
    List<Timesheet> findByOwnerIsCurrentUser();

    @Query("select timesheet from Timesheet timesheet where timesheet.approvedBy.login = ?#{principal.username}")
    List<Timesheet> findByApprovedByIsCurrentUser();

}
