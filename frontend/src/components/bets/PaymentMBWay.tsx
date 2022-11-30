import { useState } from "react";

interface Data {
  cancelCallback: (show: boolean) => void;
  promotionalCode: string;
  handlePromoCallback: (event: React.ChangeEvent<HTMLInputElement>) => void;
  payCallback: () => void;
}

const PaymentMBWay = (props: Data) => {
  const [phone, setPhone] = useState("");

  const handlePhone = (e: React.ChangeEvent<HTMLInputElement>) => {
    const re = /^[0-9]{0,9}$/;
    if (e.target.value === "" || re.test(e.target.value)) {
      setPhone(e.target.value);
    }
  };

  return (
    <div className='flex justify-between items-center pl-20 pr-20'>
      <img src='Levantar/LogoMBWay.png' alt='mbway' className=' h-24 p-2' />
      <div className='flex flex-col py-1 pl-2 space-y-4'>
        <input
          placeholder='Phone Number'
          className=' pl-2 text-gray-800 focus:outline-none text-lg h-12 w-48 border-gray-400 border-2'
          onChange={handlePhone}
          value={phone}
        />

        <input
          placeholder='Promotional Code'
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
          Pay
        </button>
        <button
          className='bg-gray-400   w-40 h-10 rounded-xl flex items-center justify-center uppercase font-semibold text-xl'
          onClick={() => props.cancelCallback(false)}
        >
          Cancel
        </button>
      </div>
    </div>
  );
};

export default PaymentMBWay;
