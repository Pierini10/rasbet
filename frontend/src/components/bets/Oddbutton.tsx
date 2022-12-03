import { UseAuthentication } from "../../contexts/authenticationContext";

interface Data {
  id: string;
  ent: string;
  description: string;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
  changeOddCallback: (id: string, entity: string, description: string) => void;
  isChanged: boolean;
  odd?: number;
}

const Oddbutton = (props: Data) => {
  const { isNormal, isAdministrator } = UseAuthentication();

  const callback = () => {
    if (isNormal()) props.changeCallback(props.id, props.ent);
    else props.changeOddCallback(props.id, props.ent, props.description);
  };

  return !isAdministrator() ? (
    <button
      className={"text-gray-800 h-full w-full border-[1px] rounded-xl items-center space-y-1 font-bold border-green-900".concat(
        props.isChanged
          ? " bg-yellow-200"
          : props.checkCallback(props.id, props.ent)
          ? " bg-orange-500 "
          : ""
      )}
      onClick={() => callback()}
    >
      <div>{props.ent}</div>
      {props.odd !== -1 ? (
        <div>{props.odd}</div>
      ) : (
        <div className='text-blue-500 underline'>Inserir Odd</div>
      )}
    </button>
  ) : (
    <div className='text-gray-800 h-full w-full border-[1px] rounded-xl items-center space-y-1 font-bold border-green-900 flex justify-center'>
      <div className='flex flex-col items-center'>
        <div>{props.ent}</div>
        {props.odd !== -1 ? (
          <div>{props.odd}</div>
        ) : (
          <div className='text-blue-500 underline'>Inserir Odd</div>
        )}
      </div>
    </div>
  );
};

export default Oddbutton;
