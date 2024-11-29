import CIcon from "@coreui/icons-react";
import React, { useEffect, useState } from "react";
import { toast, ToastContainer } from "react-toastify";
import {
    cilSortAlphaDown,
    cilImage,
    cilPhone,
    cilCheckCircle,
    cilLink,
    cilTag,
    cilCalendar,
    cilClipboard,
} from "@coreui/icons";
import TablePagination from "@mui/material/TablePagination";
import { approveAds, deleteAds, getAllAds, rejectAds } from "../../../../service/AdsService";
import Swal from "sweetalert2";
import LoadingSpinner from "../../../../utils/LoadingSpinner";

const CensorAds = () => {

    const [ads, setAds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(7);
    const [page, setPage] = useState(0);

    useEffect(() => {
        const fetchAds = async () => {
            setLoading(true);
            try {
                const data = await getAllAds(currentPage);
                if (data.code === 200) {
                    setAds(data.result.data);
                    setTotalPages(data.result.totalPages);
                }
            } catch (error) {
                console.log("Error fetching ads:", error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchAds();
    }, [currentPage]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
        setCurrentPage(newPage + 1);
    };


    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleApprover = (id) => {
        Swal.fire({
            title: 'Are you sure?',
            text: "You are about to approve this advertisement.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, approve it!',
            cancelButtonText: 'Cancel',
            reverseButtons: true
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const data = await approveAds(id);
                    if (data.code === 200) {
                        const updatedStatus = data.result.status;
                        setAds(prevAds =>
                            prevAds.map(ad =>
                                ad.id === id ? { ...ad, status: updatedStatus } : ad
                            )
                        );
                        toast.success('The advertisement has been approved.');
                    } else {
                        toast.error('Accept failed ads');
                    }
                } catch (error) {
                    toast.error('Accept failed ads');
                    console.log(error);
                }
            }
        });



    }

    const handleReject = (id) => {
        Swal.fire({
            title: 'Are you sure?',
            text: "You are about to reject this advertisement.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, reject it!',
            cancelButtonText: 'Cancel',
            reverseButtons: true
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const data = await rejectAds(id);
                    if (data.code === 200) {
                        const updatedStatus = data.result.status;
                        setAds(prevAds =>
                            prevAds.map(ad =>
                                ad.id === id ? { ...ad, status: updatedStatus } : ad
                            )
                        );
                        toast.success('The advertisement has been rejected.');
                    } else {
                        toast.error('Reject failed for the advertisement.');
                    }
                } catch (error) {
                    toast.error('Reject failed for the advertisement.');
                    console.log(error);
                }
            }
        });
    }

    const handleDelete = (id) => {
        Swal.fire({
            title: 'Are you sure?',
            text: "You are about to delete this advertisement.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'Cancel',
            reverseButtons: true
        }).then(async (result) => {
            if (result.isConfirmed) {
                try {
                    const data = await deleteAds(id);
                    if (data.code === 200) {
                        setAds(prevAds => prevAds.filter(ad => ad.id !== id));
                        toast.success('The advertisement has been deleted.');
                    } else {
                        toast.error('Delete failed for the advertisement.');
                    }
                } catch (error) {
                    toast.error('Delete failed for the advertisement.');
                    console.log(error);
                }
            }
        });
    }

    if (loading) {
        return (
            <LoadingSpinner />
        )
    }

    return (
        <div className="user-manage">
            <h2 className="user-manage-title">Ad Application Censorship</h2>
            <div className="user-manage-controls">
                <div className="user-manage-sort">
                    <select
                    // onChange={handleSortChange}
                    >
                        <option value="date-asc">Sort by Date (Oldest)</option>
                        <option value="date-desc">Sort by Date (Newest)</option>
                    </select>
                    <CIcon icon={cilSortAlphaDown} className="sort-icon" />
                </div>
            </div>
            <div className="user-manage-table">
                <table>
                    <thead>
                        <tr>
                            <th>
                                <CIcon icon={cilClipboard} className="table-icon" /> Title
                            </th>
                            <th>
                                <CIcon icon={cilImage} className="table-icon" /> Image
                            </th>
                            <th>
                                <CIcon icon={cilPhone} className="table-icon" /> Phone
                            </th>
                            <th>
                                <CIcon icon={cilCheckCircle} className="table-icon" /> Status
                            </th>
                            <th>
                                <CIcon icon={cilLink} className="table-icon" /> Link
                            </th>
                            <th>
                                <CIcon icon={cilTag} className="table-icon" /> Price
                            </th>
                            <th>
                                <CIcon icon={cilCalendar} className="table-icon" /> Start
                            </th>
                            <th>
                                <CIcon icon={cilCalendar} className="table-icon" /> End
                            </th>
                            <th>
                                <CIcon icon={cilCheckCircle} className="table-icon" /> Action
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {/* Hiển thị dữ liệu quảng cáo */}
                        {ads.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((ad) => (
                            <tr key={ad.id}>
                                <td>{ad.title}</td>
                                <td>
                                    <img src={ad.imageUrl} alt={ad.title} style={{ width: "50px", height: "50px" }} />
                                </td>
                                <td>{ad.contactPhone}</td>
                                <td>{ad.status}</td>
                                <td><a href={ad.link} target="_blank" rel="noopener noreferrer">Link</a></td>
                                <td>{ad.priceAds}</td>
                                <td>{ad.startDate}</td>
                                <td>{ad.endDate}</td>
                                <td>
                                    {ad.status === 'PENDING' && <button
                                        onClick={() => handleApprover(ad.id)}
                                        style={{
                                            backgroundColor: "#28a745",  // Green color for approve
                                            color: "#fff",
                                            padding: "5px 10px",
                                            borderRadius: "5px",
                                            cursor: "pointer",
                                            marginRight: "5px"
                                        }}
                                    >
                                        <CIcon icon={cilCheckCircle} /> Approve
                                    </button>
                                    }

                                    {ad.status === 'PENDING' &&
                                        <button
                                            onClick={() => handleReject(ad.id)}
                                            style={{
                                                backgroundColor: "#d9534f",  // Red color for reject
                                                color: "#fff",
                                                padding: "5px 10px",
                                                borderRadius: "5px",
                                                cursor: "pointer"
                                            }}
                                        >
                                            <CIcon icon={cilCheckCircle} /> Reject
                                        </button>
                                    }

                                    {(
                                        ad.status === 'AWAITING_PAYMENT'
                                        || ad.status === 'ACTIVE'
                                        || ad.status === 'COMPLETED') && (
                                            <button
                                                onClick={() => handleDelete(ad.id)}
                                                style={{
                                                    backgroundColor: "#dc3545",
                                                    color: "#fff",
                                                    padding: "5px 10px",
                                                    borderRadius: "5px",
                                                    cursor: "pointer"
                                                }}
                                            >
                                                <CIcon icon={cilCheckCircle} /> Delete
                                            </button>
                                        )}

                                </td>

                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <div className="pagination-container">
                <TablePagination
                    component="div"
                    count={totalPages * rowsPerPage} // Adjust count to reflect total pages correctly
                    page={page} // Use page state for TablePagination
                    onPageChange={handleChangePage} // Handle page change
                    rowsPerPage={rowsPerPage} // Use rowsPerPage state
                    onRowsPerPageChange={handleChangeRowsPerPage} // Handle rows per page change
                    rowsPerPageOptions={[7, 14, 21]} // Rows per page options
                    labelDisplayedRows={({ from, to, count }) => `${from}-${to} of ${count}`} // Display rows info
                />
            </div>
            <ToastContainer position="top-right" autoClose={3000} />
        </div>
    );
};

export default CensorAds;
