import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate để điều hướng
import TablePagination from "@mui/material/TablePagination";
import { getAllUsers, searchUsers } from "../../service/Admin_UserService"; // Import dịch vụ lấy dữ liệu người dùng
import {
    getAllWithdrawal,
    confirmWithdrawal,
    cancelWithdrawal,
    getAllWithdrawalWithPaginate,
    getProcessingWithdrawal,
    getCompletedWithdrawal,
    getCancelledWithdrawal
} from "../../service/Admin_Withdrawal";
import "../../css/UserManage.css"; // Import CSS cho căn chỉnh
import { toast, ToastContainer } from "react-toastify";

import {
    cilSearch,
    cilSortAlphaDown,
    cilUser,
    cilEnvelopeClosed,
    cilCalendar,
    cilTrash,
    cilUserFemale,
    cilCheck,
    cilBan
} from "@coreui/icons";
import CIcon from "@coreui/icons-react";

const Withdrawal = () => {
    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(7);
    const [totalItems, setTotalItems] = useState(0);
    const [sort, setSort] = useState("user.name,asc");
    const [searchTerm, setSearchTerm] = useState("");
    const [currentFilter, setCurrentFilter] = useState("ALL");
    const navigate = useNavigate(); // Khởi tạo useNavigate

    const [listWithdrawals, setListWithdrawals] = useState([]);

    useEffect(() => {
        fetchAllWithdrawalWithPaginate(page + 1, rowsPerPage, sort);
        // fetchAllWithdrawal(); // Gọi API với trang bắt đầu từ 1
    }, [page, rowsPerPage, sort, currentFilter]);

    const fetchUsers = async (page, size, sort, keywords = "") => {
        try {
            let response;
            if (keywords) {
                response = await searchUsers(page, size, sort, keywords);
            } else {
                response = await getAllUsers(page, size, sort);
            }
            setUsers(response.content);
            setTotalItems(response.totalElements);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    const fetchAllWithdrawal = async () => {
        let res = await getAllWithdrawal();
        setListWithdrawals(res.data.result);
    };

    const fetchAllWithdrawalWithPaginate = async (page, size, sort) => {
        try {
            let response;
            if (currentFilter === "ALL") {
                response = await getAllWithdrawalWithPaginate(page, size, sort);
            } else if (currentFilter === "PROCESSING") {
                console.log("processing");
                response = await getProcessingWithdrawal(page, size, sort);
            } else if (currentFilter === "COMPLETED") {
                response = await getCompletedWithdrawal(page, size, sort);
            } else if (currentFilter === "CANCELLED") {
                response = await getCancelledWithdrawal(page, size, sort);
            }
            setListWithdrawals(response.content);
            setTotalItems(response.totalElements);
        } catch (error) {
            toast.error("Error fetching withdrawal:", error);
        }
    };

    const confirmWithdrawalOfTeacher = async (id) => {
        let res = await confirmWithdrawal(id);
        if (res.data.code === 200) {
            toast.success("Confirm successfully");
            await fetchAllWithdrawalWithPaginate();
        } else {
            toast.error("Confirm failed");
        }
    };

    const cancelWithdrawalOfTeacher = async (id) => {
        let res = await cancelWithdrawal(id);
        if (res.data.code === 200) {
            toast.success("Cancel successfully");
            await fetchAllWithdrawalWithPaginate();
        } else {
            toast.error("Cancel failed");
        }
    };

    const handleSearchChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleSearchKeyDown = (e) => {
        if (e.key === "Enter") {
            setPage(0);
            fetchUsers(1, rowsPerPage, sort, searchTerm);
        }
    };

    const handleSortChange = (e) => {
        setSort(e.target.value);
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleFilterChange = (filter) => {
        setCurrentFilter(filter);
        setPage(0); // Reset về trang đầu
    };

    const handleRowClick = (id) => {
        navigate(`/admin/users/detail/${id}`); // Điều hướng đến trang chi tiết người dùng
    };

    return (
        <div className="user-manage">
            <h2 className="user-manage-title">Withdrawal Management</h2>
            <div className="user-manage-controls">
                <div className="user-manage-search">
                    <input
                        type="text"
                        placeholder="Search by name or email"
                        value={searchTerm}
                        onChange={handleSearchChange}
                        onKeyDown={handleSearchKeyDown}
                    />
                    <CIcon icon={cilSearch} className="search-icon" />
                </div>
                <div className="user-manage-sort">
                    <select onChange={handleSortChange}>
                        <option value="user.name,asc">Sort by Name (A-Z)</option>
                        <option value="user.name,desc">Sort by Name (Z-A)</option>
                        <option value="createdAt,asc">Sort by Date (Oldest)</option>
                        <option value="createdAt,desc">Sort by Date (Newest)</option>
                    </select>
                    <CIcon icon={cilSortAlphaDown} className="sort-icon" />
                </div>
                <div className="course-manage-filters">
                    <button
                        className={`filter-button ${currentFilter === "ALL" ? "active" : ""
                            }`}
                        onClick={() => handleFilterChange("ALL")}
                    >
                        All
                    </button>
                    <button
                        className={`filter-button ${currentFilter === "PROCESSING" ? "active" : ""
                            }`}
                        onClick={() => handleFilterChange("PROCESSING")}
                    >
                        Processing
                    </button>
                    <button
                        className={`filter-button ${currentFilter === "COMPLETED" ? "active" : ""
                            }`}
                        onClick={() => handleFilterChange("COMPLETED")}
                    >
                        Completed
                    </button>
                    <button
                        className={`filter-button ${currentFilter === "CANCELLED" ? "active" : ""
                            }`}
                        onClick={() => handleFilterChange("CANCELLED")}
                    >
                        Cancelled
                    </button>
                </div>
            </div>
            <div className="user-manage-table">
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>
                                <CIcon icon={cilCalendar} className="table-icon" /> Date
                            </th>
                            <th>
                                <CIcon icon={cilCalendar} className="table-icon" /> Name
                            </th>
                            <th>
                                <CIcon icon={cilUser} className="table-icon" /> Points
                            </th>
                            <th>
                                <CIcon icon={cilEnvelopeClosed} className="table-icon" /> Money
                            </th>
                            <th>
                                <CIcon icon={cilUserFemale} className="table-icon" /> Bank
                            </th>
                            <th>
                                <CIcon icon={cilEnvelopeClosed} className="table-icon" /> Bank
                                Number
                            </th>
                            <th>
                                <CIcon icon={cilEnvelopeClosed} className="table-icon" /> Status
                            </th>
                            <th>
                                <CIcon icon={cilEnvelopeClosed} className="table-icon" /> Action
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {listWithdrawals.map((item, index) => (
                            <tr key={item.id} style={{ cursor: "pointer" }}>
                                <td>{page * rowsPerPage + index + 1}</td>
                                <td class="text-nowrap">{item.createdAt}</td>
                                <td class="text-nowrap">{item.name}</td>
                                <td>{item.points}</td>
                                <td>{item.money}</td>
                                <td>{item.bank}</td>
                                <td>{item.bankNumber}</td>
                                <td>{item.status}</td>
                                <td class="text-nowrap">
                                    {item.status === "PROCESSING" && (
                                        <div>
                                            <button className="btn btn-primary"
                                                onClick={() => confirmWithdrawalOfTeacher(item.id)}
                                                style={{

                                                    color: "#fff",
                                                    border: "none",
                                                    padding: "5px 10px",
                                                    borderRadius: "5px",
                                                    cursor: "pointer",
                                                }}
                                            >
                                                <CIcon icon={cilCheck} /> Confirm
                                            </button>

                                            <button className="btn btn-danger"
                                                onClick={() => cancelWithdrawalOfTeacher(item.id)}
                                                style={{
                                                    color: "#fff",
                                                    border: "none",
                                                    padding: "5px 10px",
                                                    borderRadius: "5px",
                                                    cursor: "pointer",
                                                }}
                                            >
                                                <CIcon icon={cilTrash} /> Cancel
                                            </button>
                                        </div>
                                    )}

                                    {item.status === "COMPLETED" && (
                                        <div>
                                            <CIcon
                                                icon={cilCheck}
                                                style={{ color: "#00FF00", fontSize: "24px" }}
                                            />
                                        </div>
                                    )}

                                    {item.status === "CANCELLED" && <div>
                                        <CIcon
                                            icon={cilBan}
                                            style={{ color: "#FF3333", fontSize: "24px" }}
                                        />
                                    </div>}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <div className="pagination-container">
                <TablePagination
                    component="div"
                    count={totalItems}
                    page={page}
                    onPageChange={handleChangePage}
                    rowsPerPage={rowsPerPage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                    rowsPerPageOptions={[7, 14, 21]}
                    className="custom-pagination"
                    labelDisplayedRows={({ from, to, count }) =>
                        `${from}-${to} of ${count}`
                    }
                />
            </div>
            <ToastContainer
                position="top-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
            />
        </div>
    );
};

export default Withdrawal;
