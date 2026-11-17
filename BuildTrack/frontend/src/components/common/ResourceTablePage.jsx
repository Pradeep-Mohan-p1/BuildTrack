import { useMemo, useState } from 'react';
import { Search } from 'lucide-react';
import EmptyState from './EmptyState';
import SectionHeader from './SectionHeader';

export default function ResourceTablePage({
  title,
  subtitle,
  items,
  columns,
  searchFields = [],
  searchPlaceholder = 'Search',
  filterField,
  filterOptions = [],
  filterLabel = 'All',
  loading,
  error,
  onRetry,
  emptyTitle = 'No records yet',
  emptyDescription = 'Records will appear here when they are available.',
  action
}) {
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('');

  const filteredItems = useMemo(() => {
    const query = search.trim().toLowerCase();
    return items.filter((item) => {
      const matchesSearch = !query || searchFields.some((field) =>
        String(item[field] ?? '').toLowerCase().includes(query));
      const matchesFilter = !filter || String(item[filterField] ?? '') === filter;
      return matchesSearch && matchesFilter;
    });
  }, [filter, filterField, items, search, searchFields]);

  return (
    <div className="space-y-5">
      <SectionHeader title={title} subtitle={subtitle} action={action} />

      <div className="flex flex-col gap-3 sm:flex-row">
        <label className="flex min-w-0 flex-1 items-center gap-2 rounded-xl border border-slate-200 bg-white px-3 py-2.5">
          <Search className="h-4 w-4 shrink-0 text-slate-400" />
          <span className="sr-only">{searchPlaceholder}</span>
          <input
            type="search"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
            placeholder={searchPlaceholder}
            className="w-full bg-transparent text-sm outline-none"
          />
        </label>
        {filterField ? (
          <label className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3 py-2.5 text-sm text-slate-600">
            <span>{filterLabel}</span>
            <select
              value={filter}
              onChange={(event) => setFilter(event.target.value)}
              className="bg-transparent font-medium text-slate-800 outline-none"
            >
              <option value="">All</option>
              {filterOptions.map((option) => (
                <option key={option.value} value={option.value}>{option.label}</option>
              ))}
            </select>
          </label>
        ) : null}
      </div>

      {error ? (
        <div role="alert" className="flex flex-wrap items-center justify-between gap-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          <span>{error}</span>
          <button type="button" onClick={onRetry} className="font-semibold underline">Try again</button>
        </div>
      ) : loading ? (
        <div role="status" className="rounded-2xl border border-slate-200 bg-white p-8 text-center text-sm text-slate-500">
          Loading {title.toLowerCase()}…
        </div>
      ) : filteredItems.length === 0 ? (
        <EmptyState
          title={items.length ? 'No matching records' : emptyTitle}
          description={items.length ? 'Try changing your search or filter.' : emptyDescription}
        />
      ) : (
        <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-soft">
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-slate-200 text-left text-sm">
              <thead className="bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
                <tr>
                  {columns.map((column) => (
                    <th key={column.key} scope="col" className="whitespace-nowrap px-4 py-3 font-semibold">
                      {column.label}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filteredItems.map((item, index) => (
                  <tr key={item.id ?? index} className="text-slate-700 hover:bg-slate-50">
                    {columns.map((column) => (
                      <td key={column.key} className="whitespace-nowrap px-4 py-3">
                        {column.render ? column.render(item) : (item[column.key] ?? '—')}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
