import { useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Event } from "../../models/event.model";
import Oddbutton from "./Oddbutton";

interface Data {
  event: Event;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
  eventStateCallback: (id: string, description: string, state: string) => void;
  changeOddCallback: (id: string, entity: string, description: string) => void;
  checkChangedCallback: (id: string, entity: string) => number;
}

const EventBlock = (props: Data) => {
  const { isNormal, isAdministrator } = UseAuthentication();
  const [showOptions, setShowOptions] = useState(false);

  const event = props.event;
  const entities = Array.from(event.odds.keys());
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
  const hasTemplate = event.description.split(" v ").length > 1;

  let hasDraw = false,
    home = "",
    away = "",
    homeIsChanged = -1,
    awayIsChanged = -1,
    drawIsChanged = -1;
  if (entities.length < 4 && hasTemplate) {
    hasDraw = Array.from(event.odds.keys()).includes("Draw");
    const [homeG, awayG] = event.description.split(" v ");
    home = homeG;
    away = awayG;

    homeIsChanged = props.checkChangedCallback(event.id, home);
    awayIsChanged = props.checkChangedCallback(event.id, away);

    if (hasDraw) {
      drawIsChanged = props.checkChangedCallback(event.id, "Draw");
    }
  }

  const changeShow = () => {
    const newShow = !showOptions;
    setShowOptions(newShow);
  };

  return (
    <div className='flex align-middle'>
      <div className='grow ml-12 mr-12 p-4 rounded-xl border-dashed border-gray-400 border-2 '>
        <div className='grid grid-cols-5 gap-3 items-center h-20'>
          <div
            className={
              entities.length > 3
                ? "col-span-4"
                : entities.length === 3
                ? "col-span-2"
                : "col-span-3"
            }
          >
            <div className='text-lg uppercase text-gray-600 font-medium'>
              {entities.length < 4 && hasTemplate
                ? home + " - " + away
                : event.description}
            </div>
            <div className='text-gray-500'>{date}</div>
            {!isNormal() &&
              (isAdministrator() ? (
                <div className='flex'>
                  <div className='text-gray-600'>Event state:</div>
                  <button
                    className='text-gray-800 pl-2 uppercase font-semibold'
                    onClick={() =>
                      props.eventStateCallback(
                        event.id,
                        event.description,
                        event.state
                      )
                    }
                  >
                    {event.state}
                  </button>
                </div>
              ) : (
                <div className='flex'>
                  <div className='text-gray-600'>Event state:</div>
                  <div className='text-gray-800 pl-2 uppercase font-semibold'>
                    {event.state}
                  </div>
                </div>
              ))}
          </div>
          {entities.length < 4 ? (
            hasTemplate ? (
              <div
                className={"flex h-full space-x-3".concat(
                  entities.length === 3 ? " col-span-3" : " col-span-2"
                )}
              >
                <Oddbutton
                  id={event.id}
                  ent={home}
                  odd={
                    homeIsChanged !== -1
                      ? homeIsChanged
                      : event.odds.get(home)!.odd
                  }
                  changeCallback={props.changeCallback}
                  checkCallback={props.checkCallback}
                  changeOddCallback={props.changeOddCallback}
                  isChanged={homeIsChanged !== -1}
                  description={event.description}
                />
                {hasDraw ? (
                  <Oddbutton
                    id={event.id}
                    ent='Draw'
                    odd={
                      drawIsChanged !== -1
                        ? drawIsChanged
                        : event.odds.get("Draw")!.odd
                    }
                    changeCallback={props.changeCallback}
                    checkCallback={props.checkCallback}
                    changeOddCallback={props.changeOddCallback}
                    isChanged={drawIsChanged !== -1}
                    description={event.description}
                  />
                ) : (
                  false
                )}
                <Oddbutton
                  id={event.id}
                  ent={away}
                  odd={
                    awayIsChanged !== -1
                      ? awayIsChanged
                      : event.odds.get(away)!.odd
                  }
                  changeCallback={props.changeCallback}
                  checkCallback={props.checkCallback}
                  changeOddCallback={props.changeOddCallback}
                  isChanged={awayIsChanged !== -1}
                  description={event.description}
                />
              </div>
            ) : (
              <div
                className={"flex h-full space-x-3".concat(
                  entities.length === 3 ? " col-span-3" : " col-span-2"
                )}
              >
                {entities.map((e) => (
                  <Oddbutton
                    id={event.id}
                    ent={e}
                    odd={
                      props.checkChangedCallback(event.id, e) !== -1
                        ? props.checkChangedCallback(event.id, e)
                        : event.odds.get(e)!.odd
                    }
                    changeCallback={props.changeCallback}
                    checkCallback={props.checkCallback}
                    changeOddCallback={props.changeOddCallback}
                    isChanged={props.checkChangedCallback(event.id, e) !== -1}
                    description={event.description}
                  />
                ))}
              </div>
            )
          ) : (
            <button
              className='col-span-1 font-semibold'
              onClick={() => changeShow()}
            >
              {(!showOptions ? "+ " : "- ") + "Options"}
            </button>
          )}
        </div>
        {showOptions && (
          <div className='mt-5 grid grid-cols-5 gap-3'>
            {entities.map((e) => (
              <Oddbutton
                id={event.id}
                ent={e}
                odd={
                  props.checkChangedCallback(event.id, e) !== -1
                    ? props.checkChangedCallback(event.id, e)
                    : event.odds.get(e)!.odd
                }
                changeCallback={props.changeCallback}
                checkCallback={props.checkCallback}
                changeOddCallback={props.changeOddCallback}
                isChanged={props.checkChangedCallback(event.id, e) !== -1}
                description={event.description}
              />
            ))}
          </div>
        )}
      </div>
      {!isNormal() && (
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
