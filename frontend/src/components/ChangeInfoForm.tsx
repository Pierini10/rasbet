import { useState } from "react"
import { UseAuthentication } from "../contexts/authenticationContext"
import { ChangeableInfo } from "../models/info.model"

const dummy_Date = {
    nome: "João",
    apelido: "Silva",
    saldo: 100,
    pass: "1234",
}
function ChangeInfoForm() {
    const { fetchdataAuth } = UseAuthentication()
    const [info, setInfo] = useState<ChangeableInfo>();
    const handleSubmit = async () => {
        await fetchdataAuth("http://localhost:8080/changeInfo", "POST", undefined, info)
        setInfo({})
    }
    return (
        <div>
            <div className="flex flex-col">
                <div className="flex mt-5 ml-20 text-center">
                    <div>Email:&#160;</div>
                    <div className="container flex flex-col justify-center max-w-xs border border-black rounded-lg h-[2rem] ml-96">{dummy_Date.nome}</div>
                </div>
                <div className="flex mt-5 ml-20 text-center">
                    <div>Nome:</div>
                    <div className="container flex flex-col justify-center max-w-xs border border-black rounded-lg h-[2rem] ml-96">{dummy_Date.nome}</div>
                </div>
                <div className="flex mt-5 ml-20 text-center">
                    <div>Apelido:</div>
                    <div className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[23.2rem]">{dummy_Date.apelido}</div>
                </div>
                <div className="flex mt-5 ml-20 text-center">
                    <div>Morada:</div>
                    <div className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[23.2rem]">{dummy_Date.apelido}</div>
                </div>
                <div className="flex mt-5 ml-20 text-center">
                    <div >Número de telemovel:</div>
                    <div className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg lg:ml-[17rem] ml-[23.2rem]">{'*'.repeat(dummy_Date.pass.length)}</div>
                </div>
                <div className="flex mt-5 ml-20 text-center">
                    <div >Mudar Palavra-Passe:</div>
                    <div className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg lg:ml-[17.5rem] ml-[23.2rem]">{'*'.repeat(dummy_Date.pass.length)}</div>
                </div>
            </div>
            <div className="flex justify-center w-full mt-10 "><button onClick={handleSubmit} className="px-4 py-1 text-white duration-150 ease-in bg-orange-500 rounded hover:scale-110 hover:bg-orange-700">Submeter</button></div>
        </div>)
}

export default ChangeInfoForm;