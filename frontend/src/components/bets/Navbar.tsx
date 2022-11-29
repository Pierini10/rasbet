import { Link } from "react-router-dom";
import { UseAuthentication } from "../../contexts/authenticationContext";

/**
 * a barra de cima falta melhorar com o simbolo
 */

const Navbar = () => {
  const { isAdministrator, isNormal, logout } = UseAuthentication();

  return (
    <div className='sticky top-0 bg-green-900 flex justify-between h-24 items-center p-6 text-white'>
      <img src='logo.png' alt='logo' className='' />
      <nav className='col-span-2 flex justify-between h-full items-center'>
        <div className='pl-4 pr-4 uppercase text-base font-medium'>
          <ul className='flex space-x-16'>
            <li key='e'>
              <Link to={"/bets"}>Eventos</Link>
            </li>
            {isNormal() ? (
              <li key='ha'>
                <Link to={"/historicoApostas"}>Histórico de Apostas</Link>
              </li>
            ) : (
              ""
            )}
            {isNormal() ? (
              <li key='ht'>
                <Link to={"/historicoTransicoes"}>Histórico de Transações</Link>
              </li>
            ) : (
              ""
            )}
            {isAdministrator() ? (
              <li key='ar'>
                <Link to={"/adminRegister"}>Registo de utilizadores</Link>
              </li>
            ) : (
              ""
            )}
            <li key='p'>
              <Link to={"/profile"}>Perfil</Link>
            </li>
          </ul>
        </div>
      </nav>
      <div className='flex items-center'>
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
