import axios from '../utils/CustomizeAxios';

const getWithdrawal = () => {
    return axios.get(`api/v1/get-withdrawal`);
}

const addWithdrawal = (withdrawal) => {
    return axios.post(`api/v1/add-withdrawal`, withdrawal);
}

const getWithdrawalWithPaginate = (page, size) => {
    return axios.get(`api/v1/get-withdrawal?page=${page}&size=${size}`);
}

export { getWithdrawal, addWithdrawal, getWithdrawalWithPaginate }