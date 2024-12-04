import React, { useState, useEffect } from "react";
import { getTopRatedCourses } from "../../service/TopRatedCoursesService"; // API service
import "../../css/TopRatedCourses.css"; // Import CSS
import { Link } from "react-router-dom"; // Import Link from react-router-dom

const TopRatedCourses = () => {
  const [courses, setCourses] = useState([]);
  const [month, setMonth] = useState(12); // Default to December
  const [year, setYear] = useState(2024); // Default to 2024
  const [sortOrder, setSortOrder] = useState("desc"); // Sorting order (desc/asc)
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const years = [2024, 2023, 2022, 2021]; // List of years

  // Fetch top-rated courses based on month, year, and sort order
  const fetchTopRatedCourses = async (ascending) => {
    setLoading(true);
    setError(null);
    try {
      const response = await getTopRatedCourses(month, year, 1, 10, ascending); // Fetch data with ascending parameter
      setCourses(response);
    } catch (error) {
      setError("Error fetching top-rated courses.");
    } finally {
      setLoading(false);
    }
  };

  // Automatically fetch courses when month, year, or sortOrder changes
  useEffect(() => {
    fetchTopRatedCourses(sortOrder === "asc");
  }, [month, year, sortOrder]);

  // Handle sorting by rating
  const handleSort = () => {
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  // Handle month and year change
  const handleMonthChange = (e) => setMonth(e.target.value);
  const handleYearChange = (e) => setYear(e.target.value);

  return (
    <div className="top-rated-courses">
      {/* Tiêu đề thẻ H1 */}
      <h1>Top Rated Courses</h1> {/* Tiêu đề trang */}
      <div className="filters">
        <div className="month-year-container">
          <select value={month} onChange={(e) => setMonth(e.target.value)}>
            {months.map((month, index) => (
              <option key={index} value={index + 1}>
                {month}
              </option>
            ))}
          </select>
          <select value={year} onChange={(e) => setYear(e.target.value)}>
            {years.map((year) => (
              <option key={year} value={year}>
                {year}
              </option>
            ))}
          </select>
        </div>

        <div className="sort-container">
          <button onClick={handleSort}>
            Sort by Rating ({sortOrder === "asc" ? "Ascending" : "Descending"})
          </button>
        </div>
      </div>
      <div className="courses-list-1">
        {courses.map((course) => (
          <div key={course.courseId} className="course-item-1">
            <Link
              to={`http://localhost:3000/admin/course/detail/${course.courseId}`}
              className="course-item-link-1"
            >
              <div className="course-thumbnail-1">
                <img src={course.thumbnail} alt={course.title} />
              </div>
              <div className="course-info-1">
                <h3>{course.title}</h3>
                <p>Average Rating: {course.averageRating}</p>
                <div className="user-info-1">
                  <img
                    src={course.userAvatar}
                    alt={course.userName}
                    className="user-avatar-1"
                  />
                  <span>{course.userName}</span>
                </div>
              </div>
            </Link>
          </div>
        ))}
      </div>
      {loading && <div>Loading...</div>}
      {error && <div>{error}</div>}
    </div>
  );
};

export default TopRatedCourses;
