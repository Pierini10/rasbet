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
    getToken: () => { return "" },
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
    const saveToken = (newToken: string) => {
        const jwtdecoded: jwt = jwtDecode(newToken);
        localStorage.setItem("token", newToken);
        setToken(token);
        setRole(jwtdecoded.role);
        setId(jwtdecoded.id);
    }

    const getToken = () => {
        const localtoken = localStorage.getItem("token");
        if (token === "" && localtoken !== null) {

            saveToken(localtoken);

        }
        return token;
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
                getToken,

            }}
        >
            {children}
        </AuthenticationContext.Provider>
    );


}

export function UseAuthentication() {
    return useContext(AuthenticationContext);
}


