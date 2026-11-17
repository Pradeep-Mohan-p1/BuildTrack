import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'Payroll ID' },
  { key: 'employeeName', label: 'Employee' },
  { key: 'employeeId', label: 'Employee ID' },
  { key: 'month', label: 'Month' },
  { key: 'year', label: 'Year' },
  { key: 'presentDays', label: 'Present days' },
  { key: 'absentDays', label: 'Absent days' },
  { key: 'grossSalary', label: 'Gross salary', render: (row) => row.grossSalary == null ? '—' : Number(row.grossSalary).toLocaleString() },
  { key: 'netSalary', label: 'Net salary', render: (row) => row.netSalary == null ? '—' : Number(row.netSalary).toLocaleString() },
  { key: 'status', label: 'Status', render: (row) => <StatusBadge status={row.status} /> }
];

export default function PayrollList() {
  const { data, loading, error, reload } = useApiResource('/payroll');
  return (
    <ResourceTablePage
      title="Payroll"
      subtitle="Payroll records returned by the payroll API."
      items={data}
      columns={columns}
      searchFields={['id', 'employeeName', 'employeeId', 'month', 'year', 'status']}
      searchPlaceholder="Search payroll"
      loading={loading}
      error={error}
      onRetry={reload}
    />
  );
}
