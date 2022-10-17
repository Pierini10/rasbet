import React from "react";
import { Route, Routes } from "react-router-dom";
import Historico from "./view/historico/Historico";
import Login from "./view/login/Login";
import Profile from "./view/perfil/Perfil";
import Registo from "./view/registro/Registo";

function App() {
  return (
    <Routes>
      <Route path="/" element={<div className="bg-orange-200">Hello</div>} />
      <Route path="/l" element={<div>HelloLLLLLLLLll</div>} />
      <Route path="/registo" element={<Registo />} />
      <Route path="/login" element={<Login />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/historico" element={<Historico />} />
    </Routes>
  );
}

export default App;
