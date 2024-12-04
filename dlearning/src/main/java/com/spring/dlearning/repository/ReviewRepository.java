package com.spring.dlearning.repository;

import com.spring.dlearning.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.course.id = :courseId AND r.chapter IS NULL AND r.lesson IS NULL")
    List<Review> findByCourseIdAndChapterIsNullAndLessonIsNull(Long courseId);

    List<Review> findByCourseIdAndChapterIdAndLessonId(Long courseId, Long chapterId, Long lessonId);

    @Query("SELECT r FROM Review r WHERE r.lesson.id = :lessonId AND r.parentReview IS NULL")
    List<Review> findByLessonId(Long lessonId);

    @Query("SELECT c.id AS courseId, AVG(r.rating) AS averageRating, c.author.id AS userId, " +
            "c.author.name AS userName, c.author.avatar AS userAvatar, " +
            "MAX(r.createdAt) AS createdAt, MAX(r.updatedAt) AS updatedAt, " +
            "c.title AS title, c.thumbnail AS thumbnail " +
            "FROM Review r " +
            "JOIN r.course c " +
            "WHERE r.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY c.id, c.author.id")
    List<Object[]> findAverageRatingForCoursesInMonthYear(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);
}
