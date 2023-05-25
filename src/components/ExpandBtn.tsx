import { AiOutlineShrink, AiOutlineExpandAlt } from 'react-icons/ai';

type Props = {
  toggle: () => void;
  isExpand: boolean;
};

function ExpandBtn({ toggle, isExpand }: Props) {
  return (
    <>
      <div className="absolute bottom-4 left-4 box-content h-8 w-8 rounded-full border-2 border-solid border-[rgba(0,0,0,0.05)] bg-white shadow transition-all duration-200 ease-in hover:shadow-btn">
        <button
          className="flex h-full w-full items-center justify-center"
          onClick={toggle}
        >
          {isExpand ? (
            <AiOutlineExpandAlt className="text-2xl" />
          ) : (
            <AiOutlineShrink className="text-2xl" />
          )}
        </button>
      </div>
    </>
  );
}
export default ExpandBtn;
