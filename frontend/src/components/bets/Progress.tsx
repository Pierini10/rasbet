interface Data {
  sports: Map<string, SportInfo>;
}

interface SportInfo {
  total: number;
  red: number;
  yellow: number;
  green: number;
}

const Progress = () => {
  return (
    <div className='fixed top-[72px] bottom-[30px] w-[22%] right-6 bg-gray-50 border-[1px] border-gray-500 rounded-3xl text-gray-800'>
      <p className='mt-10 text-center uppercase text-2xl font-medium'>
        Progresso
      </p>
      <div>
        {/* {Array.from(props.sports.keys()).map((sport) => (
          <div></div>
        ))} */}
      </div>
    </div>
  );
};

export default Progress;
