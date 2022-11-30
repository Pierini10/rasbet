/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../api/client";

import { UseAuthentication } from "../../contexts/authenticationContext";

function Login() {
    const navigate = useNavigate();
    const { saveToken } = UseAuthentication();

    const [email, setemail] = useState("");
    const [password, setpassword] = useState("");

    const updateEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
        setemail(e.target.value);
    }
    const updatePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setpassword(e.target.value);
    }

    const makeLogin = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const response = await login("validate@gmail.com", "Test3aut!");

        saveToken(response);

        navigate("/")

    }

    return (
        <div className="grid h-screen bg-green-900 place-items-center ">
            <div className="max-w-5xl h-[80%] rounded-xl  bg-white container relative">
                <img src="logo.png" alt="logo" className="absolute rounded-lg left-5 top-5 " />
                <div className="grid h-full rounded place-items-center">
                    <div className="flex flex-row justify-around w-full">
                        <form className="flex flex-col justify-center space-y-7" onSubmit={e => makeLogin(e)}>
                            <div className="text-4xl font-bold text-center text-green-900 mb-7">Welcome</div>
                            <input type="email" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Email" onChange={updateEmail} />
                            <div className="flex flex-col">
                                <input type="password" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Password" onChange={updatePassword} />
                                <a href="/" className="flex justify-end mt-1 text-xs text-green-900 underline">Forgot Password</a>
                            </div>
                            <button className="p-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:bg-orange-700" type="submit" >Access</button>
                            <div>
                                <div className="flex justify-center w-full">Don't have an account?</div>
                                <a href="/register" className="flex justify-center w-full text-green-900 underline">Register now!</a>
                            </div>
                        </form>

                        <img src="defaultPhoto.png" alt="stackphoto" className="container hidden max-w-xl sm:block sm:h-1/2 rounded-xl" />

                    </div>
                </div>
            </div>
        </div >
    )
}

export default Login;