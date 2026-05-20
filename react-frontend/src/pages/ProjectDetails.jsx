import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';
import NavBar from '../components/NavBar';

const taskStatuses = ['TODO', 'IN_PROGRESS', 'DONE'];

const normalizeDueDate = (value) => {
  if (!value) {
    return null;
  }
  return value.length === 16 ? `${value}:00` : value;
};

export default function ProjectDetails() {
  const { id } = useParams();
  const [project, setProject] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [memberEmail, setMemberEmail] = useState('');
  const [memberErrors, setMemberErrors] = useState({ email: '' });
  const [taskTitle, setTaskTitle] = useState('');
  const [taskDescription, setTaskDescription] = useState('');
  const [assignedToEmail, setAssignedToEmail] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [taskStatus, setTaskStatus] = useState('TODO');
  const [createErrors, setCreateErrors] = useState({
    title: '',
    assignedToEmail: '',
    dueDate: '',
    status: '',
  });

  const [updateTaskId, setUpdateTaskId] = useState('');
  const [updateTitle, setUpdateTitle] = useState('');
  const [updateDescription, setUpdateDescription] = useState('');
  const [updateAssignedToEmail, setUpdateAssignedToEmail] = useState('');
  const [updateDueDate, setUpdateDueDate] = useState('');
  const [updateStatus, setUpdateStatus] = useState('');
  const [updateErrors, setUpdateErrors] = useState({
    taskId: '',
    title: '',
    description: '',
    assignedToEmail: '',
    dueDate: '',
    status: '',
  });
  const [deleteTaskId, setDeleteTaskId] = useState('');
  const [deleteErrors, setDeleteErrors] = useState({ taskId: '' });

  const loadProject = async () => {
    setLoading(true);
    setError('');
    try {
      const [projectResponse, taskResponse] = await Promise.all([
        api.get(`/projects/${id}`),
        api.get(`/projects/${id}/tasks`),
      ]);
      setProject(projectResponse.data);
      setTasks(taskResponse.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load project');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProject();
  }, [id]);

  const handleAddMember = async (event) => {
    event.preventDefault();
    setMemberErrors({ email: '' });
    try {
      await api.post(`/projects/${id}/members`, { email: memberEmail });
      setMemberEmail('');
    } catch (err) {
      setMemberErrors({
        email: err.response?.data?.message || 'Failed to add member',
      });
    }
  };

  const handleCreateTask = async (event) => {
    event.preventDefault();
    setCreateErrors({ title: '', assignedToEmail: '', dueDate: '', status: '' });
    try {
      await api.post(`/projects/${id}/tasks`, {
        title: taskTitle,
        description: taskDescription,
        assignedToEmail,
        dueDate: normalizeDueDate(dueDate),
        taskStatus,
      });
      setTaskTitle('');
      setTaskDescription('');
      setAssignedToEmail('');
      setDueDate('');
      setTaskStatus('TODO');
      loadProject();
    } catch (err) {
      const message = err.response?.data?.message || 'Failed to create task';
      if (message.toLowerCase().includes('title')) {
        setCreateErrors({ title: message, assignedToEmail: '', dueDate: '', status: '' });
      } else if (
        message.toLowerCase().includes('email') ||
        message.toLowerCase().includes('user') ||
        message.toLowerCase().includes('member')
      ) {
        setCreateErrors({ title: '', assignedToEmail: message, dueDate: '', status: '' });
      } else {
        setCreateErrors({ title: message, assignedToEmail: '', dueDate: '', status: '' });
      }
    }
  };

  const handleUpdateTaskStatus = async (event) => {
    event.preventDefault();
    setUpdateErrors({
      taskId: '',
      title: '',
      description: '',
      assignedToEmail: '',
      dueDate: '',
      status: '',
    });
    if (!updateTaskId.trim()) {
      setUpdateErrors((prev) => ({ ...prev, taskId: 'Task ID is required' }));
      return;
    }

    const payload = {};
    if (updateTitle.trim()) {
      payload.title = updateTitle;
    }
    if (updateDescription.trim()) {
      payload.description = updateDescription;
    }
    if (updateAssignedToEmail.trim()) {
      payload.assignedToEmail = updateAssignedToEmail.trim();
    }
    if (updateDueDate) {
      payload.dueDate = normalizeDueDate(updateDueDate);
    }
    if (updateStatus) {
      payload.taskStatus = updateStatus;
    }
    if (Object.keys(payload).length === 0) {
      setUpdateErrors((prev) => ({
        ...prev,
        status: 'Provide at least one field to update.',
      }));
      return;
    }
    try {
      await api.patch(`/tasks/${updateTaskId}`, payload);
      setUpdateTaskId('');
      setUpdateTitle('');
      setUpdateDescription('');
      setUpdateAssignedToEmail('');
      setUpdateDueDate('');
      setUpdateStatus('');
      loadProject();
    } catch (err) {
      setUpdateErrors((prev) => ({
        ...prev,
        taskId: err.response?.data?.message || 'Failed to update task',
      }));
    }
  };

  const handleDeleteTask = async (event) => {
    event.preventDefault();
    setDeleteErrors({ taskId: '' });
    if (!deleteTaskId.trim()) {
      setDeleteErrors({ taskId: 'Task ID is required' });
      return;
    }
    try {
      await api.delete(`/tasks/${deleteTaskId}`);
      setDeleteTaskId('');
      loadProject();
    } catch (err) {
      setDeleteErrors({
        taskId: err.response?.data?.message || 'Failed to delete task',
      });
    }
  };

  return (
    <>
      <NavBar />
      <div className="container">
        <h2>Project Details</h2>
        {loading && <p className="muted">Loading...</p>}
        {error && <p className="error">{error}</p>}

        {project && (
          <div className="card">
            <h3>{project.projectName}</h3>
            <p>{project.description || 'No description'}</p>
            <p className="muted">
              Created by {project.createdByUsername} ({project.createdByEmail})
            </p>
          </div>
        )}

        <div className="card">
          <h3>Add Member</h3>
          <form onSubmit={handleAddMember}>
            <label>
              Member Email
              <input
                type="email"
                value={memberEmail}
                onChange={(e) => setMemberEmail(e.target.value)}
                required
                className={memberErrors.email ? 'input-error' : ''}
              />
              {memberErrors.email && <p className="field-error">{memberErrors.email}</p>}
            </label>
            <button type="submit">Add Member</button>
          </form>
        </div>

        <div className="card">
          <h3>Create Task</h3>
          <form onSubmit={handleCreateTask}>
            <label>
              Title
              <input
                type="text"
                value={taskTitle}
                onChange={(e) => setTaskTitle(e.target.value)}
                required
                className={createErrors.title ? 'input-error' : ''}
              />
              {createErrors.title && <p className="field-error">{createErrors.title}</p>}
            </label>
            <label>
              Description
              <textarea
                rows="3"
                value={taskDescription}
                onChange={(e) => setTaskDescription(e.target.value)}
              />
            </label>
            <label>
              Assign To (email)
              <input
                type="email"
                value={assignedToEmail}
                onChange={(e) => setAssignedToEmail(e.target.value)}
                className={createErrors.assignedToEmail ? 'input-error' : ''}
              />
              {createErrors.assignedToEmail && (
                <p className="field-error">{createErrors.assignedToEmail}</p>
              )}
            </label>
            <label>
              Due Date
              <input
                type="datetime-local"
                value={dueDate}
                onChange={(e) => setDueDate(e.target.value)}
                className={createErrors.dueDate ? 'input-error' : ''}
              />
              {createErrors.dueDate && <p className="field-error">{createErrors.dueDate}</p>}
            </label>
            <label>
              Status
              <select
                value={taskStatus}
                onChange={(e) => setTaskStatus(e.target.value)}
                className={createErrors.status ? 'input-error' : ''}
              >
                {taskStatuses.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
              {createErrors.status && <p className="field-error">{createErrors.status}</p>}
            </label>
            <button type="submit">Create Task</button>
          </form>
        </div>

        <div className="card">
          <h3>Tasks</h3>
          {tasks.length === 0 ? (
            <p className="muted">No tasks found.</p>
          ) : (
            tasks.map((task, index) => (
              <div className="task-row" key={`${task.taskTitle}-${index}`}>
                <div>
                  <strong>{task.taskTitle}</strong>
                  <p className="muted">ID: {task.id}</p>
                  <p className="muted">{task.taskDescription || 'No description'}</p>
                  <p className="muted">Status: {task.taskStatus}</p>
                  <p className="muted">
                    Assigned to: {task.assignedToUsername || 'Unassigned'} (
                    {task.assignedToEmail || 'N/A'})
                  </p>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="card">
          <h3>Update Task</h3>
          <p className="muted">
            Admins can update all fields. Assigned users can update only the status.
          </p>
          <form onSubmit={handleUpdateTaskStatus}>
            <label>
              Task ID
              <input
                type="text"
                value={updateTaskId}
                onChange={(e) => setUpdateTaskId(e.target.value)}
                required
                className={updateErrors.taskId ? 'input-error' : ''}
              />
              {updateErrors.taskId && <p className="field-error">{updateErrors.taskId}</p>}
            </label>
            <label>
              Title
              <input
                type="text"
                value={updateTitle}
                onChange={(e) => setUpdateTitle(e.target.value)}
                className={updateErrors.title ? 'input-error' : ''}
              />
              {updateErrors.title && <p className="field-error">{updateErrors.title}</p>}
            </label>
            <label>
              Description
              <textarea
                rows="3"
                value={updateDescription}
                onChange={(e) => setUpdateDescription(e.target.value)}
                className={updateErrors.description ? 'input-error' : ''}
              />
              {updateErrors.description && (
                <p className="field-error">{updateErrors.description}</p>
              )}
            </label>
            <label>
              Assign To (email)
              <input
                type="email"
                value={updateAssignedToEmail}
                onChange={(e) => setUpdateAssignedToEmail(e.target.value)}
                className={updateErrors.assignedToEmail ? 'input-error' : ''}
              />
              {updateErrors.assignedToEmail && (
                <p className="field-error">{updateErrors.assignedToEmail}</p>
              )}
            </label>
            <label>
              Due Date
              <input
                type="datetime-local"
                value={updateDueDate}
                onChange={(e) => setUpdateDueDate(e.target.value)}
                className={updateErrors.dueDate ? 'input-error' : ''}
              />
              {updateErrors.dueDate && <p className="field-error">{updateErrors.dueDate}</p>}
            </label>
            <label>
              Status
              <select
                value={updateStatus}
                onChange={(e) => setUpdateStatus(e.target.value)}
                className={updateErrors.status ? 'input-error' : ''}
              >
                <option value="">Select status (optional)</option>
                {taskStatuses.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
              {updateErrors.status && <p className="field-error">{updateErrors.status}</p>}
            </label>
            <button type="submit">Update Task</button>
          </form>
        </div>

        <div className="card">
          <h3>Delete Task</h3>
          <form onSubmit={handleDeleteTask}>
            <label>
              Task ID
              <input
                type="text"
                value={deleteTaskId}
                onChange={(e) => setDeleteTaskId(e.target.value)}
                required
                className={deleteErrors.taskId ? 'input-error' : ''}
              />
              {deleteErrors.taskId && <p className="field-error">{deleteErrors.taskId}</p>}
            </label>
            <button type="submit" className="danger">
              Delete Task
            </button>
          </form>
        </div>
      </div>
    </>
  );
}
