const EventStatePopUp = () => {
  return (
    <div className='fixed w-screen h-screen bg-black bg-opacity-60 top-0 flex justify-center items-center'>
      <div className='w-[800px] h-[450px] bg-gray-100 rounded-3xl'>
        <div className='w-full flex justify-between'>
          <div className='w-8 h-8'></div>
          <div className=''>Change Event State</div>
          <button>
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
      </div>
    </div>
  );
};

export default EventStatePopUp;
