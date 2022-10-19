interface Data {
  betType: boolean;
  btCallback: (bt: boolean) => void;
}

const Boletim = (props: Data) => {
  return (
    <div className="fixed top-[72px] bottom-[30px] w-[33%] right-6 bg-gray-50 border-[1px] border-gray-500 rounded-3xl text-gray-800">
      <p className="mt-16 text-center uppercase text-3xl font-medium">
        Boletim
      </p>
      <div className="pt-11 pl-20 pr-20 pb-11">
        <button
          className={(props.betType
            ? "bg-gray-800 text-gray-50"
            : "text-gray-800"
          ).concat(" w-[50%] h-12")}
          onClick={() => props.btCallback(true)}
        >
          Simples
        </button>
        <button
          className={(!props.betType
            ? "bg-gray-800 text-gray-50"
            : "text-gray-800"
          ).concat(" w-[50%] h-12")}
          onClick={() => props.btCallback(false)}
        >
          Múltiplas
        </button>
      </div>
      <div className="h-[56%]">bets</div>
      <div className="flex justify-between ml-6 mr-6">
        <p className="rounded-xl border-2 border-gray-600 p-2"> Cota: {3.9}</p>
        <div className="flex">
          <div className="flex items-center border-[1px] border-gray-400 pl-4 pr-4">
            Montante
          </div>
          <div className="flex items-center border-[1px] border-l-0 border-gray-400 pl-4 pr-4">
            {3}
          </div>
          <div className="flex items-center pl-2 text-xl text-gray-500">€</div>
        </div>
      </div>
      <div className="flex justify-between ml-6 mr-6 mt-6">
        <div className="text-center">
          <div className="text-sm">Total de ganhos</div>
          <div className="text-orange-500">{13.5} €</div>
        </div>
        <div className="flex items-center bg-orange-500 uppercase text-lg rounded-full pl-10 pr-10 font-medium">
          Apostar
        </div>
      </div>
    </div>
  );
};

export default Boletim;
