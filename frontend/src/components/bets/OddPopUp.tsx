import React, { useState } from "react";

interface Data {
  id: string;
  description: string;
  entity: string;
  closeCallback: () => void;
  confirmCallback: (id: string, entity: string, odd: number) => void;
}

const OddPopUp = (props: Data) => {
  const [value, setValue] = useState(0);
  const [showError, setShowError] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setValue(Number(e.target.value));
  };

  const confirm = () => {
    if (value > 0) {
      props.confirmCallback(props.id, props.entity, value);
      props.closeCallback();
    } else setShowError(true);
  };

  const [home, away] = props.description.split(" v ");
  return (
    <div className='fixed w-screen h-screen bg-black bg-opacity-60 top-0 flex justify-center items-center'>
      <div className='w-[600px] h-[400px] bg-gray-100 rounded-3xl flex flex-col justify-between'>
        <div className='w-full flex justify-between pt-5 items-center pl-5 pr-5'>
          <div className='w-8 h-8'></div>
          <div className='text-2xl font-semibold'>Change Odd</div>
          <button onClick={() => props.closeCallback()}>
            <svg
              xmlns='http://www.w3.org/2000/svg'
              fill='none'
              viewBox='0 0 24 24'
              strokeWidth={1.5}
              stroke='currentColor'
              className='w-8 h-8'
            >
              <path
                strokeLinecap='round'
                strokeLinejoin='round'
                d='M6 18L18 6M6 6l12 12'
              />
            </svg>
          </button>
        </div>
        <div className='text-xl w-full pl-20 pr-20 flex justify-center'>
          <div className='text-gray-800 text-2xl w-24 space-y-5'>
            <div className='h-10 flex items-center'>Event:</div>
            <div className='h-10 flex items-center'>Entity:</div>
            <div className='h-10 flex items-center'>Odd:</div>
          </div>
          <div className='text-gray-800 text-2xl w-64 space-y-5'>
            <div className='font-bold h-10 flex items-center'>
              {" " + home + " - " + away}
            </div>
            <div className='font-bold h-10 flex items-center'>
              {" " + props.entity}
            </div>
            <div className='h-10 flex items-center'>
              <input
                placeholder='New Odd'
                onChange={handleChange}
                className='w-32 border-gray-500 border-2 rounded-lg text-xl placeholder:text-gray-700 pl-2 pr-2'
                type={"number"}
              />
            </div>
          </div>
        </div>
        <div className='w-full flex justify-between pr-5 pb-5 items-center'>
          <div className='w-36'></div>
          {showError && <div className='text-red-500'>Invalid odd value!</div>}
          <button
            className='bg-orange-500 h-12 w-36 font-semibold text-xl rounded-2xl'
            onClick={() => confirm()}
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  );
};

export default OddPopUp;
