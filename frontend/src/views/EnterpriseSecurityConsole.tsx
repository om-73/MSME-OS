import React, { useState, useEffect } from 'react';
import { 
  ShieldCheck, 
  UserCheck, 
  Key, 
  Lock, 
  Smartphone, 
  AlertOctagon, 
  CheckCircle2, 
  XCircle, 
  Clock, 
  Plus, 
  Sparkles,
  Users,
  Building,
  ClipboardList,
  Flame
} from 'lucide-react';
import { api } from '../api/client';
import { 
  Button, 
  Card, 
  Badge, 
  Dialog, 
  Input, 
  Select,
  Table,
  TableHead,
  TableBody
} from '../components/DesignSystem';

interface EnterpriseSecurityConsoleProps {
  user?: any;
}

export default function EnterpriseSecurityConsole({ user }: EnterpriseSecurityConsoleProps) {
  const [roles, setRoles] = useState<any[]>([]);
  const [departments, setDepartments] = useState<any[]>([]);
  const [sessions, setSessions] = useState<any[]>([]);
  const [approvals, setApprovals] = useState<any[]>([]);
  const [breakGlassSessions, setBreakGlassSessions] = useState<any[]>([]);
  const [policy, setPolicy] = useState<any | null>(null);

  // Subtabs
  const [activeTab, setActiveTab] = useState<'roles' | 'departments' | 'sessions' | 'approvals' | 'policies'>('roles');

  // Modals
  const [showRoleModal, setShowRoleModal] = useState(false);
  const [showBreakGlassModal, setShowBreakGlassModal] = useState(false);
  const [breakGlassReason, setBreakGlassReason] = useState('Emergency production halt override');
  const [statusMsg, setStatusMsg] = useState('');

  // Role Form
  const [roleForm, setRoleForm] = useState({ name: 'QC Supervisor', description: 'Quality inspection lead', permissions: 'orders:view,qc:view,qc:inspect,qc:approve,qc:reject' });

  const fetchSecurityData = async () => {
    try {
      const [rolesRes, deptRes, sessRes, appRes, bgRes, polRes] = await Promise.all([
        api.get('/security/roles'),
        api.get('/security/departments'),
        api.get(`/security/sessions?userId=${user?.email || 'user@apex.com'}`),
        api.get('/security/approvals'),
        api.get('/security/break-glass'),
        api.get('/security/policies')
      ]);
      setRoles(rolesRes.data || []);
      setDepartments(deptRes.data || []);
      setSessions(sessRes.data || []);
      setApprovals(appRes.data || []);
      setBreakGlassSessions(bgRes.data || []);
      setPolicy(polRes.data || {});
    } catch (err) {
      console.error('Failed to fetch security data from database:', err);
    }
  };

  useEffect(() => {
    fetchSecurityData();
  }, [user]);

  const handleSaveRole = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/security/roles', roleForm);
      setStatusMsg('Custom enterprise role saved.');
      fetchSecurityData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to save role.');
    }
    setShowRoleModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleRevokeSession = async (sessionId: number) => {
    try {
      await api.post(`/security/sessions/${sessionId}/revoke`, {});
      setStatusMsg('User session revoked.');
      fetchSecurityData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to revoke session.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleApproveRequest = async (reqId: number) => {
    try {
      await api.post(`/security/approvals/${reqId}/approve?approvedBy=${user?.email || 'factory_owner'}`, {});
      setStatusMsg('Approval request approved & executed.');
      fetchSecurityData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to approve request.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleTriggerBreakGlass = async () => {
    try {
      await api.post('/security/break-glass', {
        reason: breakGlassReason,
        actorId: user?.email || 'factory_owner'
      });
      setStatusMsg('Emergency Break-Glass Session initiated (Active for 2 hours).');
      fetchSecurityData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to trigger break-glass session.');
    }
    setShowBreakGlassModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Enterprise Identity, RBAC & Governance</h2>
            <p className="text-xs text-slate-550 mt-0.5">Centralized permission matrix, department scoping, MFA policies, session revocation & break-glass access</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'roles', label: 'Roles & Matrix' },
              { id: 'departments', label: 'Department Scoping' },
              { id: 'sessions', label: 'Active Sessions' },
              { id: 'approvals', label: 'Approvals & Break-Glass' },
              { id: 'policies', label: 'Security Policies' }
            ].map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as any)}
                className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                  activeTab === tab.id ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>
        </div>

        <Button variant="danger" onClick={() => setShowBreakGlassModal(true)}>
          <Flame className="w-4 h-4" />
          <span>Emergency Break-Glass</span>
        </Button>
      </div>

      {statusMsg ? (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      ) : null}

      {/* Main Workspace */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm overflow-hidden">
        
        {/* 1. ROLES & PERMISSION MATRIX */}
        {activeTab === 'roles' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
                <ShieldCheck className="w-4 h-4 text-indigo-600" />
                <span>Enterprise Custom Roles & Permission Matrix</span>
              </h3>

              <Button variant="primary" onClick={() => setShowRoleModal(true)}>
                <Plus className="w-4 h-4" />
                <span>Create Custom Role</span>
              </Button>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {roles.map(r => (
                <div key={r.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2 text-xs">
                  <div className="flex justify-between items-center">
                    <span className="font-bold text-slate-900 text-sm">{r.name}</span>
                    <Badge status="info">Custom Role</Badge>
                  </div>
                  <p className="text-slate-600">{r.description}</p>
                  <div className="pt-2">
                    <span className="text-[10px] text-slate-400 font-semibold block mb-1 uppercase tracking-wider">Assigned Permission Scopes</span>
                    <div className="flex flex-wrap gap-1.5 font-mono">
                      {r.permissions.split(',').map((p: string) => (
                        <span key={p} className="px-2 py-0.5 bg-white border border-slate-200 text-indigo-700 rounded text-[10px] font-bold">
                          {p}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 2. DEPARTMENT SCOPING */}
        {activeTab === 'departments' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Building className="w-4 h-4 text-indigo-600" />
              <span>Department Boundary Scoping</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">User Email</th>
                  <th className="pb-3">Assigned Department</th>
                  <th className="pb-3">Access Level</th>
                  <th className="pb-3">Boundary Restriction</th>
                </TableHead>
                <TableBody>
                  {departments.map(d => (
                    <tr key={d.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                      <td className="py-3 font-semibold text-slate-800">{d.userId}</td>
                      <td className="py-3 font-bold text-indigo-600">{d.departmentName}</td>
                      <td className="py-3 font-mono">{d.accessLevel}</td>
                      <td className="py-3 text-slate-500 italic">Restricted to {d.departmentName} operational data</td>
                    </tr>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

        {/* 3. ACTIVE SESSIONS & DEVICE SECURITY */}
        {activeTab === 'sessions' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Smartphone className="w-4 h-4 text-indigo-600" />
              <span>Active User Login Sessions & Device Management</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Device / Client</th>
                  <th className="pb-3">Browser</th>
                  <th className="pb-3">IP Metadata</th>
                  <th className="pb-3">Last Active</th>
                  <th className="pb-3">Status</th>
                  <th className="pb-3 text-center">Action</th>
                </TableHead>
                <TableBody>
                  {sessions.map(s => {
                    const isRevoked = s.status === 'REVOKED';
                    return (
                      <tr key={s.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                        <td className="py-3 font-semibold text-slate-900">{s.deviceName}</td>
                        <td className="py-3 text-slate-600">{s.browserName}</td>
                        <td className="py-3 font-mono text-slate-500">{s.ipAddress}</td>
                        <td className="py-3 text-slate-500 font-mono">{new Date(s.lastActiveAt).toLocaleTimeString()}</td>
                        <td className="py-3"><Badge status={isRevoked ? 'error' : 'success'}>{s.status}</Badge></td>
                        <td className="py-3 text-center">
                          {!isRevoked && (
                            <Button variant="outline" onClick={() => handleRevokeSession(s.id)} className="!py-1 border-rose-200 text-rose-600 hover:bg-rose-50">
                              Revoke Session
                            </Button>
                          )}
                        </td>
                      </tr>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

        {/* 4. APPROVALS & BREAK-GLASS */}
        {activeTab === 'approvals' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <CheckCircle2 className="w-4 h-4 text-indigo-600" />
              <span>Pending Approvals & Emergency Break-Glass Log</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-6">
              {/* Approval Requests List */}
              <div className="space-y-3">
                <h4 className="font-bold text-slate-800 text-[11px] uppercase tracking-wider">Pending Approval Requests</h4>
                {approvals.map(a => {
                  const isApproved = a.status === 'APPROVED';
                  return (
                    <div key={a.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
                      <div className="flex justify-between items-center">
                        <span className="font-bold text-slate-900">{a.title}</span>
                        <Badge status={isApproved ? 'success' : 'warning'}>{a.status}</Badge>
                      </div>
                      <p className="text-slate-600">{a.details}</p>
                      <div className="flex justify-between items-center pt-2">
                        <span className="text-[10px] text-slate-400">Requested by {a.requestedBy}</span>
                        {!isApproved && (
                          <Button variant="primary" onClick={() => handleApproveRequest(a.id)} className="!py-1">
                            Approve Request
                          </Button>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* Break Glass Active Logs */}
              <div className="space-y-3">
                <h4 className="font-bold text-rose-700 text-[11px] uppercase tracking-wider flex items-center space-x-1">
                  <Flame className="w-3.5 h-3.5" />
                  <span>Break-Glass Emergency Sessions</span>
                </h4>
                {breakGlassSessions.map(bg => (
                  <div key={bg.id} className="p-4 bg-rose-50 border border-rose-200 rounded-xl space-y-1 text-rose-900">
                    <div className="flex justify-between font-bold">
                      <span>Emergency Session by {bg.actorId}</span>
                      <Badge status="error">ACTIVE (2 Hours)</Badge>
                    </div>
                    <p className="text-xs text-rose-800">Reason: {bg.emergencyReason}</p>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* 5. SECURITY POLICIES */}
        {activeTab === 'policies' && policy && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Lock className="w-4 h-4 text-indigo-600" />
              <span>Tenant Security & MFA Policies</span>
            </h3>

            <div className="max-w-xl space-y-4">
              <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                <label className="flex items-center justify-between cursor-pointer font-semibold">
                  <span>Require Multi-Factor Authentication (MFA) for Admins</span>
                  <input type="checkbox" checked={policy.mfaRequiredForAdmins} onChange={() => {}} className="w-4 h-4 text-indigo-600 rounded" />
                </label>
                <label className="flex items-center justify-between cursor-pointer font-semibold">
                  <span>Require MFA for Operational Workers</span>
                  <input type="checkbox" checked={policy.mfaRequiredForWorkers} onChange={() => {}} className="w-4 h-4 text-indigo-600 rounded" />
                </label>
              </div>

              <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                <div>
                  <label className="block text-slate-500 mb-1 font-semibold">Session Timeout (Minutes)</label>
                  <Input type="number" value={policy.sessionTimeoutMinutes} onChange={() => {}} />
                </div>
                <div>
                  <label className="block text-slate-500 mb-1 font-semibold">Minimum Password Length</label>
                  <Input type="number" value={policy.minPasswordLength} onChange={() => {}} />
                </div>
              </div>
            </div>
          </div>
        )}

      </div>

      {/* 1. Custom Role Modal */}
      <Dialog isOpen={showRoleModal} onClose={() => setShowRoleModal(false)} title="Create Custom Enterprise Role">
        <form onSubmit={handleSaveRole} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Role Name</label>
            <Input
              type="text"
              value={roleForm.name}
              onChange={(e) => setRoleForm({ ...roleForm, name: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Description</label>
            <Input
              type="text"
              value={roleForm.description}
              onChange={(e) => setRoleForm({ ...roleForm, description: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Assigned Scopes (Comma-separated)</label>
            <Input
              type="text"
              value={roleForm.permissions}
              onChange={(e) => setRoleForm({ ...roleForm, permissions: e.target.value })}
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowRoleModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Save Role
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Break Glass Modal */}
      <Dialog isOpen={showBreakGlassModal} onClose={() => setShowBreakGlassModal(false)} title="Trigger Emergency Break-Glass Access">
        <div className="space-y-4 text-xs">
          <div className="p-3 bg-rose-50 border border-rose-200 text-rose-800 rounded-lg space-y-1">
            <span className="font-bold block">WARNING: Emergency Access Override</span>
            <p>Triggering Break-Glass grants temporary administrative bypass for 2 hours. All actions will be logged into the immutable security audit log.</p>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Emergency Justification Reason</label>
            <Input
              type="text"
              value={breakGlassReason}
              onChange={(e) => setBreakGlassReason(e.target.value)}
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowBreakGlassModal(false)}>
              Cancel
            </Button>
            <Button type="button" variant="danger" onClick={handleTriggerBreakGlass}>
              Initiate Break-Glass
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
