export default function StatCard({ title, value, change, helperText, trend = 'up', icon: Icon, tone = 'blue' }) {
  const toneClasses = {
    blue: 'bg-blue-50 text-blue-600',
    green: 'bg-emerald-50 text-emerald-600',
    amber: 'bg-amber-50 text-amber-600',
    slate: 'bg-slate-100 text-slate-700'
  };

  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-sm font-medium text-slate-500">{title}</p>
          <h3 className="mt-4 text-3xl font-semibold text-slate-900">{value}</h3>
        </div>
        <div className={`rounded-xl p-3 ${toneClasses[tone] || toneClasses.blue}`}>
          {Icon ? <Icon className="h-5 w-5" /> : null}
        </div>
      </div>
      {change || helperText ? (
        <div className="mt-4 flex items-center gap-2 text-sm">
          {change ? (
            <>
              <span className={`font-medium ${trend === 'down' ? 'text-red-500' : 'text-emerald-600'}`}>
                {change}
              </span>
              <span className="text-slate-400">vs last month</span>
            </>
          ) : null}
          {helperText ? <span className="text-slate-400">{helperText}</span> : null}
        </div>
      ) : null}
    </div>
  );
}
