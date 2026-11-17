import { useState } from 'react';
import { Plus, X } from 'lucide-react';
import api from '../../api/axiosInstance';
import { apiErrorMessage } from '../../api/apiErrorMessage';
import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const PROJECT_STATUSES = ['PLANNED', 'ONGOING', 'ON_HOLD', 'COMPLETED', 'CANCELLED'];
const initialForm = {
  projectName: '',
  description: '',
  clientName: '',
  location: '',
  budget: '',
  startDate: '',
  endDate: ''
};

const columns = [
  { key: 'id', label: 'Project ID' },
  { key: 'projectName', label: 'Project name' },
  { key: 'clientName', label: 'Client' },
  { key: 'location', label: 'Location' },
  {
    key: 'budget',
    label: 'Budget',
    render: (project) => project.budget == null ? '—' : Number(project.budget).toLocaleString()
  },
  { key: 'startDate', label: 'Start date' },
  { key: 'endDate', label: 'End date' },
  { key: 'status', label: 'Status', render: (project) => <StatusBadge status={project.status} /> },
  {
    key: 'progress',
    label: 'Progress',
    render: (project) => project.progress == null ? '—' : (
      <div className="flex min-w-24 items-center gap-2">
        <div className="h-2 flex-1 overflow-hidden rounded-full bg-slate-200">
          <div className="h-full rounded-full bg-blue-600" style={{ width: `${Math.min(100, Math.max(0, project.progress))}%` }} />
        </div>
        <span>{project.progress}%</span>
      </div>
    )
  }
];

export default function ProjectList() {
  const { data: projects, loading, error, reload } = useApiResource('/projects');
  const [formOpen, setFormOpen] = useState(false);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);
  const [createError, setCreateError] = useState('');

  const handleChange = (event) => {
    setForm((current) => ({ ...current, [event.target.name]: event.target.value }));
  };

  const handleCreate = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    setCreateError('');
    try {
      await api.post('/projects', {
        ...form,
        budget: form.budget === '' ? null : Number(form.budget),
        startDate: form.startDate || null,
        endDate: form.endDate || null
      });
      setForm(initialForm);
      setFormOpen(false);
      await reload();
    } catch (requestError) {
      setCreateError(apiErrorMessage(requestError));
    } finally {
      setSubmitting(false);
    }
  };

  const action = (
    <button
      type="button"
      onClick={() => { setFormOpen((open) => !open); setCreateError(''); }}
      className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
    >
      {formOpen ? <X className="h-4 w-4" /> : <Plus className="h-4 w-4" />}
      {formOpen ? 'Close' : 'New project'}
    </button>
  );

  return (
    <div className="space-y-5">
      {formOpen ? (
        <form onSubmit={handleCreate} className="space-y-4 rounded-2xl border border-slate-200 bg-white p-5 shadow-soft">
          <div>
            <h3 className="font-semibold text-slate-900">Create project</h3>
            <p className="text-sm text-slate-500">Submit the fields supported by the project API.</p>
          </div>
          {createError ? <p role="alert" className="rounded-xl border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{createError}</p> : null}
          <div className="grid gap-3 md:grid-cols-2">
            {[
              ['projectName', 'Project name', 'text'],
              ['clientName', 'Client', 'text'],
              ['location', 'Location', 'text'],
              ['budget', 'Budget', 'number'],
              ['startDate', 'Start date', 'date'],
              ['endDate', 'End date', 'date']
            ].map(([name, label, type]) => (
              <label key={name} className="space-y-1 text-sm font-medium text-slate-700">
                {label}
                <input
                  name={name}
                  type={type}
                  value={form[name]}
                  onChange={handleChange}
                  min={type === 'number' ? '0' : undefined}
                  step={type === 'number' ? '0.01' : undefined}
                  required={name === 'projectName'}
                  className="w-full rounded-xl border border-slate-200 px-3 py-2 font-normal outline-none focus:border-blue-500"
                />
              </label>
            ))}
            <label className="space-y-1 text-sm font-medium text-slate-700 md:col-span-2">
              Description
              <textarea
                name="description"
                value={form.description}
                onChange={handleChange}
                rows="2"
                className="w-full rounded-xl border border-slate-200 px-3 py-2 font-normal outline-none focus:border-blue-500"
              />
            </label>
          </div>
          <button type="submit" disabled={submitting} className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-60">
            {submitting ? 'Creating…' : 'Create project'}
          </button>
        </form>
      ) : null}
      <ResourceTablePage
        title="Projects"
        subtitle="View active construction projects and milestones."
        items={projects}
        columns={columns}
        searchFields={['id', 'projectName', 'clientName', 'location']}
        searchPlaceholder="Search projects"
        filterField="status"
        filterLabel="Status"
        filterOptions={PROJECT_STATUSES.map((status) => ({ value: status, label: status.replaceAll('_', ' ') }))}
        loading={loading}
        error={error}
        onRetry={reload}
        emptyDescription="Projects returned by the backend will appear here."
        action={action}
      />
    </div>
  );
}
