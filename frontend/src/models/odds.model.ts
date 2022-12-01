export interface EventOdds {
  eventID: string;
  odds: OddSimple[];
}

export interface OddSimple {
  entity: string;
  odd: number;
}
