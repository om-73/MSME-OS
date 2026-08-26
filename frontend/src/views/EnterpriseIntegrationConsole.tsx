import React, { useState, useEffect } from 'react';
import { 
  Key, 
  Webhook, 
  Puzzle, 
  Workflow, 
  Activity, 
  Plus, 
  ShieldCheck, 
  Copy, 
  CheckCircle2, 
  XCircle, 
  RotateCcw, 
  Sparkles, 
  Lock, 
  Eye, 
  Globe,
  Radio
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

interface EnterpriseIntegrationConsoleProps {
  user?: any;
}

export default function EnterpriseIntegrationConsole({ user }: EnterpriseIntegrationConsoleProps) {
  const [keys, setKeys] = useState<any[]>([]);
  const [webhooks, setWebhooks] = useState<any[]>([]);
  const [webhookLogs, setWebhookLogs] = useState<any[]>([]);
  const [connections, setConnections] = useState<any[]>([]);
  const [automations, setAutomations] = useState<any[]>([]);
  const [health, setHealth] = useState<any | null>(null);

  // Active Subtab
  const [activeTab, setActiveTab] = useState<'marketplace' | 'keys' | 'webhooks' | 'automations' | 'health'>('marketplace');

  // Modals
  const [showKeyModal, setShowKeyModal] = useState(false);
  const [showWebhookModal, setShowWebhookModal] = useState(false);
  const [newSecretKey, setNewSecretKey] = useState<string | null>(null);
  const [statusMsg, setStatusMsg] = useState('');

  // Key Form
  const [keyForm, setKeyForm] = useState({ name: 'ERP Connector', scopes: 'orders:read,production:read,inventory:read' });

  // Webhook Form
  const [webhookForm, setWebhookForm] = useState({ name: 'Partner Webhook', targetUrl: 'https://api.partner.com/events', subscribedEvents: 'order.created,production.stage.completed' });

  const fetchIntegrationData = async () => {
    try {
      const [keysRes, whRes, logRes, connRes, autoRes, hRes] = await Promise.all([
        api.get('/integration/api-keys'),
        api.get('/integration/webhooks'),
        api.get('/integration/webhooks/logs'),
        api.get('/integration/connections'),
        api.get('/integration/automation-rules'),
        api.get('/integration/health')
      ]);
      setKeys(keysRes.data || []);
      setWebhooks(whRes.data || []);
      setWebhookLogs(logRes.data || []);
      setConnections(connRes.data || []);
      setAutomations(autoRes.data || []);
      setHealth(hRes.data || {});
    } catch (err) {
      console.error('Failed to fetch integration data from database:', err);
    }
  };

  useEffect(() => {
    fetchIntegrationData();
  }, []);

  const handleCreateApiKey = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await api.post('/integration/api-keys', keyForm);
      setNewSecretKey(res.data.secretKey);
      setStatusMsg('API Key generated successfully! Save the secret key now.');
      fetchIntegrationData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to generate API Key.');
    }
  };

  const handleRevokeKey = async (keyId: number) => {
    try {
      await api.post(`/integration/api-keys/${keyId}/revoke`, {});
      setStatusMsg('API Key revoked.');
      fetchIntegrationData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to revoke API Key.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleSaveWebhook = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/integration/webhooks', webhookForm);
      setStatusMsg('Webhook endpoint subscription saved.');
      fetchIntegrationData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to save webhook.');
    }
    setShowWebhookModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleToggleConnection = async (providerKey: string, currentStatus: string) => {
    const nextStatus = currentStatus === 'HEALTHY' ? 'DISCONNECTED' : 'HEALTHY';
    try {
      await api.post(`/integration/connections/${providerKey}/toggle`, { status: nextStatus });
      setStatusMsg(`Connector ${providerKey} updated to ${nextStatus}.`);
      fetchIntegrationData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to toggle connector.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleRetryWebhook = async (logId: number) => {
    try {
      await api.post(`/integration/webhooks/logs/${logId}/retry`, {});
      setStatusMsg('Webhook delivery retry executed.');
      fetchIntegrationData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to retry webhook.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Enterprise Integration & API Platform</h2>
            <p className="text-xs text-slate-550 mt-0.5">REST API Key management, HMAC webhooks, marketplace connectors & automation rules</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'marketplace', label: 'Marketplace' },
              { id: 'keys', label: 'API Keys' },
              { id: 'webhooks', label: 'Webhooks' },
              { id: 'automations', label: 'Automations' },
              { id: 'health', label: 'Observability' }
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
      </div>

      {statusMsg ? (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      ) : null}

      {/* Main Workspace */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm overflow-hidden">
        
        {/* 1. INTEGRATION MARKETPLACE */}
        {activeTab === 'marketplace' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Puzzle className="w-4 h-4 text-indigo-600" />
              <span>Enterprise Integration Marketplace</span>
            </h3>

            <div className="grid grid-cols-2 gap-6 overflow-y-auto pr-2">
              {connections.map(conn => {
                const isHealthy = conn.status === 'HEALTHY';
                return (
                  <div key={conn.providerKey} className="p-5 bg-slate-50 border border-slate-200 rounded-xl space-y-3 shadow-sm">
                    <div className="flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 text-sm block">{conn.name}</span>
                        <span className="text-[10px] text-slate-400 font-mono">Category: {conn.providerType}</span>
                      </div>
                      <Badge status={isHealthy ? 'success' : 'default'}>
                        {conn.status}
                      </Badge>
                    </div>

                    <p className="text-slate-600">Sync Frequency: <span className="font-semibold text-slate-800">{conn.syncFrequency}</span></p>
                    
                    <div className="flex justify-between items-center pt-2 border-t border-slate-200">
                      <span className="text-[10px] text-slate-400">
                        {conn.lastSyncAt ? `Last Sync: ${new Date(conn.lastSyncAt).toLocaleTimeString()}` : 'Never synced'}
                      </span>

                      <Button 
                        variant={isHealthy ? 'outline' : 'primary'} 
                        onClick={() => handleToggleConnection(conn.providerKey, conn.status)}
                        className="!py-1"
                      >
                        {isHealthy ? 'Disconnect' : 'Connect Account'}
                      </Button>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* 2. API KEYS */}
        {activeTab === 'keys' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
                <Key className="w-4 h-4 text-indigo-600" />
                <span>REST API Keys & Scoped Authorization</span>
              </h3>

              <Button variant="primary" onClick={() => setShowKeyModal(true)}>
                <Plus className="w-4 h-4" />
                <span>Generate API Key</span>
              </Button>
            </div>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Key Name</th>
                  <th className="pb-3">Key Prefix</th>
                  <th className="pb-3">Granular Scopes</th>
                  <th className="pb-3">Last Used</th>
                  <th className="pb-3">Status</th>
                  <th className="pb-3 text-center">Action</th>
                </TableHead>
                <TableBody>
                  {keys.map(k => {
                    const isRevoked = k.status === 'REVOKED';
                    return (
                      <tr key={k.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                        <td className="py-3 font-semibold text-slate-900">{k.name}</td>
                        <td className="py-3 font-mono font-bold text-indigo-600">{k.keyPrefix}...</td>
                        <td className="py-3 font-mono text-slate-600 truncate max-w-[220px]">{k.scopes}</td>
                        <td className="py-3 text-slate-500 font-mono">
                          {k.lastUsedAt ? new Date(k.lastUsedAt).toLocaleDateString() : 'Never'}
                        </td>
                        <td className="py-3"><Badge status={isRevoked ? 'error' : 'success'}>{k.status}</Badge></td>
                        <td className="py-3 text-center">
                          {!isRevoked && (
                            <Button variant="outline" onClick={() => handleRevokeKey(k.id)} className="!py-1 border-rose-200 text-rose-600 hover:bg-rose-50">
                              Revoke
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

        {/* 3. WEBHOOKS & LOGS */}
        {activeTab === 'webhooks' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
                <Webhook className="w-4 h-4 text-indigo-600" />
                <span>HMAC Webhook Subscriptions & Delivery Audit</span>
              </h3>

              <Button variant="primary" onClick={() => setShowWebhookModal(true)}>
                <Plus className="w-4 h-4" />
                <span>Add Webhook Endpoint</span>
              </Button>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 space-y-6">
              {/* Endpoint Subscriptions List */}
              <div className="space-y-3">
                <h4 className="font-bold text-slate-800 text-[11px] uppercase tracking-wider">Subscribed Endpoint Handlers</h4>
                {webhooks.map(wh => (
                  <div key={wh.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2 text-xs">
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-slate-900">{wh.name}</span>
                      <Badge status="success">Active Listener</Badge>
                    </div>
                    <span className="font-mono text-indigo-600 block">{wh.targetUrl}</span>
                    <span className="text-[10px] text-slate-400 font-mono">Events: {wh.subscribedEvents}</span>
                  </div>
                ))}
              </div>

              {/* Delivery Logs Table */}
              <div className="space-y-3">
                <h4 className="font-bold text-slate-800 text-[11px] uppercase tracking-wider">Recent Dispatch Logs</h4>
                <Table>
                  <TableHead>
                    <th className="pb-3">Idempotency Ref</th>
                    <th className="pb-3">Event Type</th>
                    <th className="pb-3">HTTP Status</th>
                    <th className="pb-3">Delivery Status</th>
                    <th className="pb-3 text-center">Retries</th>
                    <th className="pb-3 text-center">Action</th>
                  </TableHead>
                  <TableBody>
                    {webhookLogs.map(log => {
                      const isFailed = log.status === 'FAILED';
                      return (
                        <tr key={log.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                          <td className="py-3 font-mono font-bold text-slate-600 truncate max-w-[140px]">{log.idempotencyKey}</td>
                          <td className="py-3 font-bold text-indigo-600">{log.eventType}</td>
                          <td className="py-3 font-mono font-bold">{log.responseStatusCode || '—'}</td>
                          <td className="py-3"><Badge status={isFailed ? 'error' : 'success'}>{log.status}</Badge></td>
                          <td className="py-3 text-center font-mono font-bold">{log.attemptCount}</td>
                          <td className="py-3 text-center">
                            {isFailed && (
                              <Button variant="outline" onClick={() => handleRetryWebhook(log.id)} className="!py-1 border-rose-200 text-rose-600 hover:bg-rose-50">
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
          </div>
        )}

        {/* 4. AUTOMATION RULES */}
        {activeTab === 'automations' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Workflow className="w-4 h-4 text-indigo-600" />
              <span>Event-Driven Automation Engine Rules</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {automations.map(rule => (
                <div key={rule.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
                  <div className="flex justify-between items-center">
                    <span className="font-bold text-slate-900 text-sm">{rule.name}</span>
                    <Badge status="success">Active Rule</Badge>
                  </div>
                  
                  <div className="grid grid-cols-3 gap-4 p-3 bg-white border border-slate-200 rounded-lg">
                    <div>
                      <span className="text-[10px] text-slate-400 block font-semibold">WHEN EVENT FIRES</span>
                      <span className="font-mono font-bold text-indigo-600">{rule.triggerEvent}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block font-semibold">CONDITION</span>
                      <span className="font-mono text-slate-700">{rule.conditionExpression}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block font-semibold">EXECUTED ACTIONS</span>
                      <span className="font-semibold text-slate-800">{rule.actions}</span>
                    </div>
                  </div>

                  <span className="text-[10px] text-slate-400 block font-mono text-right">Executions Count: {rule.executionCount} times</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 5. OBSERVABILITY */}
        {activeTab === 'health' && health && (
          <div className="flex-1 flex flex-col space-y-6 overflow-y-auto pr-2">
            <div className="grid grid-cols-4 gap-6">
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Active API Keys</span>
                <span className="text-2xl font-bold text-slate-900 font-mono">{health.totalApiKeys}</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Webhook Success Rate</span>
                <span className="text-2xl font-bold text-emerald-650 font-mono">{health.webhookSuccessRatePct}%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Avg Webhook Latency</span>
                <span className="text-2xl font-bold text-indigo-650 font-mono">{health.avgWebhookLatencyMs} ms</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Connected Systems</span>
                <span className="text-2xl font-bold text-slate-800 font-mono">{health.connectedMarketplaceCount}</span>
              </div>
            </div>
          </div>
        )}

      </div>

      {/* 1. Generate API Key Modal */}
      <Dialog isOpen={showKeyModal} onClose={() => { setShowKeyModal(false); setNewSecretKey(null); }} title="Generate Scoped REST API Key">
        <form onSubmit={handleCreateApiKey} className="space-y-4 text-xs">
          {!newSecretKey ? (
            <>
              <div>
                <label className="block text-slate-500 mb-1 font-semibold">Key Identifier / Client Name</label>
                <Input
                  type="text"
                  value={keyForm.name}
                  onChange={(e) => setKeyForm({ ...keyForm, name: e.target.value })}
                  placeholder="e.g. ERP Integration Key"
                />
              </div>

              <div>
                <label className="block text-slate-500 mb-1 font-semibold">Granular Scopes (Comma-separated)</label>
                <Input
                  type="text"
                  value={keyForm.scopes}
                  onChange={(e) => setKeyForm({ ...keyForm, scopes: e.target.value })}
                />
              </div>

              <div className="flex space-x-3 pt-4 justify-end">
                <Button type="button" onClick={() => setShowKeyModal(false)}>
                  Cancel
                </Button>
                <Button type="submit" variant="primary">
                  Generate Key
                </Button>
              </div>
            </>
          ) : (
            <div className="space-y-4">
              <div className="p-4 bg-emerald-50 border border-emerald-200 rounded-xl space-y-2">
                <span className="font-bold text-emerald-800 text-xs block">Secret Key Generated!</span>
                <p className="text-emerald-700 text-[11px]">Save this secret key now. It will NEVER be displayed again.</p>
                <code className="bg-white p-3 border border-emerald-300 rounded-lg text-xs font-mono font-bold block select-all text-slate-900">
                  {newSecretKey}
                </code>
              </div>

              <div className="flex justify-end">
                <Button type="button" variant="primary" onClick={() => { setShowKeyModal(false); setNewSecretKey(null); }}>
                  Done & Secured
                </Button>
              </div>
            </div>
          )}
        </form>
      </Dialog>

      {/* 2. Webhook Endpoint Modal */}
      <Dialog isOpen={showWebhookModal} onClose={() => setShowWebhookModal(false)} title="Subscribe Webhook Endpoint">
        <form onSubmit={handleSaveWebhook} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Endpoint Name</label>
            <Input
              type="text"
              value={webhookForm.name}
              onChange={(e) => setWebhookForm({ ...webhookForm, name: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Target URL (HTTPS)</label>
            <Input
              type="text"
              value={webhookForm.targetUrl}
              onChange={(e) => setWebhookForm({ ...webhookForm, targetUrl: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Subscribed Events</label>
            <Input
              type="text"
              value={webhookForm.subscribedEvents}
              onChange={(e) => setWebhookForm({ ...webhookForm, subscribedEvents: e.target.value })}
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowWebhookModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Subscribe Endpoint
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
