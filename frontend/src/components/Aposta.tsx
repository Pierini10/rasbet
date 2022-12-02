import { Prediction, SimpleBet } from "../models/bets.model";



const bet = (bet: String) => {
    if (bet === 'draw') {
        return "Result: Empate";
    }
    return `Result: Winner - ${bet}`
}


const makeGame = (game: Prediction) => {
    return (
        <div className="ml-2">
            <div className="flex text-xl">{game.event} </div>
            <div className="flex ml-2 text-sm">{bet(game.prediction)}</div>
        </div>
    )

}

function Aposta(props: SimpleBet) {


    return (
        <div className="flex justify-center mt-5">
            <div className="container grid max-w-xl grid-cols-2 border border-black divide-x-4 rounded-lg">
                <div className="overflow-auto h-28 noScrollBar">
                    {props.predictions.map((prediction, index) => <div key={index}>{makeGame(prediction)}</div>)}
                </div>

                <div className="flex flex-col justify-around">
                    <div className="ml-2">Amount Staken: {props.amount.toString()}€</div>
                    <div className="ml-2">Bet State: {props.betState} </div>
                    <div className="flex ml-2">Possible Earnings: <div className="ml-5 text-orange-500"> {(props.amount * props.totalOdds).toString()}€</div></div>
                </div>
            </div>

        </div>
    )
}

export default Aposta;