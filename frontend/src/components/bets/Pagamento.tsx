const Pagamento = () => {
  return (
    <div className='col-span-2 h-96 bg-white ml-20 mr-28 rounded-3xl flex flex-col justify-between pt-5 pb-5  text-gray-800'>
      <p className='text-xl font-semibold w-full text-center'>PAGAMENTO</p>
      <div className='flex'>
        <p>Método de pagamento:</p>
        <div>MB</div>
        <div>MBway</div>
        <div>Visa</div>
      </div>
      <div>Estado</div>
    </div>
  );
};

export default Pagamento;
