import {
  TbBrandThreejs,
  TbBrandUnity,
	TbBrandBlender
} from 'react-icons/tb';

function Todo() {
  return (
    <div className="todo card">
      <div className="font-heading text-3xl">Learning</div>
      <div className="font-body text-5xl">
        <div className="flex my-4 items-center">
          <TbBrandBlender /> <span className="text-xl ml-4"> 3D Moduling</span>
        </div>
        <div className="flex my-4 items-center">
          <TbBrandThreejs /> <span className="text-xl ml-4">Three.js</span>
        </div>
        <div className="flex my-4 items-center">
          <TbBrandUnity /> <span className="text-xl ml-4">Unity</span>
        </div>
      </div>
    </div>
  );
}
export default Todo;
