package com.ft.repository;

import com.ft.domain.LeaveRequest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the LeaveRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>, JpaSpecificationExecutor<LeaveRequest> {

    @Query("select leave_request from LeaveRequest leave_request where leave_request.owner.login = ?#{principal.username}")
    List<LeaveRequest> findByOwnerIsCurrentUser();

    @Query("select leave_request from LeaveRequest leave_request where leave_request.approvedBy.login = ?#{principal.username}")
    List<LeaveRequest> findByApprovedByIsCurrentUser();

}
