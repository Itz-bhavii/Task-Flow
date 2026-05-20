import { Link, useNavigate } from 'react-router-dom';

export default function NavBar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="nav">
      <div className="nav-left">
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/projects">Projects</Link>
      </div>
      <button className="link-button" type="button" onClick={handleLogout}>
        Logout
      </button>
    </nav>
  );
}
