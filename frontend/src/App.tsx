import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Bets from "./view/bets/Bets";
import Login from "./view/login/Login";
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
    path: "bets",
    element: <Bets />,
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
