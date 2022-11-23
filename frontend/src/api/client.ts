import { Buffer } from "buffer";

export const login = async (username: string, password: string) => {
  const base64login = Buffer.from(username + ":" + password).toString("base64");

  const response = await fetch("http://localhost:8080/login", {
    method: "POST",
    headers: {
      Accept: "*/*",
      Authorization: "Basic " + base64login,
    },
  });

  const token: string = await response.text();
  return token;
};
