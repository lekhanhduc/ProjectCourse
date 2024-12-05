import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { addWithdrawal } from "../../../service/WithdrawalService";
import { toast, ToastContainer } from "react-toastify";

const ModalAddWithdrawal = (props) => {

    const { show, setShow } = props;

    const handleClose = () => {
        setShow(false);
        setPoint(0);
        setBank("");
        setBankNumber("");
    }

    const BankEnum = [
        'AGRIBANK',
        'BIDV',
        'LPBANK',
        'MBBANK',
        'SACOMBANK',
        'SCB',
        'TECHCOMBANK',
        'TPBANK',
        'VIETCOMBANK',
        'VIETTINBANK',
    ];

    const [point, setPoint] = useState(0);
    const [bank, setBank] = useState("");
    const [bankNumber, setBankNumber] = useState("");

    const addWithdrawalOfTeacher = async () => {
        const withdrawalData = {
            points: point,
            bank: bank,
            bankNumber: bankNumber
        }

        try {
            const res = await addWithdrawal(withdrawalData);
            if (res.data && res.data.code === 201) {
                toast.success("Add withdrawal successfully");
                props.setCurrentPage(1);
                await props.fetchWithdrawalWithPaginate(1);
            } else {
                toast.error("Failed to add withdrawal");
            }
        } catch (error) {
            toast.error("An error occurred while add the withdrawal");
        } finally {
            handleClose();
        }

    }

    return (
        <>

            <Modal
                show={show}
                onHide={handleClose}
                size="xl"
                backdrop='static'
                className='modal-add-user'
            >
                <Modal.Header closeButton>
                    <Modal.Title>Add withdrawal</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form className="row g-3">
                        <div className="col-md-6">
                            <label className="form-label">Point</label>
                            <input
                                type="number"
                                className="form-control"
                                value={point}
                                onChange={(event) => setPoint(event.target.value)}
                            />
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Bank</label>
                            <select
                                className="form-control"
                                value={bank}
                                onChange={(e) => setBank(e.target.value)}
                            >
                                <option value="">-- Select Bank --</option>
                                {BankEnum.map((bankItem, index) => (
                                    <option key={index} value={bankItem}>
                                        {bankItem}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Bank Number</label>
                            <input
                                type="text"
                                className="form-control"
                                value={bankNumber}
                                onChange={(event) => setBankNumber(event.target.value)}
                            />
                        </div>


                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={() => addWithdrawalOfTeacher()}>
                        Save
                    </Button>
                </Modal.Footer>
            </Modal>
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
        </>
    );
}

export default ModalAddWithdrawal;