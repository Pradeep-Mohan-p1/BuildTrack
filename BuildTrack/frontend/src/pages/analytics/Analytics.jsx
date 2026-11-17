import { Activity, ClipboardCheck, Users } from 'lucide-react';
import SectionHeader from '../../components/common/SectionHeader';
import StatCard from '../../components/common/StatCard';
import useApiResource from '../../hooks/useApiResource';

export default function Analytics() {
  const { data, loading, error, reload } = useApiResource('/analytics/dashboard', 'object');

  return (
    <div className="space-y-5">
      <SectionHeader title="Analytics" subtitle="Live workforce and attendance summary from the analytics API." />
      {error ? (
        <div role="alert" className="flex flex-wrap items-center justify-between gap-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          <span>{error}</span>
          <button type="button" onClick={reload} className="font-semibold underline">Try again</button>
        </div>
      ) : loading ? (
        <div role="status" className="rounded-2xl border border-slate-200 bg-white p-8 text-center text-sm text-slate-500">
          Loading analytics…
        </div>
      ) : data ? (
        <>
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
            <StatCard title="Total employees" value={data.totalEmployees} icon={Users} tone="blue" />
            <StatCard title="Active employees" value={data.activeEmployees} icon={Users} tone="green" />
            <StatCard title="Active assignments" value={data.activeAssignments} icon={ClipboardCheck} tone="amber" />
            <StatCard title="Attendance today" value={data.totalAttendanceToday} icon={Activity} tone="slate" />
          </div>
          <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
            <h3 className="font-semibold text-slate-900">Today’s attendance</h3>
            <p className="mt-1 text-sm text-slate-500">Counts and percentage from the backend dashboard summary.</p>
            <div className="mt-4 grid gap-4 sm:grid-cols-2 lg:grid-cols-5">
              {[
                ['Present', data.presentToday],
                ['Absent', data.absentToday],
                ['Leave', data.leaveToday],
                ['Half day', data.halfDayToday],
                ['Attendance', `${data.attendancePercentage}%`]
              ].map(([label, value]) => (
                <div key={label} className="rounded-xl bg-slate-50 p-4">
                  <p className="text-sm text-slate-500">{label}</p>
                  <p className="mt-2 text-2xl font-semibold text-slate-900">{value ?? '—'}</p>
                </div>
              ))}
            </div>
          </section>
        </>
      ) : null}
    </div>
  );
}
