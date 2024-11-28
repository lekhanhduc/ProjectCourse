package com.spring.dlearning.service;

import com.spring.dlearning.constant.PredefinedRole;
import com.spring.dlearning.dto.request.BuyCourseRequest;
import com.spring.dlearning.dto.request.CourseCreationRequest;
import com.spring.dlearning.dto.request.RelatedCourseRequest;
import com.spring.dlearning.dto.request.UploadCourseRequest;
import com.spring.dlearning.dto.response.*;
import com.spring.dlearning.elasticsearch.CourseDocument;
import com.spring.dlearning.elasticsearch.DocumentCourseRepository;
import com.spring.dlearning.entity.*;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.mapper.CourseChapterAndLessonMapper;
import com.spring.dlearning.mapper.CourseMapper;
import com.spring.dlearning.mapper.EnrollmentMapper;
import com.spring.dlearning.repository.*;
import com.spring.dlearning.common.PaymentMethodName;
import com.spring.dlearning.common.PaymentStatus;
import com.spring.dlearning.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseService {

    UserRepository userRepository;
    CourseRepository courseRepository;
    CloudinaryService cloudinaryService;
    CourseMapper courseMapper;
    CourseChapterAndLessonMapper courseChapterAndLessonMapper;
    CertificateRepository certificateRepository;
    EnrollmentRepository enrollmentRepository;
    EnrollmentMapper enrollmentMapper;
    PaymentRepository paymentRepository;
    PaymentMethodRepository paymentMethodRepository;
    DocumentCourseRepository documentCourseRepository;

    public PageResponse<CourseResponse> getAllCourses(Specification<Course> spec, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Course> pageData = courseRepository.findAll(spec, pageable);

        List<CourseResponse> courseResponses  = pageData.getContent()
                .stream().map(course -> {

            List<Review> filteredComments = course.getComments().stream()
                    .filter(r -> r.getRating() > 0 )
                    .toList();

            long totalRating = filteredComments.stream()
                            .mapToLong(Review::getRating)
                            .sum();

            int numberOfValidReviews = filteredComments.size();
            double averageRating = numberOfValidReviews > 0 ? BigDecimal.valueOf((double) totalRating / numberOfValidReviews)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue() : 0.0 ;

            CourseResponse courseResponse = courseMapper.toCourseResponse(course);
            courseResponse.setAverageRating(averageRating);
            return courseResponse;
        }).sorted(Comparator.comparing(CourseResponse::getAverageRating)
                        .reversed())
                .toList();

        return PageResponse.<CourseResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(courseResponses)
                .build();
    }

    public CourseResponse getCourseById(Long id){

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        var filteredComments = course.getComments().stream()
                .filter(r -> r.getRating() > 0 )
                .toList();

        long totalRating = filteredComments.stream()
                .mapToLong(Review::getRating)
                .sum();

        int numberOfValidReviews = filteredComments.size();
        double averageRating = numberOfValidReviews > 0 ? BigDecimal.valueOf((double) totalRating / numberOfValidReviews)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue() : 0.0 ;

        return CourseResponse.builder()
                .id(course.getId())
                .author(course.getAuthor().getName())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .language(course.getLanguage())
                .courseLevel(course.getCourseLevel())
                .thumbnail(course.getThumbnail())
                .videoUrl(course.getVideoUrl())
                .points(course.getPoints())
                .averageRating(averageRating)
                .build();
    }

    @Transactional
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('TEACHER', 'ADMIN')")
    public CourseCreationResponse createCourse(CourseCreationRequest request,
                                               MultipartFile thumbnail,
                                               MultipartFile video)
            throws IOException {
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if(! Objects.equals(user.getRole().getName(), PredefinedRole.TEACHER_ROLE)){
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        Course course = courseMapper.toCourse(request);
        String urlThumbnail = cloudinaryService.uploadImage(thumbnail);
        if(video != null) {
            String videoUrl = cloudinaryService.uploadVideoChunked(video, "courses").get("url").toString();
            course.setVideoUrl(videoUrl);
        }
        course.setThumbnail(urlThumbnail);
        course.setAuthor(user);
        courseRepository.save(course);
        saveCourseDocument(course);

        return courseMapper.toCourseCreationResponse(course);
    }

    public void saveCourseDocument(Course course) {

        CourseDocument courseDocument = CourseDocument.builder()
                .id(String.valueOf(course.getId()))
                .author(course.getAuthor().getName())
                .title(course.getTitle())
                .description(course.getDescription())
                .thumbnail(course.getThumbnail())
                .points(course.getPoints())
                .build();

        documentCourseRepository.save(courseDocument);
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('USER', 'TEACHER', 'ADMIN')")
    public List<CourseResponse> managerCourses(){
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Course> myCourse = courseRepository.findByAuthorId(user.getId());

        return myCourse.stream().map(courseMapper::toCourseResponse).toList();
    }

    @Transactional
    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN', 'TEACHER')")
    public UploadCourseResponse uploadCourse(UploadCourseRequest request, MultipartFile file, MultipartFile thumbnail)
            throws IOException {

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String videoUrl = cloudinaryService.uploadVideoChunked(file, "courses").get("url").toString();
        String thumbnailUrl = cloudinaryService.uploadImage(thumbnail);

        Course course = courseMapper.updateCourse(request);
        course.setVideoUrl(videoUrl);
        course.setThumbnail(thumbnailUrl);
        course.setAuthor(user);

        courseRepository.save(course);

        return courseMapper.toUploadCourseResponse(course);
    }

    public CourseChapterResponse getAllInfoCourse (Long courseId){

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        Long totalLessons = course.getChapters().stream()
                .mapToLong(chapter -> chapter.getLessons().size()).sum();

        CourseChapterResponse courseLessonResponse =  courseChapterAndLessonMapper
                .getCourserChapterAndLesson(courseId);

        Set<CourseChapterResponse.ChapterDto> sortedChapter = courseLessonResponse.getChapters().stream()
                .sorted(Comparator.comparing(CourseChapterResponse.ChapterDto::getChapterId))
                .peek(chapter -> {
                    Set<CourseChapterResponse.LessonDto> sortedLessons = chapter.getLessonDto().stream()
                            .sorted(Comparator.comparing(CourseChapterResponse.LessonDto::getLessonId))
                            .collect(Collectors.toCollection(LinkedHashSet::new));
                    chapter.setLessonDto(sortedLessons);
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        courseLessonResponse.setTotalLesson(totalLessons);
        courseLessonResponse.setChapters(sortedChapter);

        return courseLessonResponse;
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN', 'TEACHER')")
    @Transactional
    public void deleteCourse (Long courseId) {
        var email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        if(!Objects.equals(course.getAuthor().getId(), user.getId())
         && !Objects.equals(user.getRole().getName(), PredefinedRole.ADMIN_ROLE)){
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
            List<Certificate> certificates = certificateRepository.getAllCertificateById(course.getId());
            certificateRepository.deleteAll(certificates);
            courseRepository.delete(course);
            documentCourseRepository.deleteById(String.valueOf(course.getId()));
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public BuyCourseResponse buyCourse(BuyCourseRequest request){
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            throw new AppException(ErrorCode.COURSE_ALREADY_PURCHASED);
        }

        Long pointsCourse = Objects.requireNonNull(course.getPoints(), "Course points cannot be null");
        Long pointsUser = Objects.requireNonNull(user.getPoints(), "User points cannot be null");

        if(pointsUser < pointsCourse){
            throw new AppException(ErrorCode.BUY_COURSE_INVALID);
        }
        user.setPoints(pointsUser - pointsCourse);
        course.setQuantity(course.getQuantity() + 1);
        userRepository.save(user);

//       Cộng tiền vào account author khi người dùng mua thành công
        User authorCourse = course.getAuthor();
        authorCourse.setPoints(authorCourse.getPoints() + pointsCourse);

        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName(PaymentMethodName.BANK_TRANSFER)
                .orElseGet(() -> paymentMethodRepository.save(PaymentMethod.builder()
                                .methodName(PaymentMethodName.BANK_TRANSFER)
                        .build()));

        Payment payment = Payment.builder()
                .user(user)
                .course(course)
                .paymentMethod(paymentMethod)
                .price(BigDecimal.valueOf(pointsCourse * 100))
                .points(BigDecimal.valueOf(pointsUser))
                .status(PaymentStatus.COMPLETED)
                .build();

        paymentRepository.save(payment);

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .purchased(true)
                .build();

        enrollmentRepository.save(enrollment);
        return enrollmentMapper.toBuyCourseResponse(enrollment);
    }

    public List<CourseResponse> fetchRelatedCourses(RelatedCourseRequest request) {

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        List<Review> filteredComments = course.getComments().stream()
                .filter(r -> r.getRating() > 0 )
                .toList();

        long totalRating = filteredComments.stream()
                .mapToLong(Review::getRating)
                .sum();

        int numberOfValidReviews = filteredComments.size();
        double averageRating = numberOfValidReviews > 0 ? BigDecimal.valueOf((double) totalRating / numberOfValidReviews)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue() : 0.0 ;

        return courseRepository
                .findRelatedCourses(request.getKeywords(), request.getCourseId())
                .stream().map(c -> CourseResponse.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .averageRating(averageRating)
                        .author(c.getAuthor().getName())
                        .build())
                .toList();
    }

    public List<CourseDocument> findByTitle(String title) {
        return documentCourseRepository.findByTitle(title);
    }
}
