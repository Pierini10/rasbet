/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState } from "react";

function Registo() {
    const [email, setemail] = useState("");
    const [password, setpassword] = useState("");
    const [data, setdata] = useState("")
    const [nif, setnif] = useState("")

    const updateEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
        setemail(e.target.value);
    }
    const updatePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setpassword(e.target.value);
    }
    const updateData = (e: React.ChangeEvent<HTMLInputElement>) => {
        setdata(e.target.value);
    }
    const updateNif = (e: React.ChangeEvent<HTMLInputElement>) => {
        setnif(e.target.value);
    }

    return (

        <div className="grid h-screen bg-green-900 place-items-center ">
            <div className="max-w-7xl h-[80%] rounded-xl  bg-white container relative">
                <div className="absolute text-4xl left-5 top-5">Rasbet</div>
                <div className="grid h-full rounded place-items-center">
                    <div className="flex flex-col justify-around w-full sm:flex-row">
                        <form className="flex flex-col space-y-7 ">
                            <div className="text-4xl font-bold text-center text-green-900 mb-7">Registo</div>
                            <input required type="email" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="E-mail" onChange={updateEmail} />
                            <input required type="password" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Palavra-passe" onChange={updatePassword} />
                            <input required type="text" className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="Data de Nascimento" onChange={updateData} />
                            <input required type="number" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Nif" onChange={updateNif} />
                            <button className="p-2 bg-orange-500 border rounded-xl">Concluir</button>
                        </form>

                        <img src="registoFoto.jpg" alt="stackphoto" className="container h-full max-w-xl rounded-xl" />

                    </div>
                </div>
            </div></div >
    )
}

export default Registo;