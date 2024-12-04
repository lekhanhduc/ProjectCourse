import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1/admin/reviews/courses";

// Lấy danh sách khóa học được đánh giá cao
export const getTopRatedCourses = async (
  month,
  year,
  page,
  size,
  ascending
) => {
  try {
    // Log tất cả các tham số để kiểm tra
    console.log("API Params:", { month, year, page, size, ascending });

    const response = await axios.get(`${API_BASE_URL}/rating`, {
      params: { month, year, page, size, ascending },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    console.log("API Response:", response.data); // Log dữ liệu trả về từ API
    return response.data;
  } catch (error) {
    // Log chi tiết lỗi khi xảy ra
    console.error("Error fetching top-rated courses:", error.response || error);
    // Nếu có dữ liệu trả về từ API, ghi lại thông báo lỗi cụ thể
    if (error.response) {
      console.error("API Error Message:", error.response.data);
    }
    throw error; // Ném lỗi nếu có
  }
};
