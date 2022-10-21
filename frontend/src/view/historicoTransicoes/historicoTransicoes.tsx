
const dummy_Date = {
    nome: "João",
    apelido: "Silva",
    apostas: [
        {
            games: [
                {
                    home: "Sporting",
                    away: "varzim",
                    bet: "Sporting",
                },
                {
                    home: "Palmeiras",
                    away: "São Paulo",
                    bet: "draw",
                },
                {
                    home: "Sporting",
                    away: "varzim",
                    bet: "Sporting",
                },
                {
                    home: "Sporting",
                    away: "varzim",
                    bet: "Sporting",
                },
            ],
            bet: 100,
            gain: 200,
        },
        {
            games: [
                {
                    home: "Sporting",
                    away: "varzim",
                    bet: "Sporting",
                },
                {
                    home: "Palmeiras",
                    away: "São Paulo",
                    bet: "draw",
                },
            ],
            bet: 100,
            gain: 200,
        },
        {
            games: [
                {
                    home: "Sporting",
                    away: "varzim",
                    bet: "Sporting",
                },
                {
                    home: "Palmeiras",
                    away: "São Paulo",
                    bet: "draw",
                },
            ],
            bet: 100,
            gain: 200,
        },
    ],
};


function HistoricoTransicoes() {
    return (

        <div className="grid h-screen bg-gray-400 place-items-center ">
            <div className=" max-w-5xl bg-white border-dotted h-[80%] container rounded-3xl border-black border">

                <div className="flex justify-center mt-5 text-4xl">
                    Histórico De Transações
                </div>
                <div className="flex justify-center mt-5">Saldo: 100,00</div>
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

                <div className="flex justify-end mt-5 mr-2 text-xl">

                </div>

            </div>
        </div>
    );


}

export default HistoricoTransicoes;