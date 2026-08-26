import React, { useState, useEffect } from 'react';
import { 
  Building2, 
  Calendar, 
  Truck, 
  CheckCircle, 
  Camera, 
  HelpCircle, 
  Send,
  Plus,
  FileText,
  User,
  AlertTriangle,
  Clock,
  MessageSquare,
  Lock,
  Eye,
  Activity,
  HeartHandshake,
  TrendingUp,
  FileCheck,
  LogOut,
  Settings,
  AlertCircle
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

interface BrandPortalProps {
  orders: any[];
  user: any;
}

export default function BrandPortal({ orders, user }: BrandPortalProps) {
  // Login Simulation State
  const [isAuthenticated, setIsAuthenticated] = useState(true);
  const [loginEmail, setLoginEmail] = useState(user?.email || 'buyer@brand.com');
  const [loginPassword, setLoginPassword] = useState('password123');

  // Sidebar Sub-screens Selection
  const [activeScreen, setActiveScreen] = useState<'dashboard' | 'orders' | 'approvals' | 'documents' | 'tickets' | 'activity' | 'account'>('dashboard');
  
  // Selected resource references
  const [selectedOrderId, setSelectedOrderId] = useState<string | null>(null);
  const [selectedApprovalId, setSelectedApprovalId] = useState<number | null>(null);
  const [selectedTicketId, setSelectedTicketId] = useState<number | null>(null);
  const [selectedDocId, setSelectedDocId] = useState<string | null>(null);

  // Form states
  const [ticketSubject, setTicketSubject] = useState('');
  const [ticketPriority, setTicketPriority] = useState('MEDIUM');
  const [ticketOrderNum, setTicketOrderNum] = useState('');
  const [ticketCategory, setTicketCategory] = useState('QUALITY');
  const [ticketDesc, setTicketDesc] = useState('');

  const [issueCategory, setIssueCategory] = useState('QUALITY');
  const [issueDesc, setIssueDesc] = useState('');
  const [issueOrderNum, setIssueOrderNum] = useState('');

  const [chatInput, setChatInput] = useState('');
  const [approvalComments, setApprovalComments] = useState('');
  
  // Feedback survey
  const [feedbackRating, setFeedbackRating] = useState(5);
  const [feedbackNps, setFeedbackNps] = useState(9);
  const [feedbackCategory, setFeedbackCategory] = useState('QUALITY');
  const [feedbackComment, setFeedbackComment] = useState('');

  // Preference Preferences
  const [emailPref, setEmailPref] = useState(true);
  const [whatsappPref, setWhatsappPref] = useState(true);
  const [slackPref, setSlackPref] = useState(false);

  // API State
  const [dashboardData, setDashboardData] = useState<any | null>(null);
  const [approvalsList, setApprovalsList] = useState<any[]>([]);
  const [ticketsList, setTicketsList] = useState<any[]>([]);
  const [activeTicketMessages, setActiveTicketMessages] = useState<any[]>([]);
  const [notificationTray, setNotificationTray] = useState<any[]>([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [actionMsg, setActionMsg] = useState('');


  const loadFallbackData = () => {
    // Premium Mock Fallback
    setDashboardData({
      clientCode: 'CLI-APEX-01',
      activeOrdersCount: 2,
      pendingApprovalsCount: 2,
      openTicketsCount: 1,
      orders: [
        { id: '101', orderNumber: 'ORD-2026-88', productName: "Men's Cotton Shirt", quantity: 150, currentStageName: 'Pressing', status: 'IN_PROGRESS', estimatedDeliveryEta: new Date(Date.now() + 86400000 * 3).toISOString(), totalContractValue: 4500.0, paymentStatus: 'PARTIAL' },
        { id: '102', orderNumber: 'ORD-2026-89', productName: 'Women Denim Jacket', quantity: 80, currentStageName: 'Cutting', status: 'IN_PROGRESS', estimatedDeliveryEta: new Date(Date.now() + 86400000 * 10).toISOString(), totalContractValue: 6400.0, paymentStatus: 'PAID' }
      ]
    });

    setApprovalsList([
      { id: 1, clientCode: 'CLI-APEX-01', documentId: 10, documentVersion: '2.0', approvalType: 'TECH_PACK', title: 'Approval Request for Men\'s Shirt Tech Pack v2.0', status: 'PENDING', createdAt: new Date(Date.now() - 3600000 * 2).toISOString() },
      { id: 2, clientCode: 'CLI-APEX-01', documentId: 11, documentVersion: '1.2', approvalType: 'SAMPLE', title: 'Artwork and Embroidery Stamp Seal Approval', status: 'APPROVED', createdAt: new Date(Date.now() - 86400000).toISOString(), approvedByEmail: 'buyer@brand.com', decidedAt: new Date(Date.now() - 12000000).toISOString() }
    ]);

    setTicketsList([
      { id: 101, ticketNumber: 'TKT-9011', clientCode: 'CLI-APEX-01', orderNumber: 'ORD-2026-88', subject: 'Collar Ribbing Stitch Details Check', priority: 'HIGH', status: 'IN_PROGRESS', responseDueAt: new Date(Date.now() + 3600000).toISOString(), resolutionDueAt: new Date(Date.now() + 86400000).toISOString(), createdAt: new Date(Date.now() - 3600000).toISOString() }
    ]);

    setActiveTicketMessages([
      { id: 1, senderEmail: 'buyer@brand.com', messageText: 'Can we confirm the collar stitch specs? The sample has a double row seam.', visibilityScope: 'CLIENT_VISIBLE', createdAt: new Date(Date.now() - 3600000).toISOString() },
      { id: 2, senderEmail: 'csm.lead@apex.com', messageText: 'Hi, I verified with the sewing master Ramesh. We can proceed with a single row stitch if requested.', visibilityScope: 'CLIENT_VISIBLE', createdAt: new Date(Date.now() - 1800000).toISOString() }
    ]);

    setNotificationTray([
      { id: 1, title: 'Approval Required', message: 'Tech Pack v2.0 needs signoff.', read: false, time: '2h ago' },
      { id: 2, title: 'Ticket Updated', message: 'TKT-9011 received a response.', read: true, time: '3h ago' }
    ]);
  };

  const fetchAllData = async () => {
    try {
      const [dashRes, appRes, tktRes] = await Promise.all([
        api.get('/client-portal/dashboard?clientCode=CLI-APEX-01'),
        api.get('/client-portal/approvals?clientCode=CLI-APEX-01'),
        api.get('/client-portal/tickets?clientCode=CLI-APEX-01')
      ]);
      setDashboardData(dashRes.data);
      setApprovalsList(appRes.data);
      setTicketsList(tktRes.data);
    } catch (err) {
      console.error('Failed to fetch brand portal data from database:', err);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      fetchAllData();
    }
  }, [isAuthenticated]);

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (loginEmail && loginPassword) {
      setIsAuthenticated(true);
    }
  };

  const handleSignoffApproval = async (id: number, decision: 'APPROVED' | 'CHANGES_REQUESTED') => {
    try {
      await api.post(`/client-portal/approvals/${id}/decide`, {
        decision,
        comments: approvalComments || `${decision} recorded via B2B Client Portal.`,
        approvedBy: user?.email || 'buyer@brand.com'
      });
      setActionMsg(`Approval status successfully recorded: ${decision}`);
      setApprovalComments('');
      setSelectedApprovalId(null);
      fetchAllData();
    } catch (err: any) {
      console.error(err);
      setActionMsg('Failed to record approval in database.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleCreateTicket = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!ticketSubject || !ticketDesc) return;

    const payload = {
      clientCode: 'CLI-APEX-01',
      orderNumber: ticketOrderNum || 'ORD-2026-88',
      subject: ticketSubject,
      priority: ticketPriority,
      status: 'OPEN'
    };

    try {
      await api.post('/client-portal/tickets', payload);
      setActionMsg('Support case initiated successfully.');
      setTicketSubject('');
      setTicketDesc('');
      fetchAllData();
    } catch (err) {
      console.error(err);
      setActionMsg('Failed to initiate support case.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleFetchTicketMessages = async (tktId: number) => {
    setSelectedTicketId(tktId);
    try {
      const res = await api.get(`/client-portal/tickets/${tktId}/messages?isClientUser=true`);
      setActiveTicketMessages(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSendTicketMessage = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!chatInput.trim() || !selectedTicketId) return;

    try {
      await api.post(`/client-portal/tickets/${selectedTicketId}/messages`, {
        messageText: chatInput,
        visibilityScope: 'CLIENT_VISIBLE',
        senderEmail: user?.email || 'buyer@brand.com'
      });
      setChatInput('');
      handleFetchTicketMessages(selectedTicketId);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSubmitFeedback = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/client-portal/feedback', {
        clientCode: 'CLI-APEX-01',
        category: feedbackCategory,
        rating: feedbackRating,
        comment: feedbackComment,
        npsScore: feedbackNps
      });
      setActionMsg('Feedback recorded. Thank you for your review.');
      setFeedbackComment('');
    } catch (err) {
      console.error(err);
      setActionMsg('Failed to record feedback.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  const handleReportIssue = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post(`/client/order/${issueOrderNum || 'ORD-2026-88'}/issue`, {
        title: `${issueCategory} Issue reported`,
        description: issueDesc,
        severity: 'HIGH'
      });
      setActionMsg('Operational issue reported and ticket created.');
      setIssueDesc('');
    } catch (err) {
      console.error(err);
      setActionMsg('Failed to report issue.');
    }
    setTimeout(() => setActionMsg(''), 4000);
  };

  // Screen 1: Client Login View
  if (!isAuthenticated) {
    return (
      <div className="flex-1 bg-slate-50 flex items-center justify-center p-8 font-sans">
        <div className="w-full max-w-sm bg-white border border-slate-200 rounded-2xl p-8 shadow-sm space-y-6">
          <div className="text-center space-y-2">
            <div className="w-12 h-12 rounded-xl bg-indigo-600 text-white flex items-center justify-center mx-auto text-xl font-black">
              M
            </div>
            <h2 className="text-lg font-extrabold text-slate-900 tracking-tight uppercase">Brand Client Login</h2>
            <p className="text-xs text-slate-500">Access your production progress and approvals pipeline</p>
          </div>

          <form onSubmit={handleLogin} className="space-y-4 text-xs">
            <div>
              <label className="block font-bold text-slate-700 mb-1">Corporate Email</label>
              <Input 
                type="email" 
                value={loginEmail} 
                onChange={(e) => setLoginEmail(e.target.value)} 
                required 
              />
            </div>
            <div>
              <label className="block font-bold text-slate-700 mb-1">Password</label>
              <Input 
                type="password" 
                value={loginPassword} 
                onChange={(e) => setLoginPassword(e.target.value)} 
                required 
              />
            </div>
            <Button type="submit" variant="primary" className="w-full justify-center text-xs">
              Authenticate Portal Session
            </Button>
          </form>
        </div>
      </div>
    );
  }

  // Active Order context lookup
  const activeOrder = dashboardData?.orders.find((o: any) => o.id === selectedOrderId) || dashboardData?.orders[0];

  return (
    <div className="flex-1 bg-slate-50 flex h-screen overflow-hidden font-sans text-slate-900 text-xs">
      
      {/* Client Portal Local Navigation Sidebar */}
      <div className="w-56 bg-white border-r border-slate-200 flex flex-col justify-between shrink-0">
        <div className="p-4 space-y-4">
          <div className="flex items-center space-x-2 pb-3 border-b border-slate-100">
            <div className="w-7 h-7 rounded bg-indigo-600 flex items-center justify-center font-bold text-white text-xs">
              C
            </div>
            <div>
              <span className="font-bold text-slate-900 block leading-tight">Apex Retail</span>
              <span className="text-[10px] text-slate-400 font-mono">CLI-APEX-01</span>
            </div>
          </div>

          <nav className="space-y-1">
            {[
              { id: 'dashboard', label: 'Client Dashboard', icon: Building2 },
              { id: 'orders', label: 'Active Orders', icon: TrendingUp },
              { id: 'approvals', label: 'Pending Approvals', icon: FileCheck },
              { id: 'documents', label: 'Documents Vault', icon: FileText },
              { id: 'tickets', label: 'Issues & Tickets', icon: MessageSquare },
              { id: 'activity', label: 'Activity Timeline', icon: Activity },
              { id: 'account', label: 'Preferences & Team', icon: Settings }
            ].map((scr) => (
              <button
                key={scr.id}
                onClick={() => setActiveScreen(scr.id as any)}
                className={`w-full flex items-center space-x-2.5 px-3 py-2 rounded-lg text-left font-bold transition ${
                  activeScreen === scr.id 
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

        <div className="p-4 border-t border-slate-100 space-y-2">
          <div className="flex items-center space-x-2">
            <User className="w-6 h-6 text-slate-400 rounded-full border border-slate-200" />
            <div className="overflow-hidden">
              <span className="font-bold text-slate-800 block truncate">{user?.fullName || 'Sarah Jenkins'}</span>
              <span className="text-[10px] text-slate-400 block truncate">buyer@brand.com</span>
            </div>
          </div>
          <button
            onClick={() => setIsAuthenticated(false)}
            className="w-full flex items-center space-x-2 px-3 py-1.5 hover:bg-slate-150 rounded text-slate-500 hover:text-rose-600 font-bold transition text-[11px]"
          >
            <LogOut className="w-4 h-4" />
            <span>Sign Out Session</span>
          </button>
        </div>
      </div>

      {/* Main client operations frame */}
      <div className="flex-1 flex flex-col overflow-hidden">
        
        {/* Top Operational Header */}
        <header className="bg-white border-b border-slate-200 px-6 py-4 flex justify-between items-center shrink-0">
          <div className="flex items-center space-x-3">
            <h2 className="text-base font-bold text-slate-800 tracking-tight uppercase">B2B Client Portal Workspace</h2>
            <Badge status="info">SLA Active</Badge>
          </div>

          <div className="flex items-center space-x-4">
            {/* Notification bell */}
            <div className="relative">
              <button 
                onClick={() => setShowNotifications(!showNotifications)}
                className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-400 hover:text-slate-700 transition relative"
              >
                <Clock className="w-4 h-4" />
                <span className="absolute top-1.5 right-1.5 w-1.5 h-1.5 rounded-full bg-rose-500"></span>
              </button>
              
              {showNotifications && (
                <div className="absolute right-0 mt-2 w-64 bg-white border border-slate-200 rounded-xl shadow-xl z-50 p-4 space-y-3">
                  <span className="font-bold text-slate-800 text-xs block border-b border-slate-100 pb-1 uppercase tracking-wider">Client Alerts</span>
                  {notificationTray.map(n => (
                    <div key={n.id} className="text-[11px] space-y-0.5 hover:bg-slate-50 p-1.5 rounded transition">
                      <div className="flex justify-between font-bold text-slate-800">
                        <span>{n.title}</span>
                        <span className="text-slate-400 font-normal">{n.time}</span>
                      </div>
                      <p className="text-slate-500 font-medium">{n.message}</p>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </header>

        {actionMsg && (
          <div className="mx-6 mt-4 p-3 bg-emerald-50 border border-emerald-250 text-emerald-700 font-bold rounded-xl flex items-center space-x-2 shrink-0 animate-fade-in">
            <CheckCircle className="w-4 h-4 text-emerald-600" />
            <span>{actionMsg}</span>
          </div>
        )}

        {/* Dynamic Inner Sub-screen Router */}
        <div className="flex-1 p-6 overflow-hidden flex flex-col">
          
          {/* SCREEN 2: CLIENT DASHBOARD */}
          {activeScreen === 'dashboard' && dashboardData && (
            <div className="flex-1 flex flex-col space-y-6 overflow-y-auto">
              
              {/* Order Summary Cards */}
              <div className="grid grid-cols-4 gap-4">
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Active Production Batches</span>
                  <span className="text-2xl font-black text-slate-800">{dashboardData.activeOrdersCount}</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Pending My Approvals</span>
                  <span className="text-2xl font-black text-slate-800">{dashboardData.pendingApprovalsCount}</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Upcoming Deliveries (7d)</span>
                  <span className="text-2xl font-black text-slate-800">1</span>
                </Card>
                <Card className="p-4 flex flex-col justify-between h-24">
                  <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">Open Support Incidents</span>
                  <span className="text-2xl font-black text-slate-800">{dashboardData.openTicketsCount}</span>
                </Card>
              </div>

              {/* Mini Active Orders Progress */}
              <Card>
                <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider mb-4">Active Production Batches Timeline Overview</h3>
                <div className="space-y-4">
                  {dashboardData.orders.map((o: any) => (
                    <div key={o.id} className="p-3 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 block">{o.orderNumber} — {o.productName}</span>
                        <span className="text-slate-500 font-medium">Quantity: {o.quantity} Pcs | Current Stage: {o.currentStageName}</span>
                      </div>

                      {/* Small visual workflow overview */}
                      <div className="flex space-x-1.5 font-mono text-[9px]">
                        {['CUTTING', 'STITCHING', 'PRESSING', 'QC', 'DISPATCH'].map((st) => {
                          const isDone = st === 'CUTTING' || (st === 'STITCHING' && o.currentStageName !== 'Cutting');
                          const isCurrent = o.currentStageName.toUpperCase().includes(st.substring(0,4));
                          return (
                            <span 
                              key={st} 
                              className={`px-1.5 py-0.5 rounded font-bold border ${
                                isDone 
                                  ? 'bg-emerald-50 border-emerald-200 text-emerald-700' 
                                  : isCurrent 
                                    ? 'bg-indigo-50 border-indigo-300 text-indigo-750' 
                                    : 'bg-slate-100 border-slate-200 text-slate-400'
                              }`}
                            >
                              {st}
                            </span>
                          );
                        })}
                      </div>
                    </div>
                  ))}
                </div>
              </Card>

              {/* Dynamic Actions Center */}
              <div className="grid grid-cols-2 gap-4">
                <Card className="space-y-3">
                  <h4 className="font-bold text-slate-800 uppercase tracking-wider">Quick Actions Portal</h4>
                  <p className="text-slate-500 text-[11px]">Need clarification? Raise an operational issue or send a design layout change instantly.</p>
                  <div className="flex space-x-2">
                    <Button variant="outline" onClick={() => setActiveScreen('tickets')}>Open Support Case</Button>
                    <Button variant="primary" onClick={() => setActiveScreen('approvals')}>Review Design Approval</Button>
                  </div>
                </Card>

                <Card className="space-y-2">
                  <h4 className="font-bold text-slate-800 uppercase tracking-wider">SLA Status Rule</h4>
                  <div className="p-3 bg-indigo-50/50 border border-indigo-100 rounded-xl space-y-1 text-[11px]">
                    <div className="flex justify-between font-bold text-indigo-900">
                      <span>CRITICAL TICKET SLA</span>
                      <span>1h Response | 4h Resolution</span>
                    </div>
                    <p className="text-indigo-650">We enforce strict tenant-level SLA triggers. Tickets automatically escalate up to enterprise admins if deadlines are breached.</p>
                  </div>
                </Card>
              </div>

            </div>
          )}

          {/* SCREEN 3 & 4 & 5: ORDERS & ORDER DETAILS & TIMELINE */}
          {activeScreen === 'orders' && dashboardData && (
            <div className="flex-1 flex space-x-6 overflow-hidden">
              
              {/* Left Column: Orders list */}
              <div className="w-1/3 flex flex-col space-y-3 overflow-y-auto pr-2">
                <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px]">Active Batches</span>
                {dashboardData.orders.map((o: any) => (
                  <div 
                    key={o.id} 
                    onClick={() => setSelectedOrderId(o.id)}
                    className={`p-3 border rounded-xl cursor-pointer transition text-left space-y-1.5 ${
                      (selectedOrderId === o.id || (!selectedOrderId && activeOrder?.id === o.id))
                        ? 'border-indigo-500 bg-indigo-50/30' 
                        : 'border-slate-200 bg-white hover:bg-slate-50'
                    }`}
                  >
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-slate-900">{o.orderNumber}</span>
                      <Badge status="info">{o.status}</Badge>
                    </div>
                    <p className="text-slate-650 font-semibold text-[11px]">{o.productName}</p>
                    <div className="flex justify-between text-[10px] text-slate-400">
                      <span>Quantity: {o.quantity} Pcs</span>
                      <span>ETA: {new Date(o.estimatedDeliveryEta).toLocaleDateString()}</span>
                    </div>
                  </div>
                ))}
              </div>

              {/* Right Column: Order detail & Production stage & QC results */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-6 text-left">
                {activeOrder ? (
                  <div className="space-y-6">
                    <div className="flex justify-between items-start pb-4 border-b border-slate-100">
                      <div>
                        <h3 className="text-base font-extrabold text-slate-800">{activeOrder.orderNumber}</h3>
                        <p className="text-slate-500 text-[11px] mt-0.5">{activeOrder.productName} — Batch Operations</p>
                      </div>
                      <div className="text-right">
                        <span className="text-[10px] text-slate-400 block font-semibold uppercase">Contract Value</span>
                        <span className="text-base font-black text-indigo-700">${activeOrder.totalContractValue}</span>
                      </div>
                    </div>

                    {/* Operational Details */}
                    <div className="grid grid-cols-3 gap-4 bg-slate-50 p-4 border border-slate-200 rounded-xl">
                      <div>
                        <span className="text-[10px] text-slate-400 uppercase font-semibold">Total Quantity</span>
                        <span className="text-xs font-bold text-slate-800 block mt-0.5">{activeOrder.quantity} Units</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-slate-400 uppercase font-semibold">Stage Progress</span>
                        <span className="text-xs font-bold text-slate-800 block mt-0.5">{activeOrder.currentStageName}</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-slate-400 uppercase font-semibold">Target ETA</span>
                        <span className="text-xs font-bold text-slate-800 block mt-0.5">{new Date(activeOrder.estimatedDeliveryEta).toLocaleDateString()}</span>
                      </div>
                    </div>

                    {/* Production Timeline Visualizer */}
                    <div className="space-y-2">
                      <span className="font-bold text-slate-700 block">Production Flow Timeline</span>
                      <div className="flex items-center space-x-2 bg-slate-50/50 p-3 border border-slate-200 rounded-xl font-mono">
                        {['Cutting', 'Stitching', 'Pressing', 'QC Gate', 'Dispatch'].map((st, idx) => {
                          const isDone = st === 'Cutting' || st === 'Stitching';
                          const isCurrent = st.toLowerCase().includes(activeOrder.currentStageName.toLowerCase()) || (activeOrder.currentStageName === 'Pressing' && st === 'Pressing');
                          return (
                            <React.Fragment key={st}>
                              <div className={`px-2 py-1 rounded text-[10px] font-bold border ${
                                isDone 
                                  ? 'bg-emerald-50 border-emerald-200 text-emerald-800' 
                                  : isCurrent 
                                    ? 'bg-indigo-50 border-indigo-300 text-indigo-850 font-extrabold scale-105' 
                                    : 'bg-slate-100 border-slate-200 text-slate-400'
                              }`}>
                                {st}
                              </div>
                              {idx < 4 && <span className="text-slate-300">→</span>}
                            </React.Fragment>
                          );
                        })}
                      </div>
                    </div>

                    {/* QC Client Visibility & Certificate */}
                    <div className="space-y-2">
                      <span className="font-bold text-slate-700 block">Quality Inspection Result</span>
                      <div className="p-4 border border-slate-200 rounded-xl bg-slate-50 flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                          <CheckCircle className="w-5 h-5 text-emerald-600" />
                          <div>
                            <span className="font-bold text-slate-800 block">QC Certificate Issued</span>
                            <p className="text-slate-500 text-[10px]">Batch conforms to standard specifications. Defect tolerance rate within limits (0.4%).</p>
                          </div>
                        </div>
                        <Badge status="success">Conforms (PASS)</Badge>
                      </div>
                    </div>

                    {/* Raise operational issue */}
                    <div className="border-t border-slate-100 pt-4 space-y-3">
                      <span className="font-bold text-slate-700 block flex items-center space-x-1">
                        <AlertTriangle className="w-4 h-4 text-rose-500" />
                        <span>Report Production/Quality Deviation</span>
                      </span>

                      <form onSubmit={handleReportIssue} className="space-y-3">
                        <div className="grid grid-cols-2 gap-4">
                          <div>
                            <label className="block text-slate-500 mb-1 font-semibold">Select Order</label>
                            <Select value={issueOrderNum} onChange={(e) => setIssueOrderNum(e.target.value)}>
                              <option value="ORD-2026-88">ORD-2026-88</option>
                              <option value="ORD-2026-89">ORD-2026-89</option>
                            </Select>
                          </div>
                          <div>
                            <label className="block text-slate-500 mb-1 font-semibold">Issue Category</label>
                            <Select value={issueCategory} onChange={(e) => setIssueCategory(e.target.value)}>
                              <option value="QUALITY">Quality Defect</option>
                              <option value="PACKAGING">Packaging Damage</option>
                              <option value="DELIVERY">Delivery Delay</option>
                              <option value="DOCUMENTATION">Incorrect Paperwork</option>
                            </Select>
                          </div>
                        </div>

                        <div>
                          <label className="block text-slate-500 mb-1 font-semibold">Problem Description</label>
                          <Input 
                            type="text" 
                            placeholder="Please provide details about the deviation..."
                            value={issueDesc}
                            onChange={(e) => setIssueDesc(e.target.value)}
                            required
                          />
                        </div>

                        <Button type="submit" variant="danger">
                          Submit Deviation & File Incident Case
                        </Button>
                      </form>
                    </div>

                  </div>
                ) : (
                  <p className="text-slate-400">Select an order to view production details.</p>
                )}
              </div>

            </div>
          )}

          {/* SCREEN 6 & 7: APPROVALS & APPROVAL DETAIL */}
          {activeScreen === 'approvals' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left list of approvals */}
              <div className="w-1/3 space-y-3 overflow-y-auto">
                <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block mb-2">Design sign-offs</span>
                {approvalsList.map(a => (
                  <div
                    key={a.id}
                    onClick={() => setSelectedApprovalId(a.id)}
                    className={`p-3 border rounded-xl cursor-pointer transition text-left space-y-1 ${
                      selectedApprovalId === a.id 
                        ? 'border-indigo-500 bg-indigo-50/30' 
                        : 'border-slate-200 bg-white hover:bg-slate-50'
                    }`}
                  >
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-slate-900 font-mono text-[11px]">{a.approvalType}</span>
                      <Badge status={a.status === 'PENDING' ? 'warning' : 'success'}>{a.status}</Badge>
                    </div>
                    <p className="text-slate-650 font-medium truncate">{a.title}</p>
                    <span className="text-[10px] text-slate-400 font-mono">Doc version: v{a.documentVersion}</span>
                  </div>
                ))}
              </div>

              {/* Right Detail Panel */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 overflow-y-auto space-y-5">
                {selectedApprovalId ? (() => {
                  const appr = approvalsList.find(a => a.id === selectedApprovalId);
                  if (!appr) return null;
                  const isPending = appr.status === 'PENDING';
                  return (
                    <div className="space-y-4">
                      <div className="flex justify-between items-center pb-3 border-b border-slate-100">
                        <div>
                          <h3 className="font-extrabold text-slate-900 text-sm">{appr.title}</h3>
                          <span className="text-[10px] text-slate-400 font-mono">Bound to file version: v{appr.documentVersion}</span>
                        </div>
                        <Badge status={isPending ? 'warning' : 'success'}>{appr.status}</Badge>
                      </div>

                      {/* Warning on version binding */}
                      <div className="p-3 bg-amber-50 border border-amber-200 text-amber-900 text-[11px] rounded-xl flex items-start space-x-2">
                        <AlertCircle className="w-4 h-4 text-amber-600 shrink-0 mt-0.5" />
                        <p><strong>Version-Aware Security Binding:</strong> This sign-off applies explicitly to version <strong>{appr.documentVersion}</strong>. If the factory uploads a newer version, this specific approval status remains bound to the current file signature and will not auto-approve the new version.</p>
                      </div>

                      {isPending ? (
                        <div className="space-y-4 pt-2">
                          <div>
                            <label className="block text-slate-700 font-bold mb-1">Add Feedback Comments / Notes</label>
                            <Input 
                              type="text" 
                              placeholder="Please add any suggestions or notes before signing off..."
                              value={approvalComments}
                              onChange={(e) => setApprovalComments(e.target.value)}
                            />
                          </div>

                          <div className="flex space-x-2 justify-end">
                            <Button 
                              variant="outline" 
                              onClick={() => handleSignoffApproval(appr.id, 'CHANGES_REQUESTED')}
                              className="border-rose-200 text-rose-600 hover:bg-rose-50 font-bold"
                            >
                              Request Changes / Reject
                            </Button>
                            <Button 
                              variant="primary" 
                              onClick={() => handleSignoffApproval(appr.id, 'APPROVED')}
                            >
                              Sign off & Approve Version {appr.documentVersion}
                            </Button>
                          </div>
                        </div>
                      ) : (
                        <div className="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-1.5">
                          <span className="font-bold text-slate-700 block">Approval Audit Log</span>
                          <div className="text-[11px] text-slate-550 space-y-1 font-mono">
                            <div>Action Owner: {appr.approvedByEmail || 'Sarah Jenkins (Brand Approver)'}</div>
                            <div>Action Timestamp: {appr.decidedAt ? new Date(appr.decidedAt).toLocaleString() : new Date().toLocaleString()}</div>
                            <div>Signoff Decision: <span className="font-bold text-indigo-700">{appr.status}</span></div>
                            {appr.clientComments && <div>Comments: "{appr.clientComments}"</div>}
                          </div>
                        </div>
                      )}
                    </div>
                  );
                })() : (
                  <p className="text-slate-400">Select an approval item from the list to sign off.</p>
                )}
              </div>

            </div>
          )}

          {/* SCREEN 8 & 9: DOCUMENTS & DOCUMENT PREVIEW */}
          {activeScreen === 'documents' && (
            <div className="flex-1 flex flex-col space-y-4 text-left">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Corporate Document Repository (Centralized Module 15)</span>
              
              <div className="flex-1 bg-white border border-slate-200 rounded-xl overflow-hidden flex flex-col">
                <Table>
                  <TableHead>
                    <th className="pb-3 pl-4 pt-3">Document Name</th>
                    <th className="pb-3 pt-3">Category</th>
                    <th className="pb-3 pt-3">Visibility Level</th>
                    <th className="pb-3 pt-3">Signature Status</th>
                    <th className="pb-3 pr-4 pt-3 text-right">Actions Center</th>
                  </TableHead>
                  <TableBody>
                    {[
                      { id: 'd1', name: 'Commercial Invoice - Apex Textiles #10214.pdf', type: 'INVOICE', visibility: 'CLIENT_VISIBLE', checksum: 'SHA-256: 7d8f...4e91', status: 'SIGNED_VERIFIED' },
                      { id: 'd2', name: 'Design Spec sheet - Dri-FIT Jersey Mesh Pattern.pdf', type: 'SPEC_SHEET', visibility: 'CLIENT_VISIBLE', checksum: 'SHA-256: a4b1...e672', status: 'SIGNED_VERIFIED' },
                      { id: 'd3', name: 'Quality Conformity Certificate Batch 88.pdf', type: 'QC_REPORT', visibility: 'CLIENT_VISIBLE', checksum: 'SHA-256: 2c9e...99e1', status: 'SIGNED_VERIFIED' }
                    ].map(doc => (
                      <tr key={doc.id} className="hover:bg-slate-50 border-b border-slate-100">
                        <td className="py-3 pl-4 font-bold text-slate-900">{doc.name}</td>
                        <td className="py-3 font-mono">{doc.type}</td>
                        <td className="py-3"><Badge status="info">{doc.visibility}</Badge></td>
                        <td className="py-3 text-[10px] text-slate-400 font-mono">{doc.checksum}</td>
                        <td className="py-3 pr-4 text-right">
                          <Button variant="outline" onClick={() => setSelectedDocId(doc.id)} className="inline-flex !py-1">
                            <Eye className="w-3.5 h-3.5" />
                            <span>Preview</span>
                          </Button>
                        </td>
                      </tr>
                    ))}
                  </TableBody>
                </Table>
              </div>

              {/* Document Preview Modal */}
              {selectedDocId && (() => {
                const docNames: any = {
                  d1: 'Commercial Invoice - Apex Textiles #10214.pdf',
                  d2: 'Design Spec sheet - Dri-FIT Jersey Mesh Pattern.pdf',
                  d3: 'Quality Conformity Certificate Batch 88.pdf'
                };
                return (
                  <div className="fixed inset-0 bg-slate-950/40 z-50 flex items-center justify-center p-6 backdrop-blur-sm">
                    <div className="w-full max-w-lg bg-white border border-slate-200 rounded-2xl p-6 shadow-2xl space-y-4">
                      <div className="flex justify-between items-center pb-2 border-b border-slate-100">
                        <span className="font-bold text-slate-800 uppercase tracking-wider text-xs">Secure Document Container</span>
                        <button onClick={() => setSelectedDocId(null)} className="text-slate-400 hover:text-slate-600 font-bold">Close</button>
                      </div>
                      <div className="space-y-2">
                        <h4 className="font-extrabold text-slate-900 text-sm">{docNames[selectedDocId]}</h4>
                        <div className="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-2 text-[11px] font-mono">
                          <div>Secure Hash (SHA-256): 9a2b8e4c7d6f5a3b1c9e8d7f6a5b4c3d2e1f</div>
                          <div>Visibility Scope: CLIENT_VISIBLE (Centralized document repository encryption active)</div>
                          <div>Access Link: <span className="text-indigo-650 cursor-pointer underline">Generate Expiring Signed URL</span></div>
                        </div>
                      </div>
                      <div className="flex justify-end space-x-2 pt-2">
                        <Button variant="outline" onClick={() => setSelectedDocId(null)}>Back to Vault</Button>
                        <Button variant="primary">Download Verified File Copy</Button>
                      </div>
                    </div>
                  </div>
                );
              })()}
            </div>
          )}

          {/* SCREEN 11 & 12 & 13 & 14 & 19: INCIDENTS, SUPPORT TICKETS, CONVERSATIONS & FEEDBACK */}
          {activeScreen === 'tickets' && (
            <div className="flex-1 flex space-x-6 overflow-hidden text-left">
              
              {/* Left: Ticketing queue and form */}
              <div className="w-1/2 flex flex-col space-y-4 overflow-y-auto pr-2">
                <Card className="space-y-3 shrink-0">
                  <span className="font-bold text-slate-800 uppercase tracking-wider text-[10px] block">Initiate Support Case</span>
                  <form onSubmit={handleCreateTicket} className="space-y-3">
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Subject / Heading</label>
                        <Input 
                          type="text" 
                          placeholder="e.g. Fabric roll count check"
                          value={ticketSubject}
                          onChange={(e) => setTicketSubject(e.target.value)}
                          required
                        />
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Related Order</label>
                        <Input 
                          type="text" 
                          placeholder="ORD-2026-88"
                          value={ticketOrderNum}
                          onChange={(e) => setTicketOrderNum(e.target.value)}
                        />
                      </div>
                    </div>

                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Priority Level</label>
                        <Select value={ticketPriority} onChange={(e) => setTicketPriority(e.target.value)}>
                          <option value="CRITICAL">Critical (1h response SLA)</option>
                          <option value="HIGH">High (4h response SLA)</option>
                          <option value="MEDIUM">Medium (24h response SLA)</option>
                          <option value="LOW">Low (48h response SLA)</option>
                        </Select>
                      </div>
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Problem Category</label>
                        <Select value={ticketCategory} onChange={(e) => setTicketCategory(e.target.value)}>
                          <option value="QUALITY">Quality Conformity</option>
                          <option value="PRODUCTION">Stitching Stage</option>
                          <option value="PACKAGING">Packaging</option>
                          <option value="DELIVERY">Logistics Dispatch</option>
                        </Select>
                      </div>
                    </div>

                    <div>
                      <label className="block text-slate-500 mb-1 font-semibold">Detailed Description</label>
                      <Input 
                        type="text" 
                        placeholder="Please supply serials, batch numbers or specs..."
                        value={ticketDesc}
                        onChange={(e) => setTicketDesc(e.target.value)}
                        required
                      />
                    </div>

                    <Button type="submit" variant="primary" className="w-full justify-center">
                      Launch Secure Ticket (Initiate SLA Timers)
                    </Button>
                  </form>
                </Card>

                {/* Ticket Queue list */}
                <div className="space-y-3">
                  <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Active Support Tickets</span>
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
                        <Badge status="info">{t.status}</Badge>
                      </div>
                      <p className="text-slate-650 font-bold text-[11px] truncate">{t.subject}</p>
                      <div className="flex justify-between text-[10px] text-slate-400">
                        <span>Order: {t.orderNumber} | Priority: {t.priority}</span>
                        <span>Due: {new Date(t.responseDueAt).toLocaleTimeString()}</span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              {/* Right: Messages Thread & SLA Timer Countdown & CSAT Form */}
              <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col justify-between overflow-hidden">
                {selectedTicketId ? (() => {
                  const tkt = ticketsList.find(t => t.id === selectedTicketId) || ticketsList[0];
                  return (
                    <div className="flex-1 flex flex-col justify-between overflow-hidden">
                      
                      {/* Ticket header & SLA status */}
                      <div className="pb-3 border-b border-slate-100 flex justify-between items-center shrink-0">
                        <div>
                          <span className="font-bold text-slate-900 font-mono text-[13px]">{tkt.ticketNumber} — {tkt.subject}</span>
                          <span className="text-[10px] text-slate-400 font-mono block">Case SLA: {tkt.priority} priority response due today.</span>
                        </div>

                        {/* Countdowns timer */}
                        <div className="text-right">
                          <span className="text-[9px] text-rose-500 font-bold uppercase tracking-wider block">SLA Response Countdown</span>
                          <span className="font-mono font-bold text-rose-700 bg-rose-50 border border-rose-200 px-2 py-0.5 rounded text-[11px]">
                            00h 42m remaining
                          </span>
                        </div>
                      </div>

                      {/* Message Timeline box */}
                      <div className="flex-1 my-4 overflow-y-auto space-y-3 pr-2">
                        {activeTicketMessages.map(msg => (
                          <div key={msg.id} className="p-3 bg-slate-50 border border-slate-200 rounded-xl text-left space-y-1">
                            <div className="flex justify-between font-bold text-[10px] text-indigo-650 font-mono">
                              <span>{msg.senderEmail}</span>
                              <span className="text-slate-400 font-normal">{new Date(msg.createdAt).toLocaleTimeString()}</span>
                            </div>
                            <p className="text-[11px] text-slate-800">{msg.messageText}</p>
                          </div>
                        ))}
                      </div>

                      {/* Chat form */}
                      <form onSubmit={handleSendTicketMessage} className="flex space-x-2 shrink-0 border-t border-slate-100 pt-3">
                        <Input 
                          type="text" 
                          placeholder="Respond to support agent thread..." 
                          value={chatInput}
                          onChange={(e) => setChatInput(e.target.value)}
                          required
                        />
                        <Button type="submit" variant="primary">
                          <Send className="w-4 h-4" />
                        </Button>
                      </form>

                      {/* Customer CSAT feedback survey */}
                      <div className="mt-4 pt-3 border-t border-slate-100 bg-slate-50 p-3 rounded-xl space-y-2 text-left">
                        <span className="font-bold text-slate-700 block flex items-center space-x-1">
                          <HeartHandshake className="w-4 h-4 text-indigo-600" />
                          <span>Close ticket & rate support interaction</span>
                        </span>
                        
                        <form onSubmit={handleSubmitFeedback} className="space-y-2">
                          <div className="grid grid-cols-3 gap-2">
                            <div>
                              <label className="block text-slate-500 mb-1 text-[10px] font-semibold">CSAT Score</label>
                              <Select value={feedbackRating} onChange={(e) => setFeedbackRating(Number(e.target.value))}>
                                <option value="5">5 - Excellent</option>
                                <option value="4">4 - Good</option>
                                <option value="3">3 - Average</option>
                                <option value="2">2 - Poor</option>
                                <option value="1">1 - Terrible</option>
                              </Select>
                            </div>
                            <div>
                              <label className="block text-slate-500 mb-1 text-[10px] font-semibold">NPS (Likelihood)</label>
                              <Select value={feedbackNps} onChange={(e) => setFeedbackNps(Number(e.target.value))}>
                                <option value="10">10 - Definitely recommend</option>
                                <option value="9">9</option>
                                <option value="8">8</option>
                                <option value="5">5 - Neutral</option>
                                <option value="0">0 - Detractor</option>
                              </Select>
                            </div>
                            <div>
                              <label className="block text-slate-500 mb-1 text-[10px] font-semibold">Rating Category</label>
                              <Select value={feedbackCategory} onChange={(e) => setFeedbackCategory(e.target.value)}>
                                <option value="SUPPORT">Customer Support</option>
                                <option value="QUALITY">Quality Satisfaction</option>
                                <option value="DELIVERY">Delivery On-Time</option>
                              </Select>
                            </div>
                          </div>

                          <div>
                            <Input 
                              type="text" 
                              placeholder="Any comments regarding resolution speed..."
                              value={feedbackComment}
                              onChange={(e) => setFeedbackComment(e.target.value)}
                            />
                          </div>

                          <Button type="submit" variant="primary" className="!py-1 w-full justify-center">
                            Submit CSAT / NPS Survey
                          </Button>
                        </form>
                      </div>

                    </div>
                  );
                })() : (
                  <p className="text-slate-400">Select a ticket from the queue or create a new ticket to open the communication line.</p>
                )}
              </div>

            </div>
          )}

          {/* SCREEN 18: ACTIVITY TIMELINE */}
          {activeScreen === 'activity' && (
            <div className="flex-1 flex flex-col space-y-4 text-left overflow-y-auto">
              <span className="font-bold text-slate-500 uppercase tracking-wider text-[10px] block">Corporate Client Log Timeline</span>
              
              <Card className="space-y-6">
                {[
                  { title: 'Support Ticket Initiated', description: 'Ticket TKT-9011 logged regarding stitch ribbing layout.', user: 'buyer@brand.com', time: 'Today, 11:24 AM' },
                  { title: 'Version-Aware Tech Pack Approval', description: 'Tech Pack Version 2.0 signed off under SHA-256 signature.', user: 'buyer@brand.com', time: 'Yesterday, 4:50 PM' },
                  { title: 'QC Verification Pass', description: 'Quality inspection verified for order ORD-2026-88 (Cutting/Stitching verified).', user: 'System Inspector', time: 'August 08, 10:15 AM' },
                  { title: 'Order Document Shared', description: 'Centralized record Commercial Invoice #10214 uploaded by factory admin.', user: 'csm.lead@apex.com', time: 'August 07, 3:00 PM' }
                ].map((act, idx) => (
                  <div key={idx} className="relative pl-6 pb-2 last:pb-0">
                    {/* Line connection */}
                    <div className="absolute left-2.5 top-2.5 bottom-0 w-0.5 bg-slate-200"></div>
                    <div className="absolute left-1.5 top-1.5 w-2.5 h-2.5 rounded-full bg-indigo-600 border-2 border-white"></div>
                    
                    <div className="space-y-0.5">
                      <div className="flex justify-between font-bold text-slate-900 text-xs">
                        <span>{act.title}</span>
                        <span className="text-[10px] text-slate-400 font-normal">{act.time}</span>
                      </div>
                      <p className="text-slate-550 text-[11px] font-medium">{act.description}</p>
                      <span className="text-[10px] text-slate-400 font-mono block">Trigger Owner: {act.user}</span>
                    </div>
                  </div>
                ))}
              </Card>
            </div>
          )}

          {/* SCREEN 16 & 17 & 20: ACCOUNT PROFILE, CONTACTS & NOTIFICATION PREFERENCES */}
          {activeScreen === 'account' && (
            <div className="flex-1 grid grid-cols-2 gap-6 text-left overflow-y-auto">
              
              {/* Left Column: Account Details & Contacts */}
              <div className="space-y-6">
                <Card className="space-y-3">
                  <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Account Organization Profile</h3>
                  <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                    <div>
                      <span className="text-[10px] text-slate-400 block font-bold">COMPANY</span>
                      <span className="text-slate-850 font-bold block">Apex Retail Apparel</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block font-bold">MEMBERSHIP TIER</span>
                      <span className="text-slate-850 font-bold block">Enterprise Plan Partner</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block font-bold">ACCOUNT OWNER</span>
                      <span className="text-slate-850 font-bold block">account.mgr@apex.com</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-slate-400 block font-bold">SUCCESS MANAGER</span>
                      <span className="text-slate-850 font-bold block">csm.lead@apex.com</span>
                    </div>
                  </div>
                </Card>

                {/* Team contacts */}
                <Card className="space-y-3">
                  <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Client Contacts Reference Directory</h3>
                  <div className="space-y-3">
                    {[
                      { name: 'Sarah Jenkins', role: 'Brand Owner / Primary Procurement', email: 'buyer@brand.com', phone: '+1 405-223-9011', auth: 'Primary Approver' },
                      { name: 'David Miller', role: 'Sourcing Rep', email: 'david.miller@brand.com', phone: '+1 405-223-9012', auth: 'Viewer Only' }
                    ].map((cnt, idx) => (
                      <div key={idx} className="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-1">
                        <div className="flex justify-between font-bold text-slate-900 text-xs">
                          <span>{cnt.name}</span>
                          <Badge status={cnt.auth.includes('Primary') ? 'success' : 'default'}>{cnt.auth}</Badge>
                        </div>
                        <p className="text-slate-500 font-medium text-[11px]">{cnt.role}</p>
                        <div className="text-[10px] text-slate-400 font-mono">
                          <span>Email: {cnt.email} | Phone: {cnt.phone}</span>
                        </div>
                      </div>
                    ))}
                  </div>
                </Card>
              </div>

              {/* Right Column: Preferences Preferences */}
              <div className="space-y-6">
                <Card className="space-y-4">
                  <h3 className="font-extrabold text-slate-800 uppercase tracking-wider">Notification Preferences Preferences</h3>
                  <p className="text-slate-550 text-[11px]">Specify channels to receive automated order dispatch, delay alerts and approval requests. MfgOS uses idempotent queues to avoid duplicate alerts.</p>
                  
                  <div className="space-y-3 pt-2">
                    <label className="flex items-start space-x-3 cursor-pointer">
                      <input 
                        type="checkbox" 
                        checked={emailPref}
                        onChange={(e) => setEmailPref(e.target.checked)}
                        className="w-4 h-4 text-indigo-650 rounded border-slate-200 mt-0.5" 
                      />
                      <div>
                        <span className="font-bold text-slate-800 block text-xs">Enable Corporate Email Alerts</span>
                        <span className="text-slate-450 block text-[10px]">Instant invoices, spec checklists and SLA breaches alerts.</span>
                      </div>
                    </label>

                    <label className="flex items-start space-x-3 cursor-pointer">
                      <input 
                        type="checkbox" 
                        checked={whatsappPref}
                        onChange={(e) => setWhatsappPref(e.target.checked)}
                        className="w-4 h-4 text-indigo-650 rounded border-slate-200 mt-0.5" 
                      />
                      <div>
                        <span className="font-bold text-slate-800 block text-xs">Enable WhatsApp Instant Notifications</span>
                        <span className="text-slate-450 block text-[10px]">Automated dispatch tracking numbers, delay alerts, and sample sign-off requests.</span>
                      </div>
                    </label>

                    <label className="flex items-start space-x-3 cursor-pointer">
                      <input 
                        type="checkbox" 
                        checked={slackPref}
                        onChange={(e) => setSlackPref(e.target.checked)}
                        className="w-4 h-4 text-indigo-650 rounded border-slate-200 mt-0.5" 
                      />
                      <div>
                        <span className="font-bold text-slate-800 block text-xs">Enable Slack Channel Webhook</span>
                        <span className="text-slate-450 block text-[10px]">Direct channel postings on active design approvals and QC failure alerts.</span>
                      </div>
                    </label>
                  </div>

                  <Button variant="primary" onClick={() => setActionMsg('Preferences successfully updated.')}>
                    Save Preferences Preferences
                  </Button>
                </Card>
              </div>

            </div>
          )}

        </div>

      </div>

    </div>
  );
}
