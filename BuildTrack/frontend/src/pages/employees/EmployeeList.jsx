import StatusBadge from '../../components/common/StatusBadge';
import ResourceTablePage from '../../components/common/ResourceTablePage';
import useApiResource from '../../hooks/useApiResource';

const number = (value) => value == null ? '—' : Number(value).toLocaleString();

const columns = [
  { key: 'id', label: 'Employee ID' },
  { key: 'name', label: 'Name' },
  { key: 'email', label: 'Email' },
  { key: 'phone', label: 'Phone' },
  { key: 'designation', label: 'Designation' },
  { key: 'department', label: 'Department' },
  { key: 'dailyWage', label: 'Daily wage', render: (employee) => number(employee.dailyWage) },
  { key: 'monthlySalary', label: 'Monthly salary', render: (employee) => number(employee.monthlySalary) },
  {
    key: 'active',
    label: 'Status',
    render: (employee) => <StatusBadge status={employee.active ? 'ACTIVE' : 'INACTIVE'} />
  }
];

export default function EmployeeList() {
  const { data: employees, loading, error, reload } = useApiResource('/employees');

  return (
    <ResourceTablePage
      title="Employees"
      subtitle="Manage workforce details and assignments."
      items={employees}
      columns={columns}
      searchFields={['id', 'name', 'email', 'phone', 'designation', 'department']}
      searchPlaceholder="Search employees"
      loading={loading}
      error={error}
      onRetry={reload}
      emptyDescription="Employees returned by the backend will appear here."
    />
  );
}
