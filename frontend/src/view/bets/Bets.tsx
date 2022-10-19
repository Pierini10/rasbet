import { useState } from "react";
import Boletim from "../../components/bets/Boletim";
import Game from "../../components/bets/Game";
import Navbar from "../../components/bets/Navbar";
import Searchbar from "../../components/bets/Searchbar";

interface Jogo {
  id: string;
  casa: string;
  fora: string;
  data: string;
  oddc: string;
  odde: string;
  oddf: string;
}

const listJogos: Jogo[] = [
  {
    id: "1",
    casa: "Benfica",
    fora: "Porto",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "2",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "3",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "4",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "5",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "6",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "7",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "8",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "9",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "10",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "11",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "12",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    id: "13",
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
];

const Bets = () => {
  const [betType, setBetType] = useState(true);
  const [bets, setBets] = useState<string[][]>([]);

  const changeBetType = (bt: boolean) => {
    setBetType(bt);
  };

  const changeBets = (id: string, bet: string) => {
    const newBets = bets.map((i) => i);
    const index = newBets.findIndex((i) => i[0] === id);

    if (index === -1) {
      newBets.push([id, bet]);
    } else {
      if (newBets[index][1] === bet) newBets.splice(index, 1);
      else newBets[index][1] = bet;
    }

    setBets(newBets);
  };

  const checkBet = (id: string, bet: string) => {
    const index = bets.findIndex((i) => i[0] === id);

    if (index === -1) return false;

    if (!(bets[index][1] === bet)) return false;

    return true;
  };

  return (
    <div>
      <Navbar />
      <div className="grid grid-cols-3">
        <div className="col-span-2">
          <Searchbar />
          <div>
            <ul className="space-y-6">
              {listJogos.map((g) => (
                <li id={g.id}>
                  <Game
                    id={g.id}
                    casa={g.casa}
                    fora={g.fora}
                    data={g.data}
                    oddc={g.oddc}
                    odde={g.odde}
                    oddf={g.oddf}
                    changeCallback={changeBets}
                    checkCallback={checkBet}
                  />
                </li>
              ))}
            </ul>
          </div>
        </div>
        <Boletim betType={betType} btCallback={changeBetType} />
      </div>
    </div>
  );
};

export default Bets;
