import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1/admin/revenue";

// Gọi API lấy dữ liệu doanh thu và số lượng khóa học bán theo năm
export const getRevenueData = async (year) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/monthly`, {
      params: { year },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });
    return response.data; // trả về dữ liệu từ API
  } catch (error) {
    console.error("Error fetching revenue data:", error);
    throw error;
  }
};
