export interface Event {
  id: string;
  sport: string;
  competition: string;
  state: string;
  datetime: Date;
  description: string;
  result: string;
  odds: Map<string, Odd>;
}

export interface Odd {
  entity: string;
  odd: number;
  OddSup: boolean;
}

export function jsonToEvents(data: any[]) {
  const events: Event[] = [];

  data.forEach((elem: any) => {
    events.push({
      id: elem.id,
      competition: elem.competition,
      datetime: new Date(elem.datetime),
      description: elem.description,
      odds: new Map(Object.entries(elem.odds)),
      result: elem.result,
      sport: elem.sport,
      state: elem.state,
    });
  });

  events.sort((a, b) => (a.datetime.getTime() < b.datetime.getTime() ? -1 : 1));

  return events;
}
