package com.spring.dlearning.mapper.admin;

import com.spring.dlearning.dto.response.admin.AdminMonthlyRevenueDTO;
import com.spring.dlearning.entity.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminRevenueMapper {

    // Phương thức chuyển đổi từ tháng, tổng doanh thu và tổng số khóa học đã bán sang MonthlyRevenueDTO
    public static AdminMonthlyRevenueDTO toMonthlyRevenueDTO(int month, int year, BigDecimal totalRevenue, Long totalCoursesSold) {
        return new AdminMonthlyRevenueDTO(month, year, totalRevenue, totalCoursesSold);
    }

    // Phương thức để nhóm dữ liệu Payment theo tháng và tính tổng doanh thu và số lượng khóa học đã bán
    public static List<AdminMonthlyRevenueDTO> mapToMonthlyRevenue(List<Payment> payments, int year) {
        // Nhóm dữ liệu theo tháng và tính tổng doanh thu và tổng số khóa học đã bán
        Map<Integer, AdminMonthlyRevenueDTO> revenueMap = payments.stream()
                .filter(payment -> payment.getCreatedAt().getYear() == year) // Lọc theo năm
                .collect(Collectors.groupingBy(payment -> payment.getCreatedAt().getMonthValue(),
                        Collectors.collectingAndThen(Collectors.toList(), paymentList -> {
                            // Tính tổng doanh thu cho tháng
                            BigDecimal totalRevenue = paymentList.stream()
                                    .map(Payment::getPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            // Tính tổng số khóa học đã bán cho tháng
                            Long totalCoursesSold = (long) paymentList.size();

                            // Tạo MonthlyRevenueDTO cho tháng đó
                            return new AdminMonthlyRevenueDTO(paymentList.get(0).getCreatedAt().getMonthValue(),
                                    year, totalRevenue, totalCoursesSold);
                        })));

        // Chuyển đổi từ Map vào List
        return revenueMap.values().stream().collect(Collectors.toList());
    }



}
