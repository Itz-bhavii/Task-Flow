import { useEffect, useState } from 'react';
import api from '../api';
import NavBar from '../components/NavBar';

export default function Dashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const response = await api.get('/dashboard');
        setData(response.data);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to load dashboard');
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  return (
    <>
      <NavBar />
      <div className="container">
        <h2>Dashboard</h2>
        {loading && <p className="muted">Loading...</p>}
        {error && <p className="error">{error}</p>}
        {data && (
          <div className="grid">
            <div className="card">
              <h3>Total Projects</h3>
              <p className="big-number">{data.projectCounts}</p>
            </div>
            <div className="card">
              <h3>Total Tasks</h3>
              <p className="big-number">{data.assignedTasks}</p>
            </div>
            <div className="card">
              <h3>Pending Tasks</h3>
              <p className="big-number">{data.pendingTasks}</p>
            </div>
            <div className="card">
              <h3>Completed Tasks</h3>
              <p className="big-number">{data.completedTasks}</p>
            </div>
          </div>
        )}
      </div>
    </>
  );
}

