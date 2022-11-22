import { Buffer } from "buffer";

import { paramsMaker } from "../utils/params";

export const fetchdataBody = async (
  url: string,
  method: "GET" | "POST" | "PUT" | "DELETE",
  body?: BodyInit,
  token?: string
) => {
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
  params: { [key: string]: any },
  token?: string
) => {
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
  const base64login = Buffer.from(username + ":" + password).toString("base64");

  const response = await fetch("http://localhost:8080/login", {
    method: "POST",
    headers: {
      Accept: "*/*",
      "User-Agent": "Thunder Client (https://www.thunderclient.com)",
      Authorization: "Basic " + base64login,
    },
  });

  const token: string = await response.text();

  return token;
};
