import ResourceTablePage from '../../components/common/ResourceTablePage';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'Log ID' },
  { key: 'username', label: 'User' },
  { key: 'action', label: 'Action' },
  { key: 'endpoint', label: 'Endpoint' },
  { key: 'httpMethod', label: 'Method' },
  { key: 'responseStatus', label: 'Response' },
  { key: 'ipAddress', label: 'IP address' },
  { key: 'timestamp', label: 'Timestamp' }
];

export default function AuditList() {
  const { data, loading, error, reload } = useApiResource('/audit', 'page');
  return (
    <ResourceTablePage
      title="Audit"
      subtitle="Recent audit log records. The API returns a paged result."
      items={data}
      columns={columns}
      searchFields={['id', 'username', 'action', 'endpoint', 'httpMethod', 'responseStatus', 'ipAddress']}
      searchPlaceholder="Search audit logs"
      loading={loading}
      error={error}
      onRetry={reload}
    />
  );
}
