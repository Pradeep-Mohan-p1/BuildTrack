import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'Assignment ID' },
  { key: 'employeeName', label: 'Employee' },
  { key: 'employeeId', label: 'Employee ID' },
  { key: 'projectId', label: 'Project ID' },
  { key: 'siteId', label: 'Site ID' },
  { key: 'assignedDate', label: 'Assigned date' },
  { key: 'endDate', label: 'End date' },
  { key: 'active', label: 'Status', render: (row) => <StatusBadge status={row.active ? 'ACTIVE' : 'INACTIVE'} /> }
];

export default function AssignmentList() {
  const { data, loading, error, reload } = useApiResource('/assignments');
  return (
    <ResourceTablePage
      title="Assignments"
      subtitle="Employee assignments returned by the assignments API."
      items={data}
      columns={columns}
      searchFields={['id', 'employeeName', 'employeeId', 'projectId', 'siteId']}
      searchPlaceholder="Search assignments"
      loading={loading}
      error={error}
      onRetry={reload}
    />
  );
}
