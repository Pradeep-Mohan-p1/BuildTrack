import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'User ID' },
  { key: 'username', label: 'Username' },
  { key: 'email', label: 'Email' },
  { key: 'role', label: 'Role' },
  { key: 'enabled', label: 'Status', render: (user) => <StatusBadge status={user.enabled ? 'ACTIVE' : 'INACTIVE'} /> },
  { key: 'createdAt', label: 'Created' }
];

export default function UserList() {
  const { data, loading, error, reload } = useApiResource('/users');
  return (
    <ResourceTablePage
      title="Users"
      subtitle="Accounts and roles returned by the admin users API."
      items={data}
      columns={columns}
      searchFields={['id', 'username', 'email', 'role']}
      searchPlaceholder="Search users"
      loading={loading}
      error={error}
      onRetry={reload}
    />
  );
}
