import Axios, { AxiosRequestConfig } from "axios";

function authRequestInterceptor(config: AxiosRequestConfig) {
  return config;
}

export const axios = Axios.create({
  baseURL: process.env.REACT_API_BASE_URL,
});

axios.defaults.headers.post["Content-Type"] = "application/json";
axios.interceptors.request.use(authRequestInterceptor);
