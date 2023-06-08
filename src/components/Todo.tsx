import {
  TbBrandThreejs,
  TbBrandUnity,
	TbBrandBlender
} from 'react-icons/tb';

function Todo() {
  return (
    <div className="todo card">
      <div className="font-heading lg:text-3xl">Learning</div>
      <div className="font-body lg:text-5xl">
        <div className="flex my-2 lg:my-4 items-center">
          <TbBrandBlender /> <span className="text-sm lg:text-xl ml-2 lg:ml-4"> 3D Moduling</span>
        </div>
        <div className="flex my-2 lg:my-4 items-center">
          <TbBrandThreejs /> <span className="text-sm lg:text-xl ml-2 lg:ml-4">Three.js</span>
        </div>
        <div className="flex my-2 lg:my-4 items-center">
          <TbBrandUnity /> <span className="text-sm lg:text-xl ml-2 lg:ml-4">Unity</span>
        </div>
      </div>
    </div>
  );
}
export default Todo;
