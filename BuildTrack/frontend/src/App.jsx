import { Navigate, Route, Routes } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import AppLayout from './components/layout/AppLayout';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Dashboard from './pages/dashboard/Dashboard';
import EmployeeList from './pages/employees/EmployeeList';
import EmployeeDetails from './pages/employees/EmployeeDetails';
import ProjectList from './pages/projects/ProjectList';
import ProjectDetails from './pages/projects/ProjectDetails';
import SiteList from './pages/sites/SiteList';
import SiteDetails from './pages/sites/SiteDetails';
import AssignmentList from './pages/assignments/AssignmentList';
import AttendanceList from './pages/attendance/AttendanceList';
import ExpenseList from './pages/expenses/ExpenseList';
import PayrollList from './pages/payroll/PayrollList';
import Analytics from './pages/analytics/Analytics';
import AuditList from './pages/audit/AuditList';
import UserList from './pages/users/UserList';
import Settings from './pages/settings/Settings';
import { NAV_ITEMS } from './config/roles';

function ProtectedRoute({ children }) {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return <div className="flex min-h-screen items-center justify-center text-slate-500">Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

function PublicRoute({ children }) {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return <div className="flex min-h-screen items-center justify-center text-slate-500">Loading...</div>;
  }

  if (user) {
    return <Navigate to="/" replace />;
  }

  return children;
}

function RoleRoute({ moduleKey, children }) {
  const { user } = useAuth();
  const item = NAV_ITEMS.find((navItem) => navItem.key === moduleKey);

  if (!item || !item.roles.includes(user?.role)) {
    return (
      <div role="alert" className="rounded-2xl border border-red-200 bg-white p-6 shadow-soft">
        <h2 className="text-lg font-semibold text-slate-900">Access denied</h2>
        <p className="mt-2 text-sm text-slate-600">Your account role is not permitted to access this page.</p>
      </div>
    );
  }

  return children;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <AppLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<RoleRoute moduleKey="dashboard"><Dashboard /></RoleRoute>} />
        <Route path="employees" element={<RoleRoute moduleKey="employees"><EmployeeList /></RoleRoute>} />
        <Route path="employees/:id" element={<RoleRoute moduleKey="employees"><EmployeeDetails /></RoleRoute>} />
        <Route path="projects" element={<RoleRoute moduleKey="projects"><ProjectList /></RoleRoute>} />
        <Route path="projects/:id" element={<RoleRoute moduleKey="projects"><ProjectDetails /></RoleRoute>} />
        <Route path="sites" element={<RoleRoute moduleKey="sites"><SiteList /></RoleRoute>} />
        <Route path="sites/:id" element={<RoleRoute moduleKey="sites"><SiteDetails /></RoleRoute>} />
        <Route path="assignments" element={<RoleRoute moduleKey="assignments"><AssignmentList /></RoleRoute>} />
        <Route path="attendance" element={<RoleRoute moduleKey="attendance"><AttendanceList /></RoleRoute>} />
        <Route path="expenses" element={<RoleRoute moduleKey="expenses"><ExpenseList /></RoleRoute>} />
        <Route path="payroll" element={<RoleRoute moduleKey="payroll"><PayrollList /></RoleRoute>} />
        <Route path="analytics" element={<RoleRoute moduleKey="analytics"><Analytics /></RoleRoute>} />
        <Route path="audit" element={<RoleRoute moduleKey="audit"><AuditList /></RoleRoute>} />
        <Route path="users" element={<RoleRoute moduleKey="users"><UserList /></RoleRoute>} />
        <Route path="settings" element={<RoleRoute moduleKey="settings"><Settings /></RoleRoute>} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
