import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'Attendance ID' },
  { key: 'employeeName', label: 'Employee' },
  { key: 'employeeId', label: 'Employee ID' },
  { key: 'siteId', label: 'Site ID' },
  { key: 'attendanceDate', label: 'Date' },
  { key: 'status', label: 'Status', render: (row) => <StatusBadge status={row.status} /> },
  { key: 'overtimeHours', label: 'Overtime hours' },
  { key: 'remarks', label: 'Remarks' }
];

export default function AttendanceList() {
  const { data, loading, error, reload } = useApiResource('/attendance');
  return (
    <ResourceTablePage
      title="Attendance"
      subtitle="Attendance records returned by the attendance API."
      items={data}
      columns={columns}
      searchFields={['id', 'employeeName', 'employeeId', 'siteId', 'attendanceDate', 'status']}
      searchPlaceholder="Search attendance"
      loading={loading}
      error={error}
      onRetry={reload}
    />
  );
}
