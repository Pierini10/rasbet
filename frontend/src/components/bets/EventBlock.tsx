import { Event } from "../../models/event.model";
import Oddbutton from "./Oddbutton";

interface Data {
  event: Event;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
}

const EventBlock = (props: Data) => {
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
        </div>

        <Oddbutton
          id={event.id}
          betType={"casa"}
          ent={home}
          odd={event.odds.get(home)!.odd}
          changeCallback={props.changeCallback}
          checkCallback={props.checkCallback}
        />
        {hasDraw ? (
          <Oddbutton
            id={event.id}
            betType={"empate"}
            ent='Empate'
            odd={event.odds.get("Draw")!.odd}
            changeCallback={props.changeCallback}
            checkCallback={props.checkCallback}
          />
        ) : (
          false
        )}
        <Oddbutton
          id={event.id}
          betType={"fora"}
          ent={away}
          odd={event.odds.get(away)!.odd}
          changeCallback={props.changeCallback}
          checkCallback={props.checkCallback}
        />
      </div>
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
    </div>
  );
};

export default EventBlock;
