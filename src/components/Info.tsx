import memoji from '../assets/Image.png';

function Info() {
  return (
    <div className="info rounded-base bg-white max-[1024px]:flex p-2 lg:px-8 lg:pb-8 shadow">
      <img src={memoji} className="w-[50%] lg:mb-6 lg:w-[160px]" />
      <div className="text-xs lg:text-base max-[1024px]:pt-4">
        I'm Pengcheng Wang. You can call me "Desmond". I am an Information
        Security Student in University of Toronto. I am interested in Emacs,
        cybersecurity and full-stack developemnt.
      </div>
    </div>
  );
}

export default Info;
