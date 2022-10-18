import { Form } from "react-router-dom";

const Searchbar = () => {
  return (
    <div className="m-12 pl-3 pr-3 flex  items-center border-2 border-gray-500 rounded-full overflow-hidden space-x-3">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        strokeWidth={1.5}
        stroke="currentColor"
        className="w-6 h-6 text-gray-500"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
        />
      </svg>

      <Form id="search-form" role="search" className="w-full">
        <input
          id="q"
          className="w-full h-9"
          aria-label="Procura jogos"
          placeholder="Procura"
          type="search"
        />
      </Form>
    </div>
  );
};

export default Searchbar;
