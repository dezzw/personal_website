import { useState, useEffect, useRef } from 'react';
import html from '../assets/html.png';
import css from '../assets/css.png';
import js from '../assets/js.png';
import docker from '../assets/docker.png';
import python from '../assets/python.png';
import java from '../assets/java.png';
import git from '../assets/git.png';
import linux from '../assets/linux.png';
import react from '../assets/react.png';
import ts from '../assets/ts.png';
import neo4j from '../assets/neo4j.png';
import postgres from '../assets/postgres.png';

function Skills() {
  const [skills] = useState([
    ts,
    js,
    html,
    css,
    react,
    python,
    java,
    docker,
    git,
    linux,
    neo4j,
    postgres,
  ]);
	const [count, setCount] = useState(0);
  const [isScrolle, setIsScrolle] = useState(true);
  const speed = 30;
  const list = useRef(null);
	
  const hoverHandler = (flag: boolean) => setIsScrolle(flag);

  return (
    <div className="skills card">
      <div className="font-heading text-3xl">Skills</div>
      <div className="skill_list h-[90%] w-full overflow-auto my-4" ref={list}>
        {skills.map((src, key) => (
          <div key={key} className="text-center"
						onMouseOver={() => hoverHandler(false)}
            onMouseLeave={() => hoverHandler(true)}
					>
            <img src={src} className="my-4 inline-block w-[96px]" />
          </div>
        ))}
      </div>
    </div>
  );
}
export default Skills;
