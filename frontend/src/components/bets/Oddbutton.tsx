import { UseAuthentication } from "../../contexts/authenticationContext";

interface Data {
  id: string;
  betType: string;
  ent: string;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
  odd?: number;
}

const Oddbutton = (props: Data) => {
  const { isNormal, isSpecialist } = UseAuthentication();

  const callback = () => {
    if (isNormal()) props.changeCallback(props.id, props.betType);
    if (isSpecialist()) props.changeCallback(props.id, props.betType);
  };

  return (
    <button
      className={"text-gray-800 h-full w-full border-[1px] rounded-xl items-center space-y-1 font-bold".concat(
        props.checkCallback(props.id, props.betType)
          ? " bg-orange-500"
          : " border-green-900"
      )}
      onClick={() => callback()}
    >
      <div>{props.ent}</div>
      {props.odd ? (
        <div>{props.odd}</div>
      ) : (
        <div className='text-blue-500 underline'>Inserir Odd</div>
      )}
    </button>
  );
};

export default Oddbutton;
