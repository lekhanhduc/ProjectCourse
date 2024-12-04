package com.spring.dlearning.dto.response.admin;

import java.math.BigDecimal;

public class AdminTeacherRevenueDTO {

    private Long teacherId;
    private String teacherName;
    private BigDecimal totalRevenue;
    private int totalCoursesSold;
    private String teacherAvatar;

    // Constructor, getters, setters
    public AdminTeacherRevenueDTO(Long teacherId, String teacherName, BigDecimal totalRevenue, int totalCoursesSold, String teacherAvatar) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.totalRevenue = totalRevenue;
        this.totalCoursesSold = totalCoursesSold;
        this.teacherAvatar = teacherAvatar;
    }

    // Getters and Setters
    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalCoursesSold() {
        return totalCoursesSold;
    }

    public void setTotalCoursesSold(int totalCoursesSold) {
        this.totalCoursesSold = totalCoursesSold;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }
}


