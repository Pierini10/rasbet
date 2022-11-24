import { useState } from "react";
import LevantamentoDepositoModal from "../../components/LevantamentoDepositoModal";

const dummy_Date = {
    nome: "João",
    apelido: "Silva",
    saldo: 100,
    pass: "1234",
}

function Profile() {
    const [levantamentoModalIsOpen, setLevantamentoModalIsOpen] = useState(false);
    const [depositoModalIsOpen, setDepositoModalIsOpen] = useState(false);
    return (
        <div className="grid h-screen bg-gray-400 place-items-center">
            <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">
                <div className="flex justify-center mt-5 text-2xl">{dummy_Date.nome} {dummy_Date.apelido}</div>
                <div className="flex justify-center mt-5">Saldo: {dummy_Date.saldo}€</div>
                <div className="flex justify-center mt-5"><hr className="bg-gray-500 w-[90%]" /></div>
                <div className="flex mt-2 justify-evenly ">
                    <button className="px-10 py-1 text-orange-500 duration-150 ease-in border border-orange-500 rounded-md hover:text-white hover:bg-orange-700" onClick={() => setLevantamentoModalIsOpen(true)}> Levantar</button>
                    <button className="px-10 py-1 text-white duration-150 ease-in bg-orange-500 border-orange-500 rounded-md hover:text-white hover:bg-orange-700" onClick={() => setDepositoModalIsOpen(true)}> Depositar</button>
                </div>
                <div className="flex justify-center mt-5 text-xl sm:ml-20 sm:block">Consultar Histórico de apostas
                    <a className="px-1 ml-2 transition border border-black rounded-full hover:bg-orange-500" href="/historicoApostas"> {'>'} </a>
                </div>
                <div className="flex flex-col">
                    <div className="flex mt-5 ml-20 text-center">
                        <div>Nome:</div>
                        <div className="container flex flex-col justify-center max-w-xs border border-black rounded-lg h-[2rem] ml-96">{dummy_Date.nome}</div>
                    </div>
                    <div className="flex mt-5 ml-20 text-center">
                        <div>Apelido:</div>
                        <div className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[23.2rem]">{dummy_Date.apelido}</div>
                    </div>
                    <div className="flex mt-5 ml-20 text-center">
                        <div >Mudar Palavra-Passe:</div>
                        <div className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg lg:ml-[17.5rem] ml-[23.2rem]">{'*'.repeat(dummy_Date.pass.length)}</div>
                    </div>
                </div>
            </div>
            <LevantamentoDepositoModal isOpen={levantamentoModalIsOpen} onClose={setLevantamentoModalIsOpen} type="levantamento" />
            <LevantamentoDepositoModal isOpen={depositoModalIsOpen} onClose={setDepositoModalIsOpen} type="deposito" />
        </div >
    )
}

export default Profile;