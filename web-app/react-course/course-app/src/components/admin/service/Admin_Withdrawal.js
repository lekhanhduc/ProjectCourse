import axios from '../../../utils/CustomizeAxios';

const getAllWithdrawal = () => {
    return axios.get(`api/v1/get-all-withdrawal`);
}

const getAllWithdrawalWithPaginate = async (page, size, sort) => {
    try {
        const response = await axios.get(
            `api/v1/get-all-withdrawal`,
            {
                params: {
                    page,
                    size,
                    sort,
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching withdrawal:", error);
        throw error;
    }
};

const confirmWithdrawal = (id) => {
    return axios.post(`api/v1/accept-withdrawal/${id}`);
}

const cancelWithdrawal = (id) => {
    return axios.post(`api/v1/cancel-withdrawal/${id}`);
}

const getCancelledWithdrawal = async (page, size, sort) => {
    try {
        const response = await axios.get(
            `api/v1/get-cancelled-withdrawal`,
            {
                params: {
                    page,
                    size,
                    sort,
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching withdrawal:", error);
        throw error;
    }
}

const getProcessingWithdrawal = async (page, size, sort) => {

    try {
        const response = await axios.get(
            `api/v1/get-processing-withdrawal`,
            {
                params: {
                    page,
                    size,
                    sort,
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching withdrawal:", error);
        throw error;
    }
}

const getCompletedWithdrawal = async (page, size, sort) => {

    try {
        const response = await axios.get(
            `api/v1/get-completed-withdrawal`,
            {
                params: {
                    page,
                    size,
                    sort,
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching withdrawal:", error);
        throw error;
    }
}

export { getAllWithdrawal, confirmWithdrawal, cancelWithdrawal, getAllWithdrawalWithPaginate, getCancelledWithdrawal, getProcessingWithdrawal, getCompletedWithdrawal };