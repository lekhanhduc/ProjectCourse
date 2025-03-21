import React, { useState, useEffect } from "react";
import {
  getMonthlyRevenue,
  getYearlyRevenue,
} from "../../service/GetMonthlyRevenue"; // API service
import "../../css/TopRevenueTeachers.css"; // Import CSS
import { Link } from "react-router-dom"; // Import Link from react-router-dom

const TopRevenueTeachers = () => {
  const [teachersMonthly, setTeachersMonthly] = useState([]);
  const [teachersYearly, setTeachersYearly] = useState([]);
  const [month, setMonth] = useState(12); // Default to December
  const [year, setYear] = useState(2024); // Default to 2024
  const [sortOrder, setSortOrder] = useState("false"); // Sorting order (asc/desc)
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [viewMonthly, setViewMonthly] = useState(true); // To toggle between monthly and yearly view

  // Fetch monthly revenue data
  const fetchMonthlyRevenue = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getMonthlyRevenue(
        month,
        year,
        sortOrder === "asc" ? true : false
      );
      setTeachersMonthly(response);
    } catch (error) {
      setError("Error fetching monthly revenue data.");
    } finally {
      setLoading(false);
    }
  };

  // Fetch yearly revenue data
  const fetchYearlyRevenue = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getYearlyRevenue(
        year,
        sortOrder === "asc" ? true : false
      );
      setTeachersYearly(response);
    } catch (error) {
      setError("Error fetching yearly revenue data.");
    } finally {
      setLoading(false);
    }
  };

  // Call the appropriate fetch function when month/year or sortOrder changes
  useEffect(() => {
    if (viewMonthly) {
      fetchMonthlyRevenue();
    } else {
      fetchYearlyRevenue();
    }
  }, [month, year, sortOrder, viewMonthly]);

  return (
    <div className="top-revenue-teachers-container-24">
      <h1 className="top-revenue-teachers-header-24">Top Revenue Teachers</h1>

      <div className="select-month-year-24">
        <button
          className="select-month-year-24"
          onClick={() => setViewMonthly(true)}
        >
          Monthly
        </button>
        <button
          className="select-month-year-24"
          onClick={() => setViewMonthly(false)}
        >
          Yearly
        </button>
      </div>

      {viewMonthly ? (
        <div className="select-month-year-24">
          <select
            className="select-month-24"
            value={month}
            onChange={(e) => setMonth(e.target.value)}
          >
            {Array.from({ length: 12 }, (_, i) => (
              <option key={i} value={i + 1}>
                {new Date(0, i).toLocaleString("default", { month: "long" })}
              </option>
            ))}
          </select>

          <select
            className="select-year-24"
            value={year}
            onChange={(e) => setYear(e.target.value)}
          >
            {[2025, 2024, 2023, 2022, 2021].map((y) => (
              <option key={y} value={y}>
                {y}
              </option>
            ))}
          </select>
        </div>
      ) : (
        <div className="select-month-year-24">
          <select
            className="select-year-24"
            value={year}
            onChange={(e) => setYear(e.target.value)}
          >
            {[2025, 2024, 2023, 2022, 2021].map((y) => (
              <option key={y} value={y}>
                {y}
              </option>
            ))}
          </select>
        </div>
      )}

      <div className="data-container-24">
        {loading && <div>Loading...</div>}
        {error && <div>{error}</div>}
        {(viewMonthly ? teachersMonthly : teachersYearly).map((teacher) => (
          <div className="teacher-card-24" key={teacher.teacherId}>
            <img
              src={teacher.teacherAvatar}
              alt={teacher.teacherName}
              className="teacher-avatar-24"
            />
            <h2>{teacher.teacherName}</h2>
            <p>
              Total Revenue:{" "}
              {teacher.totalRevenue.toLocaleString("de-DE") + " VND"}
            </p>
            <p>Total Courses Sold: {teacher.totalCoursesSold}</p>
            <Link
              to={`http://localhost:3000/admin/users/detail/${teacher.teacherId}`}
              className="teacher-link-24"
            >
              View Details
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TopRevenueTeachers;
