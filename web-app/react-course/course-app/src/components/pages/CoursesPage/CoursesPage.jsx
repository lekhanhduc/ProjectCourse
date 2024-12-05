import React, { useState, useEffect, useCallback } from 'react';
import { Search } from './components/Search';
import { SearchService } from '../../../service/CourseService';
import { ViewCourses } from './components/ViewCourses';
import { motion } from 'framer-motion';
import ReactPaginate from 'react-paginate';
import LoadingSpinner from '../../../utils/LoadingSpinner';

export const Courses = () => {
    const [loading, setLoading] = useState(true);
    const [courses, setCourses] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [pageSize] = useState(6);
    const [totalPages, setTotalPages] = useState(1);
    const [filterQuery, setFilterQuery] = useState('');

    // Các state cho các trường tìm kiếm
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [courseLevel, setCourseLevel] = useState('');
    const [language, setLanguage] = useState('');
    const [minPoints, setMinPoints] = useState('');
    const [maxPoints, setMaxPoints] = useState('');

    useEffect(() => {
        document.title = 'Courses';
    }, []);

    // Hàm gọi API để lấy danh sách khóa học
    const fetchCourses = useCallback(async () => {
        setLoading(true);
        try {
            const result = await SearchService(currentPage, pageSize, filterQuery);
            if (result && result.result) {
                setCourses(result.result.data);
                setTotalPages(result.result.totalPages >= 1 ? result.result.totalPages : 1);
            } else {
                setCourses([]);
                setTotalPages(1);
            }
        } catch (err) {
            console.log(err);
            setCourses([]);
            setTotalPages(1);
        } finally {
            setLoading(false);
        }
    }, [currentPage, pageSize, filterQuery]);

    useEffect(() => {
        setCurrentPage(1);
    }, [filterQuery]);

    useEffect(() => {
        fetchCourses();
    }, [fetchCourses]);

    // Cập nhật giá trị trang khi nhấn trang mới
    const handlePageClick = (data) => {
        setCurrentPage(data.selected + 1);
    };

    // Hàm onSearch sẽ nhận filter query từ Search component và gọi lại API
    const handleSearch = (filterQuery) => {
        setFilterQuery(filterQuery);  // Cập nhật filterQuery để thực hiện tìm kiếm
    };

    // Truyền các tham số tìm kiếm vào Search component
    const searchParams = {
        title,
        author,
        courseLevel,
        language,
        minPoints,
        maxPoints
    };

    if (loading) {
        return <LoadingSpinner />;
    }

    return (
        <motion.div
            key={filterQuery || currentPage}
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            transition={{ duration: 0.5 }}
        >
            {/* Truyền hàm handleSearch và các tham số tìm kiếm vào Search */}
            <Search
                onSearch={handleSearch}
                searchParams={searchParams}
                setTitle={setTitle}
                setAuthor={setAuthor}
                setCourseLevel={setCourseLevel}
                setLanguage={setLanguage}
                setMinPoints={setMinPoints}
                setMaxPoints={setMaxPoints}
            />
            <div className="container-fluid">
                <div className="container py-3">
                    <div className="row mx-0 justify-content-center">
                        <div className="col-lg-8">
                            <div className="section-title text-center position-relative mb-5">
                                <h1 className="display-4">Explore Our Latest Courses</h1>
                            </div>
                        </div>
                    </div>
                    <ViewCourses courses={courses} />
                    <ReactPaginate
                        previousLabel={'«'}
                        nextLabel={'»'}
                        breakLabel={'...'}
                        pageCount={totalPages}
                        marginPagesDisplayed={2}
                        pageRangeDisplayed={3}
                        onPageChange={handlePageClick}
                        forcePage={currentPage - 1}
                        containerClassName={'pagination pagination-lg justify-content-center'}
                        pageClassName={'page-item'}
                        pageLinkClassName={'page-link'}
                        previousClassName={'page-item'}
                        previousLinkClassName={'page-link'}
                        nextClassName={'page-item'}
                        nextLinkClassName={'page-link'}
                        breakClassName={'page-item'}
                        breakLinkClassName={'page-link'}
                        activeClassName={'active'}
                    />
                </div>
            </div>
        </motion.div>
    );
};
