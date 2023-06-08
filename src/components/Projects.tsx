import { useState } from 'react';
import ExpandBtn from './ExpandBtn';

function Projects() {
  const defaultStyle = 'projects card relative';

  const [style, setStyle] = useState(defaultStyle);
  const [isExpand, setIsExpand] = useState(true);

  let toggle = () => {
    if (style === defaultStyle) {
      setStyle(style + ' w-[1215px] h-[595px] -top-[310px] z-50');
      setIsExpand(false);
    } else {
      setStyle(defaultStyle);
      setIsExpand(true);
    }
  };

  return (
    <div className={style}>
      <div className="font-heading title">Projects</div>
      <ExpandBtn toggle={toggle} isExpand={isExpand} />
    </div>
  );
}
export default Projects;
