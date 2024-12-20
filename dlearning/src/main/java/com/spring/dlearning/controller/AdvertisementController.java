package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.AdsApproveRequest;
import com.spring.dlearning.dto.request.AdsCreationRequest;
import com.spring.dlearning.dto.response.*;
import com.spring.dlearning.service.AdvertisementService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
public class AdvertisementController {

    AdvertisementService advertisementService;

    @GetMapping("/fetch-ads")
    ApiResponse<PageResponse<AdsCreationResponse>> getAllAds(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "7", required = false) int size
    ) {
        var result  = advertisementService.getAllAds(page, size);
        return ApiResponse.<PageResponse<AdsCreationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PostMapping("/register-ads")
    ApiResponse<AdsCreationResponse> userCreateAds(@RequestPart("request") @Valid AdsCreationRequest request,
                                                   @RequestPart("file") MultipartFile file) {
        var result  = advertisementService.userCreateAds(request, file);
        return ApiResponse.<AdsCreationResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @PutMapping("/approve-ads")
    ApiResponse<AdsApproveResponse> approveAds(@RequestBody AdsApproveRequest request) {
        return ApiResponse.<AdsApproveResponse>builder()
                .code(HttpStatus.OK.value())
                .result(advertisementService.approveAds(request))
                .build();
    }

    @PutMapping("/reject-ads")
    ApiResponse<AdsApproveResponse> rejectAds(@RequestBody AdsApproveRequest request)
            throws MessagingException, UnsupportedEncodingException {
        return ApiResponse.<AdsApproveResponse>builder()
                .code(HttpStatus.OK.value())
                .result(advertisementService.rejectAds(request))
                .build();
    }

    @DeleteMapping("/delete-ads/{id}")
    ApiResponse<Void> deleteAds(@PathVariable Long id){
        advertisementService.deleteAds(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Delete Advertisement Successfully!")
                .build();
    }

    @GetMapping("/get-ads-current")
    ApiResponse<PageResponse<AdsCreationResponse>> getAllByCurrentLogin(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    {
        return ApiResponse.<PageResponse<AdsCreationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(advertisementService.getAdsByCurrentLogin(page, size))
                .build();
    }

    @GetMapping("/get-ads-active")
    ApiResponse<List<AdsActiveResponse>> getAdsActive () {
        return ApiResponse.<List<AdsActiveResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(advertisementService.getAdsWithActive())
                .build();
    }

}
