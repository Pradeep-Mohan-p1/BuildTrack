import { useCallback, useEffect, useMemo, useState } from 'react';
import { Activity, BriefcaseBusiness, ClipboardCheck, DollarSign, Factory, Users } from 'lucide-react';
import api from '../../api/axiosInstance';
import { apiErrorMessage } from '../../api/apiErrorMessage';
import SectionHeader from '../../components/common/SectionHeader';
import StatCard from '../../components/common/StatCard';
import StatusBadge from '../../components/common/StatusBadge';
import { useAuth } from '../../context/AuthContext';

const EMPTY_DASHBOARD = {
  analytics: null,
  projects: null,
  sites: null,
  attendance: null,
  approvedExpenses: null,
  errors: {}
};

const ANALYTICS_ROLES = ['ADMIN', 'SITE_MANAGER', 'ACCOUNTANT'];
const PROJECT_ROLES = ['ADMIN', 'SITE_MANAGER'];
const ATTENDANCE_ROLES = ['EMPLOYEE'];

function localDateString() {
  const date = new Date();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${date.getFullYear()}-${month}-${day}`;
}

function readResponseData(key, data) {
  if (key === 'analytics') {
    if (!data || typeof data !== 'object' || Array.isArray(data)) {
      throw new Error('The analytics API returned an unexpected response.');
    }
    return data;
  }
  if (key === 'approvedExpenses') {
    if (data == null || !Number.isFinite(Number(data))) {
      throw new Error('The expenses API returned an unexpected total.');
    }
    return Number(data);
  }
  if (!Array.isArray(data)) {
    throw new Error(`The ${key} API returned an unexpected list response.`);
  }
  return data;
}

function countAttendance(records) {
  const counts = records.reduce((result, record) => {
    const status = record.status;
    result[status] = (result[status] || 0) + 1;
    return result;
  }, {});
  const total = records.length;
  const present = counts.PRESENT || 0;
  const halfDay = counts.HALF_DAY || 0;

  return {
    totalAttendanceToday: total,
    presentToday: present,
    absentToday: counts.ABSENT || 0,
    leaveToday: counts.LEAVE || 0,
    halfDayToday: halfDay,
    attendancePercentage: total ? Math.round(((present + halfDay * 0.5) / total) * 10000) / 100 : 0
  };
}

function displayNumber(value) {
  return value == null ? '—' : Number(value).toLocaleString();
}

async function fetchDashboardData(role, signal) {
  const requests = [];
  if (ANALYTICS_ROLES.includes(role)) {
    requests.push(['analytics', api.get('/analytics/dashboard', { signal })]);
    const now = new Date();
    requests.push(['approvedExpenses', api.get('/expenses/monthly-approved-total', {
      params: { month: now.getMonth() + 1, year: now.getFullYear() },
      signal
    })]);
  }
  if (PROJECT_ROLES.includes(role)) {
    requests.push(['projects', api.get('/projects', { signal })]);
    requests.push(['sites', api.get('/sites', { signal })]);
  }
  if (ATTENDANCE_ROLES.includes(role)) {
    requests.push(['attendance', api.get('/attendance/daily', {
      params: { date: localDateString() },
      signal
    })]);
  }

  const results = await Promise.all(requests.map(async ([key, request]) => {
    try {
      const response = await request;
      return [key, { data: readResponseData(key, response.data) }];
    } catch (requestError) {
      return [key, { error: apiErrorMessage(requestError) }];
    }
  }));

  const next = { ...EMPTY_DASHBOARD, errors: {} };
  results.forEach(([key, result]) => {
    if (result.error) next.errors[key] = result.error;
    else next[key] = result.data;
  });
  return next;
}

export default function Dashboard() {
  const { user } = useAuth();
  const role = user?.role;
  const [dashboard, setDashboard] = useState(EMPTY_DASHBOARD);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const controller = new AbortController();
    fetchDashboardData(role, controller.signal)
      .then((data) => {
        if (controller.signal.aborted) return;
        setDashboard(data);
        setLoading(false);
      })
      .catch((requestError) => {
        if (controller.signal.aborted) return;
        setDashboard({ ...EMPTY_DASHBOARD, errors: { dashboard: apiErrorMessage(requestError) } });
        setLoading(false);
      });
    return () => controller.abort();
  }, [role]);

  const reload = useCallback(() => {
    setLoading(true);
    fetchDashboardData(role)
      .then(setDashboard)
      .catch((requestError) => {
        setDashboard({ ...EMPTY_DASHBOARD, errors: { dashboard: apiErrorMessage(requestError) } });
      })
      .finally(() => setLoading(false));
  }, [role]);

  const attendance = dashboard.analytics
    || (dashboard.attendance ? countAttendance(dashboard.attendance) : null);
  const projectsWithProgress = useMemo(() => (
    dashboard.projects?.filter((project) => project.progress != null).slice(0, 5) || []
  ), [dashboard.projects]);
  const cards = [];

  if (dashboard.projects) {
    cards.push({
      title: 'Total projects',
      value: displayNumber(dashboard.projects.length),
      icon: BriefcaseBusiness,
      tone: 'blue',
      helperText: 'Live project records'
    });
  }
  if (dashboard.sites) {
    cards.push({
      title: 'Active sites',
      value: displayNumber(dashboard.sites.filter((site) => site.status === 'ACTIVE').length),
      icon: Factory,
      tone: 'green',
      helperText: 'Live site records'
    });
  }
  if (dashboard.analytics) {
    cards.push({
      title: 'Active employees',
      value: displayNumber(dashboard.analytics.activeEmployees),
      icon: Users,
      tone: 'blue',
      helperText: 'Analytics summary'
    });
    cards.push({
      title: 'Active assignments',
      value: displayNumber(dashboard.analytics.activeAssignments),
      icon: ClipboardCheck,
      tone: 'amber',
      helperText: 'Analytics summary'
    });
  }
  if (attendance) {
    cards.push({
      title: 'Attendance today',
      value: `${displayNumber(attendance.attendancePercentage)}%`,
      icon: Activity,
      tone: 'slate',
      helperText: `${displayNumber(attendance.totalAttendanceToday)} records`
    });
  }
  if (dashboard.approvedExpenses != null) {
    cards.push({
      title: 'Approved expenses',
      value: displayNumber(dashboard.approvedExpenses),
      icon: DollarSign,
      tone: 'slate',
      helperText: 'This month'
    });
  }

  const errors = Object.entries(dashboard.errors);
  const hasData = Boolean(
    dashboard.analytics
      || dashboard.projects
      || dashboard.sites
      || dashboard.attendance
      || dashboard.approvedExpenses != null
  );

  return (
    <div className="space-y-6">
      <SectionHeader
        title="Overview"
        subtitle="Live workforce, attendance, project, and site data."
        action={(
          <button
            type="button"
            onClick={reload}
            disabled={loading}
            className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60"
          >
            {loading ? 'Refreshing…' : 'Refresh'}
          </button>
        )}
      />

      {loading && !hasData ? (
        <div role="status" className="rounded-2xl border border-slate-200 bg-white p-8 text-center text-sm text-slate-500">
          Loading dashboard data…
        </div>
      ) : null}

      {errors.length ? (
        <div role="alert" className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          <p className="font-semibold">Some dashboard data could not be loaded.</p>
          <ul className="mt-1 list-inside list-disc">
            {errors.map(([key, message]) => <li key={key}>{key}: {message}</li>)}
          </ul>
          <button type="button" onClick={reload} className="mt-2 font-semibold underline">Try again</button>
        </div>
      ) : null}

      {!loading && !hasData && !errors.length ? (
        <div className="rounded-2xl border border-slate-200 bg-white p-6 text-sm text-slate-600">
          No dashboard data is available for this account.
        </div>
      ) : null}

      {cards.length ? (
        <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          {cards.map((card) => <StatCard key={card.title} {...card} />)}
        </div>
      ) : null}

      <div className="grid gap-6 xl:grid-cols-2">
        <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <h3 className="text-lg font-semibold text-slate-900">Today’s attendance</h3>
          <p className="text-sm text-slate-500">Counts from the backend dashboard summary or today’s attendance records.</p>
          {attendance ? (
            <div className="mt-5 grid grid-cols-2 gap-3 sm:grid-cols-3">
              {[
                ['Present', attendance.presentToday],
                ['Absent', attendance.absentToday],
                ['Leave', attendance.leaveToday],
                ['Half day', attendance.halfDayToday],
                ['Total records', attendance.totalAttendanceToday],
                ['Attendance rate', `${attendance.attendancePercentage}%`]
              ].map(([label, value]) => (
                <div key={label} className="rounded-xl bg-slate-50 p-4">
                  <p className="text-sm text-slate-500">{label}</p>
                  <p className="mt-2 text-2xl font-semibold text-slate-900">
                    {typeof value === 'number' ? displayNumber(value) : value}
                  </p>
                </div>
              ))}
            </div>
          ) : dashboard.errors.analytics || dashboard.errors.attendance ? (
            <p className="mt-4 text-sm text-red-600">
              {dashboard.errors.analytics || dashboard.errors.attendance}
            </p>
          ) : (
            <p className="mt-4 text-sm text-slate-500">Attendance data is not available for this role.</p>
          )}
        </section>

        <section className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <h3 className="text-lg font-semibold text-slate-900">Project progress</h3>
          <p className="text-sm text-slate-500">Progress values reported by project records.</p>
          {projectsWithProgress.length ? (
            <div className="mt-5 space-y-5">
              {projectsWithProgress.map((project) => {
                const progress = Math.min(100, Math.max(0, Number(project.progress)));
                return (
                  <div key={project.id}>
                    <div className="mb-2 flex items-center justify-between gap-3 text-sm">
                      <span className="truncate font-medium text-slate-700">{project.projectName}</span>
                      <span className="shrink-0 text-slate-500">{project.progress}%</span>
                    </div>
                    <div className="h-2.5 overflow-hidden rounded-full bg-slate-200">
                      <div className="h-full rounded-full bg-blue-600" style={{ width: `${progress}%` }} />
                    </div>
                    {project.status ? <div className="mt-2"><StatusBadge status={project.status} /></div> : null}
                  </div>
                );
              })}
            </div>
          ) : dashboard.projects?.length === 0 ? (
            <p className="mt-4 text-sm text-slate-500">There are no projects to report yet.</p>
          ) : dashboard.errors.projects ? (
            <p className="mt-4 text-sm text-red-600">{dashboard.errors.projects}</p>
          ) : dashboard.projects ? (
            <p className="mt-4 text-sm text-slate-500">Projects have no reported progress values.</p>
          ) : (
            <p className="mt-4 text-sm text-slate-500">Project data is not available for this role.</p>
          )}
        </section>
      </div>
    </div>
  );
}
