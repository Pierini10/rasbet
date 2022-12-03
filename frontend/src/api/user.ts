import { RegisterData } from "../models/RegisterData.model";
import { paramsMaker } from "../utils/params";

export const register = async (data: RegisterData, token?: String) => {
  try {
    let headers: HeadersInit = {
      Accept: "*/*",
      "Content-Type": "application/json",
    };
    if (token) {
      headers = { ...headers, Authorization: `Bearer ${token}` };
    }
    const response = await fetch(
      "http://localhost:8080/register" + paramsMaker(data),
      {
        headers,

        method: "POST",
      }
    );

    if (response.status === 200) {
      alert("Register successful");
      return true;
    } else {
      const json = await response.json();
      alert(json.message);
    }
  } catch (error) {
    alert(error);
  }
};
