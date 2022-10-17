interface ApostaProps {

    games: Game[];
    bet: number;
    gain: number;

}

interface Game {

    home: string;
    away: string;
    bet: string;

}

const bet = (bet: String) => {
    if (bet === 'draw') {
        return "Resultado: Empate";
    }
    return `Vencedor do Jogo: ${bet}`
}


const makeGame = (game: Game) => {
    return (
        <div >
            <div className="ml-2">
                <div className="flex text-xl">{game.home} - {game.bet} </div>
                <div className="flex ml-2 text-sm">{bet(game.bet)}</div>
            </div>
        </div>
    )

}

function Aposta(props: ApostaProps) {
    return (
        <div className="flex justify-center mt-5">
            <div className="container grid max-w-xl grid-cols-2 border border-black divide-x-4 rounded-lg">
                <div className="overflow-auto h-28 noScrollBar">
                    {props.games.map((g, index) => makeGame(g))}
                </div>
                <div className="flex flex-col justify-around">
                    <div className="ml-2">Montante Apostado: {props.bet}€</div>
                    <div className="flex ml-2">Total de ganhos: <div className="ml-5 text-orange-500"> {props.gain}€</div></div>
                </div>
            </div>

        </div>
    )
}

export default Aposta;