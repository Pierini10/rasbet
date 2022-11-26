/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState } from "react";
import { register } from "../../api/user";
import { UseAuthentication } from "../../contexts/authenticationContext";

function AdminRegister() {
    const { token } = UseAuthentication()
    const [email, setemail] = useState("");
    const [password, setpassword] = useState("");
    const [name, setname] = useState("");
    const [surname, setsurname] = useState("");
    const [nif, setnif] = useState("");
    const [cc, setcc] = useState("");
    const [address, setaddress] = useState("");
    const [phone, setphone] = useState("");
    const [birthday, setbirthday] = useState("")
    const [role, setrole] = useState<"Specialist" | "Administrator">("Specialist");


    const handleSubmit = async (e: any) => {
        e.preventDefault();

        const sucess = await register({ email, password: password, firstName: name, lastName: surname, NIF: parseInt(nif), CC: parseInt(cc), address, phoneNumber: phone, birthday, role }, token);

        if (sucess) {
            alert("User created with success")
        }
        else {
            alert("Error creating user")
        }
    }


    const updateEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
        setemail(e.target.value);
    }
    const updatePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setpassword(e.target.value);
    }
    const updateName = (e: React.ChangeEvent<HTMLInputElement>) => {
        setname(e.target.value);
    }
    const updateSurname = (e: React.ChangeEvent<HTMLInputElement>) => {
        setsurname(e.target.value);
    }
    const updateNif = (e: React.ChangeEvent<HTMLInputElement>) => {
        setnif(e.target.value);
    }
    const updateCc = (e: React.ChangeEvent<HTMLInputElement>) => {
        setcc(e.target.value);
    }
    const updateAddress = (e: React.ChangeEvent<HTMLInputElement>) => {
        setaddress(e.target.value);
    }
    const updatePhone = (e: React.ChangeEvent<HTMLInputElement>) => {
        setphone(e.target.value);
    }
    const updateBday = (e: React.ChangeEvent<HTMLInputElement>) => {
        setbirthday(e.target.value);
    }
    const updateRole = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setrole(e.target.value as "Specialist" | "Administrator");
    }


    return (

        <div className="grid h-screen bg-green-900 place-items-center ">
            <div className="max-w-5xl h-[80%] rounded-xl  bg-white container relative">
                <img src="logo.png" alt="logo" className="absolute rounded-lg left-5 top-5 " />
                <div className="grid h-full rounded place-items-center">
                    <div className="flex flex-row justify-around w-full">
                        <form className="flex flex-col justify-center space-y-7 " onSubmit={handleSubmit} >

                            <div className="text-4xl font-bold text-center text-green-900 mb-7">Registo</div>
                            <div className="grid gap-1 md:gap-10 sm:grid-cols-2">
                                <input required type="email" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="E-mail" onChange={updateEmail} />
                                <input required type="password" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Palavra-passe" onChange={updatePassword} />
                                <input required type="text" className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="Nome" onChange={updateName} />
                                <input required type="text" className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="Apelido" onChange={updateSurname} />
                                <input required type="number" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Nif" onChange={updateNif} />
                                <input required type="number" className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="CC" onChange={updateCc} />
                                <input required type="text" className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="Morada" onChange={updateAddress} />
                                <input required type="number" className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="Telemovel" onChange={updatePhone} />
                                <input required onFocus={e => e.target.type = 'date'} onBlur={e => e.target.type = 'text'} className="p-2 border border-green-900 rounded-lg appearance-none placeholder:text-center placeholder:text-gray-400" placeholder="Data de Nascimento" onChange={updateBday} />
                                <select onChange={(e) => updateRole(e)}>
                                    <option value="Specialist"> Especialista </option>
                                    <option value="Administrator"> Administrador </option>
                                </select>
                            </div>
                            <div className="flex justify-center w-full">
                                <button className="px-4 py-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700">Concluir</button>
                            </div>
                        </form>



                    </div>
                </div>
            </div>
        </div >
    )
}

export default AdminRegister;