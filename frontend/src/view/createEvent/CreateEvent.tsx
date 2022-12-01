import { useEffect, useState } from "react";
import Modal from "react-modal";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Competition } from "../../models/competition.model";

function CreateEvent() {
    const { fetchdataAuth } = UseAuthentication()
    const [sport, setSport] = useState("");
    const [competition, setCompetition] = useState("");
    const [costumeCompetition, setCostumeCompetition] = useState("");
    const [date, setDate] = useState("");
    const [description, setDescription] = useState("");
    const [sports, setSports] = useState<string[]>([]);
    const [competitions, setCompetitions] = useState<Competition>();
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [modalParticipantsModal, setModalParticipantsModal] = useState(false);
    const [participants, setParticipants] = useState<string[]>([]);
    const [participant, setParticipant] = useState("");
    useEffect(() => {
        fetchdataAuth("http://localhost:8080/getAllSports", "GET").then((data: string[] | undefined) => {
            if (data) {
                setSport(data[0]);
                setSports(data)
            }
        })
        fetchdataAuth("http://localhost:8080/getAllCompetitions", "GET").then((data: Competition | undefined) => {
            if (data) {
                setCompetitions(data)
            }
        })


    }, [setSports, fetchdataAuth])
    const createEvent = async (e: any) => {
        e.preventDefault();

        const event = {
            sport,
            competition, datetime: date, description
        };
        const success = await fetchdataAuth("http://localhost:8080/addEvent", "POST", JSON.stringify({ entetiesList: participants }), event)
        if (success) {
            alert("Event created")
        }
    }

    return (
        <div className="grid h-[90vh] bg-green-900 place-items-center ">
            <div className="max-w-5xl h-[80%] rounded-xl  bg-white container relative">
                <img src="logo.png" alt="logo" className="absolute rounded-lg left-5 top-5 " />
                <div className="grid h-full rounded place-items-center">



                    <div className="flex flex-col space-y-2" onSubmit={createEvent}>
                        <div className="text-4xl font-bold text-center text-green-900 mb-7">Create Event</div>
                        <select className="p-2 bg-white border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Sport" required onChange={(e) => setSport(e.target.value)} >{sports !== undefined && sports.map((sport, index) => <option key={index} value={sport}>{sport}</option>)}</select>
                        <div className="w-full">
                            <select value={competition} className="w-full p-2 bg-white border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" onChange={(e) => setCompetition(e.target.value)}>
                                {competitions !== undefined && competitions[sport] !== undefined && competitions[sport].map((c, index) => <option key={index} value={c}>{c}</option>)}
                                {costumeCompetition !== "" && <option value={costumeCompetition}>{costumeCompetition}</option>}
                            </select>

                        </div>
                        <input required onFocus={e => e.target.type = 'datetime-local'} onBlur={e => e.target.type = 'text'} className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Date" required onChange={(e) => setDate(e.target.value)} />
                        <div className="flex flex-col justify-start">
                            <input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400" placeholder="Description" required onChange={(e) => setDescription(e.target.value)} />
                            <div className="flex flex-row justify-around mt-1">
                                <button className="self-start text-xs text-blue-700 hover:underline" onClick={() => { setModalParticipantsModal(true) }}>Add participants</button>
                                <button className="text-xs text-blue-700 hover:underline" onClick={() => { setModalIsOpen(true) }}>Custom Competition</button>
                            </div>
                        </div>
                        <button className="px-4 py-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700 mt-[13.4rem]" onClick={createEvent}>Create Event</button>

                    </div>
                </div>
            </div>
            <Modal ariaHideApp={false} className="relative grid h-screen bg-transparent place-items-center" isOpen={modalIsOpen}>  <div className="relative px-10 pb-10 rounded-lg bg-slate-200">
                <button className="absolute right-5" onClick={() => setModalIsOpen(false)}>X</button>
                <div className="self-center mt-10 mb-10 text-3xl text-green-700 ">Costume Competition</div>
                <div className="flex justify-center"><input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-black" placeholder={costumeCompetition === "" ? " Custom Competition" : costumeCompetition} onChange={(e) => { setCostumeCompetition(e.target.value); setCompetition(e.target.value) }} /></div>
            </div>
            </Modal >

            <Modal ariaHideApp={false} className="relative grid h-screen bg-transparent place-items-center" isOpen={modalParticipantsModal}>  <div className="relative px-10 pb-10 rounded-lg bg-slate-200">
                <button className="absolute right-5" onClick={() => setModalParticipantsModal(false)}>X</button>
                <div className="flex justify-center mt-10 mb-10 text-3xl text-green-700 ">Participants</div>
                <div className="flex justify-center"><input className="p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-black" placeholder={costumeCompetition === "" ? " Custom Participant" : costumeCompetition} onChange={(e) => { setParticipant(e.target.value) }} value={participant} /></div>
                <div className="flex justify-center"><button className="px-4 py-2 mt-2 text-white duration-150 ease-in bg-orange-500 border rounded-xl hover:scale-110 hover:bg-orange-700" onClick={() => { if (participant !== "") participants.push(participant); setParticipant("") }}>Add Participant</button></div>
                <div className="grid grid-cols-2 mt-2">
                    {participants.map((p, index) => <div key={index} className="flex justify-between p-2 border border-green-900 rounded-lg placeholder:text-center placeholder:text-gray-400">{p}<button onClick={() => setParticipants(participants.filter((e) => p !== e))}>X</button></div>)}
                </div>
            </div>
            </Modal >
        </div >


    )
}

export default CreateEvent;