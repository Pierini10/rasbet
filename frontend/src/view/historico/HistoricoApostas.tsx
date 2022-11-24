import { useEffect, useState } from "react";
import Aposta from "../../components/Aposta";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Bet } from "../../models/bets.model";
import "./HistoricoApostas.css";
const dummy_Date = {
  nome: "João",
  apelido: "Silva",
  apostas: [
    {
      games: [
        {
          home: "Sporting",
          away: "varzim",
          bet: "Sporting",
        },
        {
          home: "Palmeiras",
          away: "São Paulo",
          bet: "draw",
        },
        {
          home: "Sporting",
          away: "varzim",
          bet: "Sporting",
        },
        {
          home: "Sporting",
          away: "varzim",
          bet: "Sporting",
        },
      ],
      bet: 100,
      gain: 200,
    },
    {
      games: [
        {
          home: "Sporting",
          away: "varzim",
          bet: "Sporting",
        },
        {
          home: "Palmeiras",
          away: "São Paulo",
          bet: "draw",
        },
      ],
      bet: 100,
      gain: 200,
    },
    {
      games: [
        {
          home: "Sporting",
          away: "varzim",
          bet: "Sporting",
        },
        {
          home: "Palmeiras",
          away: "São Paulo",
          bet: "draw",
        },
      ],
      bet: 100,
      gain: 200,
    },
  ],
};

function HistoricoApostas() {
  const { fetchdataAuth, id } = UseAuthentication()
  const [bets, setBets] = useState<Bet>()
  const [filteredBets, setFilteredBets] = useState<Bet>()

  useEffect(() => {
    fetchdataAuth("http://localhost:8080/getBetsHistory", "GET", undefined, { userID: 6 }).then(
      (data: Bet) => {
        console.log(data)
        setBets(data);
        setFilteredBets(data)
      }
    )

  }, [fetchdataAuth, id]);

  const filterBet = (filter: "simple" | "multiple" | "all") => {
    if (bets) {
      const temp = { ...bets }
      if (filter === "simple") {
        temp.bets = temp.bets.filter((bet) => bet.predictions.length === 1)
        setFilteredBets(temp)
      } else if (filter === "multiple") {
        temp.bets = temp.bets.filter((bet) => bet.predictions.length > 1)
        setFilteredBets(temp)
      }
      else {
        setFilteredBets(bets)
      }
    }
  }

  return (
    <div className="grid h-screen bg-gray-400 place-items-center ">
      <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">
        <div className="flex justify-center mt-5 text-2xl">
          {dummy_Date.nome} {dummy_Date.apelido}
        </div>
        <div className="flex justify-center mt-5 font-bold">
          Histórico De Apostas
        </div>
        <div className="flex justify-center mt-5">
          <hr className="bg-gray-500 w-[90%]" />
        </div>
        <div className="flex justify-center mt-2 space-x-2">
          <button className="py-1 text-orange-500 duration-150 ease-in border border-orange-500 rounded-md w-36 hover:bg-orange-500 hover:text-white" onClick={() => filterBet("simple")}>
            {" "}
            Simples
          </button>
          <button className="py-1 text-white duration-150 ease-in bg-blue-500 rounded-md w-36 hover:bg-blue-800 " onClick={() => filterBet("all")}>
            {" "}
            Todas
          </button>
          <button className="py-1 text-white ease-in bg-orange-500 rounded-md w-36 hover:bg-orange-800" onClick={() => filterBet("multiple")}>
            {" "}
            Múltiplas
          </button>
        </div>
        <div className="flex justify-end mt-5 mr-2 text-xl">
          <button className="w-8 ml-2 transition border border-black rounded-full hover:bg-orange-500">
            {" "}
            {">"}{" "}
          </button>
        </div>
        <div className="flex flex-col overflow-auto h-96 ">
          {filteredBets?.bets.map((a, index) => (
            <Aposta key={index} {...a} />
          ))}
        </div>
      </div>
    </div>
  );
}

export default HistoricoApostas;
