import { UseAuthentication } from "../contexts/authenticationContext";
import { paramsMaker } from "../utils/params";

const authenticationContext = UseAuthentication();
export const fetchdataBody = async (
  url: string,
  method: "GET" | "POST" | "PUT" | "DELETE",
  body?: BodyInit
) => {
  const token = authenticationContext?.token;
  if (token) {
    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer" + token,
      },
      body,
    });
    return response.json();
  }
};

export const fetchdataParams = async (
  url: string,
  method: "GET" | "POST" | "PUT" | "DELETE",
  params: { [key: string]: any }
) => {
  const token = authenticationContext?.token;
  if (token) {
    const response = await fetch(url + paramsMaker(params), {
      method: method,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer" + token,
      },
    });
    return response.json();
  }
};

export const login = async (username: string, password: string) => {
  const response = await fetch("http://localhost:8080/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization:
        "Basic" + Buffer.from(username + ":" + password).toString("base64"),
    },
  });
  const token: { token: string } = await response.json();
  authenticationContext?.setToken(token.token);
};
