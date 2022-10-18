interface Data {
  ent: string;
  odd: string;
}

const Oddbutton = (data: Data) => {
  return (
    <button className="h-full w-full border-[1px] rounded-xl border-green-900 items-center space-y-1 font-bold">
      <div>{data.ent}</div>
      <div>{data.odd}</div>
    </button>
  );
};

export default Oddbutton;
