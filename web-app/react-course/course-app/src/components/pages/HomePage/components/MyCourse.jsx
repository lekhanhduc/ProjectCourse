import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import LoadingSpinner from '../../../../utils/LoadingSpinner';
import { myCourse } from '../../../../service/CourseService';

export const MyCourses = () => {
    const [loading, setLoading] = useState(true);
    const [courses, setCourses] = useState([]);
    const navigate = useNavigate();

    const token = localStorage.getItem('token');

    useEffect(() => {
        document.title = 'My Courses'
    })

    useEffect(() => {
        if (!token) {
            setLoading(false);
            return;
        }
        const fetchMyCourse = async () => {
            try {
                const data = await myCourse();
                if (data && data.code === 200 && data.result) {
                    setCourses(data.result);
                } else {
                    setCourses([]);
                }
            } catch (error) {
                console.log(error);
            } finally {
                setLoading(false)
            }
        }
        fetchMyCourse();
    }, [token]);

    const handleLearnNow = (id) => {
        navigate(`/lesson-detail/${id}`);
    };

    if (loading) {
        return (<LoadingSpinner />);
    }

    return (
        <div className='mycousrse-page'>
            <div className="container-fluid my-course-container my-5">
                <div className="my-course-grid">
                    {courses.map(course => (
                        <div key={course.courseId} className="my-course-item">
                            <div className="card h-100 shadow-sm border-0 my-course-card">
                                {/* Hình ảnh khóa học */}
                                <div className="my-course-thumbnail-wrapper">
                                    <img
                                        src={course.thumbnail}
                                        className="card-img-top img-fluid my-course-thumbnail"
                                        alt={course.title}
                                    />
                                    {/* Nút "Học ngay" hiển thị khi hover */}
                                    <div className="my-course-hover-overlay">
                                        <button
                                            className="btn btn-primary my-course-start-learning-btn"
                                            onClick={() => handleLearnNow(course.courseId)} // Gọi hàm điều hướng
                                        >
                                            Learn now
                                        </button>
                                    </div>
                                </div>

                                <div className="card-body">
                                    {/* Tiêu đề khóa học */}
                                    <h5 className="card-title text-primary font-weight-bold my-course-title">
                                        {course.title}
                                    </h5>

                                    {/* Tác giả khóa học */}
                                    <p className="card-text text-dark my-course-author">
                                        <strong>Author:</strong> {course.author}
                                    </p>

                                    {/* Level của khóa học */}
                                    <p className="card-text my-course-level">
                                        <span className="d-flex align-items-center">
                                            <i className="fa fa-signal text-success mr-2"></i>
                                            <strong>Level:</strong> {course.courseLevel}
                                        </span>
                                    </p>

                                    {/* Giá khóa học */}
                                    <p className="my-course-price text-dark">
                                        <span className="d-flex align-items-center">
                                            <i className="fa fa-coins text-warning mr-2"></i>
                                            <strong>Points:</strong>
                                            <span className="ml-1">{course.points}</span>
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};
