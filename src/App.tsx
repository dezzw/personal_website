import Dashboard from './pages/Dashboard';

function App() {
  return (
    <>
      <div className="flex items-center justify-center flex-col">
	<div className="logo m-2 flex h-24 items-center justify-center">
          dezzw
	</div>
	<div className="mb-4  mt-2 flex items-center justify-center">
          <Dashboard />
	</div>
      </div>
    </>
  );
}

export default App;
