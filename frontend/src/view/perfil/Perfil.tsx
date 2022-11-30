import { useEffect, useState } from "react";
import ChangeInfoForm from "../../components/ChangeInfoForm";
import LevantamentoDepositoModal from "../../components/LevantamentoDepositoModal";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { ProfileInfo } from "../../models/profile.model";



function Profile() {
    const [levantamentoModalIsOpen, setLevantamentoModalIsOpen] = useState(false);
    const [depositoModalIsOpen, setDepositoModalIsOpen] = useState(false);
    const [info, setInfo] = useState<ProfileInfo>();
    const { fetchdataAuth } = UseAuthentication()
    useEffect(() => {
        fetchdataAuth("http://localhost:8080/getUser", "GET").then(
            (data: ProfileInfo) => {
                if (data) {
                    setInfo(data)
                }
            }
        )
    }, [fetchdataAuth])

    return (
        <div className="grid h-[90vh] bg-gray-400 place-items-center">
            <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">
                <div className="flex justify-center mt-5 text-2xl">{info?.firstName} {info?.lastName}</div>
                <div className="flex justify-center mt-5">Balance: {info?.balance}€</div>
                <div className="flex justify-center mt-5"><hr className="bg-gray-500 w-[90%]" /></div>
                <div className="flex mt-2 justify-evenly ">
                    <button className="px-10 py-1 text-orange-500 duration-150 ease-in border border-orange-500 rounded-md hover:text-white hover:bg-orange-700" onClick={() => setLevantamentoModalIsOpen(true)}>Withdraw</button>
                    <button className="px-10 py-1 text-white duration-150 ease-in bg-orange-500 border-orange-500 rounded-md hover:text-white hover:bg-orange-700" onClick={() => setDepositoModalIsOpen(true)}>Deposit</button>
                </div>
                <div className="flex justify-center mt-5 text-xl sm:ml-20 sm:block">View Betting History
                    <a className="px-1 ml-2 transition border border-black rounded-full hover:bg-orange-500" href="/historicoApostas"> {'>'} </a>
                </div>
                <ChangeInfoForm info={info} updateInfo={setInfo} />

            </div>
            <LevantamentoDepositoModal isOpen={levantamentoModalIsOpen} onClose={setLevantamentoModalIsOpen} type="levantamento" />
            <LevantamentoDepositoModal isOpen={depositoModalIsOpen} onClose={setDepositoModalIsOpen} type="deposito" />
        </div >
    )
}

export default Profile;