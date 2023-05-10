import { BsArrowUpRight } from 'react-icons/bs';

function ExpandBtn() {
  return (
    <>
      <div className="absolute bottom-4 left-4 box-content h-8 w-8 rounded-full border-2 border-solid border-[rgba(0,0,0,0.05)] bg-white shadow transition-all duration-200 ease-in hover:shadow-btn">
        <div className="flex h-full w-full items-center justify-center">
          <BsArrowUpRight className="text-xl" />
        </div>
      </div>
    </>
  );
}
export default ExpandBtn;
