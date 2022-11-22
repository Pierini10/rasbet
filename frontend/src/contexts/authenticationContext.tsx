import { createContext, useContext, useState } from "react";
import { AuthenticationContextype } from "../models/authenticationContext.model";
const initialValues = {
    token: "",
    setToken: () => { },
}
export const AuthenticationContext = createContext<AuthenticationContextype>(initialValues);


export default function AuthenticationProvider({ children }: { children: React.ReactNode }) {

    const [token, setToken] = useState(initialValues.token);

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


