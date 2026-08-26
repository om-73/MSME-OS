import React, { useState, useEffect } from 'react';
import { 
  CreditCard, 
  ArrowUpRight, 
  ArrowDownRight, 
  AlertTriangle, 
  CheckCircle, 
  Download, 
  ShieldCheck, 
  FileText, 
  Sparkles, 
  Layers, 
  Clock, 
  Lock,
  XCircle
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

interface TenantBillingDashboardProps {
  user: any;
}

export default function TenantBillingDashboard({ user }: TenantBillingDashboardProps) {
  const [plans, setPlans] = useState<any[]>([]);
  const [subscription, setSubscription] = useState<any | null>(null);
  const [usage, setUsage] = useState<any | null>(null);
  const [invoices, setInvoices] = useState<any[]>([]);

  // Modals
  const [showUpgradeModal, setShowUpgradeModal] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [targetPlanKey, setTargetPlanKey] = useState('PROFESSIONAL');
  const [cancelReason, setCancelReason] = useState('Found alternative');
  const [statusMsg, setStatusMsg] = useState('');

  const fetchBillingData = async () => {
    try {
      const [plansRes, subRes, usageRes, invRes] = await Promise.all([
        api.get('/billing/plans'),
        api.get('/billing/subscription'),
        api.get('/billing/usage'),
        api.get('/billing/invoices')
      ]);
      setPlans(plansRes.data || []);
      setSubscription(subRes.data);
      setUsage(usageRes.data);
      setInvoices(invRes.data || []);
    } catch (err) {
      console.error('Failed to fetch billing data from database:', err);
    }
  };

  useEffect(() => {
    fetchBillingData();
  }, [user]);

  const handleUpgrade = async (planKey: string) => {
    try {
      await api.post('/billing/subscription/upgrade', {
        planKey,
        billingCycle: 'MONTHLY'
      });
      setStatusMsg(`Subscription upgraded to ${planKey}. Invoice generated.`);
      fetchBillingData();
    } catch (err) {
      console.error(err);
      setStatusMsg(`Failed to upgrade subscription.`);
    }
    setShowUpgradeModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleCancel = async () => {
    try {
      await api.post('/billing/subscription/cancel', {
        reason: cancelReason
      });
      setStatusMsg('Subscription set to cancel at end of billing cycle.');
      fetchBillingData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to cancel subscription.');
    }
    setShowCancelModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  if (!subscription || !usage) {
    return (
      <div className="flex-1 bg-slate-50 p-8 flex items-center justify-center text-xs font-bold text-slate-500">
        Loading billing & subscription ledger...
      </div>
    );
  }

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div>
          <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Tenant Subscription & Billing</h2>
          <p className="text-xs text-slate-550 mt-0.5">Manage SaaS plan tiers, usage limit quotas, invoices, and billing profile settings</p>
        </div>

        <div className="flex space-x-2">
          <Button variant="outline" onClick={() => setShowCancelModal(true)} className="border-rose-200 text-rose-600 hover:bg-rose-50">
            <XCircle className="w-4 h-4" />
            <span>Cancel Subscription</span>
          </Button>
          <Button variant="primary" onClick={() => setShowUpgradeModal(true)}>
            <ArrowUpRight className="w-4 h-4" />
            <span>Upgrade Plan</span>
          </Button>
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
        
        {/* Top Active Subscription Card */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center space-x-2">
              <Badge status={subscription.status === 'ACTIVE' ? 'success' : 'warning'}>
                {subscription.status}
              </Badge>
              <span className="font-bold text-base text-slate-900">{subscription.planKey} Plan</span>
            </div>
            <p className="text-xs text-slate-550">
              Current Billing Cycle: <span className="font-semibold text-slate-700">{subscription.billingCycle}</span> (${subscription.currentPrice} / month)
            </p>
            <p className="text-[11px] text-slate-450">
              Next Renewal Date: {new Date(subscription.currentPeriodEnd || Date.now()).toLocaleDateString()}
            </p>
          </div>

          <div className="text-right">
            <span className="text-2xl font-bold text-slate-900 font-mono">${subscription.currentPrice}</span>
            <span className="text-xs text-slate-450 block font-semibold">Billed monthly</span>
          </div>
        </div>

        {/* Usage & Limits Progress Gauges */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm space-y-4">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
            <Layers className="w-4 h-4 text-indigo-600" />
            <span>Resource Usage & Entitlement Quotas</span>
          </h3>

          <div className="grid grid-cols-4 gap-6 text-xs">
            {/* Users Gauge */}
            <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
              <div className="flex justify-between font-semibold">
                <span>Active Users</span>
                <span className="font-mono text-slate-900 font-bold">{usage.activeUsers} / {usage.maxUsers}</span>
              </div>
              <div className="h-2 bg-slate-200 rounded-full overflow-hidden">
                <div className={`h-full ${usage.userPct >= 80 ? 'bg-amber-500' : 'bg-indigo-600'}`} style={{ width: `${usage.userPct}%` }}></div>
              </div>
              <span className="text-[10px] text-slate-400 block font-mono">{usage.userPct}% quota used</span>
            </div>

            {/* Active Orders Gauge */}
            <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
              <div className="flex justify-between font-semibold">
                <span>Active Orders</span>
                <span className="font-mono text-slate-900 font-bold">{usage.activeOrders} / {usage.maxActiveOrders}</span>
              </div>
              <div className="h-2 bg-slate-200 rounded-full overflow-hidden">
                <div className="h-full bg-emerald-600" style={{ width: `${usage.orderPct}%` }}></div>
              </div>
              <span className="text-[10px] text-slate-400 block font-mono">{usage.orderPct}% quota used</span>
            </div>

            {/* Storage Gauge */}
            <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
              <div className="flex justify-between font-semibold">
                <span>Cloud Storage</span>
                <span className="font-mono text-slate-900 font-bold">{usage.storageGb} / {usage.maxStorageGb} GB</span>
              </div>
              <div className="h-2 bg-slate-200 rounded-full overflow-hidden">
                <div className="h-full bg-indigo-600" style={{ width: `${usage.storagePct}%` }}></div>
              </div>
              <span className="text-[10px] text-slate-400 block font-mono">{usage.storagePct}% storage used</span>
            </div>

            {/* Active Workflows */}
            <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
              <div className="flex justify-between font-semibold">
                <span>Workflows</span>
                <span className="font-mono text-slate-900 font-bold">{usage.activeWorkflows} / {usage.maxWorkflows}</span>
              </div>
              <div className="h-2 bg-slate-200 rounded-full overflow-hidden">
                <div className="h-full bg-indigo-600" style={{ width: '20%' }}></div>
              </div>
              <span className="text-[10px] text-slate-400 block font-mono">20% quota used</span>
            </div>
          </div>
        </div>

        {/* Payment History & Invoices Grid */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm flex flex-col space-y-4">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
            <FileText className="w-4 h-4 text-indigo-600" />
            <span>Billing Receipts & Invoices</span>
          </h3>

          <Table>
            <TableHead>
              <th className="pb-3">Invoice Ref</th>
              <th className="pb-3">Plan Item</th>
              <th className="pb-3">Total Billed</th>
              <th className="pb-3">Invoice Date</th>
              <th className="pb-3">Status</th>
              <th className="pb-3 text-center">Receipt</th>
            </TableHead>
            <TableBody>
              {invoices.map(inv => (
                <tr key={inv.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                  <td className="py-3 font-mono font-bold text-indigo-600">{inv.invoiceNumber}</td>
                  <td className="py-3 font-semibold text-slate-800">{inv.planName}</td>
                  <td className="py-3 font-mono font-bold text-slate-900">${inv.totalAmount.toFixed(2)}</td>
                  <td className="py-3 text-slate-500">{new Date(inv.invoiceDate).toLocaleDateString()}</td>
                  <td className="py-3"><Badge status="success">{inv.status}</Badge></td>
                  <td className="py-3 text-center">
                    <Button variant="outline" className="!py-1">
                      <Download className="w-3 h-3" />
                      <span>PDF</span>
                    </Button>
                  </td>
                </tr>
              ))}
            </TableBody>
          </Table>
        </div>

      </div>

      {/* 1. Plan Upgrade Modal */}
      <Dialog isOpen={showUpgradeModal} onClose={() => setShowUpgradeModal(false)} title="Select SaaS Subscription Tier">
        <div className="space-y-4 text-xs">
          <p className="text-slate-550">
            Select an enterprise plan tier. Upgrading grants immediate feature access and higher resource limits.
          </p>

          <div className="space-y-3">
            {plans.map(p => (
              <div 
                key={p.planKey} 
                className={`p-4 rounded-xl border cursor-pointer transition flex justify-between items-center ${
                  targetPlanKey === p.planKey ? 'bg-indigo-50 border-indigo-300 ring-2 ring-indigo-500/20' : 'bg-slate-50 border-slate-200 hover:bg-slate-100'
                }`}
                onClick={() => setTargetPlanKey(p.planKey)}
              >
                <div>
                  <span className="font-bold text-slate-900 text-sm block">{p.name}</span>
                  <span className="text-[10px] text-slate-500">Up to {p.maxUsers} users, {p.maxActiveOrders} active orders</span>
                </div>
                <div className="text-right">
                  <span className="text-base font-mono font-bold text-indigo-600">${p.monthlyPrice}</span>
                  <span className="text-[10px] text-slate-400 block">/ month</span>
                </div>
              </div>
            ))}
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowUpgradeModal(false)}>
              Cancel
            </Button>
            <Button type="button" variant="primary" onClick={() => handleUpgrade(targetPlanKey)}>
              Confirm Upgrade
            </Button>
          </div>
        </div>
      </Dialog>

      {/* 2. Cancel Modal */}
      <Dialog isOpen={showCancelModal} onClose={() => setShowCancelModal(false)} title="Cancel Subscription">
        <div className="space-y-4 text-xs">
          <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 rounded-lg flex items-start space-x-2">
            <AlertTriangle className="w-4 h-4 shrink-0 mt-0.5 text-rose-500" />
            <span>Cancelling will stop automatic renewal at the end of the billing period. Historical data remains intact.</span>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Reason for Cancellation</label>
            <Input
              type="text"
              value={cancelReason}
              onChange={(e) => setCancelReason(e.target.value)}
              placeholder="Tell us why..."
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowCancelModal(false)}>
              Keep Subscription
            </Button>
            <Button type="button" variant="danger" onClick={handleCancel}>
              Confirm Cancellation
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
