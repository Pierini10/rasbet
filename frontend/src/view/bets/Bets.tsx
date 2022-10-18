import Game from "../../components/bets/Game";
import Navbar from "../../components/bets/Navbar";
import Searchbar from "../../components/bets/Searchbar";

interface Jogo {
  casa: string;
  fora: string;
  data: string;
  oddc: string;
  odde: string;
  oddf: string;
}

const listJogos: Jogo[] = [
  {
    casa: "Benfica",
    fora: "Porto",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
  {
    casa: "casa1",
    fora: "asda",
    data: "454564646",
    oddc: "21",
    odde: "32",
    oddf: "14",
  },
];

const Bets = () => {
  return (
    <div>
      <Navbar />
      <div className="grid grid-cols-3">
        <div className="col-span-2">
          <Searchbar />
          <div>
            <ul className="space-y-6">
              {listJogos.map((g) => (
                <li>
                  <Game
                    casa={g.casa}
                    fora={g.fora}
                    data={g.data}
                    oddc={g.oddc}
                    odde={g.odde}
                    oddf={g.oddf}
                  />
                </li>
              ))}
            </ul>
          </div>
        </div>
        <div className="fixed top-[72px] bottom-[30px] w-[33%] right-6 bg-gray-50 border-[1px] border-gray-500 rounded-3xl">
          <div>Boletim</div>
          <div>
            <span>Simples</span>
            <span>Múltiplas</span>
          </div>
          <div>bets</div>
          <div>
            <span>cota</span>
            <span>montante</span>
          </div>
        </div>
        <div>
          <span>
            <div>Total de ganhos</div>
            <div>ganho</div>
          </span>
          <span>Apostar</span>
        </div>
      </div>
    </div>
  );
};

export default Bets;
