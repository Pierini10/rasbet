interface Data {
  sport: string;
}

const SportButton = (props: Data) => {
  return (
    <button className="uppercase text-base font-medium">{props.sport}</button>
  );
};

export default SportButton;
