export default function StatusBadge({ status }) {
  const styles = {
    active: 'bg-emerald-100 text-emerald-700',
    pending: 'bg-amber-100 text-amber-700',
    on_track: 'bg-blue-100 text-blue-700',
    completed: 'bg-slate-200 text-slate-700',
    overdue: 'bg-red-100 text-red-700'
  };

  const label = status?.toString() || 'Pending';

  return (
    <span className={`inline-flex rounded-full px-2.5 py-1 text-xs font-medium ${styles[label.toLowerCase()] || 'bg-slate-100 text-slate-700'}`}>
      {label}
    </span>
  );
}
