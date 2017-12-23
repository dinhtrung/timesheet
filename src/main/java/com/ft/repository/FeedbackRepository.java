package com.ft.repository;

import com.ft.domain.Feedback;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Feedback entity.
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {

    @Query("select feedback from Feedback feedback where feedback.createdBy.login = ?#{principal.username}")
    List<Feedback> findByCreatedByIsCurrentUser();

    @Modifying
	void deleteAllByRepliedToId(Long id);

}
