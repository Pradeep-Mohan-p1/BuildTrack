import SectionHeader from '../../components/common/SectionHeader';

export default function Settings() {
  return (
    <div>
      <SectionHeader title="Settings" subtitle="BuildTrack application preferences." />
      <div className="rounded-2xl border border-amber-200 bg-amber-50 p-5 text-sm text-amber-900">
        Settings integration is pending: the backend currently exposes no settings endpoint.
      </div>
    </div>
  );
}
