import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import NavBar from '../components/NavBar';

export default function Projects() {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [listError, setListError] = useState('');
  const [loading, setLoading] = useState(true);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [projectId, setProjectId] = useState('');
  const [openProjectError, setOpenProjectError] = useState('');
  const [createErrors, setCreateErrors] = useState({ name: '', description: '' });

  const fetchProjects = async () => {
    setLoading(true);
    setListError('');
    try {
      const response = await api.get('/projects/my');
      setProjects(response.data);
    } catch (err) {
      setListError(err.response?.data?.message || 'Failed to load projects');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProjects();
  }, []);

  const handleCreateProject = async (event) => {
    event.preventDefault();
    setCreateErrors({ name: '', description: '' });
    try {
      await api.post('/projects', { name, description });
      setName('');
      setDescription('');
      fetchProjects();
    } catch (err) {
      setCreateErrors({
        name: err.response?.data?.message || 'Failed to create project',
        description: '',
      });
    }
  };

  const openProject = async () => {
    const trimmedId = projectId.trim();
    if (!trimmedId) {
      setOpenProjectError('Project ID is required');
      return;
    }
    setOpenProjectError('');
    try {
      await api.get(`/projects/${trimmedId}`);
      navigate(`/projects/${trimmedId}`);
    } catch (err) {
      setOpenProjectError(err.response?.data?.message || 'Project not found');
    }
  };

  return (
    <>
      <NavBar />
      <div className="container">
        <h2>Projects</h2>

        <div className="card">
          <h3>Open Project by ID</h3>
          <div className="row">
            <input
              type="text"
              placeholder="Project ID"
              value={projectId}
              onChange={(e) => setProjectId(e.target.value)}
              className={openProjectError ? 'input-error' : ''}
            />
            <button type="button" onClick={openProject}>
              Open
            </button>
          </div>
          {openProjectError && <p className="field-error">{openProjectError}</p>}
        </div>

        <div className="card">
          <h3>Create Project</h3>
          <form onSubmit={handleCreateProject}>
            <label>
              Name
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className={createErrors.name ? 'input-error' : ''}
              />
              {createErrors.name && <p className="field-error">{createErrors.name}</p>}
            </label>
            <label>
              Description
              <textarea
                rows="3"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                className={createErrors.description ? 'input-error' : ''}
              />
              {createErrors.description && (
                <p className="field-error">{createErrors.description}</p>
              )}
            </label>
            <button type="submit">Create</button>
          </form>
        </div>

        {loading && <p className="muted">Loading...</p>}
        {listError && <p className="error">{listError}</p>}

        {projects.length === 0 && !loading ? (
          <p className="muted">No projects found.</p>
        ) : (
          projects.map((project, index) => (
            <div className="card" key={`${project.projectName}-${index}`}>
              <h3>{project.projectName}</h3>
              <p>ID: {project.id}</p>
              <p>{project.description || 'No description'}</p>
              <p className="muted">
                Created by {project.createdByUsername} ({project.createdByEmail})
              </p>
            </div>
          ))
        )}
      </div>
    </>
  );
}
