import { useEffect, useState } from "react";
import Aposta from "../../components/Aposta";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Bet } from "../../models/bets.model";
import { ProfileInfo } from "../../models/profile.model";
import "./HistoricoApostas.css";

function HistoricoApostas() {
  const { fetchdataAuth, id } = UseAuthentication();
  const [bets, setBets] = useState<Bet>();
  const [filteredBets, setFilteredBets] = useState<Bet>();
  const [names, setNames] = useState<string[]>(["", ""]);
  useEffect(() => {
    fetchdataAuth("http://localhost:8080/getBetsHistory", "GET").then(
      (data: Bet) => {
        if (data !== undefined) {
          setBets(data);
          setFilteredBets(data);
        }
      }
    );
    fetchdataAuth("http://localhost:8080/getUser", "GET").then(
      (data: ProfileInfo) => {
        if (data) {
          const name = [data.firstName, data.lastName];
          setNames(name);
        }
      }
    );
  }, [fetchdataAuth, id]);

  const filterBet = (filter: "simple" | "multiple" | "all") => {
    if (bets) {
      const temp = { ...bets };
      if (filter === "simple") {
        temp.bets = temp.bets.filter((bet) => bet.predictions.length === 1);
        setFilteredBets(temp);
      } else if (filter === "multiple") {
        temp.bets = temp.bets.filter((bet) => bet.predictions.length > 1);
        setFilteredBets(temp);
      } else {
        setFilteredBets(bets);
      }
    }
  };

  return (
    <div className='grid h-[90vh] bg-gray-400 place-items-center '>
      <div className=' max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border'>
        <div className='flex justify-center mt-5 text-2xl'>
          {names[0]} {names[1]}
        </div>
        <div className='flex justify-center mt-5 font-bold'>Bet History</div>
        {bets?.winPercentage !== undefined && <div className="flex justify-center">Win percentage: {bets?.winPercentage.toString()}%</div>}
        <div className='flex justify-center mt-5'>
          <hr className='bg-gray-500 w-[90%]' />
        </div>
        <div className='flex justify-center mt-2 space-x-2'>
          <button
            className='py-1 text-orange-500 duration-150 ease-in border border-orange-500 rounded-md w-36 hover:bg-orange-500 hover:text-white'
            onClick={() => filterBet("simple")}
          >
            {" "}
            Simple
          </button>
          <button
            className='py-1 text-white duration-150 ease-in bg-blue-500 rounded-md w-36 hover:bg-blue-800 '
            onClick={() => filterBet("all")}
          >
            {" "}
            All
          </button>
          <button
            className='py-1 text-white ease-in bg-orange-500 rounded-md w-36 hover:bg-orange-800'
            onClick={() => filterBet("multiple")}
          >
            {" "}
            Multiple
          </button>
        </div>
        <div className='flex justify-end mt-5 mr-2 text-xl'>
          <a
            className='flex justify-center w-8 ml-2 transition border border-black rounded-full hover:bg-orange-500'
            href='/profile'
          >
            {" "}
            {">"}{" "}
          </a>
        </div>
        <div className='flex flex-col overflow-auto h-96 '>
          {filteredBets?.bets.map((a, index) => (
            <Aposta key={index} {...a} />
          ))}
          {!filteredBets && (
            <div className='flex items-center justify-center h-full py-5 text-3xl'>
              There is no bets history for this account.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default HistoricoApostas;
