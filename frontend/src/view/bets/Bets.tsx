import { useEffect, useState } from "react";
import Boletim from "../../components/bets/Boletim";
import EventBlock from "../../components/bets/EventBlock";
import Pagamento from "../../components/bets/Pagamento";
import Progress from "../../components/bets/Progress";
import SelectInput from "../../components/bets/SelectInput";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Event, jsonToEvents } from "../../models/event.model";

interface Bet {
  id: string;
  home: string;
  away: string;
  bet: string;
  odd: number;
}

interface SportInfo {
  total: number;
  red: number;
  yellow: number;
  green: number;
}

const Bets = () => {
  const { fetchdataAuth, id, isNormal, isSpecialist } = UseAuthentication();
  const [events, setEvents] = useState<Event[]>([]);
  const [sports, setSports] = useState<string[]>([]);
  const [competitions, setCompetitions] = useState<string[]>([]);
  const [sport, setSport] = useState("");
  const [competition, setCompetition] = useState("");
  const [betType, setBetType] = useState(true);
  const [bets, setBets] = useState<Bet[]>([]);
  const [total, setTotal] = useState("");
  const [totalGain, setTotalGain] = useState(0);
  const [cota, setCota] = useState(0);
  const [showPayment, setShowPayment] = useState(false);

  useEffect(() => {
    fetchdataAuth("http://localhost:8080/getAllSports", "GET").then(
      (data: string[]) => {
        if (data !== undefined) {
          setSports(data);
          setSport(data[0]);
        }
      }
    );
  }, [fetchdataAuth, id]);

  useEffect(() => {
    if (sport !== "")
      fetchdataAuth("http://localhost:8080/getEvents", "GET", undefined, {
        sport: sport,
      }).then((data: any[]) => {
        if (data !== undefined) {
          const newEvents = jsonToEvents(data);

          setEvents(newEvents);

          changeCompetitions(newEvents);
        }
      });
  }, [fetchdataAuth, id, sport]);

  const changeBetType = (bt: boolean) => {
    setBetType(bt);

    if (bt) {
      setBets([]);
      setCota(0);
      setTotalGain(0);
    }
  };

  const changeBets = (id: string, bet: string) => {
    let newBets = [];

    if (betType) {
      const indexEvent = events.findIndex((i) => i.id === id);

      if (
        bets.length !== 1 ||
        (bets[0].id === id && bets[0].bet !== bet) ||
        bets[0].id !== id
      ) {
        const [home, away] = events[indexEvent].description.split(" v ");
        newBets.push({
          id: events[indexEvent].id,
          home: home,
          away: away,
          bet: bet,
          odd:
            bet === "empate"
              ? events[indexEvent].odds.get("Draw")!.odd
              : bet === "casa"
              ? events[indexEvent].odds.get(home)!.odd
              : events[indexEvent].odds.get(away)!.odd,
        });
      }
    } else {
      newBets = bets.map((i) => i);
      const index = newBets.findIndex((i) => i.id === id);

      if (index === -1) {
        const indexEvent = events.findIndex((i) => i.id === id);
        const [home, away] = events[indexEvent].description.split(" v ");

        newBets.push({
          id: events[indexEvent].id,
          home: home,
          away: away,
          bet: bet,
          odd:
            bet === "empate"
              ? events[indexEvent].odds.get("Draw")!.odd
              : bet === "casa"
              ? events[indexEvent].odds.get(home)!.odd
              : events[indexEvent].odds.get(away)!.odd,
        });
      } else {
        if (newBets[index].bet === bet) newBets.splice(index, 1);
        else {
          const indexGame = events.findIndex((i) => i.id === id);

          newBets[index].bet = bet;
          newBets[index].odd =
            bet === "empate"
              ? events[indexGame].odds.get("Draw")!.odd
              : bet === "casa"
              ? events[indexGame].odds.get(newBets[index].home)!.odd
              : events[indexGame].odds.get(newBets[index].away)!.odd;
        }
      }
    }
    setBets(newBets);
    changeCota(newBets);
  };

  const checkBet = (id: string, bet: string) => {
    const index = bets.findIndex((i) => i.id === id);

    if (index === -1) return false;

    if (!(bets[index].bet === bet)) return false;

    return true;
  };

  const changeTotal = (e: React.ChangeEvent<HTMLInputElement>) => {
    const re = /^[0-9]+(\.[0-9]*)?$/;
    if (e.target.value === "" || re.test(e.target.value)) {
      setTotal(e.target.value);
      changeTotalGain(Number(e.target.value), cota);
    }
  };

  const changeTotalGain = (total: number, cota: number) => {
    setTotalGain(total * cota);
  };

  const changeCota = (bets: Bet[]) => {
    let cota = 1;
    bets.forEach((b) => (cota *= b.odd));

    setCota(cota);
    changeTotalGain(Number(total), cota);
  };

  const changeShowPayment = (show: boolean) => {
    setShowPayment(show);
  };

  const changeCompetitions = (events: Event[]) => {
    const competitionsSet: Set<string> = new Set();
    const newCompetitions = ["Todas"];

    events.forEach((event) => competitionsSet.add(event.competition));

    newCompetitions.push(...Array.from(competitionsSet));

    setCompetitions(newCompetitions);
    setCompetition(newCompetitions[0]);
  };

  const handleChangeSport = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSport(event.target.value);
    setBets([]);
    setCota(0);
    setTotalGain(0);
  };

  const handleChangeCompetition = (
    event: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setCompetition(event.target.value);
  };
  return (
    <div>
      {showPayment ? (
        <div className='fixed w-full h-full bg-black bg-opacity-60 grid grid-cols-3 items-center'>
          <Pagamento />
        </div>
      ) : (
        ""
      )}
      <div
        className={"grid ".concat(!isNormal() ? "grid-cols-3" : "grid-cols-4")}
      >
        <div className={isNormal() ? "col-span-2" : "col-span-3"}>
          <div className='fixed w-full bg-white'>
            <div className='w-[66%] flex justify-center space-x-10 h-16 items-center'>
              <SelectInput
                handleChange={handleChangeSport}
                label='Desporto: '
                listValues={sports}
                value={sport}
              />
              <SelectInput
                handleChange={handleChangeCompetition}
                label='Competição: '
                listValues={competitions}
                value={competition}
              />
            </div>
          </div>
        </div>
        <div
          className={"pt-16 ".concat(!isNormal() ? "col-span-2" : "col-span-3")}
        >
          <ul className='space-y-6'>
            {events.map((e) => {
              return competition === "Todas" ||
                e.competition === competition ? (
                <li key={e.id}>
                  <EventBlock
                    event={e}
                    changeCallback={changeBets}
                    checkCallback={checkBet}
                  />
                </li>
              ) : (
                ""
              );
            })}
          </ul>
        </div>
        {isSpecialist() ? (
          <div className='bg-white fixed bottom-0 left-0 w-full h-24'>
            <div className='h-full w-[66%] flex justify-center items-center'>
              <button
                className='bg-orange-500 uppercase h-10 font-medium pl-8 pr-8 items-center rounded-xl'
                onClick={() => {}}
              >
                Guardar Alterações
              </button>
            </div>
          </div>
        ) : (
          ""
        )}
        {!isNormal() ? (
          <Boletim
            betType={betType}
            btCallback={changeBetType}
            bets={bets}
            rmBetCallback={changeBets}
            total={total}
            totalCallback={changeTotal}
            totalGain={totalGain}
            cota={cota}
            spCallback={changeShowPayment}
          />
        ) : (
          <Progress />
        )}
      </div>
    </div>
  );
};

export default Bets;
