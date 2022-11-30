import { UseAuthentication } from "../../contexts/authenticationContext";
import { Event } from "../../models/event.model";
import Oddbutton from "./Oddbutton";

interface Data {
  event: Event;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
  eventStateCallback: (sport: string, id: string, state: string) => void;
  changeOddCallback: (id: string, bet: string) => void;
}

const EventBlock = (props: Data) => {
  const { isSpecialist } = UseAuthentication();
  const event = props.event;
  const [home, away] = event.description.split(" v ");

  const date = `${event.datetime.getDate().toLocaleString(undefined, {
    minimumIntegerDigits: 2,
    useGrouping: false,
  })}/${event.datetime.getMonth().toLocaleString(undefined, {
    minimumIntegerDigits: 2,
    useGrouping: false,
  })}/${event.datetime.getFullYear()} ${event.datetime
    .getHours()
    .toLocaleString(undefined, {
      minimumIntegerDigits: 2,
      useGrouping: false,
    })}:${event.datetime.getMinutes().toLocaleString(undefined, {
    minimumIntegerDigits: 2,
    useGrouping: false,
  })}`;

  const hasDraw = event.sport === "Soccer";

  return (
    <div className='flex align-middle'>
      <div className='grow h-28 ml-12 mr-12 p-4 rounded-xl border-dashed border-gray-400 border-2 grid grid-cols-5 gap-3 items-center'>
        <div className={hasDraw ? "col-span-2" : "col-span-3"}>
          <div className='text-lg uppercase text-gray-600 font-medium'>
            {home} - {away}
          </div>
          <div className='text-gray-500'>{date}</div>
          <div className='flex'>
            <div className='text-gray-600'>Event state:</div>
            <button
              className='text-gray-800 pl-2 uppercase font-semibold'
              onClick={() =>
                props.eventStateCallback(event.sport, event.id, event.state)
              }
            >
              {event.state}
            </button>
          </div>
        </div>

        <Oddbutton
          id={event.id}
          betType={home}
          ent={home}
          odd={event.odds.get(home)!.odd}
          changeCallback={props.changeCallback}
          checkCallback={props.checkCallback}
          changeOddCallback={props.changeOddCallback}
        />
        {hasDraw ? (
          <Oddbutton
            id={event.id}
            betType={"Draw"}
            ent='Draw'
            odd={event.odds.get("Draw")!.odd}
            changeCallback={props.changeCallback}
            checkCallback={props.checkCallback}
            changeOddCallback={props.changeOddCallback}
          />
        ) : (
          false
        )}
        <Oddbutton
          id={event.id}
          betType={away}
          ent={away}
          odd={event.odds.get(away)!.odd}
          changeCallback={props.changeCallback}
          checkCallback={props.checkCallback}
          changeOddCallback={props.changeOddCallback}
        />
      </div>
      {!isSpecialist() && (
        <div className='h-28 flex'>
          <div
            className={"w-28 mt-5 mb-5 rounded-xl flex justify-center items-center font-medium text-xl ".concat(
              event.odds.size === 0
                ? "bg-red-700"
                : (event.odds.size === 2 && !hasDraw) || event.odds.size === 3
                ? "bg-green-800 text-white"
                : "bg-yellow-500"
            )}
          >
            {event.odds.size}/{hasDraw ? 3 : 2}
          </div>
        </div>
      )}
    </div>
  );
};

export default EventBlock;
