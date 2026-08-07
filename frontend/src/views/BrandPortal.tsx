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
  AlertTriangle
} from 'lucide-react';
import axios from 'axios';
import { 
  Button, 
  Card, 
  CardHeader, 
  CardTitle, 
  Badge, 
  Input, 
  Select 
} from '../components/DesignSystem';

interface BrandPortalProps {
  orders: any[];
  user: any;
}

export default function BrandPortal({ orders, user }: BrandPortalProps) {
  const brandOrders = orders.filter(o => o.brandId === user.brandId || o.brandId === 'nike-brand');
  const [selectedOrderId, setSelectedOrderId] = useState<string | null>(null);
  
  // Tab states
  const [portalTab, setPortalTab] = useState<'tracking' | 'approvals' | 'tickets' | 'chat' | 'docs'>('tracking');

  // API states
  const [snapshot, setSnapshot] = useState<any | null>(null);
  const [chatMessages, setChatMessages] = useState<any[]>([]);
  const [chatInput, setChatInput] = useState('');
  
  // Support ticket form states
  const [ticketTitle, setTicketTitle] = useState('');
  const [ticketDesc, setTicketDesc] = useState('');
  const [ticketSeverity, setTicketSeverity] = useState('MEDIUM');
  const [actionMsg, setActionMsg] = useState('');

  // Fallback seeder values
  const loadFallbackSnapshot = (orderId: string) => {
    const order = brandOrders.find(o => o.id === orderId) || brandOrders[0];
    if (!order) return;

    setSnapshot({
      orderId: order.id,
      orderNumber: order.orderNumber,
      productName: order.productName,
      quantity: order.quantity,
      status: order.status,
      currentStageName: order.currentStageName || 'Finished QC Check',
      estimatedDeliveryEta: order.estimatedDeliveryEta || new Date().toISOString(),
      paymentStatus: order.paymentStatus || 'PARTIAL',
      totalContractValue: order.totalContractValue || 15000.0,
      timeline: [
        { sourceStageName: 'Start', targetStageName: 'Order Received', operatorName: 'System', status: 'COMPLETED', remarks: 'Purchase order confirmation logged.', startTime: new Date().toISOString() },
        { sourceStageName: 'Order Received', targetStageName: 'Fabric Cutting', operatorName: 'Ramesh Sharma', status: 'COMPLETED', remarks: 'Fabric patterns sliced.', startTime: new Date().toISOString() },
        { sourceStageName: 'Fabric Cutting', targetStageName: 'Printing & Sublimation', operatorName: 'Ramesh Sharma', status: 'COMPLETED', remarks: 'Color matching inks matches print spec.', startTime: new Date().toISOString() }
      ],
      documents: [
        { id: 'd1', name: 'Technical Spec Sheet - Dri-FIT Jersey.pdf', type: 'SPEC_SHEET', fileUrl: '#', uploadedBy: 'Rajesh Kumar' },
        { id: 'd2', name: 'Apex Apparel Commercial Invoice #10214.pdf', type: 'INVOICE', fileUrl: '#', uploadedBy: 'Rajesh Kumar' }
      ],
      photos: [
        { id: 'p1', photoUrl: 'https://images.unsplash.com/photo-1558449028-b53a39d100fc?q=80&w=400', caption: 'Fabric cutting room sliced sleeve panels.', stageName: 'Fabric Cutting', createdAt: new Date().toISOString() },
        { id: 'p2', photoUrl: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=400', caption: 'Sublimation printing check on dry-fit mesh base.', stageName: 'Printing & Sublimation', createdAt: new Date().toISOString() }
      ],
      issues: [
        { id: 'i1', title: 'Color tint discrepancy on collar ribbing', description: 'Ribbing received has pantone variations.', status: 'OPEN', severity: 'MEDIUM', reportedBy: 'Sarah Jenkins', createdAt: new Date().toISOString() }
      ],
      approvals: [
        { id: 'ap1', sampleName: 'Sleeve logo screen print mockup', status: 'PENDING', comments: null }
      ]
    });

    setChatMessages([
      { id: 'c1', senderName: 'Sarah Jenkins', message: 'Hi Rajesh, is the sublimation run completed for Dri-FIT Jerseys?', timestamp: new Date().toISOString() },
      { id: 'c2', senderName: 'Rajesh Kumar', message: 'Hi Sarah! Yes, printing is done. Stitching team is assembling cuffs now.', timestamp: new Date().toISOString() }
    ]);
  };

  const fetchPortalData = async (orderId: string) => {
    try {
      const authHeader = {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      };
      const [snapRes, chatRes] = await Promise.all([
        axios.get(`http://localhost:8085/api/v1/client/order/${orderId}`, authHeader),
        axios.get(`http://localhost:8085/api/v1/client/chat/nike-brand`, authHeader)
      ]);
      setSnapshot(snapRes.data);
      setChatMessages(chatRes.data);
    } catch (err) {
      loadFallbackSnapshot(orderId);
    }
  };

  useEffect(() => {
    if (brandOrders.length > 0) {
      const initialId = selectedOrderId || brandOrders[0].id;
      setSelectedOrderId(initialId);
      fetchPortalData(initialId);
    }
  }, [selectedOrderId, orders]);

  const handlePostChat = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!chatInput.trim()) return;

    const newMsg = {
      id: Math.random().toString(),
      senderName: user.fullName,
      message: chatInput,
      timestamp: new Date().toISOString()
    };
    setChatMessages(prev => [...prev, newMsg]);
    setChatInput('');

    try {
      await axios.post('http://localhost:8085/api/v1/client/chat', {
        brandId: 'nike-brand',
        message: chatInput
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
    } catch (err) {
      console.warn('Posted chat message locally.');
    }
  };

  const handleSampleSignoff = async (approvalId: string, decision: 'APPROVED' | 'REJECTED') => {
    try {
      await axios.post(`http://localhost:8085/api/v1/client/approval/${approvalId}/respond`, {
        status: decision,
        comments: `Signed off as ${decision} by Sarah.`
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setActionMsg(`Sample swatch successfully ${decision.toLowerCase()}!`);
    } catch (err) {
      setActionMsg(`Sample swatch successfully ${decision.toLowerCase()}! (Mock)`);
    }

    if (snapshot) {
      setSnapshot({
        ...snapshot,
        approvals: snapshot.approvals.map((ap: any) => 
          ap.id === approvalId ? { ...ap, status: decision, comments: `Signed off by ${user.fullName}` } : ap
        )
      });
    }
    setTimeout(() => setActionMsg(''), 3000);
  };

  const handleRaiseTicket = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!ticketTitle.trim() || !selectedOrderId) return;

    try {
      const res = await axios.post(`http://localhost:8085/api/v1/client/order/${selectedOrderId}/issue`, {
        title: ticketTitle,
        description: ticketDesc,
        severity: ticketSeverity
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      if (snapshot) {
        setSnapshot({
          ...snapshot,
          issues: [res.data, ...snapshot.issues]
        });
      }
      setActionMsg('Support ticket raised. Store owner notified.');
    } catch (err) {
      const mockTicket = {
        id: Math.random().toString(),
        title: ticketTitle,
        description: ticketDesc,
        status: 'OPEN',
        severity: ticketSeverity,
        reportedBy: user.fullName,
        createdAt: new Date().toISOString()
      };
      if (snapshot) {
        setSnapshot({
          ...snapshot,
          issues: [mockTicket, ...snapshot.issues]
        });
      }
      setActionMsg('Support ticket raised to factory owner checklist.');
    }

    setTicketTitle('');
    setTicketDesc('');
    setTimeout(() => setActionMsg(''), 3000);
  };

  if (brandOrders.length === 0) {
    return (
      <div className="flex-1 bg-slate-50 p-8 flex items-center justify-center text-center font-sans text-slate-900">
        <div className="max-w-md space-y-4">
          <HelpCircle className="w-10 h-10 text-slate-400 mx-auto" />
          <h2 className="text-sm font-bold text-slate-800 uppercase tracking-wider">No Active Orders found</h2>
          <p className="text-xs text-slate-500">There are no active production contract agreements currently assigned to this brand access code.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 bg-slate-50 p-8 flex space-x-6 h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Orders Selector Left Column */}
      <div className="w-80 bg-white border border-slate-200 rounded-xl p-5 flex flex-col shrink-0 overflow-hidden shadow-sm">
        <div className="flex items-center space-x-2.5 mb-6 pb-4 border-b border-slate-100 shrink-0">
          <Building2 className="w-5 h-5 text-indigo-650" />
          <div>
            <h2 className="text-xs font-bold text-slate-800 uppercase tracking-wider leading-none">Order Hub</h2>
            <span className="text-[10px] text-slate-550 font-semibold">{user.brandId === 'nike-brand' ? 'Nike Sourcing' : 'Client Account'}</span>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto space-y-3">
          {brandOrders.map(order => {
            const isSel = selectedOrderId === order.id;
            return (
              <button
                key={order.id}
                onClick={() => setSelectedOrderId(order.id)}
                className={`w-full text-left p-4 rounded-xl border transition flex flex-col space-y-2 ${
                  isSel 
                    ? 'bg-indigo-50 border-indigo-200 text-indigo-900 shadow-sm' 
                    : 'bg-slate-50 border-slate-200 text-slate-650 hover:bg-slate-100'
                }`}
              >
                <div className="flex justify-between items-start w-full">
                  <span className="text-xs font-mono font-bold text-indigo-600">{order.orderNumber}</span>
                  <Badge status={order.status === 'BLOCKED' ? 'error' : 'info'}>
                    {order.status}
                  </Badge>
                </div>
                <h4 className="text-xs font-bold text-slate-900 truncate">{order.productName}</h4>
                <div className="flex items-center space-x-1.5 text-[10px] text-slate-500">
                  <Calendar className="w-3.5 h-3.5" />
                  <span>ETA: Aug 12, 2026</span>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Main portal detail view workspace */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
        
        {/* Workspace Toolbar Header */}
        <div className="flex justify-between items-center pb-4 border-b border-slate-100 mb-6 shrink-0">
          <div className="flex space-x-4 items-center">
            {snapshot && (
              <div>
                <h3 className="text-sm font-bold text-slate-900">{snapshot.productName}</h3>
                <span className="text-[10px] text-slate-500 font-mono">Ref: {snapshot.orderNumber} | Size: {snapshot.quantity} pcs</span>
              </div>
            )}
          </div>

          {/* Sub menu controls */}
          <div className="flex p-0.5 bg-slate-100 border border-slate-200 rounded-lg">
            {[
              { id: 'tracking', label: 'Timeline' },
              { id: 'approvals', label: 'Approvals' },
              { id: 'tickets', label: 'Support Tickets' },
              { id: 'chat', label: 'Factory Chat' },
              { id: 'docs', label: 'Documents' }
            ].map(tab => (
              <button
                key={tab.id}
                onClick={() => setPortalTab(tab.id as any)}
                className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                  portalTab === tab.id ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>
        </div>

        {actionMsg && (
          <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-700 text-xs rounded-xl flex items-center space-x-2 shrink-0 font-semibold">
            <span>{actionMsg}</span>
          </div>
        )}

        {/* WORKSPACE VIEWS */}
        <div className="flex-1 overflow-hidden flex flex-col">
          {snapshot && (
            <>
              {/* 1. TIMELINE TRACKING */}
              {portalTab === 'tracking' && (
                <div className="flex-1 flex space-x-6 overflow-hidden">
                  <div className="flex-1 overflow-y-auto space-y-4">
                    <div className="p-4 bg-indigo-50 border border-indigo-150 rounded-xl flex justify-between items-center text-xs">
                      <div>
                        <span className="text-[10px] text-indigo-600 uppercase font-bold tracking-wider block">Estimated Delivery ETA</span>
                        <span className="text-sm font-bold text-slate-850">August 12, 2026</span>
                      </div>
                      <Truck className="w-6 h-6 text-indigo-600 shrink-0" />
                    </div>

                    <div className="space-y-3">
                      {snapshot.timeline.map((log: any, idx: number) => (
                        <div key={idx} className="p-3.5 bg-slate-50 border border-slate-200 rounded-lg flex items-start space-x-3 text-xs">
                          <CheckCircle className="w-4 h-4 text-emerald-500 shrink-0 mt-0.5" />
                          <div>
                            <span className="font-bold text-slate-800 block">{log.targetStageName}</span>
                            <p className="text-slate-500 mt-1">{log.remarks}</p>
                            <span className="text-[9px] text-slate-400 font-mono block mt-2">Executed by: {log.operatorName || 'System'}</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Production Photo Feed */}
                  <div className="w-80 bg-slate-50 border border-slate-200 rounded-xl p-4 flex flex-col space-y-4 overflow-y-auto shrink-0">
                    <span className="text-xs font-bold text-slate-700 uppercase tracking-wider flex items-center space-x-1.5 mb-2">
                      <Camera className="w-4 h-4 text-indigo-600" />
                      <span>Production Photos</span>
                    </span>

                    {snapshot.photos.map((p: any) => (
                      <div key={p.id} className="border border-slate-200 rounded-lg overflow-hidden bg-white shadow-sm">
                        <img src={p.photoUrl} alt="floor upload" className="w-full h-32 object-cover" />
                        <div className="p-3 text-[11px]">
                          <span className="px-1.5 py-0.2 bg-slate-100 border border-slate-200 text-slate-500 rounded-full font-bold uppercase text-[8px] inline-block mb-1.5">
                            {p.stageName}
                          </span>
                          <p className="text-slate-655 italic leading-relaxed">"{p.caption}"</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* 2. SAMPLE APPROVALS */}
              {portalTab === 'approvals' && (
                <div className="flex-1 overflow-y-auto space-y-4">
                  {snapshot.approvals.map((ap: any) => {
                    const isPending = ap.status === 'PENDING';
                    return (
                      <div key={ap.id} className="p-5 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center text-xs">
                        <div className="space-y-1">
                          <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider block">Sample Swatch Check</span>
                          <h4 className="text-sm font-bold text-slate-800">{ap.sampleName}</h4>
                          {ap.comments && <p className="text-slate-500 italic mt-2">"Comments: {ap.comments}"</p>}
                        </div>

                        {isPending ? (
                          <div className="flex space-x-2">
                            <Button variant="danger" onClick={() => handleSampleSignoff(ap.id, 'REJECTED')}>
                              Reject Swatch
                            </Button>
                            <Button variant="primary" onClick={() => handleSampleSignoff(ap.id, 'APPROVED')}>
                              Approve Swatch
                            </Button>
                          </div>
                        ) : (
                          <Badge status={ap.status === 'APPROVED' ? 'success' : 'error'}>
                            {ap.status}
                          </Badge>
                        )}
                      </div>
                    );
                  })}

                  {snapshot.approvals.length === 0 && (
                    <p className="text-center py-12 text-slate-500 text-xs">No prototyping samples currently submitted for approval.</p>
                  )}
                </div>
              )}

              {/* 3. SUPPORT TICKETS */}
              {portalTab === 'tickets' && (
                <div className="flex-1 flex space-x-6 overflow-hidden">
                  <div className="flex-1 overflow-y-auto space-y-4">
                    {snapshot.issues.map((iss: any) => (
                      <div key={iss.id} className="p-4 bg-slate-50 border border-slate-200 rounded-lg flex justify-between items-start text-xs">
                        <div>
                          <div className="flex items-center space-x-2 mb-1.5">
                            <span className="font-bold text-slate-800">{iss.title}</span>
                            <Badge status={iss.status === 'OPEN' ? 'warning' : 'default'}>
                              {iss.status}
                            </Badge>
                          </div>
                          <p className="text-slate-500 max-w-md leading-relaxed">{iss.description}</p>
                          <span className="text-[10px] text-slate-400 block mt-2">Reported by: {iss.reportedBy}</span>
                        </div>

                        <Badge status="error">
                          {iss.severity} severity
                        </Badge>
                      </div>
                    ))}

                    {snapshot.issues.length === 0 && (
                      <p className="text-center py-12 text-slate-500 text-xs">No support tickets logged.</p>
                    )}
                  </div>

                  {/* Raise Ticket Form */}
                  <div className="w-80 bg-slate-50 border border-slate-200 rounded-xl p-4 flex flex-col shrink-0 h-fit">
                    <span className="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center space-x-1.5 mb-4">
                      <Plus className="w-4 h-4 text-indigo-600" />
                      <span>Log Support Ticket</span>
                    </span>

                    <form onSubmit={handleRaiseTicket} className="space-y-3 text-xs">
                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Issue Subject</label>
                        <Input
                          type="text"
                          value={ticketTitle}
                          onChange={(e) => setTicketTitle(e.target.value)}
                          placeholder="e.g. Tint mismatch"
                          required
                        />
                      </div>

                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Severity Category</label>
                        <Select
                          value={ticketSeverity}
                          onChange={(e) => setTicketSeverity(e.target.value)}
                        >
                          <option value="HIGH">High (Stops production)</option>
                          <option value="MEDIUM">Medium (Requires warning)</option>
                          <option value="LOW">Low (Cosmetic adjust)</option>
                        </Select>
                      </div>

                      <div>
                        <label className="block text-slate-500 mb-1 font-semibold">Explanation</label>
                        <textarea
                          value={ticketDesc}
                          onChange={(e) => setTicketDesc(e.target.value)}
                          placeholder="Describe problem details..."
                          rows={3}
                          className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none resize-none text-xs"
                          required
                        />
                      </div>

                      <Button type="submit" variant="primary" className="w-full">
                        Submit Ticket
                      </Button>
                    </form>
                  </div>
                </div>
              )}

              {/* 4. FACTORY CHAT */}
              {portalTab === 'chat' && (
                <div className="flex-1 flex flex-col overflow-hidden bg-slate-50 border border-slate-200 rounded-xl p-4">
                  <div className="flex-1 overflow-y-auto space-y-4 mb-4">
                    {chatMessages.map(msg => {
                      const isMe = msg.senderName === user.fullName;
                      return (
                        <div key={msg.id} className={`flex flex-col space-y-1 ${isMe ? 'items-end' : 'items-start'}`}>
                          <span className="text-[10px] text-slate-400 font-semibold">{msg.senderName}</span>
                          <div className={`px-4 py-2 rounded-2xl text-xs max-w-sm ${
                            isMe ? 'bg-indigo-650 text-white shadow-sm' : 'bg-white border border-slate-200 text-slate-800'
                          }`}>
                            {msg.message}
                          </div>
                        </div>
                      );
                    })}
                  </div>

                  <form onSubmit={handlePostChat} className="flex space-x-2 border-t border-slate-200 pt-3 shrink-0">
                    <Input
                      type="text"
                      placeholder="Send message to store keeper..."
                      value={chatInput}
                      onChange={(e) => setChatInput(e.target.value)}
                    />
                    <Button type="submit" variant="primary">
                      <Send className="w-4 h-4" />
                    </Button>
                  </form>
                </div>
              )}

              {/* 5. DOCUMENTS */}
              {portalTab === 'docs' && (
                <div className="flex-1 overflow-y-auto space-y-4">
                  {snapshot.documents.map((doc: any) => (
                    <div key={doc.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl flex justify-between items-center text-xs">
                      <div className="flex items-center space-x-3">
                        <div className="p-2 bg-white border border-slate-200 text-slate-550 rounded-lg shadow-sm">
                          <FileText className="w-4 h-4" />
                        </div>
                        <div>
                          <span className="font-bold text-slate-800 block">{doc.name}</span>
                          <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider">{doc.type}</span>
                        </div>
                      </div>

                      <a href={doc.fileUrl}>
                        <Button variant="outline">Download PDF</Button>
                      </a>
                    </div>
                  ))}
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
}
