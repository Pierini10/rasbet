import { createContext, useContext, useState } from "react";
import { AuthenticationContextype } from "../models/authenticationContext.model";

export const AuthenticationContext = createContext<AuthenticationContextype | null>(null);


export default function AuthenticationProvider({ children }: { children: React.ReactNode }) {

    const [token, setToken] = useState("");

    return (
        <AuthenticationContext.Provider
            value={{
                token,
                setToken,
            }}
        >
            {children}
        </AuthenticationContext.Provider>
    );

}

export function UseAuthentication() {
    return useContext(AuthenticationContext);
}


