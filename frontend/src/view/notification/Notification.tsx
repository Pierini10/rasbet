import { useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";

function Notification() {
    const { fetchdataAuth } = UseAuthentication()
    const [userEmail, setUserEmail] = useState("");
    const [description, setDescription] = useState("");

    const createNotification = async (e: any) => {
        e.preventDefault();
        const notification = {
            userEmail,
            description,
        };
        const success = await fetchdataAuth("http://localhost:8080/createNotification", "POST", undefined, notification)
        if (success) {
            alert("Notification created")
        }
    }

    return (
        <div className="grid h-[90vh] bg-green-900 place-items-center ">
            <div className="max-w-5xl h-[80%] rounded-xl  bg-white container relative">
                <img src="logo.png" alt="logo" className="absolute rounded-lg left-5 top-5 " />
                <div className="mt-20">

                    <div className="flex flex-row justify-center w-full">

                        <form className="flex flex-col space-y-10" onSubmit={createNotification}>
                            <div className="text-4xl font-bold text-center text-green-900 mb-7">Create Notification</div>
                            <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="User Email" required onChange={(e) => setUserEmail(e.target.value)} />

                            <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Description" required onChange={(e) => setDescription(e.target.value)} />
                            <button className="px-4 py-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700 mt-[13.4rem]">Create Notification</button>
                            <div className="text-sm">For a global notification please insert <span className="text-green-700">rasbet@rasbet.com</span> in user Email field</div>
                        </form>


                    </div>





                </div>
            </div>
        </div >
    )
}

export default Notification;