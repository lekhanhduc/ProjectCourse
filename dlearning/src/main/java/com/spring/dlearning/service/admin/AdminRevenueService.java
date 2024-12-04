package com.spring.dlearning.service.admin;

import com.spring.dlearning.dto.response.admin.AdminMonthlyRevenueDTO;
import com.spring.dlearning.dto.response.admin.AdminTeacherRevenueDTO;
import com.spring.dlearning.dto.response.admin.AdminUserBuyDTO;
import com.spring.dlearning.entity.Course;
import com.spring.dlearning.entity.Payment;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.mapper.admin.AdminRevenueMapper;
import com.spring.dlearning.repository.EnrollmentRepository;
import com.spring.dlearning.repository.PaymentRepository;
import com.spring.dlearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminRevenueService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AdminMonthlyRevenueDTO> getMonthlyRevenue(int year) {
        List<Payment> payments = paymentRepository.findAll();
        return AdminRevenueMapper.mapToMonthlyRevenue(payments, year); // Gọi phương thức trong Mapper
    }

    public List<AdminTeacherRevenueDTO> getTeacherRevenueByYear(int year, boolean ascending) {
        // Lấy danh sách tất cả các giao dịch thanh toán trong năm
        List<Payment> payments = paymentRepository.findPaymentsByYear(year);

        // Khởi tạo map lưu doanh thu của từng giáo viên
        Map<Long, AdminTeacherRevenueDTO> teacherRevenueMap = new HashMap<>();

        // Lặp qua tất cả giao dịch thanh toán
        for (Payment payment : payments) {
            Course course = payment.getCourse();
            if (course != null) {
                User teacher = course.getAuthor();
                if (teacher != null && teacher.getRole().getId() == 3) {
                    AdminTeacherRevenueDTO revenueDTO = teacherRevenueMap.getOrDefault(teacher.getId(),
                            new AdminTeacherRevenueDTO(
                                    teacher.getId(),
                                    teacher.getFullName(),
                                    BigDecimal.ZERO,
                                    0,
                                    teacher.getAvatar()
                            )
                    );

                    // Cập nhật doanh thu và số lượng khóa học
                    revenueDTO.setTotalRevenue(revenueDTO.getTotalRevenue().add(payment.getPrice()));
                    revenueDTO.setTotalCoursesSold(revenueDTO.getTotalCoursesSold() + 1);

                    // Cập nhật avatar giáo viên
                    revenueDTO.setTeacherAvatar(teacher.getAvatar());

                    teacherRevenueMap.put(teacher.getId(), revenueDTO);
                }
            }
        }

        // Chuyển đổi map thành danh sách
        List<AdminTeacherRevenueDTO> teacherRevenueList = new ArrayList<>(teacherRevenueMap.values());

        // Sắp xếp theo doanh thu tăng dần hoặc giảm dần
        teacherRevenueList.sort((r1, r2) -> {
            int revenueComparison = r1.getTotalRevenue().compareTo(r2.getTotalRevenue()); // Sắp xếp theo doanh thu
            if (revenueComparison == 0) {
                return Integer.compare(r1.getTotalCoursesSold(), r2.getTotalCoursesSold()); // Sắp xếp theo số lượng khóa học
            }
            return revenueComparison;
        });

        // Nếu `ascending` là true thì giữ nguyên, ngược lại thì đảo ngược danh sách
        if (!ascending) {
            Collections.reverse(teacherRevenueList);
        }

        return teacherRevenueList;
    }

    public List<AdminTeacherRevenueDTO> getTeacherRevenueByMonth(int month, int year, boolean ascending) {
        LocalDateTime startDate = LocalDateTime.of(year, Month.of(month), 1, 0, 0, 0, 0);
        LocalDateTime endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Lấy danh sách tất cả các giao dịch thanh toán trong tháng
        List<Payment> payments = paymentRepository.findPaymentsByDateRange(startDate, endDate);

        // Khởi tạo map lưu doanh thu của từng giáo viên
        Map<Long, AdminTeacherRevenueDTO> teacherRevenueMap = new HashMap<>();

        // Lặp qua tất cả giao dịch thanh toán
        for (Payment payment : payments) {
            Course course = payment.getCourse();
            if (course != null) {
                User teacher = course.getAuthor();
                if (teacher != null && teacher.getRole().getId() == 3) {
                    AdminTeacherRevenueDTO revenueDTO = teacherRevenueMap.getOrDefault(teacher.getId(),
                            new AdminTeacherRevenueDTO(
                                    teacher.getId(),
                                    teacher.getFullName(),
                                    BigDecimal.ZERO,
                                    0,
                                    teacher.getAvatar()
                            )
                    );

                    // Cập nhật doanh thu và số lượng khóa học
                    revenueDTO.setTotalRevenue(revenueDTO.getTotalRevenue().add(payment.getPrice()));
                    revenueDTO.setTotalCoursesSold(revenueDTO.getTotalCoursesSold() + 1);

                    // Cập nhật avatar giáo viên
                    revenueDTO.setTeacherAvatar(teacher.getAvatar());

                    teacherRevenueMap.put(teacher.getId(), revenueDTO);
                }
            }
        }

        // Chuyển đổi map thành danh sách
        List<AdminTeacherRevenueDTO> teacherRevenueList = new ArrayList<>(teacherRevenueMap.values());

        // Sắp xếp theo doanh thu tăng dần hoặc giảm dần
        teacherRevenueList.sort((r1, r2) -> {
            int revenueComparison = r1.getTotalRevenue().compareTo(r2.getTotalRevenue()); // Sắp xếp theo doanh thu
            if (revenueComparison == 0) {
                return Integer.compare(r1.getTotalCoursesSold(), r2.getTotalCoursesSold()); // Sắp xếp theo số lượng khóa học
            }
            return revenueComparison;
        });

        // Nếu `ascending` là true thì giữ nguyên, ngược lại thì đảo ngược danh sách
        if (!ascending) {
            Collections.reverse(teacherRevenueList);
        }

        return teacherRevenueList;
    }


    public List<AdminUserBuyDTO> getUserBuyStatsByMonth(int month, int year, boolean ascending) {
        List<Object[]> stats = enrollmentRepository.countEnrollmentsByMonthAndYear(month, year);

        List<AdminUserBuyDTO> result = stats.stream()
                .map(obj -> new AdminUserBuyDTO(
                        ((User) obj[0]).getId(),
                        ((User) obj[0]).getFullName(),
                        ((Long) obj[1]).intValue()))  // Chuyển Long sang int
                .collect(Collectors.toList());

        result.sort((user1, user2) -> Integer.compare(user1.getTotalCoursesBought(), user2.getTotalCoursesBought()));

        if (!ascending) {
            Collections.reverse(result);  // Đảo ngược nếu không phải sắp xếp tăng dần
        }

        return result;
    }

    public List<AdminUserBuyDTO> getUserBuyStatsByYear(int year, boolean ascending) {
        List<Object[]> stats = enrollmentRepository.countEnrollmentsByYear(year);

        List<AdminUserBuyDTO> result = stats.stream()
                .map(obj -> new AdminUserBuyDTO(
                        ((User) obj[0]).getId(),
                        ((User) obj[0]).getFullName(),
                        ((Long) obj[1]).intValue()))  // Chuyển Long sang int
                .collect(Collectors.toList());

        result.sort((user1, user2) -> Integer.compare(user1.getTotalCoursesBought(), user2.getTotalCoursesBought()));

        if (!ascending) {
            Collections.reverse(result);  // Đảo ngược nếu không phải sắp xếp tăng dần
        }

        return result;
    }



}
