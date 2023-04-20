import uoft from '../assets/uoft.png';

function Edu() {
	return (
		<div className="edu card p-8">
			<div className="font-heading text-3xl w-full">Education</div>
			<div className="flex">
				<img src={uoft} className="w-20 mt-4" />
				<div className="m-4">
					<div className="font-heading text-2xl">
						Universty of toronto
					</div>
					<div className="my-4">
						Specializing in <span className="text-prime">computer science</ span> and <span className="text-prime">information security</span>
					</div>
					<div className="my-4">
						Minoring in <span className="text-prime">mathematics</span>
					</div>
					<div className="my-4">
						<span className="text-prime">CGPA:</span> 3.71/4.0
					</div>
				</div>
			</div>
		</div>
	)
}

export default Edu;

