export const ROLE_OPTIONS = ['ADMIN', 'SITE_MANAGER', 'ACCOUNTANT', 'EMPLOYEE'];

export const ROLE_LABELS = {
  ADMIN: 'Administrator',
  SITE_MANAGER: 'Site Manager',
  ACCOUNTANT: 'Accountant',
  EMPLOYEE: 'Employee'
};

export const ROLE_PERMISSIONS = {
  ADMIN: [
    'dashboard',
    'employees',
    'projects',
    'sites',
    'assignments',
    'attendance',
    'expenses',
    'payroll',
    'analytics',
    'audit',
    'users',
    'settings'
  ],
  SITE_MANAGER: [
    'dashboard',
    'employees',
    'sites',
    'assignments',
    'attendance',
    'expenses',
    'analytics',
    'projects'
  ],
  ACCOUNTANT: ['dashboard', 'expenses', 'payroll', 'analytics'],
  EMPLOYEE: ['dashboard', 'profile', 'attendance', 'payroll']
};

export const NAV_ITEMS = [
  { label: 'Dashboard', key: 'dashboard', path: '/', roles: ['ADMIN', 'SITE_MANAGER', 'ACCOUNTANT', 'EMPLOYEE'] },
  { label: 'Employees', key: 'employees', path: '/employees', roles: ['ADMIN', 'SITE_MANAGER'] },
  { label: 'Projects', key: 'projects', path: '/projects', roles: ['ADMIN', 'SITE_MANAGER'] },
  { label: 'Sites', key: 'sites', path: '/sites', roles: ['ADMIN', 'SITE_MANAGER'] },
  { label: 'Assignments', key: 'assignments', path: '/assignments', roles: ['ADMIN', 'SITE_MANAGER'] },
  { label: 'Attendance', key: 'attendance', path: '/attendance', roles: ['ADMIN', 'SITE_MANAGER', 'EMPLOYEE'] },
  { label: 'Expenses', key: 'expenses', path: '/expenses', roles: ['ADMIN', 'SITE_MANAGER', 'ACCOUNTANT'] },
  { label: 'Payroll', key: 'payroll', path: '/payroll', roles: ['ADMIN', 'ACCOUNTANT', 'EMPLOYEE'] },
  { label: 'Analytics', key: 'analytics', path: '/analytics', roles: ['ADMIN', 'SITE_MANAGER', 'ACCOUNTANT'] },
  { label: 'Audit', key: 'audit', path: '/audit', roles: ['ADMIN'] },
  { label: 'Users', key: 'users', path: '/users', roles: ['ADMIN'] },
  { label: 'Settings', key: 'settings', path: '/settings', roles: ['ADMIN'] }
];

export const canAccess = (role, key) => {
  if (!role) return false;
  const permissions = ROLE_PERMISSIONS[role] || [];
  return permissions.includes(key);
};
