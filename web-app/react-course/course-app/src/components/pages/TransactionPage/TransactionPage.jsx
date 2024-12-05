import React, { useEffect, useState } from 'react';
import LoadingSpinner from '../../../utils/LoadingSpinner';
import ReactPaginate from 'react-paginate';
import { searchTransaction } from '../../../service/PaymentService';
import deposit from '../../../img/naptien.png';

const TransactionPage = () => {

    const [dataPayment, setDataPayment] = useState([]);
    const [loading, setLoading] = useState(true);
    const [totalPages, setTotalPages] = useState();
    const [currentPage, setCurrentPage] = useState(1);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    const fetchPayment = async () => {
        setLoading(true);
        try {
            const data = await searchTransaction(currentPage, startDate, endDate);  // Truyền ngày vào đây
            if (data.code === 200 && data.result && data.result.data) {
                setTotalPages(data.result.totalPages);
                setDataPayment(data.result.data);
            } else {
                setDataPayment([]);
            }
        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPayment();
    }, [currentPage]);

    const handlePageClick = (data) => {
        setCurrentPage(data.selected + 1);
    };

    const handleSearch = () => {
        setCurrentPage(1);
        fetchPayment();
    };

    const handleStartDateChange = (e) => {
        setStartDate(e.target.value);
    };

    const handleEndDateChange = (e) => {
        setEndDate(e.target.value);
    };

    if (loading) {
        return <LoadingSpinner />;
    }

    return (
        <div className='transaction-page'>
            <div className="transaction-container">
                <h1 className="transaction-title">Course Transactions</h1>
                <div className="transaction-toolbar">
                    <div className="transaction-date-filter">
                        <input
                            type="date"
                            className="transaction-date-input"
                            placeholder="From Date"
                            value={startDate || ""}
                            onChange={handleStartDateChange}
                        />
                        <input
                            type="date"
                            className="transaction-date-input"
                            placeholder="To Date"
                            value={endDate || ""}
                            onChange={handleEndDateChange}
                        />
                        <button className="transaction-search-button" onClick={handleSearch}>Search</button>
                    </div>
                </div>
                <table className="transaction-table">
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Course</th>
                            <th>Date</th>
                            <th>Amount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {dataPayment.length > 0 ? (
                            dataPayment.map((payment, index) => (
                                <tr key={index}>
                                    <td>
                                        <img
                                            src={payment.thumbnail || deposit}
                                            alt="Course Thumbnail"
                                            className="transaction-course-img"
                                        />
                                    </td>
                                    <td>{payment.title}</td>
                                    <td>{payment.createAt}</td>
                                    <td>
                                        <span className={`payment-points ${payment.points > 80 ? 'high' : payment.points > 40 ? 'medium' : 'low'}`}>
                                            {payment.points !== null ? payment.points.toLocaleString('de-DE') : ""}
                                            <i className="fa fa-coins"></i>
                                        </span>
                                    </td>

                                    <td>
                                        <span className={`transaction-status ${payment.status === 'Completed' ? 'success' : 'pending'}`}>
                                            {payment.status}
                                        </span>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" style={{ textAlign: "center" }}>
                                    No transactions available
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>

                <ReactPaginate
                    previousLabel={'«'}
                    nextLabel={'»'}
                    breakLabel={'...'}
                    pageCount={totalPages}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={3}
                    onPageChange={handlePageClick}
                    forcePage={currentPage - 1}
                    className='paginate-transaction'
                    containerClassName={'transaction-pagination'}
                    pageClassName={'transaction-page-item'}
                    pageLinkClassName={'transaction-page-link'}
                    previousClassName={'transaction-page-item'}
                    previousLinkClassName={'transaction-page-link'}
                    nextClassName={'transaction-page-item'}
                    nextLinkClassName={'transaction-page-link'}
                    breakClassName={'transaction-page-item'}
                    breakLinkClassName={'transaction-page-link'}
                    activeClassName={'active'}
                />
            </div>
        </div>
    );
};

export default TransactionPage;
