package com.spring.dlearning.dto.response.admin;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMonthlyRevenueDTO {

    private int month;
    private int year;
    private BigDecimal totalRevenue;
    private Long totalCoursesSold;

}
