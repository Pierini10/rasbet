interface Data {
  betType: boolean;
  btCallback: (bt: boolean) => void;
  bets: Bet[];
  rmBetCallback: (id: string, bet: string) => void;
  total: string;
  totalCallback: (e: React.ChangeEvent<HTMLInputElement>) => void;
  totalGain: number;
  cota: number;
  spCallback: (show: boolean) => void;
  showError: boolean;
}

interface Bet {
  id: string;
  home: string;
  away: string;
  bet: string;
  odd: number;
}

const Bill = (props: Data) => {
  return (
    <div className='fixed top-[72px] bottom-[30px] w-[33%] right-6 bg-gray-50 border-[1px] border-gray-500 rounded-3xl text-gray-800'>
      <p className='mt-16 text-center uppercase text-3xl font-medium'>Bill</p>
      <div className='pt-11 pl-20 pr-20 pb-8'>
        <button
          className={(props.betType
            ? "bg-gray-800 text-gray-50"
            : "text-gray-800"
          ).concat(" w-[50%] h-12")}
          onClick={() => props.btCallback(true)}
        >
          Simple
        </button>
        <button
          className={(!props.betType
            ? "bg-gray-800 text-gray-50"
            : "text-gray-800"
          ).concat(" w-[50%] h-12")}
          onClick={() => props.btCallback(false)}
        >
          Multiple
        </button>
      </div>
      <div className='h-[55%] overflow-y-auto pl-7 pr-7 mb-4'>
        <div>
          <ul className='space-y-6'>
            {props.bets.map((b) => (
              <li key={b.id}>
                <div className='bg-gray-200 mb-[2px] pl-3 pr-3 flex items-center  justify-between h-8 '>
                  <div className='font-medium '>
                    {b.home} - {b.away}
                  </div>
                  <button onClick={() => props.rmBetCallback(b.id, b.bet)}>
                    <svg
                      xmlns='http://www.w3.org/2000/svg'
                      fill='none'
                      viewBox='0 0 24 24'
                      strokeWidth={1.5}
                      stroke='currentColor'
                      className='w-6 h-6'
                    >
                      <path
                        strokeLinecap='round'
                        strokeLinejoin='round'
                        d='M6 18L18 6M6 6l12 12'
                      />
                    </svg>
                  </button>
                </div>
                <div className='bg-gray-200 pl-3 h-11 flex items-center '>
                  {b.bet === "Draw"
                    ? "Resultado:  Empate"
                    : `Vencedor do jogo: ${b.bet === b.home ? b.home : b.away}`}
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
      <div className='flex justify-between ml-6 mr-6'>
        <p className='rounded-xl border-2 border-gray-600 p-2'>
          {" "}
          Cota: {props.cota.toFixed(2)}
        </p>
        <div className='flex'>
          <div className='flex items-center border-[1px] border-gray-400 pl-4 pr-4'>
            Amount
          </div>
          <div className='flex items-center border-[1px] border-l-0 border-gray-400 pl-4 pr-4 '>
            <input
              type='text'
              className='w-10 focus:outline-none text-right bg-transparent'
              onChange={props.totalCallback}
              value={props.total}
            />
          </div>
          <div className='flex items-center pl-2 text-xl text-gray-500'>€</div>
        </div>
      </div>
      <div className='flex justify-between ml-6 mr-6 mt-6'>
        <div className='text-center'>
          <div className=''>Total gain</div>
          <div className='text-orange-500'>{props.totalGain.toFixed(2)} €</div>
        </div>
        <button
          className='flex items-center bg-orange-500 uppercase text-lg rounded-full pl-10 pr-10 font-medium'
          onClick={() => props.spCallback(true)}
        >
          Bet
        </button>
      </div>
      {props.showError && (
        <div className='text-red-700 text-center pt-2'>
          Enter an amount and select at least an event.
        </div>
      )}
    </div>
  );
};

export default Bill;
