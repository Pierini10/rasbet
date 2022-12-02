import { useEffect, useState } from "react";
import Bill from "../../components/bets/Bill";
import EventBlock from "../../components/bets/EventBlock";
import EventStatePopUp from "../../components/bets/EventStatePopUp";
import OddPopUp from "../../components/bets/OddPopUp";
import Payment from "../../components/bets/Payment";
import Progress from "../../components/bets/Progress";
import SelectInput from "../../components/bets/SelectInput";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Event, jsonToEvents } from "../../models/event.model";
import { EventOdds } from "../../models/odds.model";

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
  const [showChangeState, setShowChangeState] = useState(false);
  const [showChangeOdd, setShowChangeOdd] = useState(false);
  const [idEventCS, setIdEventCS] = useState("");
  const [stateCS, setStateCS] = useState("");
  const [descriptionCS, setDescriptionCS] = useState("");
  const [updateBets, setUpdateBets] = useState(true);
  const [idEventCO, setIdEventCO] = useState("");
  const [entityCO, setEntityCO] = useState("");
  const [descriptionCO, setDescriptionCO] = useState("");
  const [eventsOddsC, setEventsOddsC] = useState<EventOdds[]>([]);

  useEffect(() => {
    if (updateBets) {
    }

    const loadEvents = async () => {
      const dataSports: string[] = await fetchdataAuth(
        "http://localhost:8080/getAllSports",
        "GET"
      );

      if (dataSports !== undefined) {
        const mapEvents: Map<string, Event[]> = new Map();

        for (const s of dataSports) {
          const dataEvents: any[] = await fetchdataAuth(
            "http://localhost:8080/getEvents",
            "GET",
            undefined,
            {
              sport: s,
              eventState: !isNormal(),
            }
          );
          if (dataEvents !== undefined) {
            const newEvents = jsonToEvents(dataEvents);
            mapEvents.set(s, newEvents);
          }
        }

        changeCompetitions(mapEvents.get(dataSports[0])!);
        setEvents(mapEvents);
        setSport(dataSports[0]);
        setSports(dataSports);
      }
    };

    loadEvents();
  }, [fetchdataAuth, updateBets, isNormal]);

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
    const newCompetitions = ["All"];

    events.forEach((event) => competitionsSet.add(event.competition));

    newCompetitions.push(...Array.from(competitionsSet));

    setCompetitions(newCompetitions);
    setCompetition(newCompetitions[0]);
  };

  const changeEventState = (id: string, description: string, state: string) => {
    setIdEventCS(id);
    setStateCS(state);
    setDescriptionCS(description);

    changeShowChangeState();
  };

  const changeOdd = (id: string, entity: string, description: string) => {
    setIdEventCO(id);
    setEntityCO(entity);
    setDescriptionCO(description);

    changeShowChangeOdd();
  };

  const changeShowChangeOdd = () => {
    const newShow = !showChangeOdd;
    setShowChangeOdd(newShow);
  };

  const updateEvents = () => {
    const newUpdate = !updateBets;
    setUpdateBets(newUpdate);
  };

  const changeShowChangeState = () => {
    const newShow = !showChangeState;
    setShowChangeState(newShow);
  };

  const addOddChange = (id: string, entity: string, odd: number) => {
    const newEventsOdds = [...eventsOddsC];

    const index = newEventsOdds.findIndex((e) => e.eventID === id);

    if (index !== -1) {
      const indexEntity = newEventsOdds[index].odds.findIndex(
        (o) => o.entity === entity
      );

      if (indexEntity !== -1) {
        newEventsOdds[index].odds[indexEntity].odd = odd;
      } else {
        newEventsOdds[index].odds.push({ entity: entity, odd: odd });
      }
    } else {
      newEventsOdds.push({ eventID: id, odds: [{ entity: entity, odd: odd }] });
    }

    setEventsOddsC(newEventsOdds);
  };

  const checkOddChanged = (id: string, entity: string) => {
    const index = eventsOddsC.findIndex((e) => e.eventID === id);

    if (index !== -1) {
      const indexEntity = eventsOddsC[index].odds.findIndex(
        (o) => o.entity === entity
      );

      if (indexEntity !== -1) return eventsOddsC[index].odds[indexEntity].odd;
    }

    return -1;
  };

  const cancelChanges = () => {
    setEventsOddsC([]);
  };

  const makeOddsChanges = async () => {
    const data: string[] = await fetchdataAuth(
      "http://localhost:8080/insertOdd",
      "POST",
      JSON.stringify(eventsOddsC)
    );
    if (data) {
      updateEvents();
      setEventsOddsC([]);
    }
  };

  const clearBets = () => {
    setBets([]);
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
        className={"grid ".concat(isNormal() ? "grid-cols-3" : "grid-cols-4")}
      >
        <div className='col-span-2'>
          <div className='fixed w-full bg-white'>
            <div className='w-[66%] flex justify-center space-x-10 h-16 items-center'>
              <SelectInput
                handleChange={handleChangeSport}
                label='Sport: '
                listValues={sports}
                value={sport}
              />
              <SelectInput
                handleChange={handleChangeCompetition}
                label='Competition: '
                listValues={competitions}
                value={competition}
              />
            </div>
          </div>
        </div>

        <div
          className={"pt-16 ".concat(isNormal() ? "col-span-2" : "col-span-3")}
        >
          {events.get(sport) !== undefined && (
            <ul className='space-y-6'>
              {events.get(sport)!.map((e) => {
                return competition === "All" ||
                  e.competition === competition ? (
                  <li key={e.id}>
                    <EventBlock
                      event={e}
                      changeCallback={changeBets}
                      checkCallback={checkBet}
                      eventStateCallback={changeEventState}
                      changeOddCallback={changeOdd}
                      checkChangedCallback={checkOddChanged}
                    />
                  </li>
                ) : (
                  ""
                );
              })}
            </ul>
          )}
          {!isNormal() ? (
            <div className='bg-white fixed bottom-0 left-0 w-full h-24'>
              <div className='h-full w-[66%] flex justify-center items-center space-x-5'>
                <button
                  className='uppercase h-10 w-48 font-medium pl-8 pr-8 items-center rounded-xl bg-gray-400'
                  onClick={() => cancelChanges()}
                >
                  Cancel
                </button>
                <button
                  className={"uppercase h-10 w-48 font-medium pl-8 pr-8 items-center rounded-xl bg-orange-500".concat(
                    eventsOddsC.length === 0 ? " opacity-50" : ""
                  )}
                  onClick={() => makeOddsChanges()}
                >
                  Save Changes
                </button>
              </div>
            </div>
          ) : (
            ""
          )}
        </div>

        {isNormal() ? (
          <Bill
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
      {showPayment ? (
        <Payment
          cancelCallback={changeShowPayment}
          bets={betsDB}
          amount={Number(total)}
          clearCallback={clearBets}
        />
      ) : (
        ""
      )}
      {showChangeState && (
        <EventStatePopUp
          closeCallback={changeShowChangeState}
          id={idEventCS}
          description={descriptionCS}
          state={stateCS}
          updateEventsCallback={updateEvents}
        />
      )}
      {showChangeOdd && (
        <OddPopUp
          closeCallback={changeShowChangeOdd}
          id={idEventCO}
          description={descriptionCO}
          entity={entityCO}
          confirmCallback={addOddChange}
        />
      )}
    </div>
  );
};

export default Bets;
