interface Data {
  id: string;
  betType: string;
  ent: string;
  odd: string;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
}

const Oddbutton = (props: Data) => {
  return (
    <button
      className={"h-full w-full border-[1px] rounded-xl items-center space-y-1 font-bold".concat(
        props.checkCallback(props.id, props.betType)
          ? " bg-orange-500"
          : " border-green-900"
      )}
      onClick={() => props.changeCallback(props.id, props.betType)}
    >
      <div>{props.ent}</div>
      <div>{props.odd}</div>
    </button>
  );
};

export default Oddbutton;
