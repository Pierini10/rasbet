import React from "react";
import { Route, Routes } from "react-router-dom";
import Registo from "./view/registro/Registo";

function App() {
  return (
    <Routes>
      <Route path="/" element={<div className="bg-orange-200">Hello</div>} />
      <Route path="/l" element={<div>HelloLLLLLLLLll</div>} />
      <Route path="/registo" element={<Registo />} />
    </Routes>
  );
}

export default App;
