import { useEffect, useState } from "react";
import Modal from 'react-bootstrap/Modal';
import { FcPlus } from "react-icons/fc";
import ModalAddWithdrawal from "./ModalAddWithdrawal";
import "./Teacher.css";
import WithdrawalHistory from "./WithdrawalHistory";
import { getWithdrawal, getWithdrawalWithPaginate } from "../../../service/WithdrawalService";
const Withdrawal = () => {
    const LIMIT = 3;
    const token = localStorage.getItem('token');
    const [showModalAddWithdrawal, setShowModalAddWithdrawal] = useState(false);
    const [listWithdrawals, setListWithdrawals] = useState([]);
    const [pageCount, setPageCount] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);

    useEffect(() => {
        fetchWithdrawalWithPaginate(1);
    }, [token]);

    const getWithdrawalUser = async () => {
        let res = await getWithdrawal();
        setListWithdrawals(res.data.result);
    }

    const fetchWithdrawalWithPaginate = async (page) => {
        let res = await getWithdrawalWithPaginate(page, LIMIT);
        setListWithdrawals(res.data.result.data);
        setPageCount(res.data.result.totalPages);
    }


    return (
        <div className="withdrawal-container">
            <div className="title">
                Withdrawal
            </div>
            <div className="withdrawal-content">
                <div className="btn-add-withdrawl">
                    <button className="btn btn-primary" onClick={() => setShowModalAddWithdrawal(true)}><FcPlus /> Add new withdrawal</button>
                </div>
                <div className="withdrawal-history">
                    <WithdrawalHistory
                        listWithdrawals={listWithdrawals}
                        pageCount={pageCount}
                        fetchWithdrawalWithPaginate={fetchWithdrawalWithPaginate}
                        currentPage={currentPage}
                        setCurrentPage={setCurrentPage}
                    />
                </div>
                <ModalAddWithdrawal
                    show={showModalAddWithdrawal}
                    setShow={setShowModalAddWithdrawal}
                    getWithdrawalUser={getWithdrawalUser}
                    fetchWithdrawalWithPaginate={fetchWithdrawalWithPaginate}
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                />
            </div>

        </div>
    );
}

export default Withdrawal;