import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ProtectedRoute from "./components/ProtectedRoute";
import AuthenticationProvider from "./contexts/authenticationContext";
import AdminRegister from "./view/AdminRegister/AdminRegister";

import Bets from "./view/bets/Bets";
import CreateEvent from "./view/createEvent/CreateEvent";
import HistoricoApostas from "./view/historico/HistoricoApostas";
import HistoricoTransicoes from "./view/historicoTransicoes/HistoricoTransicoes";
import Home from "./view/home/Home";
import Login from "./view/login/Login";
import Notification from "./view/notification/Notification";
import Profile from "./view/perfil/Perfil";
import Promotion from "./view/promotion/Promotion";
import Registo from "./view/registo/Registo";

export const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <ProtectedRoute>
        <Home />
      </ProtectedRoute>
    ),
    children: [
      {
        path: "/bets",
        element: (
          <ProtectedRoute>
            <Bets />
          </ProtectedRoute>
        ),
      },
      {
        path: "/profile",
        element: (
          <ProtectedRoute>
            <Profile />
          </ProtectedRoute>
        ),
      },
      {
        path: "/betHistory",
        element: (
          <ProtectedRoute>
            <HistoricoApostas />
          </ProtectedRoute>
        ),
      },
      {
        path: "/transactionHistory",
        element: (
          <ProtectedRoute>
            <HistoricoTransicoes />
          </ProtectedRoute>
        ),
      },
      {
        path: "/adminregister",
        element: (
          <ProtectedRoute>
            <AdminRegister />
          </ProtectedRoute>
        ),
      },
      {
        path: "/promotion",
        element: (
          <ProtectedRoute>
            <Promotion />
          </ProtectedRoute>
        ),
      },
      {
        path: "/notification",
        element: (
          <ProtectedRoute>
            <Notification />
          </ProtectedRoute>
        ),
      },
      {
        path: "/createevent",
        element: (
          <ProtectedRoute>
            <CreateEvent />
          </ProtectedRoute>
        ),
      },
    ],
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
    path: "*",
    element: <div>This page does not exist</div>,
  },
]);

function App() {
  return (
    <AuthenticationProvider>
      {" "}
      <RouterProvider router={router} />
    </AuthenticationProvider>
  );
}

export default App;
