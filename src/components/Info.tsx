import memoji from "../assets/Image.png";

function Info() {
  return (
    <div className="info card px-8">
      <img src={memoji} className="my-4 w-[160px] h-[160px]" />
      <div>I'm Pengcheng Wang. You can call me "Desmond". I am An Information Security Student in University of Toronto. I am interested in Emacs, cybersecurity and full-stack developemnt.</div>
    </div>
  );
}

export default Info;
