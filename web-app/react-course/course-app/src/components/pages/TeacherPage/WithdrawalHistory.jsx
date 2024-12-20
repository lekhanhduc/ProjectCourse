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
                    </tr>
                </thead>
                <tbody>
                    {listWithdrawals && listWithdrawals.length > 0 &&
                        listWithdrawals.map((item, index) => {
                            return (
                                <tr key={`table-users-${index}`}>
                                    <th>{item.name}</th>
                                    <td>{item.points.toLocaleString('de-DE')}</td>
                                    <td>{item.money.toLocaleString('de-DE')} VND</td>
                                    <td>{item.bank}</td>
                                    <td>{item.bankNumber}</td>
                                    <td>
                                        {item.status === "PROCESSING" && (
                                            <div
                                                className={`status-box status-${item.status.toLowerCase()}`}
                                                style={{
                                                    backgroundColor: "#007FFF",
                                                    textAlign: "center",
                                                    borderRadius: "5px",
                                                    color: "white"
                                                }}>
                                                {item.status}
                                            </div>
                                        )}

                                        {item.status === "COMPLETED" && (
                                            <div
                                                className={`status-box status-${item.status.toLowerCase()}`}
                                                style={{
                                                    backgroundColor: "#00C853",
                                                    textAlign: "center",
                                                    borderRadius: "5px",
                                                    color: "white"
                                                }}>
                                                {item.status}
                                            </div>
                                        )}

                                        {item.status === "CANCELLED" &&
                                            <div
                                                className={`status-box status-${item.status.toLowerCase()}`}
                                                style={{
                                                    backgroundColor: "#FF1744",
                                                    textAlign: "center",
                                                    borderRadius: "5px",
                                                    color: "white"
                                                }}>
                                                {item.status}
                                            </div>
                                        }

                                    </td>

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