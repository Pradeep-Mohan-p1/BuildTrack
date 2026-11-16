import { Bell, Search } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export default function Navbar() {
  const { user } = useAuth();

  return (
    <header className="border-b border-slate-200 bg-white/80 px-6 py-4 backdrop-blur-sm">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-3 rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-slate-500">
          <Search className="h-4 w-4" />
          <input
            className="w-64 border-0 bg-transparent text-sm outline-none placeholder:text-slate-400"
            placeholder="Search projects, sites…"
          />
        </div>

        <div className="flex items-center gap-4">
          <button type="button" className="rounded-xl border border-slate-200 p-2 text-slate-600 transition hover:bg-slate-50">
            <Bell className="h-4 w-4" />
          </button>
          <div className="flex items-center gap-3 rounded-xl border border-slate-200 px-3 py-2">
            <div className="flex h-9 w-9 items-center justify-center rounded-full bg-blue-100 text-sm font-semibold text-blue-700">
              {user?.email?.slice(0, 1).toUpperCase() || 'U'}
            </div>
            <div className="text-left">
              <p className="text-sm font-medium text-slate-900">{user?.email || 'User'}</p>
              <p className="text-xs text-slate-500">{user?.role || 'EMPLOYEE'}</p>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
