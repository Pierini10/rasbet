import { UseAuthentication } from "../../contexts/authenticationContext";

interface Data {
  id: string;
  description: string;
  state: string;
  closeCallback: () => void;
  updateEventsCallback: () => void;
}

const EventStatePopUp = (props: Data) => {
  const { fetchdataAuth } = UseAuthentication();
  const [home, away] = props.description.split(" v ");

  const changeState = async () => {
    const data: string[] = await fetchdataAuth(
      "http://localhost:8080/changeEventState",
      "POST",
      undefined,
      {
        idEvent: props.id,
        state: props.state === "Pending" ? "Closed" : "Pending",
      }
    );

    if (data) {
      props.updateEventsCallback();
      props.closeCallback();
    }
  };

  return (
    <div className='fixed w-screen h-screen bg-black bg-opacity-60 top-0 flex justify-center items-center'>
      <div className='w-[800px] h-[450px] bg-gray-100 rounded-3xl flex flex-col justify-between'>
        <div className='w-full flex justify-between pt-5 items-center pl-5 pr-5'>
          <div className='w-8 h-8'></div>
          <div className='text-2xl font-semibold'>Change Event State</div>
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
        <div className='text-center text-2xl w-full pl-20 pr-20'>
          Do you want to change the event
          <span className='font-medium text-green-900'>
            {" " + home + " - " + away + " "}
          </span>
          from
          <span className='font-bold text-xl'>{" " + props.state + " "}</span>
          to
          <span className='font-bold text-xl'>
            {" " + (props.state === "Pending" ? "Closed" : "Pending")}
          </span>
          ?
        </div>
        <div className='w-full flex justify-end pr-5 pb-5'>
          <button
            className='bg-orange-500 h-12 w-36 font-semibold text-xl rounded-2xl'
            onClick={() => changeState()}
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  );
};

export default EventStatePopUp;
