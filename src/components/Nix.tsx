import nixos from '../assets/nixos.png';
import ExpandBtn from './ExpandBtn'

function Nix() {
  return (
    <div className="nix relative flex items-center justify-center rounded-base bg-[rgba(127,188,229,0.3)] p-8 shadow">
      <img src={nixos} />
			<ExpandBtn />
    </div>
  );
}

export default Nix;
