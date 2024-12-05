//package com.spring.dlearning.controller.admin;
//
//import com.spring.dlearning.dto.response.admin.AdminReviewDTO;
//import com.spring.dlearning.service.admin.AdminReviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/admin/reviews")
//@RequiredArgsConstructor
//public class AdminReviewController {
//
//    private final AdminReviewService reviewService;
//
//    @GetMapping("/courses/rating")
//    public ResponseEntity<List<AdminReviewDTO>> getReviewsByCourseAndRating(
//            @RequestParam int month,
//            @RequestParam int year,
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "false") boolean ascending) {
//        List<AdminReviewDTO> reviews = reviewService.getReviewsByCourseAndRating(month, year, page, size, ascending);
//        return ResponseEntity.ok(reviews);
//    }
//}
package com.spring.dlearning.controller.admin;

import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.admin.AdminReviewDTO;
import com.spring.dlearning.service.admin.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService reviewService;

    @GetMapping("/courses/rating")
    public ApiResponse<PageResponse<AdminReviewDTO>> getReviewsByCourseAndRating(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size,
            @RequestParam(defaultValue = "false") boolean ascending) {
        var result = reviewService.getReviewsByCourseAndRating(month, year, page, size, ascending);
        return ApiResponse.<PageResponse<AdminReviewDTO>>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }
}