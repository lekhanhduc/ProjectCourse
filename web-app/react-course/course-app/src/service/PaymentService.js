import axios from "../utils/CustomizeAxios";

export const fetchTransactionByUserLogin = async (page) => {
  const response = await axios.get(`api/v1/payment/transaction/user-current`, {
    params: {
      page: page,
    },
  });
  return response.data;
};

export const searchTransaction = async (page, startDate, endDate) => {
  const response = await axios.get(`api/v1/payment/transaction/search`, {
    params: {
      page: page,
      startDate: startDate ? startDate : "",
      endDate: endDate ? endDate : "",
    },
  });
  return response.data;
};
