package com.spring.dlearning.dto.response.admin;

public class AdminUserBuyDTO {
    private Long userId;
    private String fullName;
    private int totalCoursesBought;

    public AdminUserBuyDTO(Long userId, String fullName, int totalCoursesBought) {
        this.userId = userId;
        this.fullName = fullName;
        this.totalCoursesBought = totalCoursesBought;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTotalCoursesBought() {
        return totalCoursesBought;
    }

    public void setTotalCoursesBought(int totalCoursesBought) {
        this.totalCoursesBought = totalCoursesBought;
    }
}

