import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/admin/revenue/user";

// Hàm gọi API cho Monthly
export const getMonthlyData = async (month, year, ascending = true) => {
  try {
    const response = await axios.get(`${API_URL}/monthly`, {
      params: {
        month,
        year,
        ascending,
      },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching monthly data", error);
    throw error;
  }
};

// Hàm gọi API cho Yearly
export const getYearlyData = async (year, ascending = true) => {
  try {
    const response = await axios.get(`${API_URL}/yearly`, {
      params: {
        year,
        ascending,
      },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching yearly data", error);
    throw error;
  }
};
