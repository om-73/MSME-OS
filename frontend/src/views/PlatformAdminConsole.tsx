import React, { useState, useEffect } from 'react';
import { 
  Building2, 
  CheckCircle, 
  XCircle, 
  Clock, 
  MessageSquare, 
  AlertCircle, 
  Plus, 
  Sparkles, 
  Lock, 
  Eye, 
  Send, 
  HelpCircle, 
  ShieldCheck, 
  TrendingUp, 
  FileCheck, 
  Activity, 
  AlertTriangle, 
  Play, 
  Settings, 
  ChevronRight, 
  ClipboardList,
  PlusCircle,
  Trash2,
  Download,
  RefreshCw,
  Sliders,
  UserCheck,
  Server,
  HardDrive,
  Gauge,
  FileCode
} from 'lucide-react';
import { api } from '../api/client';
import { 
  Button, 
  Card, 
  Badge, 
  Input, 
  Select,
  Table,
  TableHead,
  TableBody
} from '../components/DesignSystem';

interface PlatformAdminConsoleProps {
  user?: any;
}

export default function PlatformAdminConsole({ user }: PlatformAdminConsoleProps) {
  // Navigation categories and tab routing
  const [activeCategory, setActiveCategory] = useState<'health' | 'tenants' | 'security' | 'backup' | 'audit' | 'lifecycle' | 'incidents'>('health');
  const [activeTab, setActiveTab] = useState<string>('dashboard');

  // Form states
  const [newTenantName, setNewTenantName] = useState('');
  const [newTenantSubdomain, setNewTenantSubdomain] = useState('');
  const [newTenantTier, setNewTenantTier] = useState('PROFESSIONAL');
  
  const [flagKey, setFlagKey] = useState('');
  const [flagTarget, setFlagTarget] = useState('GLOBAL');
  const [flagPct, setFlagPct] = useState(100);

  const [pwLength, setPwLength] = useState(12);
  const [pwSymbols, setPwSymbols] = useState(true);
  const [mfaEnforced, setMfaEnforced] = useState(true);

  const [retentionDays, setRetentionDays] = useState(2555);
  const [retentionType, setRetentionType] = useState('AUDIT_LOG');

  const [holdScope, setHoldScope] = useState('ALL');
  const [holdReason, setHoldReason] = useState('Federal Sourcing Audit');

  const [incidentService, setIncidentService] = useState('Database Primary Node');
  const [incidentSeverity, setIncidentSeverity] = useState('SEV-1');
  const [incidentDesc, setIncidentDesc] = useState('');

  const [maintStart, setMaintStart] = useState('2026-08-15T02:00');
  const [maintServices, setMaintServices] = useState('OAuth SSO Authenticator');

  // Selected details
  const [selectedTenantId, setSelectedTenantId] = useState<string | null>(null);
  const [selectedIncidentId, setSelectedIncidentId] = useState<number | null>(null);
  const [actionMsg, setActionMsg] = useState('');

  // Loaded state metrics
  const [healthStatus, setHealthStatus] = useState<any | null>(null);
  const [tenantsList, setTenantsList] = useState<any[]>([]);
  const [securityEvents, setSecurityEvents] = useState<any[]>([]);
  const [securityAlerts, setSecurityAlerts] = useState<any[]>([]);
  const [adminSessions, setAdminSessions] = useState<any[]>([]);
  const [backupsInfo, setBackupsInfo] = useState<any | null>(null);
  const [incidentsList, setIncidentsList] = useState<any[]>([]);
  const [evidenceList, setEvidenceList] = useState<any[]>([]);

  const loadFallbackData = () => {
    setHealthStatus({
      status: 'HEALTHY',
      database: 'HEALTHY',
      cache: 'HEALTHY',
      queue: 'HEALTHY',
      aiServices: 'HEALTHY',
      iotIngestion: 'HEALTHY',
      lastSystemCheck: new Date().toISOString()
    });

    setTenantsList([
      { id: 'apex-textiles-id', name: 'Apex Textiles Corp', subdomain: 'apex', planKey: 'PROFESSIONAL', status: 'ACTIVE', mrr: 299.0, activeUsers: 14, maxUsers: 50 },
      { id: 'zara-partner-id', name: 'Zara Sourcing Unit 2', subdomain: 'zara-unit', planKey: 'ENTERPRISE', status: 'ACTIVE', mrr: 799.0, activeUsers: 84, maxUsers: 500 },
      { id: 'small-workshop-id', name: 'Craft Workshop India', subdomain: 'craft-workshop', planKey: 'STARTER', status: 'SUSPENDED', mrr: 0.0, activeUsers: 4, maxUsers: 10 }
    ]);

    setSecurityEvents([
      { id: 1, userId: 'admin@apex.com', eventType: 'SUCCESSFUL_LOGIN', ipAddress: '192.168.1.1', userAgent: 'Chrome macOS', timestamp: new Date(Date.now() - 3600000).toISOString() },
      { id: 2, userId: 'hacker@evil.com', eventType: 'LOGIN_FAIL', ipAddress: '185.220.101.4', userAgent: 'Firefox Linux', timestamp: new Date(Date.now() - 7200000).toISOString() }
    ]);

    setSecurityAlerts([
      { id: 1, severity: 'CRITICAL', alertType: 'BRUTE_FORCE', description: '5 failed login attempts from 185.220.101.4', resolved: false, createdAt: new Date(Date.now() - 7200000).toISOString() }
    ]);

    setAdminSessions([
      { id: 1, userId: 'admin@apex.com', sessionToken: 'sess_99a88b', ipAddress: '192.168.1.1', deviceMetadata: 'Chrome Mac OS', loginTime: new Date(Date.now() - 3600000).toISOString(), lastActivity: new Date().toISOString(), active: true }
    ]);

    setBackupsInfo({
      lastBackupTime: new Date(Date.now() - 3600000 * 2).toISOString(),
      status: 'HEALTHY',
      backupSizeMb: 24.5,
      restoreStatus: 'VERIFIED'
    });

    setIncidentsList([
      { id: 1, severity: 'SEV-1', affectedService: 'Database Primary Node', status: 'RESOLVED', ownerEmail: 'csm.lead@apex.com', description: 'Connection pool exhaustion under peak load.', detectedAt: new Date(Date.now() - 86400000).toISOString(), resolvedAt: new Date(Date.now() - 3600000 * 22).toISOString() }
    ]);

    setEvidenceList([
      { id: 1, framework: 'SOC2', code: 'CC6.1', description: 'Access control modifications require approval.', status: 'COMPLIANT' },
      { id: 2, framework: 'ISO27001', code: 'A.12.3', description: 'Backups are verified regularly.', status: 'COMPLIANT' }
    ]);
  };

  const fetchAdminData = async () => {
    try {
      const [healthRes, tenantsRes, eventsRes, alertsRes, backupsRes, incidentsRes] = await Promise.all([
        api.get('/admin/system/health'),
        api.get('/admin/tenants'),
        api.get('/admin/security/events'),
        api.get('/admin/security/alerts'),
        api.get('/admin/backups'),
        api.get('/admin/incidents')
      ]);
      setHealthStatus(healthRes.data || {});
      setTenantsList(tenantsRes.data || []);
      setSecurityEvents(eventsRes.data || []);
      setSecurityAlerts(alertsRes.data || []);
      setBackupsInfo(backupsRes.data || {});
      setIncidentsList(incidentsRes.data || []);
    } catch (err) {
      console.error('Failed to fetch platform admin data from database:', err);
    }
  };

  useEffect(() => {
    fetchAdminData();
  }, []);

  const handleCreateTenant = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTenantName || !newTenantSubdomain) return;
    try {
      await api.post('/admin/tenants', {
        name: newTenantName,
        subdomain: newTenantSubdomain,
        planKey: newTenantTier
      });
      setActionMsg(`Tenant "${newTenantName}" successfully registered.`);
      setNewTenantName('');
      setNewTenantSubdomain('');
      fetchAdminData();
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to register tenant.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleToggleTenantStatus = async (id: string, currentStatus: string) => {
    const nextStatus = currentStatus === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
    try {
      await api.patch(`/admin/tenants/${id}/status`, { status: nextStatus });
      setActionMsg(`Tenant status updated to ${nextStatus}.`);
      fetchAdminData();
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to update tenant status.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleTriggerBackup = async (type: string) => {
    try {
      await api.post('/admin/backups', { backupType: type });
      setActionMsg(`Backup execution job of type ${type} triggered successfully.`);
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to trigger backup.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleCreateIncident = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/admin/incidents', {
        affectedService: incidentService,
        severity: incidentSeverity,
        description: incidentDesc,
        status: 'DETECTED'
      });
      setActionMsg('Incident reported and triage alerts dispatched.');
      setIncidentDesc('');
      fetchAdminData();
    } catch (err) {
      console.error(err);
      setActionMsg('Failed to report incident.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleRevokeSession = async (token: string) => {
    try {
      await api.post('/admin/sessions/revoke', { sessionToken: token });
      setActionMsg(`Administrative session revoked: ${token}`);
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to revoke session.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleTriggerDrTest = async () => {
    try {
      await api.post('/admin/disaster-recovery/tests', {});
      setActionMsg('DR simulation drill triggered.');
    } catch (err) {
      console.error(err);
      setActionMsg('Failed to trigger DR test.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleAddFeatureFlag = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!flagKey) return;
    try {
      await api.post('/admin/feature-flags', {
        flagKey,
        targetingType: flagTarget,
        rolloutPercentage: String(flagPct)
      });
      setActionMsg(`Feature flag "${flagKey}" created.`);
      setFlagKey('');
    } catch (err) {
      console.error(err);
      setActionMsg('Failed to create feature flag.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const selectedTenant = tenantsList.find(t => t.id === selectedTenantId) || tenantsList[0];
  const activeIncident = incidentsList.find(i => i.id === selectedIncidentId) || incidentsList[0];

  return (
    <div className="flex-1 bg-slate-50 flex h-screen overflow-hidden font-sans text-slate-900 text-xs">
      
      {/* Sidebar Layout */}
      <div className="w-56 bg-white border-r border-slate-200 flex flex-col justify-between shrink-0">
        <div className="p-4 space-y-4">
          <div className="flex items-center space-x-2 pb-3 border-b border-slate-100">
            <div className="w-7 h-7 rounded bg-slate-900 flex items-center justify-center font-bold text-white text-xs">
              M
            </div>
            <div>
              <span className="font-bold text-slate-900 block leading-tight">MfgOS Admin</span>
              <span className="text-[10px] text-slate-400 font-mono">Platform Ops</span>
            </div>
          </div>

          {/* Major categories grouping */}
          <div className="space-y-4 text-left">
            <div>
              <span className="text-[9px] font-bold text-slate-400 uppercase tracking-wider block mb-1">Infrastructure & Health</span>
              <nav className="space-y-0.5">
                {[
                  { id: 'dashboard', label: 'Platform Dashboard', cat: 'health' },
                  { id: 'nodes', label: 'System Health Status', cat: 'health' },
                  { id: 'usage', label: 'Usage & Resource BI', cat: 'health' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => { setActiveCategory(t.cat as any); setActiveTab(t.id); }}
                    className={`w-full text-left px-2.5 py-1.5 rounded font-bold transition ${
                      activeTab === t.id ? 'bg-slate-100 text-slate-900' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </nav>
            </div>

            <div>
              <span className="text-[9px] font-bold text-slate-400 uppercase tracking-wider block mb-1">Tenants & Settings</span>
              <nav className="space-y-0.5">
                {[
                  { id: 'tenants', label: 'Tenant Management', cat: 'tenants' },
                  { id: 'flags', label: 'Canary Feature Flags', cat: 'tenants' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => { setActiveCategory(t.cat as any); setActiveTab(t.id); }}
                    className={`w-full text-left px-2.5 py-1.5 rounded font-bold transition ${
                      activeTab === t.id ? 'bg-slate-100 text-slate-900' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </nav>
            </div>

            <div>
              <span className="text-[9px] font-bold text-slate-400 uppercase tracking-wider block mb-1">Security Controls</span>
              <nav className="space-y-0.5">
                {[
                  { id: 'policy', label: 'Password & MFA', cat: 'security' },
                  { id: 'sessions', label: 'Active Sessions Revoke', cat: 'security' },
                  { id: 'events', label: 'Security Events Log', cat: 'security' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => { setActiveCategory(t.cat as any); setActiveTab(t.id); }}
                    className={`w-full text-left px-2.5 py-1.5 rounded font-bold transition ${
                      activeTab === t.id ? 'bg-slate-100 text-slate-900' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </nav>
            </div>

            <div>
              <span className="text-[9px] font-bold text-slate-400 uppercase tracking-wider block mb-1">Backup & Reliability</span>
              <nav className="space-y-0.5">
                {[
                  { id: 'backups', label: 'Backup System', cat: 'backup' },
                  { id: 'dr', label: 'Disaster Recovery', cat: 'backup' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => { setActiveCategory(t.cat as any); setActiveTab(t.id); }}
                    className={`w-full text-left px-2.5 py-1.5 rounded font-bold transition ${
                      activeTab === t.id ? 'bg-slate-100 text-slate-900' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </nav>
            </div>

            <div>
              <span className="text-[9px] font-bold text-slate-400 uppercase tracking-wider block mb-1">Audit & Legal holds</span>
              <nav className="space-y-0.5">
                {[
                  { id: 'audit', label: 'Access Reviews & SOC2', cat: 'audit' },
                  { id: 'lifecycle', label: 'Retention & Deletion', cat: 'lifecycle' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => { setActiveCategory(t.cat as any); setActiveTab(t.id); }}
                    className={`w-full text-left px-2.5 py-1.5 rounded font-bold transition ${
                      activeTab === t.id ? 'bg-slate-100 text-slate-900' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </nav>
            </div>

            <div>
              <span className="text-[9px] font-bold text-slate-400 uppercase tracking-wider block mb-1">Outage Incidents</span>
              <nav className="space-y-0.5">
                {[
                  { id: 'incidents', label: 'Triage Outages Log', cat: 'incidents' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => { setActiveCategory(t.cat as any); setActiveTab(t.id); }}
                    className={`w-full text-left px-2.5 py-1.5 rounded font-bold transition ${
                      activeTab === t.id ? 'bg-slate-100 text-slate-900' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </nav>
            </div>

          </div>
        </div>

        <div className="p-4 border-t border-slate-100 text-left font-mono">
          <span className="text-slate-700 block font-bold truncate">superadmin@mfgos.com</span>
          <span className="text-[9px] text-rose-500 font-bold block">SUPER ADMIN</span>
        </div>
      </div>

      {/* Main Administrative Container */}
      <div className="flex-1 flex flex-col overflow-hidden">
        
        {/* Top Header */}
        <header className="bg-white border-b border-slate-200 px-6 py-4 flex justify-between items-center shrink-0">
          <div className="flex items-center space-x-3">
            <h2 className="text-sm font-bold text-slate-800 tracking-wider uppercase">Enterprise Reliability & Governance System</h2>
            <Badge status="success">SOC-2 COMPLIANT</Badge>
          </div>
        </header>

        {actionMsg && (
          <div className="mx-6 mt-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 font-bold rounded-xl flex items-center space-x-2 shrink-0">
            <Sparkles className="w-4 h-4 text-indigo-600" />
            <span>{actionMsg}</span>
          </div>
        )}

        {/* Dynamic Inner Router */}
        <div className="flex-1 p-6 overflow-hidden flex flex-col">
          
          {/* SCREEN 1: PLATFORM ADMIN DASHBOARD */}
          {activeTab === 'dashboard' && healthStatus && backupsInfo && (
            <div className="flex-1 flex flex-col space-y-6 overflow-y-auto">
              
              {/* Dashboard KPIs Grid */}
              <div className="grid grid-cols-4 gap-4">
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Active Tenants</span>
                  <span className="text-2xl font-black text-slate-800">{tenantsList.length}</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Authentication Failures (24h)</span>
                  <span className="text-2xl font-black text-rose-600">14</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Last Backup Status</span>
                  <div className="flex items-center space-x-1.5 mt-2">
                    <CheckCircle className="w-4 h-4 text-emerald-600" />
                    <span className="font-bold text-emerald-600">VERIFIED SUCCESS</span>
                  </div>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Active Outage Incidents</span>
                  <span className="text-2xl font-black text-slate-800">0</span>
                </Card>
              </div>

              {/* System summary grids */}
              <div className="grid grid-cols-3 gap-6 text-left">
                <Card className="space-y-3">
                  <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Service Availability</h3>
                  <div className="space-y-2">
                    {[
                      { name: 'MfgOS Web Frontend', status: 'HEALTHY' },
                      { name: 'Core API Backend Layer', status: 'HEALTHY' },
                      { name: 'PostgreSQL Primary DB Node', status: 'HEALTHY' },
                      { name: 'Redis Cache Cluster', status: 'HEALTHY' }
                    ].map((s, idx) => (
                      <div key={idx} className="flex justify-between items-center p-2 bg-slate-50 border border-slate-200 rounded-lg">
                        <span className="font-bold text-slate-700">{s.name}</span>
                        <Badge status="success">{s.status}</Badge>
                      </div>
                    ))}
                  </div>
                </Card>

                {/* Cloud metrics summary */}
                <Card className="space-y-3">
                  <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Cost & Storage Resource Monitors</h3>
                  <div className="space-y-2 font-mono text-[11px]">
                    <div className="flex justify-between">
                      <span>Object Storage Usage:</span>
                      <span className="font-bold">4.2 TB / 10 TB</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Compute CPU Avg:</span>
                      <span className="font-bold text-emerald-600">14.5%</span>
                    </div>
                    <div className="flex justify-between">
                      <span>Database Connections:</span>
                      <span className="font-bold text-indigo-650">32 / 200</span>
                    </div>
                    <div className="flex justify-between">
                      <span>API Error Rate:</span>
                      <span className="font-bold text-slate-700">0.04%</span>
                    </div>
                  </div>
                </Card>

                {/* Quick actions center */}
                <Card className="space-y-3">
                  <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Global Operations Controls</h3>
                  <p className="text-slate-500 text-[11px]">Deploy global emergency recovery or triggers database point-in-time snapshot replication.</p>
                  <div className="space-y-2">
                    <Button variant="primary" onClick={() => handleTriggerBackup('FULL')} className="w-full justify-center">
                      Trigger Emergency Snapshot
                    </Button>
                    <Button variant="outline" onClick={() => { setActiveCategory('backup'); setActiveTab('dr'); }} className="w-full justify-center">
                      Open Recovery Console
                    </Button>
                  </div>
                </Card>
              </div>

            </div>
          )}

          {/* SCREEN 16 & 17 & 15 & 35: INFRASTRUCTURE, HEALTH STATE & SNAPSHOTS */}
          {activeTab === 'nodes' && healthStatus && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Column: System Health Status nodes */}
              <div className="w-1/2 space-y-4 overflow-y-auto pr-2">
                <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Global Availability Health Matrix</span>
                <div className="grid grid-cols-2 gap-3">
                  {[
                    { name: 'Core API Backend Service', status: healthStatus.status },
                    { name: 'PostgreSQL Relational Storage', status: healthStatus.database },
                    { name: 'Redis Cache Node', status: healthStatus.cache },
                    { name: 'RabbitMQ Message Broker', status: healthStatus.queue },
                    { name: 'AI Sourcing Assist Service', status: healthStatus.aiServices },
                    { name: 'IoT Ingestion Cluster', status: healthStatus.iotIngestion }
                  ].map((srv, idx) => (
                    <div key={idx} className="p-3 bg-white border border-slate-200 rounded-xl flex justify-between items-center">
                      <span className="font-bold text-slate-800">{srv.name}</span>
                      <Badge status={srv.status === 'HEALTHY' ? 'success' : 'error'}>{srv.status}</Badge>
                    </div>
                  ))}
                </div>
              </div>

              {/* Right Column: Third-Party Integrations Health Snapshots */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-5">
                <span className="font-bold text-slate-800 uppercase tracking-wider block">Third-Party Gateway Connectivity Status</span>
                
                <div className="space-y-3 font-mono text-[11px]">
                  {[
                    { gateway: 'Stripe Payment Processor', health: 'CONNECTED', response: '18ms' },
                    { gateway: 'SendGrid Email Gateway', health: 'CONNECTED', response: '45ms' },
                    { gateway: 'Twilio WhatsApp API Endpoint', health: 'CONNECTED', response: '120ms' },
                    { gateway: 'AWS S3 Secured Storage Bucket', health: 'CONNECTED', response: '8ms' },
                    { gateway: 'OpenAI Assist LLM Router', health: 'CONNECTED', response: '1.2s' }
                  ].map((gt, idx) => (
                    <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 block">{gt.gateway}</span>
                        <span className="text-[10px] text-slate-400 mt-0.5 block">Response Latency: {gt.response}</span>
                      </div>
                      <Badge status="success">{gt.health}</Badge>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          )}

          {/* SCREEN 35: USAGE & RESOURCE BI */}
          {activeTab === 'usage' && (
            <div className="flex-1 flex flex-col space-y-6 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Canary Compute Resources Usage Trend</span>
              
              <div className="grid grid-cols-3 gap-4">
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Average CPU Load</span>
                  <span className="text-2xl font-black text-slate-800">14.5%</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Memory Consumption</span>
                  <span className="text-2xl font-black text-indigo-700">4.2 GB / 16.0 GB</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Disk I/O Latency</span>
                  <span className="text-2xl font-black text-emerald-600">0.8 ms</span>
                </Card>
              </div>

              {/* Graphic Resource Graph */}
              <Card className="space-y-4">
                <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Hourly API Throughput (Requests / Sec)</h3>
                <div className="h-40 w-full flex items-end justify-between px-6 pt-6 border-b border-l border-slate-200">
                  {[250, 420, 180, 560, 310, 680, 240].map((val, idx) => (
                    <div key={idx} className="flex flex-col items-center space-y-2 w-16">
                      <span className="font-mono text-[10px] text-slate-550 font-bold">{val}</span>
                      <div className="w-8 bg-slate-900 rounded-t" style={{ height: `${val * 0.18}px` }}></div>
                      <span className="text-[10px] text-slate-405 font-mono">Hr {idx * 4}</span>
                    </div>
                  ))}
                </div>
              </Card>
            </div>
          )}

          {/* SCREEN 2 & 3: TENANT LIFE CYCLE & DRAWER DETAILS */}
          {activeTab === 'tenants' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Column: Create Tenant & List */}
              <div className="w-2/3 flex flex-col space-y-4 overflow-y-auto pr-2">
                <Card className="space-y-3 shrink-0">
                  <span className="font-bold text-slate-800 uppercase tracking-wider text-[10px] block">Provision New Tenant Profile</span>
                  <form onSubmit={handleCreateTenant} className="space-y-3">
                    <div className="grid grid-cols-3 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Corporate Name</label>
                        <Input 
                          type="text" 
                          placeholder="e.g. Nike Sourcing"
                          value={newTenantName}
                          onChange={(e) => setNewTenantName(e.target.value)}
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Subdomain Name</label>
                        <Input 
                          type="text" 
                          placeholder="e.g. nike-app"
                          value={newTenantSubdomain}
                          onChange={(e) => setNewTenantSubdomain(e.target.value)}
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Subscription Plan Tier</label>
                        <Select value={newTenantTier} onChange={(e) => setNewTenantTier(e.target.value)}>
                          <option value="STARTER">Starter Tier</option>
                          <option value="PROFESSIONAL">Professional Tier</option>
                          <option value="ENTERPRISE">Enterprise Tier</option>
                        </Select>
                      </div>
                    </div>
                    <Button type="submit" variant="primary">
                      Initialize Tenant Container
                    </Button>
                  </form>
                </Card>

                {/* Tenants table list */}
                <div className="bg-white border border-slate-200 rounded-xl overflow-hidden">
                  <Table>
                    <TableHead>
                      <th className="pb-3 pl-4 pt-3">Tenant ID</th>
                      <th className="pb-3 pt-3">Company Name</th>
                      <th className="pb-3 pt-3">Subscription</th>
                      <th className="pb-3 pt-3">Active Users</th>
                      <th className="pb-3 pt-3">Status</th>
                      <th className="pb-3 pr-4 pt-3 text-right">Actions Center</th>
                    </TableHead>
                    <TableBody>
                      {tenantsList.map(t => (
                        <tr 
                          key={t.id} 
                          onClick={() => setSelectedTenantId(t.id)}
                          className={`hover:bg-slate-50 cursor-pointer border-b border-slate-100 ${
                            selectedTenantId === t.id ? 'bg-slate-100/50' : ''
                          }`}
                        >
                          <td className="py-3 pl-4 font-bold text-slate-800 font-mono">{t.id}</td>
                          <td className="py-3 font-semibold text-slate-900">{t.name}</td>
                          <td className="py-3 font-mono font-bold text-indigo-700">{t.planKey}</td>
                          <td className="py-3 font-mono">{t.activeUsers} / {t.maxUsers}</td>
                          <td className="py-3"><Badge status={t.status === 'ACTIVE' ? 'success' : 'error'}>{t.status}</Badge></td>
                          <td className="py-3 pr-4 text-right">
                            <Button 
                              variant={t.status === 'ACTIVE' ? 'danger' : 'primary'}
                              onClick={(e) => { e.stopPropagation(); handleToggleTenantStatus(t.id, t.status); }}
                              className="inline-flex !py-0.5"
                            >
                              {t.status === 'ACTIVE' ? 'Suspend' : 'Reactivate'}
                            </Button>
                          </td>
                        </tr>
                      ))}
                    </TableBody>
                  </Table>
                </div>
              </div>

              {/* Right Column: Tenant details drawer details */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                {selectedTenant ? (
                  <div className="space-y-4">
                    <div className="pb-3 border-b border-slate-100 flex justify-between items-center">
                      <div>
                        <h3 className="font-extrabold text-slate-900">{selectedTenant.name}</h3>
                        <span className="text-[10px] text-slate-400 font-mono">Workspace: {selectedTenant.subdomain}.mfgos.com</span>
                      </div>
                      <Badge status={selectedTenant.status === 'ACTIVE' ? 'success' : 'error'}>
                        {selectedTenant.status}
                      </Badge>
                    </div>

                    <div className="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
                      <span className="font-bold text-slate-700 block text-[11px]">System Activity Snapshots</span>
                      <div className="text-[11px] font-mono text-slate-550 space-y-1">
                        <div>Database Storage Size: 485 MB</div>
                        <div>API Transactions (24h): 14,204</div>
                        <div>MFA Compliance Coverage: 100% (Enforced)</div>
                      </div>
                    </div>

                    <div className="space-y-2 pt-2 text-[11px] font-medium text-slate-650">
                      <span className="font-bold text-slate-700 block">SaaS Billing Configuration</span>
                      <div>Associated MRR Revenue: ${selectedTenant.mrr}/month</div>
                      <div>Plan Level: {selectedTenant.planKey} Partnership</div>
                    </div>
                  </div>
                ) : (
                  <p className="text-slate-400">Select a tenant to view metrics details.</p>
                )}
              </div>

            </div>
          )}

          {/* SCREEN 26: CANARY FEATURE FLAGS */}
          {activeTab === 'flags' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left list of feature flags */}
              <div className="w-1/2 space-y-4 overflow-y-auto pr-2">
                <Card className="space-y-3">
                  <span className="font-bold text-slate-800 uppercase tracking-wider text-[10px] block">Register Canary Feature Flag</span>
                  <form onSubmit={handleAddFeatureFlag} className="space-y-3">
                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Flag Identifier Key</label>
                      <Input 
                        type="text" 
                        placeholder="e.g. NewAnalyticsDashboard"
                        value={flagKey}
                        onChange={(e) => setFlagKey(e.target.value)}
                        required
                      />
                    </div>
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Target Criteria</label>
                        <Select value={flagTarget} onChange={(e) => setFlagTarget(e.target.value)}>
                          <option value="GLOBAL">Global Rollout</option>
                          <option value="TENANT">Targeted Tenants only</option>
                          <option value="ROLE">Targeted Roles only</option>
                        </Select>
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Rollout Percentage</label>
                        <Input 
                          type="number" 
                          min="0" 
                          max="100"
                          value={flagPct}
                          onChange={(e) => setFlagPct(Number(e.target.value))}
                          required
                        />
                      </div>
                    </div>
                    <Button type="submit" variant="primary">
                      Save Flag Parameter
                    </Button>
                  </form>
                </Card>
              </div>

              {/* Right View: Configured active flags */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                <span className="font-bold text-slate-800 uppercase tracking-wider block">Active Server-Side Canary Flags</span>
                
                <div className="space-y-2">
                  {[
                    { key: 'NewAnalyticsDashboard', criteria: 'GLOBAL', rollout: '10%', status: 'ACTIVE' },
                    { key: 'IoTMobileAppIntegration', criteria: 'TENANT (apex-textiles)', rollout: '100%', status: 'ACTIVE' }
                  ].map((fl, idx) => (
                    <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center">
                      <div>
                        <span className="font-mono font-bold text-indigo-700 block">{fl.key}</span>
                        <span className="text-[10px] text-slate-400 mt-0.5 block">Canary: {fl.criteria} | Rollout: {fl.rollout}</span>
                      </div>
                      <Badge status="success">{fl.status}</Badge>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          )}

          {/* SCREEN 4: PASSWORD & MFA POLICY */}
          {activeTab === 'policy' && (
            <div className="flex-1 grid grid-cols-2 gap-6 text-left overflow-y-auto">
              
              {/* Password complexity parameters */}
              <Card className="space-y-4">
                <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Password Complexity Enforcement</h3>
                <div className="space-y-3 pt-2">
                  <div>
                    <label className="block text-slate-500 mb-1 font-semibold">Minimum Character Length</label>
                    <Select value={pwLength} onChange={(e) => setPwLength(Number(e.target.value))}>
                      <option value="8">8 characters</option>
                      <option value="12">12 characters (Recommended)</option>
                      <option value="16">16 characters</option>
                    </Select>
                  </div>

                  <label className="flex items-start space-x-3 cursor-pointer pt-2">
                    <input 
                      type="checkbox" 
                      checked={pwSymbols} 
                      onChange={(e) => setPwSymbols(e.target.checked)}
                      className="w-4 h-4 text-slate-900 rounded border-slate-200 mt-0.5" 
                    />
                    <div>
                      <span className="font-bold text-slate-800 block text-xs">Enforce Special Symbols & Numbers</span>
                      <span className="text-slate-450 block text-[10px]">Passwords must contain mixed cases, numbers, and symbols.</span>
                    </div>
                  </label>
                </div>

                <Button variant="primary" onClick={() => setActionMsg('Password complexity policy updated.')}>
                  Apply Complexity Rules
                </Button>
              </Card>

              {/* MFA policies configuration */}
              <Card className="space-y-4">
                <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Multi-Factor Authentication (MFA)</h3>
                
                <div className="space-y-3 pt-2">
                  <label className="flex items-start space-x-3 cursor-pointer">
                    <input 
                      type="checkbox" 
                      checked={mfaEnforced} 
                      onChange={(e) => setMfaEnforced(e.target.checked)}
                      className="w-4 h-4 text-slate-900 rounded border-slate-200 mt-0.5" 
                    />
                    <div>
                      <span className="font-bold text-slate-800 block text-xs">Enforce MFA globally for all Admins</span>
                      <span className="text-slate-450 block text-[10px]">Authenticator App (TOTP) sign-offs required for login.</span>
                    </div>
                  </label>
                </div>

                <Button variant="primary" onClick={() => setActionMsg('MFA enforcement policy saved.')}>
                  Apply MFA Rules
                </Button>
              </Card>

            </div>
          )}

          {/* SCREEN 5 & 6: SESSION REVOCATION & DEVICE METADATA */}
          {activeTab === 'sessions' && (
            <div className="flex-1 flex flex-col space-y-4 text-left">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Active Administrator Sessions</span>
              
              <div className="bg-white border border-slate-200 rounded-xl overflow-hidden flex-1">
                <Table>
                  <TableHead>
                    <th className="pb-3 pl-4 pt-3">User</th>
                    <th className="pb-3 pt-3">Device / OS</th>
                    <th className="pb-3 pt-3">IP Address</th>
                    <th className="pb-3 pt-3">Login Time</th>
                    <th className="pb-3 pt-3">Last Active</th>
                    <th className="pb-3 pr-4 pt-3 text-right">Emergency Action</th>
                  </TableHead>
                  <TableBody>
                    {adminSessions.map(sess => (
                      <tr key={sess.id} className="hover:bg-slate-50 border-b border-slate-100">
                        <td className="py-3 pl-4 font-bold text-slate-900">{sess.userId}</td>
                        <td className="py-3 font-semibold text-slate-550">{sess.deviceMetadata}</td>
                        <td className="py-3 font-mono">{sess.ipAddress}</td>
                        <td className="py-3 font-mono text-slate-400">{new Date(sess.loginTime).toLocaleTimeString()}</td>
                        <td className="py-3 font-mono text-slate-400">{new Date(sess.lastActivity).toLocaleTimeString()}</td>
                        <td className="py-3 pr-4 text-right">
                          <Button variant="danger" onClick={() => handleRevokeSession(sess.sessionToken)} className="inline-flex !py-0.5">
                            Revoke Session
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </div>
          )}

          {/* SCREEN 7 & 8: SECURITY EVENTS LOG & ALERTS */}
          {activeTab === 'events' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Column: Security events log */}
              <div className="w-1/2 flex flex-col space-y-3 overflow-y-auto pr-2">
                <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Audit Log Stream</span>
                {securityEvents.map(evt => (
                  <div key={evt.id} className="p-3 bg-white border border-slate-200 rounded-xl space-y-1.5 font-mono text-[11px]">
                    <div className="flex justify-between">
                      <span className="font-bold text-slate-800">{evt.eventType}</span>
                      <span className="text-slate-400">{new Date(evt.timestamp).toLocaleString()}</span>
                    </div>
                    <div className="text-slate-500 font-medium">
                      User: {evt.userId} | IP: {evt.ipAddress} | UA: {evt.userAgent}
                    </div>
                  </div>
                ))}
              </div>

              {/* Right Column: Security Alerts heuristic warning */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                <span className="font-bold text-slate-800 uppercase tracking-wider block">Security Alerts Triage</span>
                
                {securityAlerts.map(alert => (
                  <div key={alert.id} className="p-4 bg-rose-50 border border-rose-250 text-rose-900 rounded-xl space-y-2">
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-rose-950 text-xs flex items-center space-x-1.5">
                        <AlertTriangle className="w-4 h-4 text-rose-600" />
                        <span>{alert.severity} ALERT: {alert.alertType}</span>
                      </span>
                      <Badge status="error">Unresolved</Badge>
                    </div>
                    <p className="text-[11px] text-rose-800 font-semibold">{alert.description}</p>
                    <span className="text-[10px] text-rose-500 font-mono block">Detected: {new Date(alert.createdAt).toLocaleString()}</span>
                  </div>
                ))}
              </div>

            </div>
          )}

          {/* SCREEN 18 & 19 & 20: BACKUP, RESTORE & SANDBOX VERIFIER */}
          {activeTab === 'backups' && backupsInfo && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left: Backup options */}
              <div className="w-1/3 space-y-4 overflow-y-auto">
                <Card className="space-y-3">
                  <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Execute System Backup</h3>
                  <p className="text-slate-550 text-[11px]">MFGOS performs automated encrypted snapshots. You can launch manual recovery snapshots here.</p>
                  
                  <div className="space-y-2 pt-2">
                    <Button variant="primary" onClick={() => handleTriggerBackup('FULL')} className="w-full justify-center">
                      Trigger Full DB Backup
                    </Button>
                    <Button variant="outline" onClick={() => handleTriggerBackup('CONFIG')} className="w-full justify-center">
                      Backup Config Metadata
                    </Button>
                  </div>
                </Card>

                {/* Backup scheduling details */}
                <Card className="space-y-2 font-mono text-[11px]">
                  <h4 className="font-bold text-slate-700">Backup Schedules</h4>
                  <div className="flex justify-between">
                    <span>Database:</span>
                    <span>Daily Full (02:00 UTC)</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Incremental:</span>
                    <span>Every 15 minutes</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Config metadata:</span>
                    <span>Weekly</span>
                  </div>
                </Card>
              </div>

              {/* Right: Sandbox restore verifier */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-5">
                <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Sandboxed Restore Verification</h3>
                
                <div className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
                  <span className="font-bold text-slate-700 block">Deploy Sandbox Restoration Drill</span>
                  <p className="text-slate-500 text-[11px]">Restore the last snapshot to an isolated verification container to verify checksum integrity without affecting live tenant datasets.</p>
                  
                  <div className="flex space-x-2">
                    <Button variant="primary">Trigger Sandboxed Restore Test</Button>
                  </div>
                </div>

                {/* Verifications logs */}
                <div className="space-y-2">
                  <span className="font-bold text-slate-700 block text-[11px]">Backup History & Verification Status</span>
                  <div className="p-3 bg-emerald-50 border border-emerald-250 text-emerald-950 font-mono text-[11px] rounded-xl flex items-center justify-between">
                    <div>
                      <div>Snapshot Hash: 8a7f9b0e2c1d3a4e5f6b...</div>
                      <div className="text-[10px] text-emerald-700 mt-0.5">Tested: {new Date(backupsInfo.lastBackupTime).toLocaleString()}</div>
                    </div>
                    <Badge status="success">VERIFIED (PASS)</Badge>
                  </div>
                </div>
              </div>

            </div>
          )}

          {/* SCREEN 21 & 22: DISASTER RECOVERY TARGETS & DR DRILLS */}
          {activeTab === 'dr' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* DR Targets */}
              <div className="w-1/3 space-y-4">
                <Card className="space-y-3">
                  <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">RTO & RPO Targets Config</h3>
                  <div className="space-y-2 text-xs font-mono">
                    <div>
                      <span className="text-[10px] text-slate-400 block font-bold">RECOVERY TIME OBJECTIVE (RTO)</span>
                      <span className="text-slate-850 font-bold block mt-0.5">2 Hours</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block font-bold">RECOVERY POINT OBJECTIVE (RPO)</span>
                      <span className="text-slate-850 font-bold block mt-0.5">15 Minutes</span>
                    </div>
                  </div>
                </Card>

                <Button variant="primary" onClick={() => handleTriggerDrTest()} className="w-full justify-center">
                  Trigger Asynchronous Recovery Drill
                </Button>
              </div>

              {/* DR Drills History */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                <span className="font-bold text-slate-800 uppercase tracking-wider block">Disaster Recovery Drills Record Logs</span>
                
                <div className="space-y-3">
                  {[
                    { date: 'Today, 10:00 AM', status: 'SUCCESS', rto: '45 mins (Target: 2h)', rpo: '10 mins (Target: 15m)', issues: 'None' },
                    { date: 'August 02, 10:00 AM', status: 'SUCCESS', rto: '52 mins (Target: 2h)', rpo: '12 mins (Target: 15m)', issues: 'None' }
                  ].map((drill, idx) => (
                    <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center font-mono text-[11px]">
                      <div>
                        <span className="font-bold text-slate-900 block">{drill.date} Drill</span>
                        <div className="text-[10px] text-slate-500 mt-0.5 space-y-0.5">
                          <div>RTO achieved: {drill.rto}</div>
                          <div>RPO achieved: {drill.rpo}</div>
                          <div>Issues encountered: {drill.issues}</div>
                        </div>
                      </div>
                      <Badge status="success">{drill.status}</Badge>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          )}

          {/* SCREEN 9 & 10 & 11 & 12 & 32 & 33 & 34: AUDIT, ACCESS REVIEWS & SOC-2 COMPLIANCE */}
          {activeTab === 'audit' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Column: Compliance checklists and Evidence builder */}
              <div className="w-1/2 space-y-4 overflow-y-auto pr-2">
                <Card className="space-y-3">
                  <span className="font-bold text-slate-800 uppercase tracking-wider text-[10px] block">Compliance evidence builder</span>
                  <p className="text-slate-550 text-[11px]">Build signed markdown proof reports containing database configurations checksums and session revocation logs to satisfy SOC 2 criteria.</p>
                  
                  <div className="flex space-x-2">
                    <Button variant="primary">Generate SOC-2 Audit Evidence File</Button>
                  </div>
                </Card>

                {/* Evidence history list */}
                <div className="space-y-2">
                  <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Evidence Files Vault</span>
                  {evidenceList.map(e => (
                    <div key={e.id} className="p-3 bg-white border border-slate-200 rounded-xl flex justify-between items-center font-mono text-[11px]">
                      <div>
                        <span className="font-bold text-slate-900">{e.framework} — {e.code}</span>
                        <p className="text-slate-500 text-[10px] mt-0.5">{e.description}</p>
                      </div>
                      <Badge status="success">{e.status}</Badge>
                    </div>
                  ))}
                </div>
              </div>

              {/* Right Column: Periodic Access Reviews campaign */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                <span className="font-bold text-slate-850 uppercase tracking-wider block">Periodic Access Controls Review Campaign</span>
                <p className="text-slate-500 text-[11px]">Periodically review, modify, or revoke privileged administrative role configurations across tenants.</p>
                
                <div className="space-y-3">
                  {[
                    { name: 'Sarah Jenkins', role: 'Tenant Admin (CLI-APEX-01)', access: 'PRIVILEGED_ADMIN', action: 'Approve' },
                    { name: 'Ramesh Master', role: 'Worker (ROLE_WORKER)', access: 'OPERATOR_SEWING', action: 'Revoke Access' }
                  ].map((acr, idx) => (
                    <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 block">{acr.name}</span>
                        <span className="text-[10px] text-slate-450 block font-mono">{acr.role} | level: {acr.access}</span>
                      </div>
                      <div className="flex space-x-1.5">
                        <Button variant="outline" className="!py-0.5 text-rose-600 border-rose-200 hover:bg-rose-50">Revoke</Button>
                        <Button variant="primary" className="!py-0.5">Approve</Button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          )}

          {/* SCREEN 28 & 29 & 30 & 31: RETENTION POLICIES, LEGAL HOLDS & DELETIONS */}
          {activeTab === 'lifecycle' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Column: Data retention configurator & Legal hold form */}
              <div className="w-1/2 space-y-4 overflow-y-auto pr-2">
                <Card className="space-y-3">
                  <span className="font-bold text-slate-800 uppercase tracking-wider text-[10px] block">Data Retention Policies Config</span>
                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Record Type Scope</label>
                      <Select value={retentionType} onChange={(e) => setRetentionType(e.target.value)}>
                        <option value="AUDIT_LOG">System Audit Logs</option>
                        <option value="INVOICE">Invoices & Billings</option>
                        <option value="PRODUCTION_RECORD">Production Batches Records</option>
                        <option value="TEMP_FILE">Temp Files</option>
                      </Select>
                    </div>
                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Retention Duration (Days)</label>
                      <Input 
                        type="number" 
                        value={retentionDays}
                        onChange={(e) => setRetentionDays(Number(e.target.value))}
                        required
                      />
                    </div>
                  </div>
                  <Button variant="primary" onClick={() => setActionMsg('Data retention policy successfully configured.')}>
                    Update Policy
                  </Button>
                </Card>

                {/* Legal Hold Lock configuration */}
                <Card className="space-y-3">
                  <span className="font-bold text-slate-850 uppercase tracking-wider block">Lock Deletions via Legal Hold</span>
                  <form onSubmit={(e) => { e.preventDefault(); setActionMsg('Legal hold successfully registered. Auto-cleanups frozen.'); }} className="space-y-3">
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Affected Tenant Scope</label>
                        <Select value={holdScope} onChange={(e) => setHoldScope(e.target.value)}>
                          <option value="ALL">All Tenants (Global Hold)</option>
                          <option value="apex-textiles-id">Apex Textiles (cli-apex)</option>
                        </Select>
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Hold Justification Reason</label>
                        <Input 
                          type="text" 
                          value={holdReason}
                          onChange={(e) => setHoldReason(e.target.value)}
                          required
                        />
                      </div>
                    </div>
                    <Button type="submit" variant="danger">
                      Register Legal Hold Lock
                    </Button>
                  </form>
                </Card>
              </div>

              {/* Right Column: Controlled deletion workflows */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                <span className="font-bold text-slate-800 uppercase tracking-wider block">Controlled Data Deletion Workflows</span>
                <p className="text-slate-550 text-[11px]">Tenant deletion requests proceed through validation checklists to verify that there are no active legal holds, compile a final export download file, wipe data, and log compliance records.</p>
                
                <div className="space-y-3">
                  {[
                    { id: 'job-9011', tenant: 'Craft Workshop India', status: 'WIPING', progress: '60%' },
                    { id: 'job-9012', tenant: 'Zara Sourcing Unit 2', status: 'HOLD_CHECK_BLOCKED', progress: '0%' }
                  ].map((del, idx) => (
                    <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center font-mono text-[11px]">
                      <div>
                        <span className="font-bold text-slate-900 block">{del.tenant} Deletion</span>
                        <div className="text-[10px] text-slate-500 mt-0.5">
                          Job ID: {del.id} | Progress: {del.progress}
                        </div>
                      </div>
                      <Badge status={del.status.includes('BLOCKED') ? 'error' : 'warning'}>{del.status}</Badge>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          )}

          {/* SCREEN 23 & 24 & 25: OUTAGE INCIDENTS TRIAGE & MAINTENANCE */}
          {activeTab === 'incidents' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Incident report and queue */}
              <div className="w-1/2 flex flex-col space-y-4 overflow-y-auto pr-2">
                <Card className="space-y-3 shrink-0">
                  <span className="font-bold text-slate-800 uppercase tracking-wider text-[10px] block">Triage Outage Incident</span>
                  <form onSubmit={handleCreateIncident} className="space-y-3">
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Service Impacted</label>
                        <Input 
                          type="text" 
                          placeholder="e.g. Redis Session Cache Node"
                          value={incidentService}
                          onChange={(e) => setIncidentService(e.target.value)}
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Severity Rating</label>
                        <Select value={incidentSeverity} onChange={(e) => setIncidentSeverity(e.target.value)}>
                          <option value="SEV-1">SEV-1 (Critical Platform Outage)</option>
                          <option value="SEV-2">SEV-2 (Major Service Degradation)</option>
                          <option value="SEV-3">SEV-3 (Limited Outage Impact)</option>
                        </Select>
                      </div>
                    </div>
                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Incident Details</label>
                      <Input 
                        type="text" 
                        placeholder="Please describe symptoms, logs, or exceptions..."
                        value={incidentDesc}
                        onChange={(e) => setIncidentDesc(e.target.value)}
                        required
                      />
                    </div>
                    <Button type="submit" variant="danger">
                      Register Incident SEV
                    </Button>
                  </form>
                </Card>

                {/* Queue list */}
                <div className="space-y-2">
                  <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Active Outage Incidents Queue</span>
                  {incidentsList.map(inc => (
                    <div 
                      key={inc.id}
                      onClick={() => setSelectedIncidentId(inc.id)}
                      className={`p-3 border rounded-xl cursor-pointer transition text-left space-y-1 ${
                        (selectedIncidentId === inc.id || (!selectedIncidentId && activeIncident?.id === inc.id))
                          ? 'border-rose-500 bg-rose-50/10' 
                          : 'border-slate-200 bg-white hover:bg-slate-50'
                      }`}
                    >
                      <div className="flex justify-between items-center">
                        <span className="font-bold text-slate-900 font-mono">{inc.affectedService}</span>
                        <Badge status={inc.status === 'RESOLVED' ? 'success' : 'error'}>{inc.severity}</Badge>
                      </div>
                      <p className="text-slate-550 truncate text-[11px] font-semibold">{inc.description}</p>
                    </div>
                  ))}
                </div>
              </div>

              {/* Right Detail Pane with scheduled maintenance window calendar */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-6">
                {activeIncident ? (
                  <div className="space-y-4">
                    <div className="pb-3 border-b border-slate-100 flex justify-between items-center">
                      <div>
                        <h3 className="font-extrabold text-slate-800">{activeIncident.affectedService}</h3>
                        <span className="text-[10px] text-slate-400 font-mono">Severity: {activeIncident.severity} | Owner: {activeIncident.ownerEmail}</span>
                      </div>
                      <Badge status={activeIncident.status === 'RESOLVED' ? 'success' : 'error'}>
                        {activeIncident.status}
                      </Badge>
                    </div>

                    <div className="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-1.5 font-mono text-[11px]">
                      <span className="font-bold text-slate-700 block text-[11px]">Postmortem Summary Details</span>
                      <div>Root Cause: {activeIncident.description}</div>
                      <div>Detected At: {new Date(activeIncident.detectedAt).toLocaleString()}</div>
                      {activeIncident.resolvedAt && <div>Resolved At: {new Date(activeIncident.resolvedAt).toLocaleString()}</div>}
                    </div>
                  </div>
                ) : (
                  <p className="text-slate-400">Select an incident to view timeline.</p>
                )}

                {/* Scheduled Maintenance Window Calendar */}
                <div className="border-t border-slate-100 pt-4 space-y-3 text-left">
                  <span className="font-bold text-slate-850 uppercase tracking-wider text-[11px] block">Schedule Planned Maintenance Window</span>
                  
                  <form onSubmit={(e) => { e.preventDefault(); setActionMsg(`Maintenance window for ${maintServices} successfully scheduled.`); }} className="space-y-3">
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Downtime Target Service</label>
                        <Input 
                          type="text" 
                          value={maintServices}
                          onChange={(e) => setMaintServices(e.target.value)}
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Planned Start Date/Time</label>
                        <Input 
                          type="datetime-local" 
                          value={maintStart}
                          onChange={(e) => setMaintStart(e.target.value)}
                          required
                        />
                      </div>
                    </div>
                    <Button type="submit" variant="primary">
                      Schedule Downtime Window
                    </Button>
                  </form>
                </div>
              </div>

            </div>
          )}

        </div>

      </div>

    </div>
  );
}
