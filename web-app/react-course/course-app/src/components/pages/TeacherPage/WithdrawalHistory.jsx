import ReactPaginate from "react-paginate";
import "./Teacher.css";

const WithdrawalHistory = (props) => {

    const { listWithdrawals, pageCount } = props;

    const handlePageClick = (event) => {
        props.fetchWithdrawalWithPaginate(+event.selected + 1);
        props.setCurrentPage(+event.selected + 1);
    };

    return (
        <>
            <table className="table table-hover table-bordered">
                <thead>
                    <tr>
                        <th scope="col">Name</th>
                        <th scope="col">Point</th>
                        <th scope="col">Money</th>
                        <th scope="col">Bank</th>
                        <th scope="col">Bank Number</th>
                        <th scope="col">Status</th>
                        {/* <th>Action</th> */}
                    </tr>
                </thead>
                <tbody>
                    {listWithdrawals && listWithdrawals.length > 0 &&
                        listWithdrawals.map((item, index) => {
                            return (
                                <tr key={`table-users-${index}`}>
                                    <th>{item.name}</th>
                                    <td>{item.points}</td>
                                    <td>{item.money} VND</td>
                                    <td>{item.bank}</td>
                                    <td>{item.bankNumber}</td>
                                    <td>{item.status}</td>
                                    {/* <td>
                                        <button
                                            className="btn btn-secondary"
                                        >
                                            View
                                        </button>
                                        <button
                                            className="btn btn-warning mx-3"
                                        >
                                            Update
                                        </button>
                                        <button
                                            className="btn btn-danger"

                                        >
                                            Delete
                                        </button>
                                    </td> */}
                                </tr>
                            )
                        })
                    }
                    {listWithdrawals && listWithdrawals.length === 0 &&
                        <tr>
                            <td colSpan={4}>Not found data</td>
                        </tr>
                    }


                </tbody>
            </table>
            <div className="withdrawal-pagination">
                <ReactPaginate
                    nextLabel="Next >"
                    onPageChange={handlePageClick}
                    pageRangeDisplayed={3}
                    marginPagesDisplayed={2}
                    pageCount={pageCount}
                    previousLabel="< Prev"
                    pageClassName="page-item"
                    pageLinkClassName="page-link"
                    previousClassName="page-item"
                    previousLinkClassName="page-link"
                    nextClassName="page-item"
                    nextLinkClassName="page-link"
                    breakLabel="..."
                    breakClassName="page-item"
                    breakLinkClassName="page-link"
                    containerClassName="pagination"
                    activeClassName="active"
                    renderOnZeroPageCount={null}
                    forcePage={props.currentPage - 1}
                />
            </div>
        </>
    )
}

export default WithdrawalHistory;