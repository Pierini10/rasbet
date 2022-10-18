import SportButton from "./SportButton";

const listaDesportos: string[] = ["Futebol", "Basquetebol", "Ténis", "MotoGP"];

/**
 * a barra de cima falta melhorar com o simbolo
 */

const Navbar = () => {
  return (
    <div className="bg-green-900 grid grid-cols-3 h-24 items-center p-6 text-white">
      <nav className="col-span-2 flex justify-between h-full items-center">
        <img src="logo.png" alt="logo" className="" />
        <div className="pr-4 uppercase text-base font-medium">
          <ul className="flex space-x-20">
            <li>
              <SportButton sport="Todos" />
            </li>
            {listaDesportos.map((e) => (
              <li>
                <SportButton sport={e} />
              </li>
            ))}
          </ul>
        </div>
      </nav>
      <div className="flex place-content-end">Bem vindo, manel</div>
    </div>
  );
};

export default Navbar;
