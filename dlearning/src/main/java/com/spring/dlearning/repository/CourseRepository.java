package com.spring.dlearning.repository;

import com.spring.dlearning.entity.Course;
import com.spring.dlearning.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course>, CustomCourseRepository {

    @Query("SELECT c FROM Course c JOIN c.author u WHERE u.id = :userId")
    List<Course> findByAuthorId(@Param("userId") Long userId);

    @Query("SELECT count(*) FROM Review r WHERE r.user.id=:userId")
    int totalReview (Long userId);

    @Query(value = "SELECT * FROM courses " +
            "WHERE MATCH(title, description) AGAINST (:keywords IN NATURAL LANGUAGE MODE) " +
            "AND id != :currentCourseId " +
            "ORDER BY MATCH(title, description) AGAINST (:keywords IN NATURAL LANGUAGE MODE) DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<Course> findRelatedCourses(@Param("keywords") String keywords, @Param("currentCourseId") Long currentCourseId);

    List<Course> findByAuthor(User user);

    @Query("SELECT c.title FROM Course c WHERE c.title LIKE %:query%")
    List<String> findTitleSuggestions(String query);

    Page<Course> findByEnabled(boolean enabled, Pageable pageable); // Lọc khóa học theo trạng thái enabled

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.author WHERE c.id = :id")
    Optional<Course> findCourseDetailsById(@Param("id") Long id);
}
