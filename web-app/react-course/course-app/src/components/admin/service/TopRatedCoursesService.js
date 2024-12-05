import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/v1/admin/reviews/courses";

// Lấy danh sách khóa học được đánh giá cao
export const getTopRatedCourses = async (month, year, page, ascending) => {
  try {
    console.log("API Params:", { month, year, page, ascending });

    const response = await axios.get(`${API_BASE_URL}/rating`, {
      params: { month, year, page, ascending },
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    });

    return response.data;
  } catch (error) {
    console.error("Error fetching top-rated courses:", error.response || error);
    if (error.response) {
      console.error("API Error Message:", error.response.data);
    }
    throw error;
  }
};
