package com.spring.dlearning.repository;

import com.spring.dlearning.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.course.id = :courseId AND r.chapter IS NULL AND r.lesson IS NULL")
    List<Review> findByCourseIdAndChapterIsNullAndLessonIsNull(Long courseId);

    List<Review> findByCourseIdAndChapterIdAndLessonId(Long courseId, Long chapterId, Long lessonId);

    @Query("SELECT r FROM Review r WHERE r.lesson.id = :lessonId AND r.parentReview IS NULL")
    List<Review> findByLessonId(Long lessonId);

}
