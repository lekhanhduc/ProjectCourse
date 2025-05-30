package com.spring.dlearning.repository;

import com.spring.dlearning.entity.Course;
import com.spring.dlearning.entity.Payment;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.common.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    @Query("SELECT p FROM Payment p WHERE p.user = :user AND p.createdAt BETWEEN :start AND :end ORDER BY p.createdAt DESC")
    List<Payment> findByUserAndDateRange(@Param("user") User user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p FROM Payment p WHERE p.course.author = :author AND p.paymentStatus = :status AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsByAuthorStatusAndDateRange(@Param("author") User author,
                                                         @Param("status") PaymentStatus status,
                                                         @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.user.id=:id")
    Page<Payment> findPaymentByUserLogin(Long id, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.course.author.id=:id")
    List<Payment> getCoursesSoldByTeacher(Long id);

    @Query("SELECT p FROM Payment p where p.course=:course")
    List<Payment> findByCourse(Course course);
    // Lọc Payments theo năm
    @Query("SELECT p FROM Payment p WHERE EXTRACT(YEAR FROM p.createdAt) = :year")
    List<Payment> findPaymentsByYear(@Param("year") int year);

    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Optional<Payment> findByOrderCode(Long orderCode);

}
