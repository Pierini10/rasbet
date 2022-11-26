import { useEffect } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { UseAuthentication } from "../contexts/authenticationContext";


const ProtectedRoute = ({ children }: any) => {

    const location = useLocation();
    const { token, saveToken, testToken, setToken, role } = UseAuthentication();
    let ls = localStorage.getItem("token")
    const ADMIN_PATHS = ["/AdminRegister"]
    useEffect(() => {

        if (ls) {
            saveToken(ls)
            testToken(ls).then(
                (data) => {
                    if (typeof data === "string") {
                        localStorage.removeItem("token");
                        setToken("");
                    }
                }
            );
        }
    }, [saveToken, ls, testToken, setToken]);

    if (token === "" && !ls) {
        return <Navigate to="/login" />
    } else if (ADMIN_PATHS.includes(location.pathname) && role && role !== "ROLE_Administrator") {
        return <Navigate to="/" />
    }




    return (children)
}
export default ProtectedRoute;