import Info from "../components/Info";
import Icon from "../components/Icon";
import Contact from "../components/Contact";
import Edu from "../components/Edu";

function Dashboard() {
  return (
    <div className="dashboard h-[140vh] w-[80vw]">
      <Info />
      <Icon />
      <div className="recent card"></div>
      <div className="Nix card"></div>
      <div className="Emacs card"></div>
      <div className="project1 card"></div>
      <div className="project2 card"></div>
      <div className="project3 card"></div>
			<Edu />
      <Contact />
    </div>
  );
}

export default Dashboard;
