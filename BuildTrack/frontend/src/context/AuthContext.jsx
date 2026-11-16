import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { getCurrentUser, loginUser, registerUser as registerApi } from '../api/authApi';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  const persistSession = useCallback((email, password) => {
    sessionStorage.setItem('buildtrack_auth', JSON.stringify({ email, password }));
  }, []);

  const clearSession = useCallback(() => {
    sessionStorage.removeItem('buildtrack_auth');
  }, []);

  const refreshUser = useCallback(async (email, password) => {
    try {
      setError('');
      const response = await loginUser(email, password);
      const profile = response.data?.user || response.data || { email, role: 'EMPLOYEE' };
      const normalizedUser = {
        ...profile,
        email: profile.email || email,
        role: profile.role || 'EMPLOYEE'
      };
      setUser(normalizedUser);
      persistSession(email, password);
      return normalizedUser;
    } catch (err) {
      setUser(null);
      clearSession();
      const message = err.response?.data?.message || 'Invalid email or password.';
      setError(message);
      throw err;
    }
  }, [clearSession, persistSession]);

  const login = useCallback(async (email, password) => {
    return refreshUser(email, password);
  }, [refreshUser]);

  const register = useCallback(async ({ username, email, password }) => {
    try {
      setError('');
      await registerApi({ username, email, password });
      return true;
    } catch (err) {
      const message = err.response?.data?.message || 'Registration failed.';
      setError(message);
      throw err;
    }
  }, []);

  const logout = useCallback(() => {
    clearSession();
    setUser(null);
    setError('');
  }, [clearSession]);

  useEffect(() => {
    const loadSession = async () => {
      const stored = sessionStorage.getItem('buildtrack_auth');
      if (!stored) {
        setIsLoading(false);
        return;
      }

      try {
        const { email, password } = JSON.parse(stored);
        if (email && password) {
          await refreshUser(email, password);
        }
      } catch (error) {
        clearSession();
      } finally {
        setIsLoading(false);
      }
    };

    loadSession();
  }, [clearSession, refreshUser]);

  const value = useMemo(() => ({
    user,
    isLoading,
    error,
    setError,
    login,
    register,
    logout,
    isAuthenticated: Boolean(user),
    refreshUser,
    clearSession
  }), [clearSession, error, isLoading, login, logout, refreshUser, register, user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext;
