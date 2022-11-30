import { Dispatch, useState } from "react"
import { UseAuthentication } from "../contexts/authenticationContext"
import { ChangeableInfo } from "../models/info.model"
import { ProfileInfo } from "../models/profile.model"


function ChangeInfoForm(props: { info: ProfileInfo | undefined, updateInfo: Dispatch<ProfileInfo> }) {
    const { fetchdataAuth } = UseAuthentication()


    const [email, setEmail] = useState<string | undefined>()
    const [nome, setNome] = useState<string | undefined>()
    const [apelido, setApelido] = useState<string | undefined>()
    const [morada, setMorada] = useState<string | undefined>()
    const [pass, setPass] = useState<string | undefined>()
    const [phone, setPhone] = useState<string | undefined>()

    const handleSubmit = async () => {
        const info: ChangeableInfo = {}
        if (email) info.email = email
        if (nome) info.firstName = nome
        if (apelido) info.lastName = apelido
        if (morada) info.address = morada
        if (pass) info.password = pass
        if (phone) info.phoneNumber = phone



        await fetchdataAuth("http://localhost:8080/changeInfo", "POST", undefined, info).then(
            (data: boolean) => {
                if (data) {
                    alert("Informação alterada com sucesso")
                    fetchdataAuth("http://localhost:8080/getUser", "POST").then(
                        (data: ProfileInfo) => {
                            if (data) {
                                props.updateInfo(data)
                            }
                        }
                    )
                } else {
                    alert("Erro ao alterar informação")
                }
            }
        )


    }
    return (

        <div className="flex flex-col overflow-auto">
            <div className="flex mt-5 ml-20 text-center">
                <div>Email:&#160;</div>
                <input className="container flex flex-col justify-center max-w-xs border border-black rounded-lg h-[2rem] ml-96 pl-2 placeholder:text-black" onChange={(e) => setEmail(e.target.value)} placeholder={props.info?.email} />
            </div>
            <div className="flex mt-5 ml-20 text-center">
                <div>Name:</div>
                <input className="container flex flex-col justify-center max-w-xs border border-black rounded-lg h-[2rem] ml-96 pl-2 placeholder:text-black" onChange={(e) => { setNome(e.target.value) }} placeholder={props.info?.firstName} />
            </div>
            <div className="flex mt-5 ml-20 text-center">
                <div>Surname:</div>
                <input className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[22.7rem] pl-2 placeholder:text-black" onChange={(e) => setApelido(e.target.value)} placeholder={props.info?.lastName} />
            </div>
            <div className="flex mt-5 ml-20 text-center">
                <div>Address:</div>
                <input className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[23.2rem] pl-2 placeholder:text-black" onChange={(e) => setMorada(e.target.value)} placeholder={props.info?.address} />
            </div>
            <div className="flex mt-5 ml-20 text-center">
                <div >Phone Number:</div>
                <input className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[20rem]  placeholder:text-black pl-2" type="number" onChange={(e) => setPhone(e.target.value)} placeholder={props.info?.phoneNumber} />
            </div>
            <div className="flex mt-5 ml-20 text-center">
                <div >Change Password:</div>
                <input className="container max-w-xs h-[2rem]  flex flex-col justify-center border border-black rounded-lg ml-[18.9rem]  pl-2 placeholder:text-black" type="password" onChange={(e) => setPass(e.target.value)} placeholder="Password" />

            </div >
            <div className="flex justify-center w-full mt-10 "><button onClick={handleSubmit} className="px-6 py-2 text-white duration-150 ease-in bg-orange-500 rounded hover:bg-orange-700">Submit</button></div>
        </div >
    )
}

export default ChangeInfoForm;