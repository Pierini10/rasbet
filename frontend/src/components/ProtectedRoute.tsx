import { useEffect } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { UseAuthentication } from "../contexts/authenticationContext";

const ProtectedRoute = ({ children }: any) => {
  const location = useLocation();
  const { token, saveToken, testToken, setToken, role } = UseAuthentication();
  let ls = localStorage.getItem("token");
  const ADMIN_PATHS = ["/adminregister", "/promotion", "/notification"];
  const SPECIALIST_PATHS = ["/createevent"];

  useEffect(() => {
    if (ls && token === "") {
      saveToken(ls);
      testToken(ls).then((data: boolean) => {
        if (!data) {
          localStorage.removeItem("token");
          setToken("");
        }
      });
    }
  }, [saveToken, ls, testToken, setToken, token]);

  if (token === "" && !ls) {
    return <Navigate to='/login' />;
  } else if (
    ADMIN_PATHS.includes(location.pathname.toLocaleLowerCase()) &&
    role &&
    role !== "ROLE_Administrator"
  ) {
    return <Navigate to='/bets' />;
  } else if (
    SPECIALIST_PATHS.includes(location.pathname.toLocaleLowerCase()) &&
    role &&
    role !== "ROLE_Specialist"
  ) {
    return <Navigate to='/bets' />;
  }

  return children;
};
export default ProtectedRoute;
