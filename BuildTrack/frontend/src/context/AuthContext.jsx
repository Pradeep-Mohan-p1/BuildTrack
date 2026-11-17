import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { getCurrentUser, loginUser, registerUser as registerApi } from '../api/authApi';

const AuthContext = createContext(null);
const TOKEN_STORAGE_KEY = 'buildtrack_token';
const LEGACY_SESSION_KEY = 'buildtrack_auth';

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  const persistSession = useCallback((token) => {
    sessionStorage.setItem(TOKEN_STORAGE_KEY, token);
  }, []);

  const clearSession = useCallback(() => {
    sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    sessionStorage.removeItem(LEGACY_SESSION_KEY);
  }, []);

  const refreshUser = useCallback(async (email, password) => {
    try {
      setError('');
      const response = await loginUser(email, password);
      const { token, user: normalizedUser } = response.data;
      if (!token || !normalizedUser) {
        throw new Error('The authentication response did not include a token and user profile.');
      }

      persistSession(token);
      setUser(normalizedUser);
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
      const token = sessionStorage.getItem(TOKEN_STORAGE_KEY);
      sessionStorage.removeItem(LEGACY_SESSION_KEY);
      if (!token) {
        setIsLoading(false);
        return;
      }

      try {
        const response = await getCurrentUser();
        setUser(response.data);
      } catch {
        clearSession();
      } finally {
        setIsLoading(false);
      }
    };

    loadSession();
  }, [clearSession]);

  useEffect(() => {
    const handleUnauthorized = () => {
      clearSession();
      setUser(null);
      setError('Your session expired. Sign in again to continue.');
    };

    window.addEventListener('buildtrack:unauthorized', handleUnauthorized);
    return () => window.removeEventListener('buildtrack:unauthorized', handleUnauthorized);
  }, [clearSession]);

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
