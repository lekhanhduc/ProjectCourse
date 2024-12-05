package com.spring.dlearning.service;

import com.spring.dlearning.constant.PredefinedRole;
import com.spring.dlearning.dto.event.NotificationEvent;
import com.spring.dlearning.dto.event.PaymentEvent;
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
import com.spring.dlearning.service.specitification.CourseSpecificationBuilder;
import com.spring.dlearning.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    DocumentCourseRepository documentCourseRepository;
    PaymentMethodRepository paymentMethodRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    private final PaymentRepository paymentRepository;

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
                .level(course.getCourseLevel())
                .author(course.getAuthor().getName())
                .title(course.getTitle())
                .language(course.getLanguage())
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

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .userId(user.getId())
                .courseId(course.getId())
                .paymentMethod(paymentMethod)
                .points(BigDecimal.valueOf(pointsCourse))
                .price(BigDecimal.valueOf(pointsCourse * 1000))
                .status(PaymentStatus.COMPLETED)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("author", authorCourse.getName());
        data.put("title", course.getTitle());
        data.put("buyer", user.getEmail());
        data.put("points", course.getPoints());

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("Send Email")
                .subject("A User Has Purchased Your Course")
                .recipient(authorCourse.getEmail())
                .templateCode("course-purchase-notification")
                .param(data)
                .build();

        kafkaTemplate.send("payment-creation", paymentEvent);
        kafkaTemplate.send("notification-delivery", notificationEvent);

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

    public List<Course> getCourse(String [] course, String sortBy) {
        CourseSpecificationBuilder builder = new CourseSpecificationBuilder();
        for(String c: course) {
            Pattern pattern = Pattern.compile("(\\w+?)([:><~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
            Matcher matcher = pattern.matcher(c);
            if(matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
            }
        }

        Sort sort = Sort.unsorted();
        if(sortBy != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                String columnName = matcher.group(1);
                String direction = matcher.group(3);

                sort = (direction.equalsIgnoreCase("asc")) ? Sort.by(columnName).ascending()
                        : Sort.by(columnName).descending();
            }
        }

        return courseRepository.findAll(builder.build(), sort);
    }

    public List<CourseDocument> findByTitle(String title) {
        return documentCourseRepository.findByTitle(title);
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN', 'TEACHER')")
    public List<OverviewCourseResponse> overviewCourse() {
        var email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Course> courses = courseRepository.findByAuthorId(user.getId());


        return courses.stream().map(c -> {
            List<Review> reviews = c.getComments()
                    .stream().filter(r -> r.getRating() > 0).toList();

            long totalRating = reviews.stream()
                    .mapToLong(Review::getRating)
                    .sum();

            int numberOfReviews = reviews.size();

            double averageRating = (numberOfReviews > 0) ? (double) totalRating / numberOfReviews : 0;
            OverviewCourseResponse response = new OverviewCourseResponse();
            response.setTitle(c.getTitle());
            response.setQuantity(c.getQuantity());
            response.setThumbnail(c.getThumbnail());
            response.setTotalPrice(BigDecimal.valueOf(c.getPoints() * 100 * (c.getQuantity())));
            response.setAvgReview(BigDecimal.valueOf(averageRating).setScale(2, RoundingMode.HALF_UP));
            return response;
        }).toList();
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN', 'TEACHER')")
    public OverviewCourseResponse overviewCourseDetail(Long id) {

       Course course = courseRepository.findById(id)
               .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

       List<Review> reviews = course.getComments()
               .stream().filter(r -> r.getRating() > 0)
               .toList();

       long totalRating = reviews.stream().mapToLong(Review::getRating).sum();
       int numberOfReviews = reviews.size();

       BigDecimal avgRating = (numberOfReviews > 0) ? BigDecimal.valueOf(totalRating / numberOfReviews) : new BigDecimal(0);

       List<Payment> payments = paymentRepository.findByCourse(course);
       BigDecimal totalPrice = payments.stream().map(Payment::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

       return OverviewCourseResponse.builder()
               .quantity(course.getQuantity())
               .avgReview(avgRating)
               .totalPrice(totalPrice)
               .build();
    }
}
