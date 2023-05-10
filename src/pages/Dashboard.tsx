import Info from '../components/Info';
import Icon from '../components/Icon';
import Contact from '../components/Contact';
import Edu from '../components/Edu';
import Emacs from '../components/Emacs';
import Nix from '../components/Nix';
import Skills from '../components/Skills';
import Experience from '../components/Experience';
import Projects from '../components/Projects';
import Todo from '../components/Todo';

function Dashboard() {
  return (
    <div className="dashboard h-[140vh] w-[80vw]">
      <Info />
			<Icon />
			<Todo />
      <Nix />
      <Emacs />
			<Projects />
			<Skills />
			<Experience />
      <Edu />
      <Contact />
    </div>
  );
}

export default Dashboard;
