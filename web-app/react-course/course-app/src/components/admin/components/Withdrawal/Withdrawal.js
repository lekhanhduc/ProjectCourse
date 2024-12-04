import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate để điều hướng
import TablePagination from "@mui/material/TablePagination";
import { getAllUsers, searchUsers } from "../../service/Admin_UserService"; // Import dịch vụ lấy dữ liệu người dùng
import { getAllWithdrawal, confirmWithdrawal, cancelWithdrawal, getAllWithdrawalWithPaginate } from "../../service/Admin_Withdrawal";
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
    cilCheck
} from "@coreui/icons";
import CIcon from "@coreui/icons-react";

const Withdrawal = () => {
    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(7);
    const [totalItems, setTotalItems] = useState(0);
    const [sort, setSort] = useState("name,asc");
    const [searchTerm, setSearchTerm] = useState("");
    const navigate = useNavigate(); // Khởi tạo useNavigate

    const [listWithdrawals, setListWithdrawals] = useState([]);

    useEffect(() => {
        fetchAllWithdrawalWithPaginate(page + 1, rowsPerPage, sort);
        // fetchAllWithdrawal(); // Gọi API với trang bắt đầu từ 1
    }, [page, rowsPerPage, sort]);

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
            let response = await getAllWithdrawalWithPaginate(page, size, sort);

            setListWithdrawals(response.result);
            setTotalItems(response.totalElements);
        } catch (error) {
            console.error("Error fetching withdrawal:", error);
        }
    };

    const confirmWithdrawalOfTeacher = async (id) => {
        let res = await confirmWithdrawal(id);
        if (res.data.code === 200) {
            toast.success("Confirm successfully")
            fetchAllWithdrawal();
        }
    };

    const cancelWithdrawalOfTeacher = async (id) => {
        let res = await cancelWithdrawal(id);
        if (res.data.code === 200) {
            toast.success("Cancel successfully")
            fetchAllWithdrawal();
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
                        <option value="name,asc">Sort by Name (A-Z)</option>
                        <option value="name,desc">Sort by Name (Z-A)</option>
                        <option value="createdAt,asc">Sort by Date (Oldest)</option>
                        <option value="createdAt,desc">Sort by Date (Newest)</option>
                    </select>
                    <CIcon icon={cilSortAlphaDown} className="sort-icon" />
                </div>
            </div>
            <div className="user-manage-table">
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
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
                                <CIcon icon={cilEnvelopeClosed} className="table-icon" /> Bank Number
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
                            <tr
                                key={item.id}
                                style={{ cursor: "pointer" }}
                            >
                                <td>{page * rowsPerPage + index + 1}</td>
                                {/* <td>{new Date(item.createAt).toLocaleDateString()}</td> */}
                                <td>{item.name}</td>
                                <td>{item.points}</td>
                                <td>{item.money}</td>
                                <td>{item.bank}</td>
                                <td>{item.bankNumber}</td>
                                <td>{item.status}</td>
                                <td>
                                    {item.status === 'PROCESSING' &&
                                        <div>
                                            <button
                                                onClick={() => confirmWithdrawalOfTeacher(item.id)}
                                                style={{
                                                    backgroundColor: "#d9534f",
                                                    color: "#fff",
                                                    border: "none",
                                                    padding: "5px 10px",
                                                    borderRadius: "5px",
                                                    cursor: "pointer",
                                                }}
                                            >
                                                <CIcon icon={cilTrash} /> Confirm
                                            </button>

                                            <button
                                                onClick={() => cancelWithdrawalOfTeacher(item.id)}
                                                style={{
                                                    backgroundColor: "#d9534f",
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
                                    }

                                    {item.status === 'COMPLETED' &&
                                        <div>
                                            < CIcon icon={cilCheck} style={{ color: 'green', fontSize: '24px' }} />
                                        </div>
                                    }

                                    {item.status === 'CANCELLED' && true


                                    }

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
