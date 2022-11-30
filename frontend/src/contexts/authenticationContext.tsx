import jwtDecode from "jwt-decode";
import { createContext, useContext, useState } from "react";

import { AuthenticationContextype } from "../models/authenticationContext.model";
import { jwt } from "../models/jwtdecoded.model";
import { paramsMaker } from "../utils/params";
const initialValues = {
  token: "",
  role: "",
  id: -1,
  balance: 0,
  setToken: () => {},
  setId: () => {},
  setRole: () => {},
  setBalance: () => {},
  fetchdataAuth: () => {
    return new Promise((resolve) => {
      resolve({});
    });
  },
  testToken: () => {
    return new Promise((resolve) => {
      resolve({});
    });
  },
  saveToken: () => {},
  logout: () => {},
  isAdministrator: () => true,
  isSpecialist: () => true,
  isNormal: () => true,
};
export const AuthenticationContext =
  createContext<AuthenticationContextype>(initialValues);

export default function AuthenticationProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [token, setToken] = useState(initialValues.token);
  const [role, setRole] = useState(initialValues.role);
  const [id, setId] = useState(initialValues.id);
  const [balance, setBalance] = useState(initialValues.balance);
  const fetchdataAuth = async (
    url: string,
    method: "GET" | "POST" | "PUT" | "DELETE",
    body?: BodyInit,
    params?: { [key: string]: any }
  ) => {
    try {
      if (token) {
        const response = await fetch(
          url + (params ? paramsMaker(params) : ""),
          {
            method: method,
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
            body,
          }
        );
        const isJson = response.headers
          .get("content-type")
          ?.includes("application/json");
        const data = isJson ? await response.json() : undefined;
        if (response.status === 200) {
          if (data) {
            return data;
          } else return true;
        } else {
          alert(data.message);
        }
      }
    } catch (error) {
      console.log(error);
    }
  };

  const testToken = async (tk: string) => {
    try {
      const response = await fetch("http://localhost:8080/validToken", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + tk,
        },
      });
      if (response.status === 200) {
        return true;
      } else {
        return false;
      }
    } catch (error) {
      return false;
    }
  };
  const saveToken = (newToken: string) => {
    const jwtdecoded: jwt = jwtDecode(newToken);
    localStorage.setItem("token", newToken);
    setToken(newToken);
    setRole(jwtdecoded.role);
    setId(jwtdecoded.id);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken("");
    setRole("");
    setId(-1);
  };

  const isNormal = () => {
    return role === "ROLE_Normal";
  };

  const isSpecialist = () => {
    return role === "ROLE_Specialist";
  };

  const isAdministrator = () => {
    return role === "ROLE_Administrator";
  };

  return (
    <AuthenticationContext.Provider
      value={{
        token,
        role,
        id,
        balance,
        setId,
        setToken,
        setRole,
        setBalance,
        fetchdataAuth,
        testToken,
        saveToken,
        logout,
        isAdministrator,
        isSpecialist,
        isNormal,
      }}
    >
      {children}
    </AuthenticationContext.Provider>
  );
}

export function UseAuthentication() {
  return useContext(AuthenticationContext);
}
