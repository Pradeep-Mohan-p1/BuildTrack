import { canAccess } from '../config/roles';

export const useRoleAccess = (user) => ({
  canAccess: (key) => canAccess(user?.role, key),
  isAdmin: user?.role === 'ADMIN',
  isSiteManager: user?.role === 'SITE_MANAGER',
  isAccountant: user?.role === 'ACCOUNTANT',
  isEmployee: user?.role === 'EMPLOYEE'
});
