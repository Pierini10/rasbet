import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Bets from "./view/bets/Bets";
import Historico from "./view/historico/Historico";
import Login from "./view/login/Login";
import Profile from "./view/perfil/Perfil";
import Registo from "./view/registro/Registo";

const router = createBrowserRouter([
  {
    path: "/",
    element: <div>Home</div>,
  },
  {
    path: "/registo",
    element: <Registo />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/bets",
    element: <Bets />,
  },
  {
    path: "/profile",
    element: <Profile />,
  },
  {
    path: "/historico",
    element: <Historico />,
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
