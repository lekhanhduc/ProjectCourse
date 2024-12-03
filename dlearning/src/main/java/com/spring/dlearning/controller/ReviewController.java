package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.ReviewRequest;
import com.spring.dlearning.dto.request.UpdateReviewRequest;
import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.ReviewResponse;
import com.spring.dlearning.dto.response.DeleteCommentResponse;
import com.spring.dlearning.dto.response.UpdateReviewResponse;
import com.spring.dlearning.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class ReviewController {

    ReviewService reviewService;

    @Operation(summary = "Get reviews for a specific course", description = "This API retrieves all reviews for the specified course by course ID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved course reviews",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid course ID supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/courses-review/{courseId}")
    ApiResponse<List<ReviewResponse>> getReviewByCourseId(@PathVariable @Min(1) Long courseId){
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.getReviewByCourse(courseId))
                .build();
    }

    @Operation(summary = "Add a new review for a course", description = "This API allows a user to add a review for a specific course by providing review details.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Review successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data or missing required fields"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Course not found for the provided ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while adding the review")
    })
    @PostMapping("/add-review")
    ApiResponse<ReviewResponse> addReview(@RequestBody @Valid ReviewRequest reviewRequest, @RequestParam Long id){
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(reviewService.addReview(reviewRequest, id))
                .build();
    }

    @Operation(summary = "Delete a review by ID", description = "This API allows deleting a specific review by providing its ID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review successfully deleted",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteCommentResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid ID or other input error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found for the provided ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while deleting the review")
    })
    @DeleteMapping("/delete-review/{id}")
    ApiResponse<DeleteCommentResponse> deleteReview(@PathVariable @Min(1) Long id) {
        return ApiResponse.<DeleteCommentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.deleteReviewById(id))
                .build();
    }

    @PutMapping("/update-review/{id}")
    @Operation(summary = "Update a review by ID", description = "This API allows updating an existing review by providing its ID and the updated information.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateReviewResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid ID or input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found for the provided ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while updating the review")
    })
    ApiResponse<UpdateReviewResponse> updateReview(@PathVariable @Min(1) Long id, @RequestBody UpdateReviewRequest request) {
        return ApiResponse.<UpdateReviewResponse>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.updateReview(id, request))
                .build();
    }

}
