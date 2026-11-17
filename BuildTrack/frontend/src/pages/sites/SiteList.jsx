import { useMemo } from 'react';
import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'Site ID' },
  { key: 'siteName', label: 'Site name' },
  { key: 'address', label: 'Address' },
  { key: 'city', label: 'City' },
  { key: 'state', label: 'State' },
  {
    key: 'projectName',
    label: 'Project',
    render: (site) => site.projectName || (site.projectId ? `Project ${site.projectId}` : '—')
  },
  { key: 'startDate', label: 'Start date' },
  { key: 'endDate', label: 'End date' },
  { key: 'status', label: 'Status', render: (site) => <StatusBadge status={site.status} /> }
];

export default function SiteList() {
  const { data: sites, loading, error, reload } = useApiResource('/sites');
  const projectOptions = useMemo(() => {
    const projects = new Map();
    sites.forEach((site) => {
      if (site.projectId != null) {
        projects.set(String(site.projectId), site.projectName || `Project ${site.projectId}`);
      }
    });
    return [...projects].map(([value, label]) => ({ value, label }));
  }, [sites]);

  return (
    <ResourceTablePage
      title="Sites"
      subtitle="Track job site progress and assignments."
      items={sites}
      columns={columns}
      searchFields={['id', 'siteName', 'address', 'city', 'state', 'projectName']}
      searchPlaceholder="Search sites"
      filterField="projectId"
      filterLabel="Project"
      filterOptions={projectOptions}
      loading={loading}
      error={error}
      onRetry={reload}
      emptyDescription="Sites returned by the backend will appear here."
    />
  );
}
