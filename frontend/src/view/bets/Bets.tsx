import { useState } from "react";
import Navbar from "../../components/bets/Navbar";
import Searchbar from "../../components/bets/Searchbar";

const Bets = () => {
  const [search, setSearch] = useState("");

  return (
    <div>
      <Navbar />
      <div className="grid grid-cols-3">
        <div className="col-span-2">
          <Searchbar />
          <div>jogos</div>
        </div>
        <div>boletim</div>
      </div>
    </div>
  );
};

export default Bets;
