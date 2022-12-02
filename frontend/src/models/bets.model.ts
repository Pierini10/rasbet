export interface Bet {
  winPercentage: Number;
  bets: SimpleBet[];
}

export interface Prediction {
  betState: String;
  event: String;
  idEvent: String;
  odd: Number;
  prediction: String;
}

export interface SimpleBet {
  amount: number;
  betState: String;
  dataTime: String;
  gamesLeft: Number;
  id: Number;
  idUser: Number;
  predictions: Prediction[];
  totalOdds: number;
}
