import Oddbutton from "./Oddbutton";

interface Data {
  casa: string;
  fora: string;
  data: string;
  oddc: string;
  odde: string;
  oddf: string;
}

const Game = (data: Data) => {
  return (
    <div className="h-28 ml-12 mr-12 p-4 rounded-xl border-dashed border-gray-400 border-2 grid grid-cols-5 gap-3 items-center">
      <div className="col-span-2 pl-14">
        <div className="text-2xl uppercase text-gray-600 font-medium">
          {data.casa} - {data.fora}
        </div>
        <div className="text-gray-500">{data.data}</div>
      </div>

      <Oddbutton ent={data.casa} odd={data.oddc} />
      <Oddbutton ent="Empate" odd={data.odde} />
      <Oddbutton ent={data.fora} odd={data.oddf} />
    </div>
  );
};

export default Game;
