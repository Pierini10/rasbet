import { useEffect, useState } from "react";
import { Event } from "../../models/event.model";

interface Data {
  events: Map<string, Event[]>;
}

interface SportInfo {
  sport: string;
  total: number;
  red: number;
  yellow: number;
  green: number;
}

const Progress = (props: Data) => {
  const [sportsInfo, setSportsInfo] = useState<SportInfo[]>([]);

  useEffect(() => {
    const newSportsInfo: SportInfo[] = [];
    props.events.forEach((events, sport) => {
      let green = 0,
        yellow = 0,
        red = 0;
      for (const event of events) {
        const size = event.odds.size;
        if (size === 0) red++;
        else if ((size === 2 && sport === "Basketball") || size === 3) green++;
        else yellow++;
      }

      newSportsInfo.push({
        green: green,
        red: red,
        yellow: yellow,
        sport: sport,
        total: events.length,
      });
    });

    setSportsInfo(newSportsInfo);
  }, [props]);

  return (
    <div className='fixed top-[72px] h-[90vh] mb-[30px] w-[22%] right-6 bg-gray-50 border-[1px] border-gray-500 rounded-3xl text-gray-800'>
      <p className='mt-10 text-center uppercase text-2xl font-medium'>
        Progress
      </p>
      <div>
        <ul className='mt-10 space-y-14 pr-5 pl-5 h-[75vh] overflow-y-auto'>
          {sportsInfo.map((si) => (
            <li key={si.sport}>
              <div className='bg-white w-full h-60 flex justify-between items-center pr-5 pl-5'>
                <div>
                  <div className='text-xl font-medium'>{si.sport}</div>
                  <div className='text-gray-500'>{"Total: " + si.total}</div>
                </div>
                <div className='space-y-3'>
                  <div className='h-12 w-20 bg-red-700 rounded-xl flex items-center justify-center text-lg font-medium '>
                    {si.red}
                  </div>
                  <div className='h-12 w-20 bg-yellow-500 rounded-xl flex items-center justify-center text-lg font-medium '>
                    {si.yellow}
                  </div>
                  <div className='h-12 w-20 bg-green-800 rounded-xl flex items-center justify-center text-lg font-medium text-white '>
                    {si.green}
                  </div>
                </div>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default Progress;
