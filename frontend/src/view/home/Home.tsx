import { Outlet } from "react-router-dom";
import Navbar from "../../components/bets/Navbar";

function Home() {
  return (
    <div>
      <Navbar />
      <Outlet />
    </div>
  );
}

export default Home;
