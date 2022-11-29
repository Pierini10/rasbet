interface Data {
  handleChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
  value: any;
  listValues: any[];
  label: string;
}

const SelectInput = (props: Data) => {
  return (
    <label className='text-gray-800 text-xl'>
      {props.label}
      <select value={props.value} onChange={props.handleChange}>
        {props.listValues.map((elem) => (
          <option key={elem} value={elem}>
            {elem}
          </option>
        ))}
      </select>
    </label>
  );
};

export default SelectInput;
