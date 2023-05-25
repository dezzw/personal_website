import { useState } from 'react';
import nix from '../assets/nix.svg';
import ExpandBtn from './ExpandBtn';

function Nix() {
  const defaultStyle = 'nix relative flex items-center justify-center rounded-base bg-[#d6e8f4] p-8 shadow';

  const [style, setStyle] = useState(defaultStyle);
  const [isExpand, setIsExpand] = useState(true);

  let toggle = () => {
    if (style === defaultStyle) {
      setStyle(style + ' w-[1215px] h-[595px] z-50');
      setIsExpand(false);
    } else {
      setStyle(defaultStyle);
      setIsExpand(true);
    }
  };

  return (
    <div className={style}>
      <img src={nix}/>
      <ExpandBtn toggle={toggle} isExpand={isExpand} />
    </div>
  );
}

export default Nix;
