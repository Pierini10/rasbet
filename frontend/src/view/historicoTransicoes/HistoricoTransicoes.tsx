import { useEffect, useState } from "react";
import { UseAuthentication } from "../../contexts/authenticationContext";
import { Transaction } from "../../models/transaction.model";




function HistoricoTransicoes() {
    const [transicoes, setTransicoes] = useState<Transaction[]>([]);
    const { fetchdataAuth, id, balance, setBalance } = UseAuthentication()

    useEffect(() => {
        fetchdataAuth(`http://localhost:8080/getBalance`, "GET").then((data) => {
            setBalance(data)
        })
        fetchdataAuth("http://localhost:8080/getTransactionsHistory", "GET").then(
            (data: Transaction[]) => {
                if (data !== undefined) {
                    setTransicoes(data.reverse());

                }
            }

        )


    }, [fetchdataAuth, id, setBalance]);

    return (

        <div className="grid h-[90vh] bg-gray-400 place-items-center ">
            <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">

                <div className="flex justify-center mt-5 text-4xl">
                    Transaction History
                </div>
                <div className="flex justify-center mt-5">Balance: {balance} €</div>
                <div className="flex flex-col justify-center mt-5 space-y-4">
                    {transicoes.length > 0 &&
                        <div>
                            <div className="flex justify-center"> <hr className="border-black w-[90%]" /></div>
                            <div className="flex justify-around ml-2">
                                <div>Date</div>
                                <div>Description</div>
                                <div>Operation</div>
                                <div>Balance after Transaction</div>
                            </div>
                            <div className="flex justify-center"> <hr className="border-black w-[90%]" />
                            </div>
                        </div>}
                </div>

                <div className=" mt-5 overflow-auto text-xl h-[500px] flex flex-col">
                    {transicoes.length > 0 && transicoes.map((transicao, key) => {
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
                    {transicoes.length === 0 &&

                        <div className="flex justify-center py-5 text-3xl" >
                            Não existe transações para esta conta.

                        </div>

                    }

                </div>

            </div>

        </div>
    );


}

export default HistoricoTransicoes;