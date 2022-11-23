import { RegisterData } from "../models/RegisterData.model";

export const register = async (data: RegisterData) => {
  try {
    const response = await fetch("http://localhost:8080/register", {
      headers: {
        Accept: "*/*",
        "Content-Type": "application/json",
      },

      method: "POST",
      body: JSON.stringify(data),
    });

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
