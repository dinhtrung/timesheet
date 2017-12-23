package com.ft.repository;

import com.ft.domain.Meeting;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Meeting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, JpaSpecificationExecutor<Meeting> {

    @Query("select meeting from Meeting meeting where meeting.user.login = ?#{principal.username}")
    List<Meeting> findByUserIsCurrentUser();

}
