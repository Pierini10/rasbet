import Oddbutton from "./Oddbutton";

interface Data {
  id: string;
  casa: string;
  fora: string;
  data: string;
  oddc: string;
  odde: string;
  oddf: string;
  changeCallback: (id: string, bet: string) => void;
  checkCallback: (id: string, bet: string) => boolean;
}

const Game = (props: Data) => {
  return (
    <div className="h-28 ml-12 mr-12 p-4 rounded-xl border-dashed border-gray-400 border-2 grid grid-cols-5 gap-3 items-center">
      <div className="col-span-2 pl-14">
        <div className="text-2xl uppercase text-gray-600 font-medium">
          {props.casa} - {props.fora}
        </div>
        <div className="text-gray-500">{props.data}</div>
      </div>

      <Oddbutton
        id={props.id}
        betType={"casa"}
        ent={props.casa}
        odd={props.oddc}
        changeCallback={props.changeCallback}
        checkCallback={props.checkCallback}
      />
      <Oddbutton
        id={props.id}
        betType={"empate"}
        ent="Empate"
        odd={props.odde}
        changeCallback={props.changeCallback}
        checkCallback={props.checkCallback}
      />
      <Oddbutton
        id={props.id}
        betType={"fora"}
        ent={props.fora}
        odd={props.oddf}
        changeCallback={props.changeCallback}
        checkCallback={props.checkCallback}
      />
    </div>
  );
};

export default Game;
