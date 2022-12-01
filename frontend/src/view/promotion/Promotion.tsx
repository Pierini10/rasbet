import { useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";

function Promotion() {
    const { fetchdataAuth } = UseAuthentication()

    const [createCode, setCreateCode] = useState("");
    const [description, setDescription] = useState("");
    const [discount, setDiscount] = useState(0);
    const [minValue, setMinValue] = useState(0);
    const [type, setType] = useState(1);
    const [deleteCode, setDeleteCode] = useState("");

    const createPromotion = async (e: any) => {
        e.preventDefault();
        const promotion = { code: createCode, description, discount, minValue, type }
        const success = await fetchdataAuth("http://localhost:8080/createPromotion", "POST", undefined, promotion)
        if (success) {
            alert("Promotion created")
        }
    }

    const deletePromotion = async (e: any) => {
        e.preventDefault();

        const success = await fetchdataAuth("http://localhost:8080/deletePromotion", "POST", undefined, { code: deleteCode })
        if (success) {
            alert("Promotion deleted")
        }
    }


    return (

        <div className="grid h-[90vh] bg-green-900 place-items-center ">
            <div className="max-w-5xl h-[80%] rounded-xl  bg-white container relative">
                <img src="logo.png" alt="logo" className="absolute rounded-lg left-5 top-5 " />
                <div className="grid h-full rounded place-items-center">

                    <div className="flex flex-row justify-around w-full">
                        <form className="flex flex-col space-y-2" onSubmit={createPromotion}>
                            <div className="text-4xl font-bold text-center text-green-900 mb-7">Create Promotion</div>
                            <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Code" required onChange={(e) => setCreateCode(e.target.value)} />
                            <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Description" required onChange={(e) => setDescription(e.target.value)} />
                            <input type="number" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Discount" required onChange={(e) => setDiscount(parseFloat(e.target.value))} />
                            <input type="number" className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Min Value" required onChange={(e) => setMinValue(parseFloat(e.target.value))} />
                            <select className="p-2 bg-white border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" onChange={(e) => setType(parseFloat(e.target.value))} >
                                <option value={1}> Percentage </option>
                                <option value={2}> Nominal </option>
                            </select>
                            <button className="px-4 py-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700">Create Promotion</button>
                        </form>
                        <form className="flex flex-col" onSubmit={deletePromotion}>
                            <div className="text-4xl font-bold text-center text-green-900 mb-7">Delete Promotion</div>
                            <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Code" required onChange={(e) => setDeleteCode(e.target.value)} />
                            <button className="px-4 py-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700 mt-[13.4rem]">Delete Promotion</button>
                        </form>


                    </div>





                </div>
            </div>
        </div >
    )
}

export default Promotion