import { Outlet } from "react-router-dom";
import Navbar from "../../components/bets/Navbar";
import { UseAuthentication } from "../../contexts/authenticationContext";

function Home() {
  const auth = UseAuthentication();
  return (
    <div>
      <Navbar />
      <Outlet />
      <button onClick={auth.logout}>logout</button>
    </div>
  );
}

export default Home;
