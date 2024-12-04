package com.spring.dlearning.controller.admin;

import com.spring.dlearning.dto.response.admin.AdminMonthlyRevenueDTO;
import com.spring.dlearning.dto.response.admin.AdminTeacherRevenueDTO;
import com.spring.dlearning.dto.response.admin.AdminUserBuyDTO;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.service.admin.AdminRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/revenue")
public class AdminRevenueController {

    @Autowired
    private AdminRevenueService revenueService;

    // API lấy doanh thu hàng tháng của năm
    @GetMapping("/monthly")
    public List<AdminMonthlyRevenueDTO> getMonthlyRevenue(@RequestParam int year) {
        return revenueService.getMonthlyRevenue(year);
    }

    @GetMapping("/teacher/yearly")
    public List<AdminTeacherRevenueDTO> getTeacherRevenueByYear(@RequestParam int year,
                                                                @RequestParam(defaultValue = "true") boolean ascending) {
        return revenueService.getTeacherRevenueByYear(year, ascending);
    }

    // API lấy doanh thu của từng giáo viên trong tháng
    @GetMapping("/teacher/monthly")
    public List<AdminTeacherRevenueDTO> getTeacherRevenueByMonth(@RequestParam int month, @RequestParam int year,
                                                                 @RequestParam(defaultValue = "true") boolean ascending) {
        return revenueService.getTeacherRevenueByMonth(month, year, ascending);
    }

    // API lấy người dùng mua nhiều khóa học nhất trong tháng
    @GetMapping("/user/monthly")
    public List<AdminUserBuyDTO> getUserBuyStatsByMonth(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "true") boolean ascending) {
        return revenueService.getUserBuyStatsByMonth(month, year, ascending);
    }

    // API lấy người dùng mua nhiều khóa học nhất trong năm
    @GetMapping("/user/yearly")
    public List<AdminUserBuyDTO> getUserBuyStatsByYear(
            @RequestParam int year,
            @RequestParam(defaultValue = "true") boolean ascending) {
        return revenueService.getUserBuyStatsByYear(year, ascending);
    }

}
