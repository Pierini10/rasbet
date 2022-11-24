
import { Dispatch, useState } from "react";
import Modal from "react-modal";
import { UseAuthentication } from "../contexts/authenticationContext";
function LevantamentoModal(props: { isOpen: boolean, onClose: Dispatch<boolean>, type: "deposito" | "levantamento" }) {
    const [step, setStep] = useState<"choices" | "MBway" | "Card" | "Bank">("choices");
    const [value, setValue] = useState(0);

    const { fetchdataAuth, id } = UseAuthentication()

    const withdrawMoney = async () => {
        const val = props.type === "deposito" ? value : -value;
        const data = await fetchdataAuth("http://localhost:8080/withdrawDeposit", "POST", undefined, { userID: id, amount: val, method: step });
        if (typeof data === "string") {
            alert(data)
        }
        else {
            props.type === "levantamento" ? alert("Levantamento realizado com sucesso!") : alert("Deposito realizado com sucesso!")
            props.onClose(false)
        }
    }

    const updateValue = (e: React.ChangeEvent<HTMLInputElement>) => {
        setValue(Number(e.target.value))
    }

    return (
        <Modal ariaHideApp={false} isOpen={props.isOpen} className="relative grid h-screen bg-transparent place-items-center">
            <div className="relative px-10 pb-10 rounded-lg bg-slate-200">
                <button className="absolute right-3" onClick={() => { setStep("choices"); props.onClose(false) }}>X</button>
                {step === "choices" &&
                    <div>
                        <div className="flex justify-center mb-5 text-2xl text-black">{props.type === "levantamento" ? "Levantar" : "Deposito"}</div>
                        <div className="flex space-x-3">
                            <button className="flex items-center justify-center w-32 h-20 duration-100 ease-in bg-white rounded hover:bg-slate-400" onClick={() => setStep("Bank")}><img src="Levantar/transferencia_bancaria.png" alt="bank" className=" w-14 aspect-square" /></button>
                            <button className="flex items-center justify-center w-32 h-20 duration-100 ease-in bg-white rounded hover:bg-slate-400" onClick={() => setStep("MBway")}><img src="Levantar/LogoMBWay.png" alt="mbway" className="w-20 h-16 p-2 aspect-square" /></button>
                            <button className="flex items-center justify-center w-32 h-20 duration-100 ease-in bg-white rounded hover:bg-slate-400" onClick={() => setStep("Card")}><img src="Levantar/visa-mastercard-logo.png" alt="card" className="w-20 h-12 aspect-square" /></button>
                        </div>
                    </div>
                }
                {step === "Bank" && <div>
                    <div className="flex justify-center mb-5 text-2xl text-black">Transferência Bancária</div>
                    <div className="flex flex-col py-1 pl-2 space-y-4">
                        <input placeholder="value" className="py-1 pl-2" onChange={updateValue} />
                        <input placeholder="NIB" className="py-1 pl-2" />
                        <button className="px-5 py-1 text-white rounded bg-slate-400 hover:bg-slate-600" onClick={withdrawMoney}>Confirmar</button>
                    </div>
                </div>}
                {step === "MBway" &&
                    <div>
                        <div className="flex justify-center mb-5 text-2xl text-black">MBWay</div>
                        <div className="flex flex-col py-1 pl-2 space-y-4">
                            <input placeholder="value" className="py-1 pl-2" onChange={updateValue} />
                            <input placeholder="numero de telemovel" className="py-1 pl-2" />
                            <button className="px-5 py-1 text-white rounded bg-slate-400 hover:bg-slate-600" onClick={withdrawMoney}>Confirmar</button>
                        </div>
                    </div>}
                {step === "Card" &&
                    <div>
                        <div className="flex justify-center mb-5 text-2xl text-black">Bank Card</div>
                        <div className="flex flex-col py-1 pl-2 space-y-4">
                            <input placeholder="value" className="py-1 pl-2" onChange={updateValue} />
                            <input placeholder="numero de cartão" className="py-1 pl-2" />
                            <div className="space-x-2">
                                <input placeholder="numero de segurança" className="py-1 pl-2" type="Number" />
                                <input placeholder="data de validade" className="py-1 pl-2" type="month" />
                            </div>
                            <button className="px-5 py-1 text-white rounded bg-slate-400 hover:bg-slate-600" onClick={withdrawMoney}>Confirmar</button>
                        </div>
                    </div>}
            </div>
        </Modal >)
}

export default LevantamentoModal