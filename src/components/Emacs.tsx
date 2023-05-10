import emacs from '../assets/emacs.png';
import ExpandBtn from './ExpandBtn'

function Emacs() {
  return (
    <div className="emacs card relative bg-gradient-to-br from-[#837ec4] to-[#963ec1]">
      <img src={emacs} />
			<ExpandBtn />
    </div>
  );
}

export default Emacs;
