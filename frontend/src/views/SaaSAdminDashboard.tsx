import React, { useState, useEffect } from 'react';
import { 
  Building, 
  DollarSign, 
  TrendingUp, 
  Users, 
  AlertOctagon, 
  CheckCircle2, 
  ShieldAlert, 
  Search, 
  Sparkles,
  ClipboardList
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

export default function SaaSAdminDashboard() {
  const [overview, setOverview] = useState<any | null>(null);
  const [tenants, setTenants] = useState<any[]>([]);
  const [auditLogs, setAuditLogs] = useState<any[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [statusMsg, setStatusMsg] = useState('');

  const fetchAdminData = async () => {
    try {
      const [overRes, tenRes, logRes] = await Promise.all([
        api.get('/admin/overview'),
        api.get('/admin/tenants'),
        api.get('/admin/audit-logs')
      ]);
      setOverview(overRes.data);
      setTenants(tenRes.data || []);
      setAuditLogs(logRes.data || []);
    } catch (err) {
      console.error('Failed to fetch SaaS admin data from database:', err);
    }
  };

  useEffect(() => {
    fetchAdminData();
  }, []);

  const handleSuspendTenant = async (tenantId: string) => {
    try {
      await api.post(`/admin/tenants/${tenantId}/suspend`, {
        reason: 'Administrative policy'
      });
      setStatusMsg(`Tenant ${tenantId} suspended.`);
      fetchAdminData();
    } catch (err) {
      console.error(err);
      setStatusMsg(`Failed to suspend tenant.`);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleReactivateTenant = async (tenantId: string) => {
    try {
      await api.post(`/admin/tenants/${tenantId}/reactivate`, {});
      setStatusMsg(`Tenant ${tenantId} reactivated.`);
      fetchAdminData();
    } catch (err) {
      console.error(err);
      setStatusMsg(`Failed to reactivate tenant.`);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  if (!overview) {
    return (
      <div className="flex-1 bg-slate-50 p-8 flex items-center justify-center text-xs font-bold text-slate-500">
        Loading SaaS Super Admin Platform Analytics...
      </div>
    );
  }

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div>
          <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">SaaS Super Admin Platform Center</h2>
          <p className="text-xs text-slate-550 mt-0.5">Platform revenue metrics, tenant subscription management, and system audits</p>
        </div>
      </div>

      {statusMsg ? (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      ) : null}

      {/* Main Workspace */}
      <div className="flex-1 overflow-y-auto space-y-6 pr-2 pb-6">
        
        {/* Metric Cards Grid */}
        <div className="grid grid-cols-4 gap-6">
          <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
            <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Monthly Recurring (MRR)</span>
            <span className="text-2xl font-bold text-slate-900 font-mono">${overview.mrr.toLocaleString()}</span>
          </div>
          <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
            <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Annual Recurring (ARR)</span>
            <span className="text-2xl font-bold text-emerald-650 font-mono">${overview.arr.toLocaleString()}</span>
          </div>
          <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
            <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Active Tenants</span>
            <span className="text-2xl font-bold text-indigo-650 font-mono">{overview.activeTenants} / {overview.totalTenants}</span>
          </div>
          <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
            <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Churn Rate</span>
            <span className="text-2xl font-bold text-slate-800 font-mono">{overview.churnRate}%</span>
          </div>
        </div>

        {/* Tenant Management Table */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm flex flex-col space-y-4">
          <div className="flex justify-between items-center">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
              <Building className="w-4 h-4 text-indigo-600" />
              <span>Multi-Tenant Directory & Subscriptions</span>
            </h3>

            <div className="w-64">
              <Input
                type="text"
                placeholder="Search tenant name..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
          </div>

          <Table>
            <TableHead>
              <th className="pb-3">Tenant ID</th>
              <th className="pb-3">Organization Name</th>
              <th className="pb-3">Plan Tier</th>
              <th className="pb-3">Monthly Yield</th>
              <th className="pb-3">Status</th>
              <th className="pb-3 text-center">Actions</th>
            </TableHead>
            <TableBody>
              {tenants
                .filter(t => t.name.toLowerCase().includes(searchQuery.toLowerCase()))
                .map(t => {
                  const isSuspended = t.status === 'SUSPENDED';
                  return (
                    <tr key={t.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                      <td className="py-3 font-mono font-bold text-indigo-600">{t.id}</td>
                      <td className="py-3 font-semibold text-slate-800">{t.name}</td>
                      <td className="py-3 font-bold text-slate-700">{t.planKey}</td>
                      <td className="py-3 font-mono text-slate-900">${t.mrr}</td>
                      <td className="py-3">
                        <Badge status={isSuspended ? 'error' : t.status === 'ACTIVE' ? 'success' : 'warning'}>
                          {t.status}
                        </Badge>
                      </td>
                      <td className="py-3 text-center">
                        {isSuspended ? (
                          <Button variant="outline" onClick={() => handleReactivateTenant(t.id)} className="!py-1 border-emerald-200 text-emerald-600 hover:bg-emerald-50">
                            Reactivate
                          </Button>
                        ) : (
                          <Button variant="outline" onClick={() => handleSuspendTenant(t.id)} className="!py-1 border-rose-200 text-rose-600 hover:bg-rose-50">
                            Suspend
                          </Button>
                        )}
                      </td>
                    </tr>
                  );
                })}
            </TableBody>
          </Table>
        </div>

        {/* Audit Log Panel */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm flex flex-col space-y-4">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
            <ClipboardList className="w-4 h-4 text-indigo-600" />
            <span>Platform Billing & Subscription Audit Trail</span>
          </h3>

          <Table>
            <TableHead>
              <th className="pb-3">Timestamp</th>
              <th className="pb-3">Tenant ID</th>
              <th className="pb-3">Actor</th>
              <th className="pb-3">Action</th>
              <th className="pb-3">Remarks</th>
            </TableHead>
            <TableBody>
              {auditLogs.map(log => (
                <tr key={log.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                  <td className="py-3 text-slate-500 font-mono">{new Date(log.timestamp).toLocaleTimeString()}</td>
                  <td className="py-3 font-mono font-bold text-indigo-600">{log.tenantId}</td>
                  <td className="py-3 font-semibold text-slate-700">{log.actorId}</td>
                  <td className="py-3 font-bold text-slate-900">{log.action}</td>
                  <td className="py-3 text-slate-600">{log.remarks}</td>
                </tr>
              ))}
            </TableBody>
          </Table>
        </div>

      </div>
    </div>
  );
}
