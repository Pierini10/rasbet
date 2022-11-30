import { useEffect, useState } from "react";
import Boletim from "../../components/bets/Boletim";
import EventBlock from "../../components/bets/EventBlock";
import Payment from "../../components/bets/Payment";
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

interface DBBet {
  prediction: string;
  odd: number;
  event: string;
  idEvent: string;
  betState: string;
}

const Bets = () => {
  const { fetchdataAuth, isNormal, isSpecialist } = UseAuthentication();
  const [events, setEvents] = useState<Map<string, Event[]>>(new Map());
  const [sports, setSports] = useState<string[]>([]);
  const [competitions, setCompetitions] = useState<string[]>([]);
  const [sport, setSport] = useState("");
  const [competition, setCompetition] = useState("");
  const [betType, setBetType] = useState(true);
  const [bets, setBets] = useState<Bet[]>([]);
  const [betsDB, setBetsDB] = useState<DBBet[]>([]);
  const [total, setTotal] = useState("");
  const [totalGain, setTotalGain] = useState(0);
  const [cota, setCota] = useState(0);
  const [showPayment, setShowPayment] = useState(false);
  const [showError, setShowError] = useState(false);

  useEffect(() => {
    const loadEvents = async () => {
      const dataSports: string[] = await fetchdataAuth(
        "http://localhost:8080/getAllSports",
        "GET"
      );

      if (dataSports !== undefined) {
        const mapEvents: Map<string, Event[]> = new Map();

        dataSports.forEach(async (s) => {
          const dataEvents: any[] = await fetchdataAuth(
            "http://localhost:8080/getEvents",
            "GET",
            undefined,
            {
              sport: s,
            }
          );
          if (dataEvents !== undefined) {
            const newEvents = jsonToEvents(dataEvents);
            mapEvents.set(s, newEvents);
          }
        });

        return { mapEvents, dataSports };
      }
    };

    loadEvents().then((res) => {
      if (res !== undefined) {
        setEvents(res.mapEvents);
        setSport(res.dataSports[0]);
        setSports(res.dataSports);
      }
    });
  }, [fetchdataAuth]);

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
      const indexEvent = events.get(sport)!.findIndex((i) => i.id === id);

      if (
        bets.length !== 1 ||
        (bets[0].id === id && bets[0].bet !== bet) ||
        bets[0].id !== id
      ) {
        const [home, away] = events
          .get(sport)!
          [indexEvent].description.split(" v ");
        newBets.push({
          id: events.get(sport)![indexEvent].id,
          home: home,
          away: away,
          bet: bet,
          odd:
            bet === "Draw"
              ? events.get(sport)![indexEvent].odds.get("Draw")!.odd
              : bet === home
              ? events.get(sport)![indexEvent].odds.get(home)!.odd
              : events.get(sport)![indexEvent].odds.get(away)!.odd,
        });
      }
    } else {
      newBets = bets.map((i) => i);
      const index = newBets.findIndex((i) => i.id === id);

      if (index === -1) {
        const indexEvent = events.get(sport)!.findIndex((i) => i.id === id);
        const [home, away] = events
          .get(sport)!
          [indexEvent].description.split(" v ");

        newBets.push({
          id: events.get(sport)![indexEvent].id,
          home: home,
          away: away,
          bet: bet,
          odd:
            bet === "Draw"
              ? events.get(sport)![indexEvent].odds.get("Draw")!.odd
              : bet === home
              ? events.get(sport)![indexEvent].odds.get(home)!.odd
              : events.get(sport)![indexEvent].odds.get(away)!.odd,
        });
      } else {
        if (newBets[index].bet === bet) newBets.splice(index, 1);
        else {
          const indexGame = events.get(sport)!.findIndex((i) => i.id === id);
          const teams = events.get(sport)![indexGame].description.split(" v ");

          newBets[index].bet = bet;
          newBets[index].odd =
            bet === "Draw"
              ? events.get(sport)![indexGame].odds.get("Draw")!.odd
              : bet === teams[0]
              ? events.get(sport)![indexGame].odds.get(newBets[index].home)!.odd
              : events.get(sport)![indexGame].odds.get(newBets[index].away)!
                  .odd;
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
    if (bets.length === 0 || Number(total) === 0) {
      setShowError(true);
    } else {
      if (show) {
        const newBetsDB: DBBet[] = [];
        bets.forEach((b) =>
          newBetsDB.push({
            idEvent: b.id,
            betState: "",
            event: "",
            odd: b.odd,
            prediction: b.bet,
          })
        );
        setBetsDB(newBetsDB);
      }
      setShowError(false);
      setShowPayment(show);
    }
  };

  const changeCompetitions = (events: Event[]) => {
    const competitionsSet: Set<string> = new Set();
    const newCompetitions = ["Todas"];

    events.forEach((event) => competitionsSet.add(event.competition));

    newCompetitions.push(...Array.from(competitionsSet));

    setCompetitions(newCompetitions);
    setCompetition(newCompetitions[0]);
  };

  const changeEventState = (sport: string, id: string, state: string) => {
    console.log("muda estado de jogo");
  };

  const changeOdd = (id: string, bet: string) => {
    console.log("muda a odd");
  };

  const handleChangeSport = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSport(event.target.value);
    setBets([]);
    setCota(0);
    setTotalGain(0);
    changeCompetitions(events.get(event.target.value)!);
  };

  const handleChangeCompetition = (
    event: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setCompetition(event.target.value);
  };

  return (
    <div>
      <div
        className={"grid ".concat(!isNormal() ? "grid-cols-3" : "grid-cols-4")}
      >
        <div className='col-span-2'>
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
        {showPayment ? (
          <div className='fixed w-full h-full bg-black bg-opacity-60 grid grid-cols-3 items-center'>
            <Payment
              cancelCallback={changeShowPayment}
              bets={betsDB}
              amount={Number(total)}
            />
          </div>
        ) : (
          ""
        )}
        <div
          className={"pt-16 ".concat(!isNormal() ? "col-span-2" : "col-span-3")}
        >
          {events.get(sport) !== undefined && (
            <ul className='space-y-6'>
              {events.get(sport)!.map((e) => {
                return competition === "Todas" ||
                  e.competition === competition ? (
                  <li key={e.id}>
                    <EventBlock
                      event={e}
                      changeCallback={changeBets}
                      checkCallback={checkBet}
                      eventStateCallback={changeEventState}
                      changeOddCallback={changeOdd}
                    />
                  </li>
                ) : (
                  ""
                );
              })}
            </ul>
          )}
          {!isSpecialist() ? (
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
        </div>

        {isNormal() ? (
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
            showError={showError}
          />
        ) : (
          <Progress events={events} />
        )}
      </div>
    </div>
  );
};

export default Bets;
