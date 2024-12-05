package com.spring.dlearning.service.admin;

import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.admin.AdminReviewDTO;
import com.spring.dlearning.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final ReviewRepository reviewRepository;

    public PageResponse<AdminReviewDTO> getReviewsByCourseAndRating(int month, int year, int page, int size, boolean ascending) {
        LocalDateTime startDate = LocalDateTime.of(year, Month.of(month), 1, 0, 0, 0, 0);
        LocalDateTime endDate = startDate.withDayOfMonth(startDate.toLocalDate().lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách khóa học và average rating của các Review trong tháng và năm cụ thể
        Page<Object[]> result = reviewRepository.findAverageRatingForCoursesInMonthYear(startDate, endDate, pageable);

        // Map kết quả sang AdminReviewDTO và sắp xếp theo rating
        List<AdminReviewDTO> reviews = result.stream()
                .map(row -> {
                    Long courseId = (Long) row[0];
                    Double averageRating = (Double) row[1];
                    Long userId = (Long) row[2];
                    String userName = (String) row[3];
                    String userAvatar = (String) row[4];
                    LocalDateTime createdAt = (LocalDateTime) row[5];
                    LocalDateTime updatedAt = (LocalDateTime) row[6];
                    String title = (String) row[7];  // thumbnail
                    String thumbnail = (String) row[8];  // title
                    return new AdminReviewDTO(courseId, averageRating, createdAt, updatedAt, userId, userName, userAvatar, title, thumbnail);
                })
                .collect(Collectors.toList());


        // Sắp xếp theo average rating
        if (ascending) {
            reviews.sort(Comparator.comparingDouble(AdminReviewDTO::getAverageRating));
        } else {
            reviews.sort((r1, r2) -> Double.compare(r2.getAverageRating(), r1.getAverageRating()));
        }

        return PageResponse.<AdminReviewDTO>builder()
                .currentPage(page)
                .totalPages(result.getTotalPages())
                .pageSize(size)
                .totalElements(result.getTotalElements())
                .data(reviews)
                .build();
    }
}
