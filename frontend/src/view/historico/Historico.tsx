import Aposta from "../../componentes/Aposta";
import './Historico.css';
const dummy_Date = {
    nome: "João",
    apelido: "Silva",
    apostas:

        [
            {
                games: [
                    {
                        home: 'Sporting',
                        away: 'varzim',
                        bet: 'Sporting',
                    },
                    {
                        home: 'Palmeiras',
                        away: 'São Paulo',
                        bet: 'draw',
                    },
                    {
                        home: 'Sporting',
                        away: 'varzim',
                        bet: 'Sporting',
                    }
                    ,
                    {
                        home: 'Sporting',
                        away: 'varzim',
                        bet: 'Sporting',
                    },
                ],
                bet: 100,
                gain: 200,
            },
            {
                games: [
                    {
                        home: 'Sporting',
                        away: 'varzim',
                        bet: 'Sporting',
                    },
                    {
                        home: 'Palmeiras',
                        away: 'São Paulo',
                        bet: 'draw',
                    }
                ],
                bet: 100,
                gain: 200,
            }
            ,
            {
                games: [
                    {
                        home: 'Sporting',
                        away: 'varzim',
                        bet: 'Sporting',
                    },
                    {
                        home: 'Palmeiras',
                        away: 'São Paulo',
                        bet: 'draw',
                    }
                ],
                bet: 100,
                gain: 200,
            }
        ]

}




function Historico() {
    return (
        <div className="grid h-screen bg-gray-400 place-items-center ">
            <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">
                <div className="flex justify-center mt-5 text-2xl">{dummy_Date.nome} {dummy_Date.apelido}</div>
                <div className="flex justify-center mt-5 font-bold">Histórico De Apostas</div>
                <div className="flex justify-center mt-5"><hr className="bg-gray-500 w-[90%]" /></div>
                <div className="flex mt-2 justify-evenly ">
                    <button className="px-10 py-1 text-orange-500 border border-orange-500 rounded-md"> Simples</button>
                    <button className="px-10 py-1 text-white bg-orange-500 border-orange-500 rounded-md"> Múltiplas</button>
                </div>
                <div className="flex justify-end mt-5 mr-2 text-xl">
                    <button className="w-8 ml-2 transition border border-black rounded-full hover:bg-orange-500"> {'>'} </button>
                </div>
                <div className="flex flex-col overflow-auto h-72 noScrollBar">
                    {dummy_Date.apostas.map((a, index) => <Aposta key={index} {...a} />)}
                </div>
            </div>
        </div>
    )
}

export default Historico;