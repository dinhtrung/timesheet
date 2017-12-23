package com.ft.repository;

import com.ft.domain.MeetingInvitation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MeetingInvitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingInvitationRepository extends JpaRepository<MeetingInvitation, Long>, JpaSpecificationExecutor<MeetingInvitation> {

}
