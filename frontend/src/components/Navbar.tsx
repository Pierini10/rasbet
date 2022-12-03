import { Link } from "react-router-dom";
import { UseAuthentication } from "../contexts/authenticationContext";

interface Data {
  showNotCallback: () => void;
}

const Navbar = (props: Data) => {
  const { isAdministrator, isNormal, logout, balance, isSpecialist } =
    UseAuthentication();
  return (
    <div className='sticky top-0 flex items-center justify-between h-24 p-6 text-white bg-green-900'>
      <img src='logo.png' alt='logo' className='' />
      <nav className='flex items-center justify-between h-full col-span-2'>
        <div className='pl-4 pr-4 text-base font-medium uppercase'>
          <ul className='flex space-x-16'>
            <li key='e'>
              <Link to={"/bets"}>Events</Link>
            </li>
            {isNormal() ? (
              <li key='ha'>
                <Link to={"/betHistory"}>Bet history</Link>
              </li>
            ) : (
              ""
            )}
            {isNormal() ? (
              <li key='ht'>
                <Link to={"/transactionHistory"}>Transaction history</Link>
              </li>
            ) : (
              ""
            )}
            {isAdministrator() ? (
              <li key='ar'>
                <Link to={"/adminRegister"}>Register users</Link>
              </li>
            ) : (
              ""
            )}
            {isAdministrator() ? (
              <li key='promotion'>
                <Link to={"/promotion"}>Promotion</Link>
              </li>
            ) : (
              ""
            )}
            {isAdministrator() ? (
              <li key='notification'>
                <Link to={"/Notification"}>Notification</Link>
              </li>
            ) : (
              ""
            )}
            {isSpecialist() ? (
              <li key='createEvent'>
                <Link to={"/createEvent"}>Create Event</Link>
              </li>
            ) : (
              ""
            )}
            <li key='p'>
              <Link to={"/profile"}>Profile</Link>
            </li>
          </ul>
        </div>
      </nav>
      <div className='flex items-center'>
        {isNormal() && (
          <button onClick={() => props.showNotCallback()}>
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
                d='M14.857 17.082a23.848 23.848 0 005.454-1.31A8.967 8.967 0 0118 9.75v-.7V9A6 6 0 006 9v.75a8.967 8.967 0 01-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 01-5.714 0m5.714 0a3 3 0 11-5.714 0'
              />
            </svg>
          </button>
        )}
        {isNormal() && (
          <div className='flex items-center justify-center h-10 ml-5 mr-5 text-lg font-medium border-2 border-white rounded-full pl-5 pr-5'>
            {(balance !== 0 ? balance.toFixed(2) : balance) + " €"}
          </div>
        )}
        <button onClick={logout}>
          <svg
            xmlns='http://www.w3.org/2000/svg'
            fill='none'
            viewBox='0 0 24 24'
            strokeWidth={1.5}
            stroke='currentColor'
            className='w-9 h-9 '
          >
            <path
              strokeLinecap='round'
              strokeLinejoin='round'
              d='M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15M12 9l-3 3m0 0l3 3m-3-3h12.75'
            />
          </svg>
        </button>
      </div>
    </div>
  );
};

export default Navbar;
