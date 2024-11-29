import axios from "../utils/CustomizeAxios";

export const getAdsByCurrentLogin = async (page) => {
  try {
    const response = await axios.get(`api/v1/get-ads-current`, {
      params: {
        page: page,
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(
      `Failed to fetch ads: ${error.response?.status || error.message}`
    );
  }
};

export const registerAds = async (token, formData) => {
  try {
    const response = await axios.post("api/v1/register-ads", formData, {});
    return response.data;
  } catch (error) {
    const errorMessage = error.response?.data?.message || "Fail to registerAds";
    throw new Error(errorMessage);
  }
};

export const getAdsActive = async () => {
  return axios.get(`api/v1/get-ads-active`);
};

export const getAllAds = async (currentPage) => {
  const response = await axios.get(`api/v1/fetch-ads`, {
    params: {
      page: currentPage,
    },
  });
  return response.data;
};

export const approveAds = async (id) => {
  const response = await axios.put(`api/v1/approve-ads`, { id: id });
  return response.data;
};

export const rejectAds = async (id) => {
  const response = await axios.put(`api/v1/reject-ads`, { id: id });
  return response.data;
};

export const deleteAds = async (id) => {
  const response = await axios.delete(`api/v1/delete-ads/${id}`);
  return response.data;
};
