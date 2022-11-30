import { useState } from "react";

interface Data {
  cancelCallback: (show: boolean) => void;
  promotionalCode: string;
  handlePromoCallback: (event: React.ChangeEvent<HTMLInputElement>) => void;
  payCallback: () => void;
}

const PaymentBank = (props: Data) => {
  const [nib, setNIB] = useState("");

  const handleNIB = (e: React.ChangeEvent<HTMLInputElement>) => {
    const re = /^[0-9]{0,21}$/;
    if (e.target.value === "" || re.test(e.target.value)) {
      setNIB(e.target.value);
    }
  };

  return (
    <div className='flex justify-between items-center pl-20 pr-20'>
      <img
        src='Levantar/transferencia_bancaria.png'
        alt='mbway'
        className=' h-28 p-2'
      />
      <div className='flex flex-col py-1 pl-2 space-y-4'>
        <input
          placeholder='NIB'
          className=' pl-2 text-gray-800 focus:outline-none text-lg h-12 w-48 border-gray-400 border-2'
          onChange={handleNIB}
          value={nib}
        />

        <input
          placeholder='Código Promocional'
          className='pl-2 text-gray-800 focus:outline-none text-lg h-12 w-48 border-gray-400 border-2'
          onChange={props.handlePromoCallback}
          value={props.promotionalCode}
        />
      </div>

      <div className='space-y-2'>
        <button
          className='bg-orange-500 w-40 h-10 rounded-xl flex items-center justify-center uppercase font-semibold text-xl'
          onClick={() => props.payCallback()}
        >
          Pagar
        </button>
        <button
          className='bg-gray-400   w-40 h-10 rounded-xl flex items-center justify-center uppercase font-semibold text-xl'
          onClick={() => props.cancelCallback(false)}
        >
          Cancelar
        </button>
      </div>
    </div>
  );
};

export default PaymentBank;
