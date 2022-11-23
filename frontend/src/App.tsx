
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import AuthenticationProvider from "./contexts/authenticationContext";

import Bets from "./view/bets/Bets";
import HistoricoApostas from "./view/historico/HistoricoApostas";
import HistoricoTransicoes from "./view/historicoTransicoes/historicoTransicoes";
import Home from "./view/home/Home";
import Login from "./view/login/Login";
import Profile from "./view/perfil/Perfil";
import Registo from "./view/registo/Registo";


export const router = createBrowserRouter([
  {
    path: "/",
    element: <ProtectedRoute><Home /></ProtectedRoute>,
  },
  {
    path: "/register",
    element: <Registo />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/bets",
    element: <ProtectedRoute><Bets /></ProtectedRoute>,
  },
  {
    path: "/profile",
    element: <ProtectedRoute><Profile /></ProtectedRoute>,
  },
  {
    path: "/historicoApostas",
    element: <ProtectedRoute><HistoricoApostas /></ProtectedRoute>,
  },
  {
    path: "/historicoTransicoes",
    element: <ProtectedRoute><HistoricoTransicoes /></ProtectedRoute>,
  },
]);

function App() {

  return (<AuthenticationProvider> <RouterProvider router={router} /></AuthenticationProvider>);
}

export default App;
