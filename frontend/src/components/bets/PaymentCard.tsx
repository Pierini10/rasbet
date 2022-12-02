import { useState } from "react";

interface Data {
  cancelCallback: (show: boolean) => void;
  payCallback: () => void;
}

const PaymentCard = (props: Data) => {
  const [cardNumber, setCardNumber] = useState("");

  const handleCardNumber = (e: React.ChangeEvent<HTMLInputElement>) => {
    const re = /^[0-9]{0,16}$/;
    if (e.target.value === "" || re.test(e.target.value)) {
      setCardNumber(e.target.value);
    }
  };

  return (
    <div className='flex justify-between items-center pl-12 pr-12'>
      <img
        src='Levantar/visa-mastercard-logo.png'
        alt='mbway'
        className=' h-16 p-2'
      />
      <div className='flex flex-col py-1 pl-2 space-y-1'>
        <div className='flex space-x-1'>
          <input
            placeholder='Card Number'
            className=' pl-2 text-gray-800 focus:outline-none h-12 w-44 border-gray-400 border-2'
            onChange={handleCardNumber}
            value={cardNumber}
          />
          <input
            placeholder='Security Number'
            className=' pl-2 text-gray-800 focus:outline-none h-12 w-44 border-gray-400 border-2'
          />
        </div>
        <div className='flex space-x-1'>
          <input
            placeholder='Validation Date'
            className=' pl-2 text-gray-800 focus:outline-none h-12 w-44 border-gray-400 border-2'
            type='month'
          />
        </div>
      </div>
      <div className='space-y-2'>
        <button
          className='bg-orange-500 w-36 h-10 rounded-xl flex items-center justify-center uppercase font-semibold text-lg'
          onClick={() => props.payCallback()}
        >
          Pay
        </button>
        <button
          className='bg-gray-400   w-36 h-10 rounded-xl flex items-center justify-center uppercase font-semibold text-lg'
          onClick={() => props.cancelCallback(false)}
        >
          Cancel
        </button>
      </div>
    </div>
  );
};

export default PaymentCard;
