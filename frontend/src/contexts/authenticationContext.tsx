import jwtDecode from "jwt-decode";
import { createContext, useContext, useState } from "react";

import { AuthenticationContextype } from "../models/authenticationContext.model";
import { jwt } from "../models/jwtdecoded.model";
import { paramsMaker } from "../utils/params";
const initialValues = {
    token: "",
    setToken: () => { },
    role: "",
    id: -1,
    setId: () => { },
    setRole: () => { },
    fetchdataAuth: () => { return new Promise((resolve) => { resolve({}) }) },
    saveToken: () => { },
    logout: () => { },

}
export const AuthenticationContext = createContext<AuthenticationContextype>(initialValues);


export default function AuthenticationProvider({ children }: { children: React.ReactNode }) {

    const [token, setToken] = useState(initialValues.token);
    const [role, setRole] = useState(initialValues.role);
    const [id, setId] = useState(initialValues.id);

    const fetchdataAuth = async (
        url: string,
        method: "GET" | "POST" | "PUT" | "DELETE",
        body?: BodyInit,
        params?: { [key: string]: any },

    ) => {
        try {
            if (token && params) {
                const response = await fetch(url + paramsMaker(params), {
                    method: method,
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                    body,
                });
                const data = await response.json();
                return data
            }
        } catch (error) {
            console.log(error)
        }
    };
    const saveToken = (newToken: string) => {
        const jwtdecoded: jwt = jwtDecode(newToken);
        localStorage.setItem("token", newToken);
        setToken(newToken);
        setRole(jwtdecoded.role);
        setId(jwtdecoded.id);

    }


    const logout = () => {

        localStorage.removeItem("token");
        setToken("");
        setRole("");
        setId(-1);

    }
    return (
        <AuthenticationContext.Provider
            value={{
                token,
                role,
                id,
                setId,
                setToken,
                setRole,
                fetchdataAuth,
                saveToken,
                logout


            }}
        >
            {children}
        </AuthenticationContext.Provider>
    );


}

export function UseAuthentication() {

    return useContext(AuthenticationContext);
}


