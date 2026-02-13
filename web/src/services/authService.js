import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

const register = (fullName, email, password) => {
  return axios.post(API_URL + 'register', {
    fullName,
    email,
    password,
  });
};

const login = async (emailOrUsername, password) => {
  const response = await axios.post(API_URL + 'login', {
    emailOrUsername,
    password,
  });
  if (response.data.token) {
    localStorage.setItem('user', JSON.stringify(response.data));
  }
  return response.data;
};

const logout = () => {
  localStorage.removeItem('user');
  return axios.post(API_URL + 'logout');
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};

const authService = {
  register,
  login,
  logout,
  getCurrentUser,
};

export default authService;
