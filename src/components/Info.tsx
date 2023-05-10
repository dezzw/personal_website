import memoji from '../assets/Image.png';

function Info() {
  return (
    <div className="info rounded-base bg-white px-8 pb-8 shadow">
      <img src={memoji} className="mb-6 h-[160px] w-[160px]" />
      <div>
        I'm Pengcheng Wang. You can call me "Desmond". I am an Information
        Security Student in University of Toronto. I am interested in Emacs,
        cybersecurity and full-stack developemnt.
      </div>
    </div>
  );
}

export default Info;
