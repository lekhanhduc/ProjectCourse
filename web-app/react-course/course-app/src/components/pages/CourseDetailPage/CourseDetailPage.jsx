import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { FaStar } from 'react-icons/fa';
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Swal from "sweetalert2";
import { buyCourse, getChapterById, getCommentByCourseId, getCourseById } from "../../../service/CourseService";
import CourseContent from "./components/CourseContent";
import { ReviewSection } from "./components/ReviewSection";
import { CourseFeature } from "./components/CourseFeature";
import { addReplyReview, addReview, deleteReview, editReview } from "../../../service/ReviewService";
import { checkPurchase } from "../../../service/Enrollment";
import LoadingSpinner from "../../../utils/LoadingSpinner";
import { Tab, Nav } from 'react-bootstrap';
import { fetchInfoTeacher } from "../../../service/TeacherService";
import Instructor from "./components/Instructor";
import { getRelatedCourses } from "../../../service/ElasticSearchService";

export const CourseDetail = () => {
    const token = localStorage.getItem('token');
    const { id } = useParams();
    const [course, setCourse] = useState(null);  // thông tin chi tiết về khóa học
    const [relatedCourses, setRelatedCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [comments, setComments] = useState([]); // list comment
    const [newComment, setNewComment] = useState(""); // add comment
    const [replyContent, setReplyContent] = useState({});
    const [editContent, setEditContent] = useState({}); // cập nhật nội dung chỉnh sửa của bình luận.
    const [editingCommentId, setEditingCommentId] = useState(null); // lưu ID của bình luận đang được chỉnh sửa
    const [newRating, setNewRating] = useState(0);
    const [chapter, setChapter] = useState(null);
    const [isPurchase, setIsPurchase] = useState(false);

    const [infoTeacher, setInfoTeacher] = useState({
        userId: null,
        name: '',
        avatar: '',
        avgRating: 0,
        reviewAmount: 0,
        studentAmount: 0,
        courseAmount: 0,
        description: ''
    });

    useEffect(() => {
        document.title = 'Courses Detail'
    })
    useEffect(() => {
        const fetchCourseData = async () => {
            setLoading(true);
            try {
                const courseResponse = await getCourseById(id);
                console.log(courseResponse)
                if (courseResponse && courseResponse.result) {
                    setCourse(courseResponse.result);

                    const relatedCoursesResponse = await getRelatedCourses(courseResponse.result.title);
                    if (relatedCoursesResponse && relatedCoursesResponse.result) {
                        setRelatedCourses(relatedCoursesResponse.result.filter(c => c.title !== courseResponse.result.title));
                    }
                }
            } catch (error) {
                console.error("Error fetching data:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchCourseData();
    }, [id]);

    // Fetch Info Teacher

    useEffect(() => {
        const getInfoTeacher = async () => {
            try {
                const data = await fetchInfoTeacher(id);
                if (data.code === 200) {
                    setInfoTeacher({
                        userId: data.result.userId,
                        name: data.result.name,
                        avatar: data.result.avatar,
                        avgRating: data.result.avgRating,
                        reviewAmount: data.result.reviewAmount,
                        studentAmount: data.result.studentAmount,
                        courseAmount: data.result.courseAmount,
                        description: data.result.description
                    });
                }
            } catch (error) {
                console.log(error)
            }
        }
        getInfoTeacher();
    }, [id]);

    useEffect(() => {
        getChapterById(id)
            .then(data => {
                setChapter(data.result)
                setLoading(false);
            }).catch(error => {
                console.log(error)
                setLoading(false);
            });
    }, [id]);

    useEffect(() => {
        const fetchPurchaseStatus = async () => {
            try {
                const result = await checkPurchase(id);
                if (result && result.result && result.result.purchased) {
                    setIsPurchase(result.result.purchased);
                }
            } catch (error) {
                console.error("Error checking purchase status:", error);
                setIsPurchase(false);
            }
        };

        if (id) {
            fetchPurchaseStatus();
        }
    }, [id]);

    // Fetch comments
    useEffect(() => {
        getCommentByCourseId(id)
            .then(data => {
                const updatedComments = data.result.map(comment => ({
                    ...comment,
                    replying: false,
                    replies: comment.replies || []
                }));
                setComments(updatedComments);
            }).catch(error => console.log(error));
    }, [id]);

    // Add a new comment
    const handleAddReview = async () => {
        if (!newComment.trim() && newRating === 0) {
            toast.error('Please enter a comment or select a rating');
            return;
        }

        const commentData = {
            content: newComment.trim(),
            rating: newRating,
            parentCommentId: null,
            courseId: id
        };

        try {
            const result = await addReview(id, commentData);
            setComments([{
                ...result, // sao chép toàn bộ thuộc tính trong result:  bình luận mới được trả về từ server.
                replying: false,
                replies: []
            }, ...comments]);  // sao chép toàn bộ các comment trước đó và bao gồm cả comment mới được thêm vào đầu danh sách

            setNewComment("");
            setNewRating(0);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    // Handle adding a reply
    const handleAddReply = async (commentId) => {
        const replyText = replyContent[commentId];

        if (!replyText.trim()) {
            toast.error('Please enter a reply');
            return;
        }

        const replyData = {
            content: replyText.trim(),
            parentReviewId: commentId,
            rating: 0,
            courseId: id
        };

        try {
            const result = await addReplyReview(id, replyData);

            if (result) {
                // Cập nhật lại danh sách comments với reply mới
                const updatedComments = comments.map(comment => {
                    if (comment.id === commentId) {
                        return {
                            ...comment,
                            replies: [...comment.replies, { ...result, replying: false }]
                        };
                    }
                    return comment;
                });
                setComments(updatedComments);
                setReplyContent({ ...replyContent, [commentId]: '' });
            }
        } catch (error) {
            console.error('Error in component:', error);
        }
    };

    // Edit comment
    const handleEditReview = async (commentId) => {
        const updatedContent = (editContent[commentId] || "").trim();

        if (!updatedContent) {
            toast.error('Please enter new content');
            return;
        }
        try {
            const result = await editReview(commentId, updatedContent, token);

            if (result) {
                setComments((prevComments) => {
                    const updatedComments = prevComments.map(comment => {
                        // Cập nhật comment cha nếu cần
                        if (comment.id === commentId) {
                            return { ...comment, content: result.content };
                        }
                        // Cập nhật replies nếu comment là trả lời
                        const updatedReplies = comment.replies.map(reply => {
                            if (reply.id === commentId) {
                                return { ...reply, content: result.content };
                            }
                            return reply;
                        });
                        return { ...comment, replies: updatedReplies };
                    });
                    return [...updatedComments];
                });

                setEditingCommentId(null); // Thoát khỏi chế độ chỉnh sửa
            }
        } catch (error) {
            console.error('Error editing comment:', error);
        }
    };

    // Delete comment
    const handleDeleteReview = async (reviewId) => {
        try {
            const result = await deleteReview(reviewId, token);
            if (result) {
                // Cập nhật lại danh sách comments sau khi xóa thành công
                setComments((prevComments) => {
                    const updatedReviews = prevComments
                        .filter(comment => comment.id !== reviewId)  // Loại bỏ bình luận cha đã bị xoá
                        .map(comment => ({
                            ...comment,
                            replies: comment.replies.filter(reply => reply.id !== reviewId)  // Loại bỏ cả các replies nếu có
                        }));

                    return updatedReviews;
                });
            }
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    // Toggle reply input
    const handleReplyToggle = (reviewId) => {
        const updatedReviews = comments.map(comment => {
            if (comment.id === reviewId) {
                return { ...comment, replying: !comment.replying };
            }
            return comment;
        });
        setComments(updatedReviews);
    };

    const handleEnrollNow = async () => {
        Swal.fire({
            title: 'Are you sure?',
            text: 'Do you want to buy this course?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, buy now!',
            cancelButtonText: 'No, cancel.'
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const response = await buyCourse(id);
                    if (response.data.result) {
                        Swal.fire({
                            title: 'Purchase successful!',
                            text: `You have purchased the course: ${response.data.result.title}`,
                            icon: 'success'
                        });
                    } else {
                        Swal.fire({
                            title: 'Purchase fail!',
                            text: `${response.data.message}`,
                            icon: 'error'
                        });
                    }
                } catch (error) {
                    Swal.fire({
                        title: 'Error!',
                        text: error.message || 'An error occurred while purchasing the course. Please try again later.',
                        icon: 'error'
                    });
                    console.error(error);
                }
            }
        });
    };

    const renderStars = (rating, handleRating) => {
        return (
            <div className="star-rating">
                {[1, 2, 3, 4, 5].map((star) => (
                    <FaStar
                        key={star}
                        size={25}
                        color={star <= rating ? "#ffc107" : "#e4e5e9"} // Nếu giá trị của star (ngôi sao hiện tại trong vòng lặp) nhỏ hơn hoặc bằng rating, thì ngôi sao sẽ có màu vàng (#ffc107).
                        onClick={() => handleRating(star)}
                        style={{ cursor: "pointer" }}
                    />
                ))}
            </div>
        );
    };

    const handleRatingChange = (rating) => {
        if (newRating === rating) {
            setNewRating(rating - 1);
        } else {
            setNewRating(rating);
        }
    };

    if (loading)
        return <div><LoadingSpinner /></div>;

    if (!course) {
        return <div>Course data is not available</div>;
    }

    return (
        <div className="container-fluid py-5">
            <div className="container py-5">
                <div className="row">
                    <div className="col-lg-8">
                        <div className="mb-5">
                            <h1 className="display-4">{course.title}</h1>
                            <img className="img-fluid rounded w-100 mb-4" src={course.thumbnail} alt="Course" />
                            <p>{course.description}</p>
                        </div>

                        <Tab.Container defaultActiveKey="course-detail">
                            <Nav className="custom-tabs mb-4">
                                <Nav.Item>
                                    <Nav.Link eventKey="course-detail">Course Content</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link eventKey="info-teach">Instructor</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link eventKey="review">Reviews</Nav.Link>
                                </Nav.Item>
                            </Nav>

                            <Tab.Content>
                                <Tab.Pane eventKey="course-detail">
                                    <CourseContent chapter={chapter} />
                                </Tab.Pane>

                                <Tab.Pane eventKey="info-teach">
                                    <Instructor
                                        infoTeacher={infoTeacher} />
                                </Tab.Pane>

                                <Tab.Pane eventKey="review">
                                    <ReviewSection
                                        comments={comments}
                                        newComment={newComment}
                                        editingCommentId={editingCommentId}
                                        setEditingCommentId={setEditingCommentId}
                                        newRating={newRating}
                                        setNewComment={setNewComment}
                                        setNewRating={setNewRating}
                                        replyContent={replyContent}
                                        setReplyContent={setReplyContent}
                                        editContent={editContent}
                                        setEditContent={setEditContent}
                                        renderStars={renderStars}
                                        handleAddReview={handleAddReview}
                                        handleReplyToggle={handleReplyToggle}
                                        handleAddReply={handleAddReply}
                                        handleEditReview={handleEditReview}
                                        handleDeleteReview={handleDeleteReview}
                                        handleRatingChange={handleRatingChange}
                                        isPurchase={isPurchase}
                                    />
                                </Tab.Pane>

                            </Tab.Content>
                        </Tab.Container>
                    </div>

                    <CourseFeature
                        course={course}
                        handleEnrollNow={handleEnrollNow}
                        isPurchase={isPurchase}
                        relatedCourses={relatedCourses}
                        id={id}
                    />
                </div>
            </div>
            <ToastContainer position="top-right" autoClose={3000} className="custom-toast-container" />
        </div>
    );


};