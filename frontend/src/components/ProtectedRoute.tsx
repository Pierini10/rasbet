import { useEffect } from "react";
import { Navigate } from "react-router-dom";
import { UseAuthentication } from "../contexts/authenticationContext";


const ProtectedRoute = ({ children }: any) => {


    const { token, saveToken } = UseAuthentication();
    const ls = localStorage.getItem("token")

    useEffect(() => {

        if (ls) {
            saveToken(ls)
        }
    }, [saveToken, ls]);

    if (ls === null && token === "") {
        return <Navigate to="/login" />
    }



    return (children)
}
export default ProtectedRoute;