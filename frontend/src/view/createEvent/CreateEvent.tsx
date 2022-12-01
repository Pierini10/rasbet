import { useEffect, useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";

function CreateEvent() {
    const { fetchdataAuth } = UseAuthentication()
    const [sport, setSport] = useState("");
    const [competition, setCompetition] = useState("");
    const [date, setDate] = useState("");
    const [description, setDescription] = useState("");
    const [sports, setSports] = useState<string[]>([]);
    useEffect(() => {
        fetchdataAuth("http://localhost:8080/getAllSports", "GET").then((data: string[] | undefined) => {
            if (data) {
                setSports(data)
            }
        })
    })
    const createEvent = async (e: any) => {
        e.preventDefault();

        const event = {
            sport,
            competition, datetime: date, description
        };
        const success = await fetchdataAuth("http://localhost:8080/addEvent", "POST", undefined, event)
        if (success) {
            alert("Event created")
        }
    }

    return (
        <div className="grid h-[90vh] bg-green-900 place-items-center ">
            <div className="max-w-5xl h-[80%] rounded-xl  bg-white container relative">
                <img src="logo.png" alt="logo" className="absolute rounded-lg left-5 top-5 " />
                <div className="grid h-full rounded place-items-center">



                    <form className="flex flex-col space-y-2" onSubmit={createEvent}>
                        <div className="text-4xl font-bold text-center text-green-900 mb-7">Create Event</div>
                        <select className="p-2 bg-white border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Sport" required onChange={(e) => setSport(e.target.value)} >{sports !== undefined && sports.map((sport) => <option value={sport}>{sport}</option>)}</select>
                        <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Competition" required onChange={(e) => setCompetition(e.target.value)} />
                        <input onFocus={e => e.target.type = 'datetime-local'} onBlur={e => e.target.type = 'text'} className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Date" required onChange={(e) => setDate(e.target.value)} />
                        <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Description" required onChange={(e) => setDescription(e.target.value)} />
                        <button className="px-4 py-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700 mt-[13.4rem]">Create Notification</button>
                        <div className="text-sm">The Description needs to be of the format: <span className="text-green-800">Team1 v Team2</span></div>
                    </form>

                </div>
            </div>
        </div>
    )
}

export default CreateEvent;