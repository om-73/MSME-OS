import React, { useState, useEffect } from 'react';
import { 
  Building2, 
  CheckCircle2, 
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
  HeartHandshake,
  ShieldCheck,
  TrendingUp,
  FileCheck,
  Activity,
  AlertTriangle,
  Play,
  Settings,
  ChevronRight,
  ClipboardList
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

interface EnterpriseClientPortalConsoleProps {
  user?: any;
}

export default function EnterpriseClientPortalConsole({ user }: EnterpriseClientPortalConsoleProps) {
  // Navigation tabs for 13 internal screens
  const [internalTab, setInternalTab] = useState<'dashboard' | 'accounts' | 'tickets' | 'escalations' | 'approvals' | 'playbooks' | 'analytics' | 'audit'>('dashboard');
  
  // Selection states
  const [selectedClientCode, setSelectedClientCode] = useState<string | null>(null);
  const [selectedTicketId, setSelectedTicketId] = useState<number | null>(null);

  // Form states
  const [csatText, setCsatText] = useState('');
  const [replyMessage, setReplyMessage] = useState('');
  const [isInternalNote, setIsInternalNote] = useState(false);
  const [escalationMail, setEscalationMail] = useState('csm.lead@apex.com');
  const [playbookTriggerType, setPlaybookTriggerType] = useState('ONBOARDING');
  const [actionMsg, setActionMsg] = useState('');

  // API State data
  const [accountsList, setAccountsList] = useState<any[]>([]);
  const [ticketsList, setTicketsList] = useState<any[]>([]);
  const [approvalsList, setApprovalsList] = useState<any[]>([]);
  const [activeTicketMessages, setActiveTicketMessages] = useState<any[]>([]);
  const [auditLogs, setAuditLogs] = useState<any[]>([]);


  const loadFallbackData = () => {
    setAccountsList([
      { id: 1, clientCode: 'CLI-APEX-01', companyName: 'Apex Retail Apparel', tier: 'ENTERPRISE', healthStatus: 'HEALTHY', onTimeDeliveryPct: 95.5, openIssuesCount: 1, slaBreachesCount: 0, accountManagerEmail: 'account.mgr@apex.com', successManagerEmail: 'csm.lead@apex.com', explanation: 'All performance metrics within limits.' },
      { id: 2, clientCode: 'CLI-NORDIC-02', companyName: 'Nordic Wear Co.', tier: 'GROWTH', healthStatus: 'AT_RISK', onTimeDeliveryPct: 88.0, openIssuesCount: 3, slaBreachesCount: 1, accountManagerEmail: 'account.mgr@apex.com', successManagerEmail: 'csm.lead@apex.com', explanation: 'On-time delivery dropped below 90% threshold.' }
    ]);

    setTicketsList([
      { id: 101, ticketNumber: 'TKT-9011', clientCode: 'CLI-APEX-01', orderNumber: 'ORD-2026-88', subject: 'Collar Ribbing Stitch Details Check', priority: 'HIGH', status: 'IN_PROGRESS', responseDueAt: new Date(Date.now() + 3600000).toISOString(), resolutionDueAt: new Date(Date.now() + 86400000).toISOString(), assignedToEmail: 'account.mgr@apex.com', createdAt: new Date(Date.now() - 3600000).toISOString() },
      { id: 102, ticketNumber: 'TKT-9012', clientCode: 'CLI-NORDIC-02', orderNumber: 'ORD-2026-99', subject: 'Packing Box Logo Misalignment', priority: 'CRITICAL', status: 'OPEN', responseDueAt: new Date(Date.now() - 600000).toISOString(), resolutionDueAt: new Date(Date.now() + 12000000).toISOString(), assignedToEmail: 'csm.lead@apex.com', createdAt: new Date(Date.now() - 7200000).toISOString() }
    ]);

    setApprovalsList([
      { id: 1, clientCode: 'CLI-APEX-01', documentId: 10, documentVersion: '2.0', approvalType: 'TECH_PACK', title: 'Approval Request for Men\'s Shirt Tech Pack v2.0', status: 'PENDING', createdAt: new Date(Date.now() - 3600000 * 2).toISOString() }
    ]);

    setActiveTicketMessages([
      { id: 1, senderEmail: 'buyer@brand.com', messageText: 'Can we confirm the collar stitch specs? The sample has a double row seam.', visibilityScope: 'CLIENT_VISIBLE', createdAt: new Date(Date.now() - 3600000).toISOString() },
      { id: 2, senderEmail: 'account.mgr@apex.com', messageText: 'INTERNAL NOTE: Ramesh says we can swap to single seam easily.', visibilityScope: 'INTERNAL_NOTE', createdAt: new Date(Date.now() - 1800000).toISOString() }
    ]);

    setAuditLogs([
      { id: 1, action: 'Ticket Created', details: 'Ticket TKT-9011 initialized for client CLI-APEX-01', timestamp: new Date(Date.now() - 3600000).toISOString() },
      { id: 2, action: 'Approval Granted', details: 'Client approved Embroidery Stamp Seal v1.2', timestamp: new Date(Date.now() - 7200000).toISOString() },
      { id: 3, action: 'Client Data Changed', details: 'Updated communication preferences for CLI-APEX-01', timestamp: new Date(Date.now() - 86400000).toISOString() }
    ]);
  };

  const fetchInternalData = async () => {
    try {
      const [accRes, tktRes, appRes] = await Promise.all([
        api.get('/customer-success/accounts'),
        api.get('/client-portal/tickets'),
        api.get('/client-portal/approvals?clientCode=CLI-APEX-01')
      ]);
      setAccountsList(accRes.data || []);
      setTicketsList(tktRes.data || []);
      setApprovalsList(appRes.data || []);
    } catch (err) {
      console.error('Failed to fetch client portal management data from database:', err);
    }
  };

  useEffect(() => {
    fetchInternalData();
  }, []);

  const handleEvaluateHealth = async (code: string) => {
    try {
      await api.post(`/customer-success/accounts/${code}/health`, {});
      setActionMsg(`Health recalculation triggered for ${code}.`);
      fetchInternalData();
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to recalculate health.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleManualEscalate = async (id: number) => {
    try {
      await api.post(`/customer-success/escalate/${id}`, {
        level: '2',
        email: escalationMail
      });
      setActionMsg(`Ticket ${id} escalated to level 2.`);
      fetchInternalData();
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to escalate ticket.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleTriggerPlaybook = async () => {
    try {
      await api.post('/customer-success/playbooks/configure', {
        clientCode: selectedClientCode || 'CLI-APEX-01',
        triggerType: playbookTriggerType
      });
      setActionMsg(`Playbook ${playbookTriggerType} successfully executed.`);
      fetchInternalData();
    } catch (err) {
      console.error(err);
      setActionMsg(`Failed to execute playbook.`);
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleFetchTicketMessages = async (tktId: number) => {
    setSelectedTicketId(tktId);
    try {
      const res = await api.get(`/client-portal/tickets/${tktId}/messages?isClientUser=false`);
      setActiveTicketMessages(res.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSendTicketReply = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!replyMessage.trim() || !selectedTicketId) return;
    const scope = isInternalNote ? 'INTERNAL_NOTE' : 'CLIENT_VISIBLE';

    try {
      await api.post(`/client-portal/tickets/${selectedTicketId}/messages`, {
        messageText: replyMessage,
        visibilityScope: scope,
        senderEmail: user?.email || 'csm.lead@apex.com'
      });
      setReplyMessage('');
      handleFetchTicketMessages(selectedTicketId);
    } catch (err) {
      console.error(err);
    }
  };

  // KPI Calculations
  const healthyCount = accountsList.filter(a => a.healthStatus === 'HEALTHY').length;
  const atRiskCount = accountsList.filter(a => a.healthStatus === 'AT_RISK').length;
  const criticalCount = accountsList.filter(a => a.healthStatus === 'CRITICAL').length;
  const openIncidents = ticketsList.filter(t => t.status === 'OPEN' || t.status === 'IN_PROGRESS').length;
  const criticalTickets = ticketsList.filter(t => t.priority === 'CRITICAL').length;

  return (
    <div className="flex-1 bg-slate-50 flex h-screen overflow-hidden font-sans text-slate-900 text-xs">
      
      {/* Internal Navigation Menu */}
      <div className="w-56 bg-white border-r border-slate-200 flex flex-col justify-between shrink-0">
        <div className="p-4 space-y-4 text-left">
          <div className="flex items-center space-x-2 pb-3 border-b border-slate-100">
            <div className="w-7 h-7 rounded bg-indigo-650 flex items-center justify-center font-bold text-white text-xs">
              CS
            </div>
            <div>
              <span className="font-bold text-slate-900 block leading-tight">CSM Console</span>
              <span className="text-[10px] text-slate-400 font-mono">Internal Portal</span>
            </div>
          </div>

          <nav className="space-y-1">
            {[
              { id: 'dashboard', label: 'CS Dashboard', icon: HeartHandshake },
              { id: 'accounts', label: 'Client Accounts', icon: Building2 },
              { id: 'tickets', label: 'Support Queue', icon: MessageSquare },
              { id: 'escalations', label: 'Escalation Board', icon: AlertTriangle },
              { id: 'approvals', label: 'Approvals Monitor', icon: FileCheck },
              { id: 'playbooks', label: 'Playbook Automation', icon: Play },
              { id: 'analytics', label: 'BI & Feedback Analysis', icon: TrendingUp },
              { id: 'audit', label: 'System Audit Logs', icon: ClipboardList }
            ].map((scr) => (
              <button
                key={scr.id}
                onClick={() => setInternalTab(scr.id as any)}
                className={`w-full flex items-center space-x-2.5 px-3 py-2 rounded-lg text-left font-bold transition ${
                  internalTab === scr.id 
                    ? 'bg-slate-100 text-indigo-650' 
                    : 'text-slate-500 hover:bg-slate-50 hover:text-slate-800'
                }`}
              >
                <scr.icon className="w-4 h-4 text-slate-500" />
                <span>{scr.label}</span>
              </button>
            ))}
          </nav>
        </div>

        <div className="p-4 border-t border-slate-100 text-left">
          <span className="font-bold text-slate-700 block truncate">{user?.fullName || 'CS Lead Admin'}</span>
          <span className="text-[10px] text-slate-400 block truncate">csm.lead@apex.com</span>
        </div>
      </div>

      {/* Main console content pane */}
      <div className="flex-1 flex flex-col overflow-hidden">
        
        {/* Header bar */}
        <header className="bg-white border-b border-slate-200 px-6 py-4 flex justify-between items-center shrink-0">
          <div className="flex items-center space-x-3">
            <h2 className="text-base font-bold text-slate-800 tracking-tight uppercase">Customer Success Operational Platform</h2>
            <Badge status="success">Operational</Badge>
          </div>
        </header>

        {actionMsg && (
          <div className="mx-6 mt-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 font-bold rounded-xl flex items-center space-x-2 shrink-0">
            <Sparkles className="w-4 h-4 text-indigo-600" />
            <span>{actionMsg}</span>
          </div>
        )}

        <div className="flex-1 p-6 overflow-hidden flex flex-col">
          
          {/* SCREEN 21: CUSTOMER SUCCESS DASHBOARD */}
          {internalTab === 'dashboard' && (
            <div className="flex-1 flex flex-col space-y-6 overflow-y-auto">
              
              {/* Internal KPI Cards */}
              <div className="grid grid-cols-4 gap-4">
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Total Accounts</span>
                  <span className="text-2xl font-black text-slate-800">{accountsList.length}</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Healthy / At Risk / Critical</span>
                  <div className="flex items-baseline space-x-1.5 mt-2">
                    <span className="text-2xl font-black text-emerald-600">{healthyCount}</span>
                    <span className="text-slate-300">/</span>
                    <span className="text-xl font-bold text-amber-500">{atRiskCount}</span>
                    <span className="text-slate-300">/</span>
                    <span className="text-xl font-bold text-rose-500">{criticalCount}</span>
                  </div>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Active support cases</span>
                  <span className="text-2xl font-black text-slate-800">{openIncidents}</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24 text-left">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Critical priority issues</span>
                  <span className="text-2xl font-black text-rose-600">{criticalTickets}</span>
                </Card>
              </div>

              {/* Explainable Account Health Center */}
              <div className="grid grid-cols-2 gap-6 text-left">
                <Card className="space-y-4">
                  <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Customer Health Index & Rules</h3>
                  <p className="text-slate-500 text-[11px]">We calculate tenant health scores dynamically based on: On-Time Delivery % (50% weight), Open Issues Count (30% weight), and SLA Breaches (20% weight).</p>
                  
                  <div className="space-y-2">
                    {accountsList.map(a => (
                      <div key={a.id} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center">
                        <div>
                          <span className="font-bold text-slate-900 block">{a.companyName}</span>
                          <span className="text-[10px] text-slate-400 block mt-0.5">Delivery: {a.onTimeDeliveryPct}% | Open Issues: {a.openIssuesCount}</span>
                        </div>
                        <Badge status={a.healthStatus === 'HEALTHY' ? 'success' : a.healthStatus === 'AT_RISK' ? 'warning' : 'error'}>
                          {a.healthStatus}
                        </Badge>
                      </div>
                    ))}
                  </div>
                </Card>

                {/* Quick playbook automation trigger */}
                <Card className="space-y-4">
                  <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Trigger Playbook Task Flow</h3>
                  <div className="space-y-3">
                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Select Account Target</label>
                      <Select value={selectedClientCode || ''} onChange={(e) => setSelectedClientCode(e.target.value)}>
                        <option value="CLI-APEX-01">Apex Retail Apparel (CLI-APEX-01)</option>
                        <option value="CLI-NORDIC-02">Nordic Wear Co. (CLI-NORDIC-02)</option>
                      </Select>
                    </div>

                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Playbook Template Trigger</label>
                      <Select value={playbookTriggerType} onChange={(e) => setPlaybookTriggerType(e.target.value)}>
                        <option value="ONBOARDING">New Client Onboarding Flow</option>
                        <option value="DELAY_ALERT">Order Delay CS Escalation</option>
                        <option value="QC_ALERT">QC Fail Account Alert</option>
                      </Select>
                    </div>

                    <Button variant="primary" onClick={handleTriggerPlaybook} className="w-full justify-center">
                      Execute CS Playbook (Generates Tasks)
                    </Button>
                  </div>
                </Card>
              </div>

            </div>
          )}

          {/* SCREEN 22 & 23 & 29: CLIENT ACCOUNTS, DETAIL & EXPLAINABLE HEALTH */}
          {internalTab === 'accounts' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Data Table */}
              <div className="w-2/3 bg-white border border-slate-200 rounded-xl overflow-y-auto">
                <Table>
                  <TableHead>
                    <th className="pb-3 pl-4 pt-3">Client Code</th>
                    <th className="pb-3 pt-3">Company Name</th>
                    <th className="pb-3 pt-3">Tier</th>
                    <th className="pb-3 pt-3">On-Time Delivery</th>
                    <th className="pb-3 pt-3">Issues</th>
                    <th className="pb-3 pt-3">Breaches</th>
                    <th className="pb-3 pr-4 pt-3 text-right">Actions</th>
                  </TableHead>
                  <TableBody>
                    {accountsList.map(acc => (
                      <tr 
                        key={acc.id} 
                        onClick={() => setSelectedClientCode(acc.clientCode)}
                        className={`hover:bg-slate-50 cursor-pointer border-b border-slate-100 ${
                          selectedClientCode === acc.clientCode ? 'bg-indigo-50/20' : ''
                        }`}
                      >
                        <td className="py-3 pl-4 font-bold text-indigo-650 font-mono">{acc.clientCode}</td>
                        <td className="py-3 font-semibold text-slate-900">{acc.companyName}</td>
                        <td className="py-3 font-mono">{acc.tier}</td>
                        <td className="py-3 font-mono font-bold text-emerald-650">{acc.onTimeDeliveryPct}%</td>
                        <td className="py-3 font-mono">{acc.openIssuesCount}</td>
                        <td className="py-3 font-mono font-bold text-rose-600">{acc.slaBreachesCount}</td>
                        <td className="py-3 pr-4 text-right">
                          <Button variant="outline" onClick={() => handleEvaluateHealth(acc.clientCode)} className="inline-flex !py-0.5">
                            Recalculate
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </TableBody>
                </Table>
              </div>

              {/* Right Account details slide drawer simulation */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-4">
                {selectedClientCode ? (() => {
                  const client = accountsList.find(a => a.clientCode === selectedClientCode);
                  if (!client) return null;
                  return (
                    <div className="space-y-4">
                      <div className="pb-3 border-b border-slate-100 flex justify-between items-center">
                        <div>
                          <h3 className="font-extrabold text-slate-800">{client.companyName}</h3>
                          <span className="text-[10px] text-slate-400 font-mono">{client.clientCode}</span>
                        </div>
                        <Badge status={client.healthStatus === 'HEALTHY' ? 'success' : 'warning'}>{client.healthStatus}</Badge>
                      </div>

                      {/* Health details and explanation */}
                      <div className="space-y-1.5 p-3 bg-slate-50 border border-slate-200 rounded-xl">
                        <span className="font-bold text-slate-700 block text-[11px]">Explainable Health Snapshot</span>
                        <p className="text-slate-550 font-medium text-[11px] font-mono">"{client.explanation}"</p>
                      </div>

                      <div className="space-y-2">
                        <span className="font-bold text-slate-700 block text-[11px]">Staff Management Assignments</span>
                        <div className="text-[11px] space-y-1 font-mono">
                          <div>Account Owner: {client.accountManagerEmail}</div>
                          <div>Customer Success Lead: {client.successManagerEmail}</div>
                          <div>Subscription Level: {client.tier} tier</div>
                        </div>
                      </div>

                      <div className="pt-2 border-t border-slate-100">
                        <Button variant="primary" onClick={() => handleEvaluateHealth(client.clientCode)} className="w-full justify-center">
                          Evaluate Performance Score
                        </Button>
                      </div>
                    </div>
                  );
                })() : (
                  <p className="text-slate-400">Select a client account row from the table to view details.</p>
                )}
              </div>

            </div>
          )}

          {/* SCREEN 24 & 25 & 26: SUPPORT QUEUE, TICKET DETAIL & SLA DASHBOARD */}
          {internalTab === 'tickets' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left Tickets queue */}
              <div className="w-1/3 space-y-3 overflow-y-auto pr-2">
                <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block mb-2">Support Ticket Queue</span>
                {ticketsList.map(t => (
                  <div
                    key={t.id}
                    onClick={() => handleFetchTicketMessages(t.id)}
                    className={`p-3 border rounded-xl cursor-pointer transition text-left space-y-1.5 ${
                      selectedTicketId === t.id 
                        ? 'border-indigo-500 bg-indigo-50/30' 
                        : 'border-slate-200 bg-white hover:bg-slate-50'
                    }`}
                  >
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-slate-900 font-mono">{t.ticketNumber}</span>
                      <Badge status={t.priority === 'CRITICAL' ? 'error' : 'warning'}>{t.priority}</Badge>
                    </div>
                    <p className="text-slate-650 font-bold truncate text-[11px]">{t.subject}</p>
                    <div className="flex justify-between items-center text-[10px] text-slate-400">
                      <span>Owner: {t.assignedToEmail}</span>
                      <span className="font-mono text-rose-600 font-bold bg-rose-50 px-1.5 py-0.5 rounded">SLA count active</span>
                    </div>
                  </div>
                ))}
              </div>

              {/* Right Detail Pane with Internal privacy note switch */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col justify-between overflow-hidden">
                {selectedTicketId ? (() => {
                  const tkt = ticketsList.find(t => t.id === selectedTicketId);
                  if (!tkt) return null;
                  return (
                    <div className="flex-1 flex flex-col justify-between overflow-hidden">
                      
                      {/* Ticket header details */}
                      <div className="pb-3 border-b border-slate-100 flex justify-between items-start shrink-0">
                        <div>
                          <h4 className="font-extrabold text-slate-800 text-[13px]">{tkt.ticketNumber} — {tkt.subject}</h4>
                          <span className="text-[10px] text-slate-400 font-mono block">Assignee: {tkt.assignedToEmail} | Client: {tkt.clientCode}</span>
                        </div>

                        {/* Manual escalation controls */}
                        <div className="flex items-center space-x-2">
                          <Select 
                            value={escalationMail} 
                            onChange={(e) => setEscalationMail(e.target.value)} 
                            className="!py-1 w-44"
                          >
                            <option value="csm.lead@apex.com">Escalate to CSM</option>
                            <option value="factory.owner@apex.com">Escalate to Factory Owner</option>
                          </Select>
                          <Button variant="danger" onClick={() => handleManualEscalate(tkt.id)} className="!py-1">
                            Escalate
                          </Button>
                        </div>
                      </div>

                      {/* Messages Log showing CLIENT_VISIBLE and INTERNAL_NOTE */}
                      <div className="flex-1 my-4 overflow-y-auto space-y-3 pr-2">
                        {activeTicketMessages.map(msg => {
                          const isInternal = msg.visibilityScope === 'INTERNAL_NOTE';
                          return (
                            <div 
                              key={msg.id} 
                              className={`p-3 border rounded-xl text-left space-y-1 ${
                                isInternal 
                                  ? 'bg-amber-50 border-amber-250 text-amber-900' 
                                  : 'bg-slate-50 border-slate-200'
                              }`}
                            >
                              <div className="flex justify-between font-bold text-[10px] font-mono">
                                <span>{msg.senderEmail}</span>
                                <div className="flex items-center space-x-1.5">
                                  {isInternal && <Badge status="warning">Internal Note (Private)</Badge>}
                                  <span className="text-slate-400 font-normal">{new Date(msg.createdAt).toLocaleTimeString()}</span>
                                </div>
                              </div>
                              <p className="text-[11px] text-slate-800">{msg.messageText}</p>
                            </div>
                          );
                        })}
                      </div>

                      {/* Reply Form */}
                      <form onSubmit={handleSendTicketReply} className="border-t border-slate-100 pt-3 space-y-2 shrink-0">
                        {/* Toggle switch for note privacy */}
                        <label className="flex items-center space-x-2 text-amber-800 font-bold cursor-pointer text-[11px]">
                          <input 
                            type="checkbox" 
                            checked={isInternalNote} 
                            onChange={(e) => setIsInternalNote(e.target.checked)} 
                            className="w-4 h-4 text-amber-600 rounded border-amber-300"
                          />
                          <span>Mark Reply as "Internal Note (Private)" — Client cannot view this entry</span>
                        </label>

                        <div className="flex space-x-2">
                          <Input 
                            type="text" 
                            placeholder="Type message text here..." 
                            value={replyMessage}
                            onChange={(e) => setReplyMessage(e.target.value)}
                            required
                          />
                          <Button type="submit" variant="primary">
                            <Send className="w-4 h-4" />
                          </Button>
                        </div>
                      </form>

                    </div>
                  );
                })() : (
                  <p className="text-slate-400">Select a ticket item from the queue to view messages.</p>
                )}
              </div>

            </div>
          )}

          {/* SCREEN 27: ESCALATION QUEUE */}
          {internalTab === 'escalations' && (
            <div className="flex-1 flex flex-col space-y-4 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Breached or Near-Breach Escalation Monitor</span>
              
              <Card className="space-y-4">
                <div className="p-4 bg-rose-50 border border-rose-250 text-rose-900 rounded-xl flex items-start space-x-3">
                  <AlertCircle className="w-5 h-5 text-rose-600 shrink-0 mt-0.5" />
                  <div>
                    <span className="font-bold text-rose-900 block text-xs">SLA BREACH ALERT: TKT-9012 has breached Response SLA!</span>
                    <p className="text-rose-700 text-[11px] mt-0.5">Critical Ticket initiated on ORD-2026-99 has not received a response within the 1-hour window. Account owner and CSM notified via Module 9 event.</p>
                  </div>
                </div>

                <Table>
                  <TableHead>
                    <th className="pb-3 pl-4 pt-3">Ticket ID</th>
                    <th className="pb-3 pt-3">Priority</th>
                    <th className="pb-3 pt-3">Response Due</th>
                    <th className="pb-3 pt-3">Time Remaining</th>
                    <th className="pb-3 pr-4 pt-3 text-right">Escalation Status</th>
                  </TableHead>
                  <TableBody>
                    {[
                      { id: 102, number: 'TKT-9012', priority: 'CRITICAL', due: new Date(Date.now() - 600000).toLocaleString(), remaining: 'Breached (L2 triggered)', status: 'ESCALATED_L2' },
                      { id: 101, number: 'TKT-9011', priority: 'HIGH', due: new Date(Date.now() + 3600000).toLocaleString(), remaining: '00h 42m remaining', status: 'SLA_SAFE' }
                    ].map(esc => (
                      <tr key={esc.id} className="hover:bg-slate-50 border-b border-slate-100">
                        <td className="py-3 pl-4 font-bold text-rose-600 font-mono">{esc.number}</td>
                        <td className="py-3 font-mono font-bold text-rose-600">{esc.priority}</td>
                        <td className="py-3 font-mono">{esc.due}</td>
                        <td className="py-3 font-mono font-bold text-rose-700">{esc.remaining}</td>
                        <td className="py-3 pr-4 text-right"><Badge status={esc.status.includes('ESCALATED') ? 'error' : 'success'}>{esc.status}</Badge></td>
                      </tr>
                    ))}
                  </TableBody>
                </Table>
              </Card>
            </div>
          )}

          {/* SCREEN 28: APPROVAL MONITOR */}
          {internalTab === 'approvals' && (
            <div className="flex-1 flex flex-col space-y-4 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Client Approvals Monitor Queue</span>
              
              <div className="bg-white border border-slate-200 rounded-xl overflow-hidden">
                <Table>
                  <TableHead>
                    <th className="pb-3 pl-4 pt-3">Client Code</th>
                    <th className="pb-3 pt-3">Approval Title</th>
                    <th className="pb-3 pt-3">Document ID</th>
                    <th className="pb-3 pt-3">File Version</th>
                    <th className="pb-3 pt-3">Status</th>
                    <th className="pb-3 pr-4 pt-3 text-right">Requested At</th>
                  </TableHead>
                  <TableBody>
                    {approvalsList.map(a => (
                      <tr key={a.id} className="hover:bg-slate-50 border-b border-slate-100">
                        <td className="py-3 pl-4 font-bold text-indigo-650 font-mono">{a.clientCode}</td>
                        <td className="py-3 font-semibold text-slate-800">{a.title}</td>
                        <td className="py-3 font-mono">#{a.documentId}</td>
                        <td className="py-3 font-mono font-bold text-indigo-600">v{a.documentVersion}</td>
                        <td className="py-3"><Badge status={a.status === 'PENDING' ? 'warning' : 'success'}>{a.status}</Badge></td>
                        <td className="py-3 pr-4 text-right font-mono text-[10px] text-slate-400">{new Date(a.createdAt).toLocaleString()}</td>
                      </tr>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </div>
          )}

          {/* SCREEN 31: PLAYBOOKS CONSOLE */}
          {internalTab === 'playbooks' && (
            <div className="flex-1 flex flex-col space-y-6 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Customer Success Onboarding & Alerts Playbooks</span>
              
              <div className="grid grid-cols-2 gap-6">
                
                {/* Active Playbooks list */}
                <Card className="space-y-3">
                  <span className="font-bold text-slate-700 block">Configure Automation Playbooks</span>
                  {[
                    { name: 'VIP Onboarding Playbook', trigger: 'ONBOARDING', status: 'ACTIVE', steps: 4 },
                    { name: 'Delivery Delay CSM Notification', trigger: 'DELAY_ALERT', status: 'ACTIVE', steps: 2 },
                    { name: 'QC Rate Defect Alert Escalation', trigger: 'QC_ALERT', status: 'ACTIVE', steps: 3 }
                  ].map((p, idx) => (
                    <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 block">{p.name}</span>
                        <span className="text-[10px] text-slate-400 block font-mono">Trigger Event: {p.trigger} | {p.steps} Tasks</span>
                      </div>
                      <Badge status="success">{p.status}</Badge>
                    </div>
                  ))}
                </Card>

                {/* Simulated Playbook Tasks */}
                <Card className="space-y-3">
                  <span className="font-bold text-slate-700 block">CS Playbook Generated Tasks Checklist</span>
                  <p className="text-slate-500 text-[11px]">Tasks are automatically created for Account Managers when events fire.</p>
                  
                  <div className="space-y-2 text-[11px] font-mono">
                    <label className="flex items-center space-x-2.5 p-2 bg-slate-50 rounded-lg cursor-pointer">
                      <input type="checkbox" checked={true} readOnly className="w-4 h-4 text-indigo-650" />
                      <span className="line-through text-slate-400">Onboarding - Setup B2B Credentials</span>
                    </label>
                    <label className="flex items-center space-x-2.5 p-2 bg-slate-50 rounded-lg cursor-pointer">
                      <input type="checkbox" checked={false} readOnly className="w-4 h-4 text-indigo-650" />
                      <span className="text-slate-700 font-bold">Onboarding - Verify Tech Pack Version Alignment</span>
                    </label>
                    <label className="flex items-center space-x-2.5 p-2 bg-slate-50 rounded-lg cursor-pointer">
                      <input type="checkbox" checked={false} readOnly className="w-4 h-4 text-indigo-650" />
                      <span className="text-slate-700 font-bold">DelayAlert - Inform Client Sarah on updated ETA details</span>
                    </label>
                  </div>
                </Card>

              </div>
            </div>
          )}

          {/* SCREEN 32 & 33: CUSTOMER ANALYTICS & FEEDBACK ANALYTICS */}
          {internalTab === 'analytics' && (
            <div className="flex-1 flex flex-col space-y-6 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Customer Feedback & Sentiment BI Analytics</span>
              
              <div className="grid grid-cols-3 gap-4">
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Average CSAT Score</span>
                  <span className="text-2xl font-black text-slate-800">4.8 / 5.0</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Net Promoter Score (NPS)</span>
                  <span className="text-2xl font-black text-indigo-700">82 (Excellent)</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">SLA Response Compliance %</span>
                  <span className="text-2xl font-black text-emerald-600">98.4%</span>
                </Card>
              </div>

              {/* Analytics Chart SVG representation */}
              <Card className="space-y-4">
                <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Support Incidents Volume Trend (Weekly)</h3>
                
                <div className="h-40 w-full flex items-end justify-between px-6 pt-6 border-b border-l border-slate-200">
                  {[20, 15, 8, 12, 5].map((val, idx) => (
                    <div key={idx} className="flex flex-col items-center space-y-2 w-16">
                      <span className="font-mono text-[10px] text-slate-550 font-bold">{val}</span>
                      <div className="w-8 bg-indigo-500 rounded-t" style={{ height: `${val * 6}px` }}></div>
                      <span className="text-[10px] text-slate-405 font-mono">Week {idx + 1}</span>
                    </div>
                  ))}
                </div>
              </Card>
            </div>
          )}

          {/* SCREEN 30: AUDIT LOGS */}
          {internalTab === 'audit' && (
            <div className="flex-1 flex flex-col space-y-4 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Security & Operations Audit Trail (Module 13 Scoped)</span>
              
              <div className="bg-white border border-slate-200 rounded-xl overflow-hidden">
                <Table>
                  <TableHead>
                    <th className="pb-3 pl-4 pt-3">Action Event</th>
                    <th className="pb-3 pt-3">Operation Details</th>
                    <th className="pb-3 pr-4 pt-3 text-right">Event Timestamp</th>
                  </TableHead>
                  <TableBody>
                    {auditLogs.map(log => (
                      <tr key={log.id} className="hover:bg-slate-50 border-b border-slate-100">
                        <td className="py-3 pl-4 font-bold text-slate-900">{log.action}</td>
                        <td className="py-3 font-semibold text-slate-500">{log.details}</td>
                        <td className="py-3 pr-4 text-right font-mono text-[10px] text-slate-400">{new Date(log.timestamp).toLocaleString()}</td>
                      </tr>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </div>
          )}

        </div>

      </div>

    </div>
  );
}
