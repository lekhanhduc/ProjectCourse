import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate
import TablePagination from "@mui/material/TablePagination";
import "../../css/TopPurchasingUsers.css"; // Import CSS đã tạo
import { getMonthlyData, getYearlyData } from "../../service/GetPurchases"; // Import service

const TopPurchasingUsers = () => {
  const [timeFilter, setTimeFilter] = useState("Monthly");
  const [selectedMonth, setSelectedMonth] = useState("January");
  const [selectedYear, setSelectedYear] = useState("2024");
  const [sortAscending, setSortAscending] = useState(true);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(7);
  const [totalItems, setTotalItems] = useState(0);
  const [data, setData] = useState([]);

  const navigate = useNavigate(); // Khởi tạo useNavigate

  useEffect(() => {
    fetchData(page + 1, rowsPerPage, sortAscending);
  }, [page, rowsPerPage, sortAscending]);

  const fetchData = async (page, size, sort) => {
    let fetchedData = [];
    try {
      if (timeFilter === "Monthly") {
        const monthNumber =
          new Date(Date.parse(`${selectedMonth} 1, 2022`)).getMonth() + 1;
        fetchedData = await getMonthlyData(monthNumber, selectedYear, sort);
      } else if (timeFilter === "Yearly") {
        fetchedData = await getYearlyData(selectedYear, sort);
      }
      setData(fetchedData);
      setTotalItems(fetchedData.length);
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };

  const handleSortChange = () => {
    setSortAscending(!sortAscending);
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleApplyFilters = () => {
    fetchData(page + 1, rowsPerPage, sortAscending);
  };

  const handleRowClick = (userId) => {
    navigate(`/admin/users/detail/${userId}`); // Chuyển hướng đến trang chi tiết
  };

  return (
    <div className="table-container">
      <h1>Top Purchasing Users</h1>

      {/* Thanh lọc và sắp xếp */}
      <div className="filter-container">
        <div className="filter-buttons">
          <button
            onClick={() => setTimeFilter("Monthly")}
            className={timeFilter === "Monthly" ? "active" : ""}
          >
            Monthly
          </button>
          <button
            onClick={() => setTimeFilter("Yearly")}
            className={timeFilter === "Yearly" ? "active" : ""}
          >
            Yearly
          </button>

          {/* Trường nhập tháng và ngày khi chọn Monthly */}
          {timeFilter === "Monthly" && (
            <div className="input-fields">
              <select
                value={selectedMonth}
                onChange={(e) => setSelectedMonth(e.target.value)}
              >
                {[
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
                ].map((month) => (
                  <option key={month} value={month}>
                    {month}
                  </option>
                ))}
              </select>

              <input
                type="number"
                placeholder="Year"
                value={selectedYear}
                onChange={(e) => setSelectedYear(e.target.value)}
              />
            </div>
          )}

          {/* Trường nhập năm khi chọn Yearly */}
          {timeFilter === "Yearly" && (
            <input
              type="number"
              placeholder="Year"
              value={selectedYear}
              onChange={(e) => setSelectedYear(e.target.value)}
            />
          )}
        </div>

        {/* Nút Apply và Sort */}
        <div>
          <button className="apply-button" onClick={handleApplyFilters}>
            Apply
          </button>
          <button className="sort-button" onClick={handleSortChange}>
            Sort {sortAscending ? "Ascending" : "Descending"}
          </button>
        </div>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>#</th>
            <th>User Name</th>
            <th>Total Courses Bought</th>
          </tr>
        </thead>
        <tbody className="top-users">
          {data
            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            .map((user, index) => (
              <tr
                key={user.userId}
                onClick={() => handleRowClick(user.userId)} // Thêm sự kiện onClick
                style={{ cursor: "pointer" }} // Thêm con trỏ chuột khi hover
              >
                <td>{page * rowsPerPage + index + 1}</td>

                <td
                  className={
                    index === 0
                      ? "first"
                      : index === 1
                      ? "second"
                      : index === 2
                      ? "third"
                      : ""
                  }
                >
                  {user.fullName}
                </td>
                <td>{user.totalCoursesBought}</td>
              </tr>
            ))}
        </tbody>
      </table>

      {/* Phân trang */}
      <div className="pagination-container">
        <TablePagination
          component="div"
          count={totalItems}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          rowsPerPageOptions={[7, 10, 25]}
        />
      </div>
    </div>
  );
};

export default TopPurchasingUsers;
