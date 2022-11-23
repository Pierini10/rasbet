import { Navigate } from "react-router-dom";
import { UseAuthentication } from "../contexts/authenticationContext";

const ProtectedRoute = ({ children }: any) => {
    const { getToken } = UseAuthentication();
    if (getToken() === "") {
        return <Navigate to="/login" />
    }
    return (children)
}
export default ProtectedRoute;