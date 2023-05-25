import { useState } from 'react';
import emacs from '../assets/emacs.png';
import ExpandBtn from './ExpandBtn';

function Emacs() {
  const defaultStyle = 'emacs card relative flex items-center justify-center bg-gradient-to-br from-[#837ec4] to-[#963ec1]';

  const [style, setStyle] = useState(defaultStyle);
  const [isExpand, setIsExpand] = useState(true);

  let toggle = () => {
    if (style === defaultStyle && isExpand) {
      setStyle(style + ' w-[1215px] h-[595px] -left-[310px] z-50');
      setIsExpand(false);
    } else {
      setStyle(defaultStyle);
      setIsExpand(true);
    }
  };

  return (
    <div className={style}>
      <img src={emacs} />
      <ExpandBtn toggle={toggle} isExpand={isExpand} />
    </div>
  );
}

export default Emacs;
