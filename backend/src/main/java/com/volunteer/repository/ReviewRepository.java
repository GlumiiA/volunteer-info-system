package com.volunteer.repository;

import com.volunteer.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByReviewedId(Integer reviewedId);
    List<Review> findByEventId(Integer eventId);
    List<Review> findByReviewerIdAndReviewedIdAndEventId(Integer reviewerId, Integer reviewedId, Integer eventId);
}
