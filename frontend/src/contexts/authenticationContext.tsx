import { createContext, useContext, useState } from "react";
import { AuthenticationContextype } from "../models/authenticationContext.model";
import { paramsMaker } from "../utils/params";
const initialValues = {
    token: "",
    setToken: () => { },
    role: "",
    setRole: () => { },
    fetchdataParams: () => { return new Promise((resolve) => { resolve({}) }) },
    fetchdataBody: () => { return new Promise((resolve) => { resolve({}) }) },
    fetchdataBoth: () => { return new Promise((resolve) => { resolve({}) }) },
}
export const AuthenticationContext = createContext<AuthenticationContextype>(initialValues);


export default function AuthenticationProvider({ children }: { children: React.ReactNode }) {

    const [token, setToken] = useState(initialValues.token);
    const [role, setRole] = useState(initialValues.role);

    const fetchdataParams = async (
        url: string,
        method: "GET" | "POST" | "PUT" | "DELETE",
        params: { [key: string]: any },

    ) => {
        if (token) {
            const response = await fetch(url + paramsMaker(params), {
                method: method,
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
            });
            const data = await response.json();
            return data;
        }
    };

    const fetchdataBody = async (
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
                    Authorization: "Bearer " + token,
                },
                body,
            });
            return response.json();
        }
    };

    const fetchdataBoth = async (
        url: string,
        method: "GET" | "POST" | "PUT" | "DELETE",
        body?: BodyInit,
        params?: { [key: string]: any },
        token?: string
    ) => {
        if (token && params) {
            const response = await fetch(url + paramsMaker(params), {
                method: method,
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + token,
                },
                body,
            });
            return response.json();
        }
    };

    return (
        <AuthenticationContext.Provider
            value={{
                token,
                role,
                setToken,
                setRole,
                fetchdataParams,
                fetchdataBody,
                fetchdataBoth
            }}
        >
            {children}
        </AuthenticationContext.Provider>
    );


}

export function UseAuthentication() {
    return useContext(AuthenticationContext);
}


