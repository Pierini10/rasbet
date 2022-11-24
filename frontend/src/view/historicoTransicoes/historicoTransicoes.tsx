import { useEffect, useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Transaction } from "../../models/transaction.model";




function HistoricoTransicoes() {
    const [transicoes, setTransicoes] = useState<Transaction[]>([]);
    const { fetchdataAuth, id } = UseAuthentication()
    useEffect(() => {

        fetchdataAuth("http://localhost:8080/getTransactionsHistory", "GET", undefined, { userID: id }).then(
            (data) => {
                setTransicoes(data);
                console.log(data)
            }

        )


    }, [fetchdataAuth, id]);

    return (

        <div className="grid h-screen bg-gray-400 place-items-center ">
            <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">

                <div className="flex justify-center mt-5 text-4xl">
                    Histórico De Transações
                </div>
                <div className="flex justify-center mt-5">Saldo: 100,00 €</div>
                <div className="flex flex-col justify-center mt-5 space-y-4">
                    <div className="flex justify-center"> <hr className="border-black w-[90%]" /></div>
                    <div className="flex justify-around">
                        <div>Data</div>
                        <div>Descrição</div>
                        <div>Operação</div>
                        <div>Saldo após movimento</div>
                    </div>
                    <div className="flex justify-center"> <hr className="border-black w-[90%]" /></div>
                </div>

                <div className=" mt-5 overflow-auto text-xl h-[500px] flex flex-col justify-center ">
                    {transicoes && transicoes.map((transicao, key) => {
                        return (

                            <div className="flex py-5 text-base" key={key}>
                                <div className="flex justify-center w-[23%]">{transicao.date}</div>
                                <div className="flex justify-center w-[19%]">{transicao.description}</div>
                                <div className="flex justify-center w-[27%]">{transicao.value}€</div>
                                <div className="flex justify-center w-80">{transicao.balenceAfterTran
                                }€</div>

                            </div>
                        )
                    })}

                </div>

            </div>
        </div>
    );


}

export default HistoricoTransicoes;