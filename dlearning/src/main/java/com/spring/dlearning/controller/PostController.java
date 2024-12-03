package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.PostCreationRequest;
import com.spring.dlearning.dto.request.UpdateLikeCountRequest;
import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.PostCreationResponse;
import com.spring.dlearning.dto.response.PostResponse;
import com.spring.dlearning.entity.Post;
import com.spring.dlearning.service.PostService;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class PostController {

    PostService postService;

    @Operation(summary = "Create a new post", description = "This API allows the creation of a new post with optional image upload.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data or missing required fields"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error while creating post")
    })
    @PostMapping("/create-post")
    ApiResponse<PostCreationResponse> createPost (@RequestPart("request") @Valid PostCreationRequest request,
                                                  @RequestPart(value = "file", required = false) MultipartFile file) {
        return ApiResponse.<PostCreationResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(postService.createPost(request, file))
                .build();
    }

    @Operation(summary = "Delete a post", description = "This API deletes a post based on the given post ID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid post ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete-post/{postId}")
    ApiResponse<Void> deletePost (@PathVariable Long postId) {
        postService.deletePost(postId);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete post successfully")
                .build();
    }

    @Operation(
            summary = "Get all posts",
            description = "Retrieve a paginated list of all posts, optionally filtered by specifications."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved all posts"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/get-all-post")
    ApiResponse<PageResponse<PostResponse>> getAllPost (
                                      @Filter Specification<Post> spec,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(value = "size", required = false, defaultValue = "3") int size){

        return ApiResponse.<PageResponse<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(postService.getAllPost(spec, page, size))
                .build();
    }

    @Operation(
            summary = "Get posts by current logged-in user",
            description = "Retrieve a paginated list of posts created by the currently logged-in user, optionally filtered by specifications."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved posts for current login"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/get-post-current-login")
    ApiResponse<PageResponse<PostResponse>> getPostByCurrentLogin(
            @Filter Specification<Post> spec,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(postService.getPostByCurrentLogin(spec, page, size))
                .build();
    }

    @Operation(
            summary = "Update the like count of a post",
            description = "This endpoint updates the like count for a specific post based on the provided request."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Like count updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update-like-count")
    ApiResponse<Void> updateLikeCount(@RequestBody UpdateLikeCountRequest request) {
        postService.updateLikeCount(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Update like successfully")
                .build();
    }

}
