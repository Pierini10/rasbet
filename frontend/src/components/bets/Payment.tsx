import { useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";
import PaymentBank from "./PaymentBank";
import PaymentCard from "./PaymentCard";
import PaymentMBWay from "./PaymentMBWay";

interface Data {
  cancelCallback: (show: boolean) => void;
  clearCallback: () => void;
  bets: DBBet[];
  amount: number;
}

interface DBBet {
  prediction: string;
  odd: number;
  event: string;
  idEvent: string;
  betState: string;
}

const Payment = (props: Data) => {
  const { fetchdataAuth, balance, setBalance } = UseAuthentication();
  const [step, setStep] = useState(1);
  const [method, setMethod] = useState("Wallet");
  const [success, setSuccess] = useState(true);

  const payBet = async () => {
    const data = await fetchdataAuth(
      "http://localhost:8080/makeBet",
      "POST",
      JSON.stringify(props.bets),
      { amount: props.amount, paymentMethod: method }
    );

    if (!data) setSuccess(false);
    else {
      const newBalance = balance - props.amount;
      setBalance(newBalance);
    }

    setStep(3);
    setMethod("Wallet");
  };

  const close = () => {
    props.clearCallback();
    props.cancelCallback(false);
  };

  return (
    <div className='fixed top-0 flex items-center justify-center w-screen h-screen bg-black bg-opacity-60'>
      <div className='h-96 w-[900px] bg-white ml-20 mr-28 rounded-3xl flex flex-col justify-between pt-5 pb-5  text-gray-800'>
        {step === 3 && (
          <button className='absolute pl-3' onClick={() => close()}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              fill='none'
              viewBox='0 0 24 24'
              strokeWidth={1.5}
              stroke='currentColor'
              className='absolute w-8 h-8'
            >
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                d='M6 18L18 6M6 6l12 12'
              />
            </svg>
          </button>
        )}

        <p className='w-full text-2xl font-semibold text-center'>
          {step !== 3 ? "Payment" : success ? "Congratulations!" : ""}
        </p>
        {step === 1 ? (
          <div className='flex items-center justify-center w-full space-x-3'>
            <p className='text-xl font-medium'>Payment method:</p>
            <div className='flex space-x-3'>
              <button
                className='flex items-center justify-center h-24 duration-100 ease-in bg-white rounded w-44 hover:bg-slate-400'
                onClick={() => {
                  setMethod("MB");
                  setStep(2);
                }}
              >
                <img
                  src='Levantar/transferencia_bancaria.png'
                  alt='bank'
                  className='h-24'
                />
              </button>
              <button
                className='flex items-center justify-center h-24 duration-100 ease-in bg-white rounded w-44 hover:bg-slate-400'
                onClick={() => {
                  setMethod("MBWay");
                  setStep(2);
                }}
              >
                <img
                  src='Levantar/LogoMBWay.png'
                  alt='mbway'
                  className='h-24 p-2 '
                />
              </button>
              <button
                className='flex items-center justify-center h-24 duration-100 ease-in bg-white rounded w-44 hover:bg-slate-400'
                onClick={() => {
                  setMethod("Credit");
                  setStep(2);
                }}
              >
                <img
                  src='Levantar/visa-mastercard-logo.png'
                  alt='card'
                  className='h-24'
                />
              </button>
            </div>
          </div>
        ) : step === 2 ? (
          method === "MBWay" ? (
            <PaymentMBWay
              cancelCallback={props.cancelCallback}
              payCallback={payBet}
            />
          ) : method === "MB" ? (
            <PaymentBank
              cancelCallback={props.cancelCallback}
              payCallback={payBet}
            />
          ) : (
            <PaymentCard
              cancelCallback={props.cancelCallback}
              payCallback={payBet}
            />
          )
        ) : (
          <div className='flex items-center justify-center w-full text-2xl grow'>
            {success ? "Payment was successful." : "Payment failed."}
          </div>
        )}
        <div className='flex items-center justify-between w-full pl-10 pr-10'>
          {step === 1 ? (
            <button onClick={() => payBet()}>Use Wallet</button>
          ) : (
            <div></div>
          )}
          <div className='flex items-center justify-end'>
            <div
              className={"h-8 w-8 rounded-full flex justify-center items-center ".concat(
                step === 1
                  ? "bg-green-900 text-white"
                  : "bg-white border-green-900 border-2"
              )}
            >
              1
            </div>
            <div className='h-[2px] w-8 bg-green-900' />
            <div
              className={"h-8 w-8 rounded-full flex justify-center items-center ".concat(
                step === 2
                  ? "bg-green-900 text-white"
                  : "bg-white border-green-900 border-2"
              )}
            >
              2
            </div>
            <div className='h-[2px] w-8 bg-green-900' />
            <div
              className={"h-8 w-8 rounded-full flex justify-center items-center ".concat(
                step === 3
                  ? "bg-green-900 text-white"
                  : "bg-white border-green-900 border-2"
              )}
            >
              3
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Payment;
