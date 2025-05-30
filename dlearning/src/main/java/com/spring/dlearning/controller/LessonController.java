package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.CommentLessonRequest;
import com.spring.dlearning.dto.request.LessonCreationRequest;
import com.spring.dlearning.dto.request.UpdateLessonRequest;
import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.CommentLessonResponse;
import com.spring.dlearning.dto.response.LessonCreationResponse;
import com.spring.dlearning.dto.response.UpdateLessonResponse;
import com.spring.dlearning.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class LessonController {

    LessonService lessonService;

    @Operation(
            summary = "Create a new lesson",
            description = "This endpoint allows the creation of a new lesson, including uploading the lesson's video."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Lesson created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or file"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create-lesson")
    ApiResponse<LessonCreationResponse> createLesson(@RequestPart("request") LessonCreationRequest request,
                                                     @RequestPart("video") MultipartFile file)
            throws IOException {
        var result = lessonService.createLesson(request, file);

        return ApiResponse.<LessonCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(result)
                .build();
    }

    @Operation(summary = "Update an existing lesson", description = "Update lesson details including name, description, and video.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lesson updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update-lesson")
    ApiResponse<UpdateLessonResponse> updateLesson(
            @RequestPart("request") UpdateLessonRequest request,
            @RequestPart(value = "video", required = false) MultipartFile video)
            throws IOException {

        var result = lessonService.updateLesson(request, video);
        return ApiResponse.<UpdateLessonResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @DeleteMapping("/delete-lesson/{lessonId}")
    ApiResponse<Void> deleteLesson (@PathVariable @Min(1) Long lessonId ) {
        lessonService.deleteLesson(lessonId);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Lesson deleted successfully")
                .build();
    }

    @GetMapping("/get-comment-lesson/{lessonId}")
    ApiResponse<List<CommentLessonResponse>> getCommentLesson (@PathVariable @Min(1) Long lessonId) {

        var result = lessonService.getCommentByLesson(lessonId);
        return ApiResponse.<List<CommentLessonResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/add-comment-lesson")
    ApiResponse<CommentLessonResponse> addCommentLesson (@RequestBody @Valid CommentLessonRequest request) {

        var result = lessonService.addCommentLesson(request);
        return ApiResponse.<CommentLessonResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @DeleteMapping("/delete-comment-lesson/{reviewId}")
    ApiResponse<CommentLessonResponse> deleteCommentLesson (@PathVariable Long reviewId) {

        lessonService.deleteCommentLesson(reviewId);
        return ApiResponse.<CommentLessonResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Comment Lesson Successfully")
                .build();
    }
}
