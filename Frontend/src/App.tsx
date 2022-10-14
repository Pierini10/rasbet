import React from "react";
import { Route, Routes } from "react-router-dom";

function App() {
  return (
      <Routes>
        <Route path="/" element={<div>Hello</div>} />
        <Route path="/l" element={<div>HelloLLLLLLLLll</div>} />
      </Routes>
  );
}

export default App;
