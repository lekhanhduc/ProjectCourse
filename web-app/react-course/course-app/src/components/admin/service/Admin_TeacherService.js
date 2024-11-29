import axios from "../../../utils/CustomizeAxios";
const API_BASE_URL = "http://localhost:8080/api/v1/admin";

export const getAllTeachers = async (page, size, sort) => {
  try {
    const response = await axios.get(`api/v1/admin/teachers`, {
      params: { page, size, sort },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching teachers:", error);
    throw error;
  }
};

export const searchTeachers = async (page, size, sort, keywords) => {
  try {
    const response = await axios.get(`api/v1/admin/teachers/search`, {
      params: { page, size, sort, keywords },
    });
    return response.data;
  } catch (error) {
    console.error("Error searching teachers:", error);
    throw error;
  }
};

export const removeTeacherRole = async (teacherId) => {
  try {
    const response = await axios.put(
      `api/v1/admin/teachers/${teacherId}/remove-role`,
      null
    );
    return response.data;
  } catch (error) {
    console.error("Error removing teacher role:", error);
    throw error;
  }
};

export const getTeacherApplications = async (page, size) => {
  try {
    const response = await axios.get(`api/v1/admin/teachers/applications`, {
      params: {
        page,
        size,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching teacher applications:", error);
    throw error;
  }
};

export const approveTeacher = async (id) => {
  try {
    const response = await axios.post(`api/v1/save-teacher/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error approving teacher with ID ${id}:`, error);
    throw error;
  }
};

export const rejectTeacher = async (id) => {
  try {
    const response = await axios.post(`api/v1/reject-teacher/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error rejecting teacher with ID ${id}:`, error);
    throw error;
  }
};

// API để từ chối ứng dụng giáo viên
export const rejectTeacherApplication = async (id) => {
  try {
    const response = await axios.post(
      `http://localhost:8080/api/v1/admin/teachers/${id}/reject`,
      null,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error(`Error rejecting teacher application with ID ${id}:`, error);
    throw error;
  }
};

export const getTeacherDetails = async (teacherId) => {
  try {
    const response = await axios.get(
      `${API_BASE_URL}/users/${teacherId}/details`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching teacher details:", error);
    throw error;
  }
};
