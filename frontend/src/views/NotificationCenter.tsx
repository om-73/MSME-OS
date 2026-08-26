import React, { useState, useEffect } from 'react';
import { 
  Bell, 
  Send, 
  Sliders, 
  FileCode, 
  RotateCcw, 
  Server, 
  BarChart2, 
  CheckCircle, 
  AlertTriangle, 
  Sparkles, 
  ShieldCheck,
  Smartphone,
  Mail,
  MessageSquare,
  Globe,
  Clock,
  Eye,
  Check
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

interface NotificationCenterProps {
  user: any;
}

export default function NotificationCenter({ user }: NotificationCenterProps) {
  const [inbox, setInbox] = useState<any[]>([]);
  const [logs, setLogs] = useState<any[]>([]);
  const [templates, setTemplates] = useState<any[]>([]);
  const [preferences, setPreferences] = useState<any | null>(null);
  const [analytics, setAnalytics] = useState<any | null>(null);

  // Active Subtab
  const [activeTab, setActiveTab] = useState<'inbox' | 'logs' | 'templates' | 'preferences' | 'providers' | 'analytics'>('inbox');

  // Filters
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [statusMsg, setStatusMsg] = useState('');

  // Modals
  const [showPublishModal, setShowPublishModal] = useState(false);
  const [showTemplateModal, setShowTemplateModal] = useState(false);

  // Test Event Publisher Form
  const [testEvent, setTestEvent] = useState({
    eventType: 'QC_FAILED',
    orderNumber: 'ORD-2026-90',
    stageName: 'Stitching',
    priority: 'HIGH',
    simulateFailure: false
  });

  // Template Form
  const [selectedTemplate, setSelectedTemplate] = useState({
    eventType: 'STAGE_COMPLETED',
    channel: 'IN_APP',
    subjectTemplate: 'Production Milestone Update',
    bodyTemplate: 'Batch {{orderNumber}} has completed {{stageName}} stage.'
  });

  const fetchData = async () => {
    try {
      const [inboxRes, logsRes, tmplRes, prefRes, anaRes] = await Promise.all([
        api.get(`/notification-center/inbox?recipientId=${user.email || 'user@apex.com'}`),
        api.get(`/notification-center/logs`),
        api.get('/notification-center/templates'),
        api.get(`/notification-center/preferences?userId=${user.email || 'user@apex.com'}`),
        api.get('/notification-center/analytics')
      ]);
      setInbox(inboxRes.data || []);
      setLogs(logsRes.data || []);
      setTemplates(tmplRes.data || []);
      setPreferences(prefRes.data || {});
      setAnalytics(anaRes.data || {});
    } catch (err) {
      console.error('Failed to load notification center data:', err);
    }
  };

  useEffect(() => {
    fetchData();
  }, [user]);

  const handleMarkAsRead = async (logId: number) => {
    try {
      await api.post(`/notification-center/${logId}/read`, {});
      setStatusMsg('Notification marked as read.');
      fetchData();
    } catch (err) {
      console.error(err);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleRetryDelivery = async (logId: number) => {
    try {
      await api.post(`/notification-center/logs/${logId}/retry`, {});
      setStatusMsg('Notification retry triggered successfully.');
      fetchData();
    } catch (err) {
      console.error(err);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handlePublishEventSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const payload = {
      eventType: testEvent.eventType,
      idempotencyKey: `EVT-TEST-${Date.now()}`,
      priority: testEvent.priority,
      data: {
        orderNumber: testEvent.orderNumber,
        stageName: testEvent.stageName,
        defectReason: 'Thread Misalignment',
        simulateProviderFailure: testEvent.simulateFailure
      }
    };

    try {
      await api.post('/notification-center/events/publish', payload);
      setStatusMsg('Domain event published into Notification Queue!');
      fetchData();
    } catch (err) {
      console.error(err);
    }

    setShowPublishModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleSavePreferences = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post(`/notification-center/preferences?userId=${user.email || 'user@apex.com'}`, preferences);
      setStatusMsg('Channel preferences & quiet hours updated.');
    } catch (err) {
      console.error(err);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Enterprise Communication Center</h2>
            <p className="text-xs text-slate-550 mt-0.5">Asynchronous event dispatcher, multi-channel routing, retry queues & provider audits</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'inbox', label: 'Inbox' },
              { id: 'logs', label: 'Delivery Logs' },
              { id: 'templates', label: 'Templates' },
              { id: 'preferences', label: 'Preferences' },
              { id: 'providers', label: 'Providers' },
              { id: 'analytics', label: 'Analytics' }
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

        <Button variant="primary" onClick={() => setShowPublishModal(true)}>
          <Send className="w-4 h-4" />
          <span>Simulate Domain Event</span>
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
        
        {/* 1. INBOX WORKSPACE */}
        {activeTab === 'inbox' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Bell className="w-4 h-4 text-indigo-600" />
              <span>Operational Notification Inbox</span>
            </h3>

            <div className="flex-1 overflow-y-auto space-y-3 pr-2">
              {inbox.map(item => {
                const isCritical = item.priority === 'CRITICAL';
                const isHigh = item.priority === 'HIGH';
                const isRead = item.status === 'READ';
                return (
                  <div key={item.id} className={`p-4 rounded-xl border transition flex items-start justify-between ${
                    isRead ? 'bg-slate-50 border-slate-200 opacity-75' : 'bg-white border-slate-300 shadow-sm'
                  }`}>
                    <div className="space-y-1 max-w-2xl">
                      <div className="flex items-center space-x-2">
                        <Badge status={isCritical ? 'error' : isHigh ? 'warning' : 'info'}>
                          {item.priority || 'NORMAL'}
                        </Badge>
                        <span className="font-mono font-bold text-xs text-indigo-600">{item.eventType}</span>
                        <span className="text-[10px] text-slate-400">via {item.channel}</span>
                      </div>
                      <h4 className="text-xs font-bold text-slate-900">{item.subject}</h4>
                      <p className="text-xs text-slate-600 leading-relaxed">{item.body}</p>
                    </div>

                    <div className="flex flex-col items-end space-y-2">
                      <span className="text-[10px] text-slate-400 font-mono">{new Date(item.timestamp).toLocaleTimeString()}</span>
                      {!isRead && (
                        <Button variant="outline" onClick={() => handleMarkAsRead(item.id)} className="!py-1">
                          <Check className="w-3 h-3" />
                          <span>Acknowledge</span>
                        </Button>
                      )}
                    </div>
                  </div>
                );
              })}

              {inbox.length === 0 && (
                <p className="text-center py-12 text-slate-400 text-xs italic">Inbox empty. No active alerts.</p>
              )}
            </div>
          </div>
        )}

        {/* 2. DELIVERY LOGS & RETRY CONSOLE */}
        {activeTab === 'logs' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
                <RotateCcw className="w-4 h-4 text-indigo-600" />
                <span>Multi-Tenant Delivery Audit Trail</span>
              </h3>

              <Select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} className="w-40">
                <option value="ALL">All Statuses</option>
                <option value="SENT">Sent</option>
                <option value="FAILED">Failed</option>
                <option value="READ">Read</option>
              </Select>
            </div>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Idempotency Key</th>
                  <th className="pb-3">Event</th>
                  <th className="pb-3">Recipient</th>
                  <th className="pb-3">Channel / Provider</th>
                  <th className="pb-3">Status</th>
                  <th className="pb-3 text-center">Retries</th>
                  <th className="pb-3 text-center">Actions</th>
                </TableHead>
                <TableBody>
                  {logs
                    .filter(l => statusFilter === 'ALL' || l.status === statusFilter)
                    .map(log => {
                      const isFailed = log.status === 'FAILED' || log.status === 'FAILED_PERMANENTLY';
                      return (
                        <tr key={log.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                          <td className="py-3 font-mono font-bold text-slate-600 truncate max-w-[140px]">{log.idempotencyKey}</td>
                          <td className="py-3 font-bold text-indigo-600">{log.eventType}</td>
                          <td className="py-3 font-medium text-slate-800">
                            {log.recipientId}
                            <span className="text-[10px] text-slate-400 block font-normal">{log.recipientRole}</span>
                          </td>
                          <td className="py-3">
                            <span className="font-semibold block text-slate-700">{log.channel}</span>
                            <span className="text-[10px] text-slate-400 font-mono block">{log.providerName}</span>
                          </td>
                          <td className="py-3">
                            <Badge status={isFailed ? 'error' : log.status === 'READ' ? 'success' : 'info'}>
                              {log.status}
                            </Badge>
                            {log.failureReason ? (
                              <span className="text-[10px] text-rose-500 block truncate max-w-[160px] font-semibold">{log.failureReason}</span>
                            ) : null}
                          </td>
                          <td className="py-3 text-center font-mono font-bold">{log.retryCount} / {log.maxRetries}</td>
                          <td className="py-3 text-center">
                            {isFailed && (
                              <Button variant="outline" onClick={() => handleRetryDelivery(log.id)} className="!py-1 border-rose-200 text-rose-600 hover:bg-rose-50">
                                <RotateCcw className="w-3 h-3" />
                                <span>Retry</span>
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

        {/* 3. TEMPLATES & VARIABLES */}
        {activeTab === 'templates' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <FileCode className="w-4 h-4 text-indigo-600" />
              <span>Multi-Channel Notification Templates</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {templates.map(tmpl => (
                <div key={tmpl.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2 text-xs">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center space-x-2">
                      <span className="font-mono font-bold text-indigo-600">{tmpl.eventType}</span>
                      <Badge status="default">{tmpl.channel}</Badge>
                    </div>
                    <span className="text-[10px] text-slate-400 font-mono">Template ID: #{tmpl.id}</span>
                  </div>
                  <h4 className="font-bold text-slate-800">{tmpl.subjectTemplate}</h4>
                  <p className="text-slate-600 font-mono bg-white p-3 border border-slate-200 rounded-lg">{tmpl.bodyTemplate}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 4. PREFERENCES & QUIET HOURS */}
        {activeTab === 'preferences' && preferences && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Sliders className="w-4 h-4 text-indigo-600" />
              <span>User Notification Preferences & Quiet Hours</span>
            </h3>

            <form onSubmit={handleSavePreferences} className="max-w-xl space-y-6 text-xs text-slate-700 overflow-y-auto pr-2">
              <div className="space-y-3">
                <h4 className="font-bold text-slate-800 uppercase tracking-wider text-[11px]">Enabled Communication Channels</h4>
                
                {[
                  { key: 'inAppEnabled', label: 'In-App Notifications', icon: Bell },
                  { key: 'pushEnabled', label: 'Mobile & Web Push', icon: Smartphone },
                  { key: 'emailEnabled', label: 'Email Notifications', icon: Mail },
                  { key: 'smsEnabled', label: 'SMS Alerts', icon: MessageSquare },
                  { key: 'whatsappEnabled', label: 'WhatsApp Business API', icon: Globe }
                ].map(ch => (
                  <label key={ch.key} className="flex items-center justify-between p-3 bg-slate-50 border border-slate-200 rounded-lg cursor-pointer hover:bg-slate-100">
                    <div className="flex items-center space-x-2">
                      <ch.icon className="w-4 h-4 text-slate-500" />
                      <span className="font-semibold">{ch.label}</span>
                    </div>
                    <input
                      type="checkbox"
                      checked={(preferences as any)[ch.key]}
                      onChange={(e) => setPreferences({ ...preferences, [ch.key]: e.target.checked })}
                      className="w-4 h-4 text-indigo-600 rounded"
                    />
                  </label>
                ))}
              </div>

              <div className="space-y-3 pt-4 border-t border-slate-100">
                <h4 className="font-bold text-slate-800 uppercase tracking-wider text-[11px]">Quiet Hours Settings</h4>
                <label className="flex items-center space-x-3 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={preferences.quietHoursEnabled}
                    onChange={(e) => setPreferences({ ...preferences, quietHoursEnabled: e.target.checked })}
                    className="w-4 h-4 text-indigo-600 rounded"
                  />
                  <span className="font-semibold">Enable Quiet Hours Policy</span>
                </label>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-slate-500 mb-1 font-semibold">Start Time</label>
                    <Input
                      type="text"
                      value={preferences.quietHoursStart}
                      onChange={(e) => setPreferences({ ...preferences, quietHoursStart: e.target.value })}
                    />
                  </div>
                  <div>
                    <label className="block text-slate-500 mb-1 font-semibold">End Time</label>
                    <Input
                      type="text"
                      value={preferences.quietHoursEnd}
                      onChange={(e) => setPreferences({ ...preferences, quietHoursEnd: e.target.value })}
                    />
                  </div>
                </div>

                <label className="flex items-center space-x-3 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={preferences.bypassQuietHoursForCritical}
                    onChange={(e) => setPreferences({ ...preferences, bypassQuietHoursForCritical: e.target.checked })}
                    className="w-4 h-4 text-indigo-600 rounded"
                  />
                  <span className="font-semibold text-indigo-700">Bypass quiet hours for CRITICAL operational emergencies</span>
                </label>
              </div>

              <div className="pt-4 justify-end flex">
                <Button type="submit" variant="primary">
                  Save Notification Preferences
                </Button>
              </div>
            </form>
          </div>
        )}

        {/* 5. PROVIDERS & WEBHOOKS */}
        {activeTab === 'providers' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Server className="w-4 h-4 text-indigo-600" />
              <span>Multi-Channel Provider Credentials & Webhooks</span>
            </h3>

            <div className="grid grid-cols-2 gap-6 overflow-y-auto pr-2">
              <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                <div className="flex justify-between items-center">
                  <span className="font-bold text-slate-800">Meta WhatsApp Business API</span>
                  <Badge status="success">Connected</Badge>
                </div>
                <Input type="text" value="WABA-901124-APP-SECRET-MASKED" disabled />
                <span className="text-[10px] text-slate-400 block font-mono">Webhook Endpoint: https://api.mfgos.io/v1/webhooks/whatsapp</span>
              </div>

              <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                <div className="flex justify-between items-center">
                  <span className="font-bold text-slate-800">Twilio SMS Gateway</span>
                  <Badge status="success">Connected</Badge>
                </div>
                <Input type="text" value="AC-TWILIO-SECRET-KEY-MASKED" disabled />
                <span className="text-[10px] text-slate-400 block font-mono">SMS Phone: +1 (800) 555-0199</span>
              </div>

              <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                <div className="flex justify-between items-center">
                  <span className="font-bold text-slate-800">SendGrid SMTP Mailer</span>
                  <Badge status="success">Connected</Badge>
                </div>
                <Input type="text" value="SG.PRODUCTION.KEY-MASKED" disabled />
                <span className="text-[10px] text-slate-400 block font-mono">Sender Email: notifications@mfgos.io</span>
              </div>

              <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                <div className="flex justify-between items-center">
                  <span className="font-bold text-slate-800">Enterprise Webhook Bus</span>
                  <Badge status="info">Active Listener</Badge>
                </div>
                <Input type="text" value="https://hooks.client-erp.com/events/mfgos" disabled />
                <span className="text-[10px] text-slate-400 block font-mono">Secured via HMAC-SHA256 Signatures</span>
              </div>
            </div>
          </div>
        )}

        {/* 6. ANALYTICS & HEALTH */}
        {activeTab === 'analytics' && analytics && (
          <div className="flex-1 flex flex-col space-y-6 overflow-y-auto pr-2">
            <div className="grid grid-cols-4 gap-6">
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Total Dispatched</span>
                <span className="text-2xl font-bold text-slate-800 font-mono">{analytics.totalNotifications}</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Delivery Success</span>
                <span className="text-2xl font-bold text-emerald-650 font-mono">{analytics.deliverySuccessRate}%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Read Engagement</span>
                <span className="text-2xl font-bold text-indigo-650 font-mono">{analytics.readRate}%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Avg Latency</span>
                <span className="text-2xl font-bold text-slate-800 font-mono">{analytics.avgDeliveryLatencyMs} ms</span>
              </div>
            </div>

            <div className="bg-slate-50 border border-slate-200 rounded-xl p-6 shadow-sm space-y-4 text-xs">
              <h4 className="font-bold text-slate-800 uppercase tracking-wider text-[11px]">Channel Distribution</h4>
              <div className="grid grid-cols-4 gap-4">
                {Object.entries(analytics.channelDistribution || {}).map(([ch, cnt]: any) => (
                  <div key={ch} className="p-3 bg-white border border-slate-200 rounded-lg flex justify-between items-center">
                    <span className="font-bold text-slate-700">{ch}</span>
                    <span className="font-mono text-indigo-600 font-bold">{cnt} sent</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

      </div>

      {/* 1. Simulate Event Dialog */}
      <Dialog isOpen={showPublishModal} onClose={() => setShowPublishModal(false)} title="Publish Domain Event to Queue">
        <form onSubmit={handlePublishEventSubmit} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Business Event Type</label>
            <Select value={testEvent.eventType} onChange={(e) => setTestEvent({ ...testEvent, eventType: e.target.value })}>
              <option value="STAGE_COMPLETED">STAGE_COMPLETED (Production)</option>
              <option value="QC_FAILED">QC_FAILED (Quality Control)</option>
              <option value="REWORK_REQUESTED">REWORK_REQUESTED (Rework Engine)</option>
              <option value="LOW_STOCK">LOW_STOCK (Inventory)</option>
              <option value="SHIPMENT_DISPATCHED">SHIPMENT_DISPATCHED (Dispatch)</option>
            </Select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Order Number</label>
              <Input
                type="text"
                value={testEvent.orderNumber}
                onChange={(e) => setTestEvent({ ...testEvent, orderNumber: e.target.value })}
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Priority Level</label>
              <Select value={testEvent.priority} onChange={(e) => setTestEvent({ ...testEvent, priority: e.target.value })}>
                <option value="LOW">LOW</option>
                <option value="NORMAL">NORMAL</option>
                <option value="HIGH">HIGH</option>
                <option value="CRITICAL">CRITICAL</option>
              </Select>
            </div>
          </div>

          <label className="flex items-center space-x-3 p-3 bg-slate-50 border border-slate-200 rounded-lg cursor-pointer hover:bg-slate-100 font-semibold">
            <input
              type="checkbox"
              checked={testEvent.simulateFailure}
              onChange={(e) => setTestEvent({ ...testEvent, simulateFailure: e.target.checked })}
              className="w-4 h-4 text-indigo-600 rounded"
            />
            <span>Simulate Provider Failure (Triggers Retry & Fallback Queue)</span>
          </label>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowPublishModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Publish Domain Event
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
