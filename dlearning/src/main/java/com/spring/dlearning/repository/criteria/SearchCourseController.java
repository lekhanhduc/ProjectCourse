package com.spring.dlearning.repository.criteria;

import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchCourseController {
    private final SearchCourseCriteria searchCourseCriteria;

    @GetMapping("/search-course-by-criteria")
    ApiResponse<PageResponse> searchCriteria (
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "3", required = false) int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "") String ...search
    ) {
        return ApiResponse.<PageResponse>builder()
                .code(HttpStatus.OK.value())
                .result(searchCourseCriteria.advanceSearchCourse(page, size, sortBy, search))
                .build();
    }
}
