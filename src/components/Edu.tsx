import uoft from '../assets/uoft.png';

function Edu() {
  return (
    <div className="edu card">
      <div className="w-full font-heading title">Education</div>
      <div className="flex">
        <img src={uoft} className="w-12 h-24 lg:mt-6 lg:w-20 lg:h-40" />
        <div className="ml-4 lg:m-4 text-xs lg:text-base">
          <div className="font-heading lg:text-2xl">Universty of toronto</div>
          <div className="my-1 lg:my-4">
            Specializing in <span className="text-prime">computer science</span>{' '}
            and <span className="text-prime">information security</span>
          </div>
          <div className="my-1 lg:my-4">
            Minoring in <span className="text-prime">mathematics</span>
          </div>
          <div className="my-1 lg:my-4">
            <span className="text-prime">CGPA:</span> 3.71/4.0
          </div>
        </div>
      </div>
    </div>
  );
}

export default Edu;
