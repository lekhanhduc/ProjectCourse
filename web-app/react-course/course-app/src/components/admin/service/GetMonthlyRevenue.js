import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1/admin/revenue/teacher";

// Gọi API cho doanh thu theo tháng
export const getMonthlyRevenue = async (month, year, ascending) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/monthly`, {
      params: { month, year, ascending },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data; // Trả về dữ liệu
  } catch (error) {
    console.error("Error fetching monthly revenue:", error);
    throw error; // Ném lỗi nếu có
  }
};

// Gọi API cho doanh thu theo năm
export const getYearlyRevenue = async (year, ascending) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/yearly`, {
      params: { year, ascending },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data; // Trả về dữ liệu
  } catch (error) {
    console.error("Error fetching yearly revenue:", error);
    throw error; // Ném lỗi nếu có
  }
};
