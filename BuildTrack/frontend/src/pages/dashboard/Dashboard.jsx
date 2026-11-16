import { Activity, BriefcaseBusiness, ClipboardCheck, DollarSign, Factory, TrendingUp } from 'lucide-react';
import { Area, AreaChart, Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import StatCard from '../../components/common/StatCard';
import SectionHeader from '../../components/common/SectionHeader';

const overviewData = [
  { name: 'Jan', value: 25 },
  { name: 'Feb', value: 32 },
  { name: 'Mar', value: 28 },
  { name: 'Apr', value: 42 },
  { name: 'May', value: 36 },
  { name: 'Jun', value: 55 }
];

const projectProgress = [
  { name: 'Site A', value: 78 },
  { name: 'Site B', value: 62 },
  { name: 'Site C', value: 91 },
  { name: 'Site D', value: 50 }
];

const recentActivity = [
  { title: 'Concrete pour approved', site: 'North Ridge Tower', time: '15 min ago', status: 'active' },
  { title: 'Payroll batch generated', site: 'Lakeside Residences', time: '1 hour ago', status: 'pending' },
  { title: 'Safety walkthrough logged', site: 'Metro Logistics Hub', time: '3 hours ago', status: 'on_track' }
];

export default function Dashboard() {
  return (
    <div className="space-y-6">
      <SectionHeader
        title="Overview"
        subtitle="Performance across projects, workforce, and spending."
        action={<button className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700">Export report</button>}
      />

      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <StatCard title="Total projects" value="24" change="+12%" icon={BriefcaseBusiness} tone="blue" />
        <StatCard title="Active sites" value="09" change="+4%" icon={Factory} tone="green" />
        <StatCard title="Labor utilization" value="88%" change="+6%" icon={ClipboardCheck} tone="amber" />
        <StatCard title="Net expenditure" value="$482K" change="-3%" icon={DollarSign} tone="slate" trend="down" />
      </div>

      <div className="grid gap-6 xl:grid-cols-[1.6fr_0.9fr]">
        <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <div className="mb-5 flex items-center justify-between">
            <div>
              <h3 className="text-lg font-semibold text-slate-900">Operational trend</h3>
              <p className="text-sm text-slate-500">Monthly productivity</p>
            </div>
            <div className="flex items-center gap-2 text-sm font-medium text-emerald-600">
              <TrendingUp className="h-4 w-4" />
              18.4%
            </div>
          </div>
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={overviewData}>
                <defs>
                  <linearGradient id="colorValue" x1="0" x2="0" y1="0" y2="1">
                    <stop offset="5%" stopColor="#2563eb" stopOpacity={0.4} />
                    <stop offset="95%" stopColor="#2563eb" stopOpacity={0.05} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                <XAxis dataKey="name" stroke="#64748b" />
                <YAxis stroke="#64748b" />
                <Tooltip />
                <Area type="monotone" dataKey="value" stroke="#2563eb" fill="url(#colorValue)" strokeWidth={3} />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <h3 className="text-lg font-semibold text-slate-900">Project progress</h3>
          <div className="mt-5 space-y-5">
            {projectProgress.map((entry) => (
              <div key={entry.name}>
                <div className="mb-2 flex justify-between text-sm">
                  <span className="font-medium text-slate-700">{entry.name}</span>
                  <span className="text-slate-500">{entry.value}%</span>
                </div>
                <div className="h-2.5 overflow-hidden rounded-full bg-slate-200">
                  <div className="h-full rounded-full bg-blue-600" style={{ width: `${entry.value}%` }} />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="grid gap-6 xl:grid-cols-[1.1fr_1.3fr]">
        <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <h3 className="mb-4 text-lg font-semibold text-slate-900">Recent activity</h3>
          <div className="space-y-4">
            {recentActivity.map((item) => (
              <div key={item.title} className="flex items-start justify-between gap-3 rounded-xl bg-slate-50 p-3">
                <div>
                  <p className="font-medium text-slate-800">{item.title}</p>
                  <p className="mt-1 text-sm text-slate-500">{item.site}</p>
                </div>
                <div className="text-right">
                  <p className="text-xs text-slate-400">{item.time}</p>
                  <span className="mt-2 inline-flex rounded-full bg-blue-100 px-2 py-1 text-[10px] font-medium text-blue-700">
                    {item.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <h3 className="mb-4 text-lg font-semibold text-slate-900">Weekly dispatch</h3>
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={overviewData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                <XAxis dataKey="name" stroke="#64748b" />
                <YAxis stroke="#64748b" />
                <Tooltip />
                <Bar dataKey="value" fill="#0f172a" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}
