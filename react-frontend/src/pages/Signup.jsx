import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api';

export default function Signup() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [errors, setErrors] = useState({ username: '', email: '', password: '' });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrors({ username: '', email: '', password: '' });
    setMessage('');
    setLoading(true);
    try {
      const response = await api.post('/auth/signup', {
        username,
        email,
        password,
      });
      setMessage(response.data.message || 'Signup successful');
      navigate('/login');
    } catch (err) {
      setErrors({
        username: '',
        email: err.response?.data?.message || 'Signup failed',
        password: '',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Sign Up</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Username
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              className={errors.username ? 'input-error' : ''}
            />
            {errors.username && <p className="field-error">{errors.username}</p>}
          </label>
          <label>
            Email
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className={errors.email ? 'input-error' : ''}
            />
            {errors.email && <p className="field-error">{errors.email}</p>}
          </label>
          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className={errors.password ? 'input-error' : ''}
            />
            {errors.password && <p className="field-error">{errors.password}</p>}
          </label>
          {message && <p className="success">{message}</p>}
          <button type="submit" disabled={loading}>
            {loading ? 'Creating account...' : 'Sign up'}
          </button>
        </form>
        <p className="muted">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
}
