import { LayoutGrid, Users, FolderKanban, MapPinned, ClipboardList, Clock3, ReceiptText, Landmark, BarChart3, ShieldCheck, UserCog, Settings, LogOut } from 'lucide-react';
import { NavLink } from 'react-router-dom';
import { NAV_ITEMS } from '../../config/roles';
import { useAuth } from '../../context/AuthContext';

const icons = {
  dashboard: LayoutGrid,
  employees: Users,
  projects: FolderKanban,
  sites: MapPinned,
  assignments: ClipboardList,
  attendance: Clock3,
  expenses: ReceiptText,
  payroll: Landmark,
  analytics: BarChart3,
  audit: ShieldCheck,
  users: UserCog,
  settings: Settings,
  profile: Users
};

export default function Sidebar() {
  const { user, logout } = useAuth();
  const visibleItems = NAV_ITEMS.filter((item) => item.roles.includes(user?.role || 'EMPLOYEE'));

  return (
    <aside className="flex h-screen w-72 flex-col border-r border-slate-800 bg-slate-950 text-slate-200">
      <div className="flex items-center gap-3 border-b border-slate-800 px-6 py-5">
        <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-blue-600 font-bold text-white">B</div>
        <div>
          <p className="text-lg font-semibold text-white">BuildTrack</p>
          <p className="text-xs text-slate-400">Construction OS</p>
        </div>
      </div>

      <nav className="flex-1 space-y-1 px-4 py-5">
        {visibleItems.map(({ label, key, path }) => {
          const Icon = icons[key] || LayoutGrid;
          return (
            <NavLink
              key={key}
              to={path}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition ${
                  isActive ? 'bg-slate-800 text-white' : 'text-slate-300 hover:bg-slate-800/70 hover:text-white'
                }`
              }
            >
              <Icon className="h-4 w-4" />
              {label}
            </NavLink>
          );
        })}
      </nav>

      <div className="border-t border-slate-800 px-4 py-4">
        <div className="mb-3 flex items-center gap-3 rounded-xl bg-slate-900 px-3 py-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-full bg-blue-500 text-sm font-semibold text-white">
            {user?.email?.slice(0, 1).toUpperCase() || 'U'}
          </div>
          <div className="min-w-0">
            <p className="truncate text-sm font-medium text-white">{user?.email || 'User'}</p>
            <p className="text-xs text-slate-400">{user?.role || 'EMPLOYEE'}</p>
          </div>
        </div>

        <button
          type="button"
          onClick={logout}
          className="flex w-full items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white"
        >
          <LogOut className="h-4 w-4" />
          Logout
        </button>
      </div>
    </aside>
  );
}
