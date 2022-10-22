import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Bets from "./view/bets/Bets";
import HistoricoApostas from "./view/historico/HistoricoApostas";
import HistoricoTransicoes from "./view/historicoTransicoes/historicoTransicoes";
import Login from "./view/login/Login";
import Profile from "./view/perfil/Perfil";
import Registo from "./view/registo/Registo";

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
    path: "/historicoApostas",
    element: <HistoricoApostas />,
  },
  {
    path: "/historicoTransicoes",
    element: <HistoricoTransicoes />,
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
