import axios from 'axios';
import authService from './authService';

const API_URL = 'http://localhost:8080/api/user/';

const getAuthHeader = () => {
  const user = authService.getCurrentUser();
  if (user && user.token) {
    return { Authorization: 'Bearer ' + user.token };
  }
  return {};
};

const getUserProfile = () => {
  return axios.get(API_URL + 'me', { headers: getAuthHeader() });
};

const userService = {
  getUserProfile,
};

export default userService;
