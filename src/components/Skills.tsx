import { useState } from 'react';
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

  return (
    <div className="skills card">
      <div className="font-heading title">Skills</div>
      <div className="skill_list h-[90%] w-full overflow-auto my-4">
        {skills.map((src, key) => (
          <div key={key} className="text-center"
	  >
            <img src={src} className="my-4 inline-block w-[70px] lg:w-[96px]" />
          </div>
        ))}
      </div>
    </div>
  );
}
export default Skills;
