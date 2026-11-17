import ResourceTablePage from '../../components/common/ResourceTablePage';
import StatusBadge from '../../components/common/StatusBadge';
import useApiResource from '../../hooks/useApiResource';

const columns = [
  { key: 'id', label: 'Expense ID' },
  { key: 'projectName', label: 'Project' },
  { key: 'description', label: 'Description' },
  { key: 'category', label: 'Category' },
  { key: 'amount', label: 'Amount', render: (row) => row.amount == null ? '—' : Number(row.amount).toLocaleString() },
  { key: 'expenseDate', label: 'Date' },
  { key: 'vendorName', label: 'Vendor' },
  { key: 'invoiceNumber', label: 'Invoice' },
  { key: 'status', label: 'Status', render: (row) => <StatusBadge status={row.status} /> }
];

export default function ExpenseList() {
  const { data, loading, error, reload } = useApiResource('/expenses');
  return (
    <ResourceTablePage
      title="Expenses"
      subtitle="Expense records returned by the expenses API."
      items={data}
      columns={columns}
      searchFields={['id', 'projectName', 'description', 'category', 'vendorName', 'invoiceNumber', 'status']}
      searchPlaceholder="Search expenses"
      filterField="status"
      filterLabel="Status"
      filterOptions={['PENDING', 'APPROVED', 'REJECTED'].map((status) => ({ value: status, label: status }))}
      loading={loading}
      error={error}
      onRetry={reload}
    />
  );
}
