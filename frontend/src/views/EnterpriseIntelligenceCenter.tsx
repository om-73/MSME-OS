import React, { useState, useEffect } from 'react';
import { 
  BrainCircuit, 
  Sparkles, 
  TrendingUp, 
  AlertTriangle, 
  CheckCircle2, 
  ShieldCheck, 
  Zap, 
  MessageSquare, 
  FileText, 
  Clock, 
  Activity, 
  ArrowRight,
  Send,
  RotateCcw,
  Layers
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

interface EnterpriseIntelligenceCenterProps {
  user?: any;
}

export default function EnterpriseIntelligenceCenter({ user }: EnterpriseIntelligenceCenterProps) {
  const [overview, setOverview] = useState<any | null>(null);
  const [predictions, setPredictions] = useState<any[]>([]);
  const [insights, setInsights] = useState<any[]>([]);
  const [recommendations, setRecommendations] = useState<any[]>([]);
  const [auditLogs, setAuditLogs] = useState<any[]>([]);

  // Subtabs
  const [activeTab, setActiveTab] = useState<'cockpit' | 'predictions' | 'recommendations' | 'copilot' | 'audits'>('cockpit');

  // Copilot State
  const [copilotQuery, setCopilotQuery] = useState('');
  const [copilotHistory, setCopilotHistory] = useState<any[]>([]);
  const [copilotLoading, setCopilotLoading] = useState(false);
  const [statusMsg, setStatusMsg] = useState('');

  const fetchAIData = async () => {
    try {
      const [overRes, predRes, insRes, recRes, logRes] = await Promise.all([
        api.get('/ai/overview'),
        api.get('/ai/predictions'),
        api.get('/ai/insights'),
        api.get('/ai/recommendations'),
        api.get('/ai/audit-logs')
      ]);
      setOverview(overRes.data);
      setPredictions(predRes.data || []);
      setInsights(insRes.data || []);
      setRecommendations(recRes.data || []);
      setAuditLogs(logRes.data || []);
    } catch (err) {
      console.error('Failed to fetch AI intelligence data from database:', err);
    }
  };

  useEffect(() => {
    fetchAIData();
  }, []);

  const handleApproveRecommendation = async (recId: number) => {
    try {
      await api.post(`/ai/recommendations/${recId}/approve?approvedBy=${user?.email || 'factory_owner'}`, {});
      setStatusMsg('Recommendation approved & executed into production workflow.');
      fetchAIData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to approve recommendation.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleCopilotSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!copilotQuery.trim()) return;

    setCopilotLoading(true);
    const q = copilotQuery;
    setCopilotQuery('');

    try {
      const res = await api.post('/ai/copilot/query', { query: q });
      setCopilotHistory(prev => [res.data, ...prev]);
    } catch (err) {
      console.error(err);
    } finally {
      setCopilotLoading(false);
    }
  };

  if (!overview) {
    return (
      <div className="flex-1 bg-slate-50 p-8 flex items-center justify-center text-xs font-bold text-slate-500">
        Initializing Enterprise AI & Predictive Intelligence Engine...
      </div>
    );
  }

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Enterprise AI Intelligence Center</h2>
            <p className="text-xs text-slate-550 mt-0.5">Predictive delay risk, capacity forecasting, bottleneck detection & human-approved recommendations</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'cockpit', label: 'Executive Cockpit' },
              { id: 'predictions', label: 'Delay Predictions' },
              { id: 'recommendations', label: 'Recommendations' },
              { id: 'copilot', label: 'AI Copilot' },
              { id: 'audits', label: 'AI Audit Trail' }
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
        
        {/* 1. EXECUTIVE COCKPIT */}
        {activeTab === 'cockpit' && (
          <div className="flex-1 flex flex-col space-y-6 overflow-y-auto pr-2">
            
            {/* Top KPI Cards */}
            <div className="grid grid-cols-4 gap-6">
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Production Efficiency</span>
                <span className="text-2xl font-bold text-slate-900 font-mono">{overview.overallEfficiencyPct}%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Quality Pass Rate</span>
                <span className="text-2xl font-bold text-emerald-650 font-mono">{overview.qualityPassRatePct}%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">On-Time Delivery</span>
                <span className="text-2xl font-bold text-indigo-650 font-mono">{overview.onTimeDeliveryRatePct}%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Capacity Utilization</span>
                <span className="text-2xl font-bold text-slate-800 font-mono">{overview.capacityUtilizationPct}%</span>
              </div>
            </div>

            {/* AI Insights List */}
            <div className="space-y-4">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
                <BrainCircuit className="w-4 h-4 text-indigo-600" />
                <span>Active Operational AI Insights</span>
              </h3>

              {insights.map(ins => (
                <div key={ins.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2 text-xs">
                  <div className="flex justify-between items-center">
                    <div className="flex items-center space-x-2">
                      <Badge status={ins.category === 'PRODUCTION' ? 'warning' : 'info'}>
                        {ins.category}
                      </Badge>
                      <span className="font-bold text-slate-900 text-sm">{ins.title}</span>
                    </div>
                    <span className="text-[10px] font-mono text-indigo-600 font-bold">Confidence: {Math.round(ins.confidenceScore * 100)}% ({ins.confidence})</span>
                  </div>

                  <p className="text-slate-700 leading-relaxed">{ins.summary}</p>
                  <p className="text-[11px] text-slate-500 font-mono bg-white p-2.5 border border-slate-200 rounded-lg">{ins.supportingData}</p>
                  <div className="pt-1 text-indigo-700 font-semibold flex items-center space-x-1">
                    <ArrowRight className="w-3.5 h-3.5" />
                    <span>Recommended Action: {ins.recommendedAction}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 2. DELAY PREDICTIONS & BOTTLENECK ANALYSIS */}
        {activeTab === 'predictions' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Activity className="w-4 h-4 text-indigo-600" />
              <span>Predictive Delay Risk & Bottleneck Analysis</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Order Ref</th>
                  <th className="pb-3">Original ETA</th>
                  <th className="pb-3">Predicted ETA</th>
                  <th className="pb-3">Delay Probability</th>
                  <th className="pb-3">Risk Level</th>
                  <th className="pb-3">Bottleneck Cause</th>
                  <th className="pb-3">Mitigation Action</th>
                </TableHead>
                <TableBody>
                  {predictions.map(pred => {
                    const isHigh = pred.riskLevel === 'HIGH' || pred.riskLevel === 'CRITICAL';
                    return (
                      <tr key={pred.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                        <td className="py-3 font-mono font-bold text-indigo-600">{pred.orderNumber}</td>
                        <td className="py-3 text-slate-500">{pred.originalEta}</td>
                        <td className={`py-3 font-bold ${isHigh ? 'text-rose-600' : 'text-slate-800'}`}>{pred.predictedEta}</td>
                        <td className="py-3">
                          <div className="w-24 bg-slate-200 h-2 rounded-full overflow-hidden mb-1">
                            <div className={`h-full ${isHigh ? 'bg-rose-500' : 'bg-emerald-500'}`} style={{ width: `${pred.delayProbability}%` }}></div>
                          </div>
                          <span className="text-[10px] font-mono text-slate-500">{pred.delayProbability}% risk</span>
                        </td>
                        <td className="py-3">
                          <Badge status={isHigh ? 'error' : 'success'}>{pred.riskLevel}</Badge>
                        </td>
                        <td className="py-3 text-slate-700 font-medium">{pred.bottleneckCause}</td>
                        <td className="py-3 text-slate-600 italic">{pred.recommendedMitigation}</td>
                      </tr>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

        {/* 3. RECOMMENDATIONS & HUMAN APPROVAL WORKFLOW */}
        {activeTab === 'recommendations' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <div className="p-3 bg-amber-50 border border-amber-200 text-amber-800 text-xs rounded-xl font-medium mb-4 flex items-center space-x-2 shrink-0">
              <ShieldCheck className="w-4 h-4 text-amber-600 shrink-0" />
              <span>AI Safety Guardrail: AI recommendations require explicit authorized human approval prior to workflow execution.</span>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {recommendations.map(rec => {
                const isExecuted = rec.approvalStatus === 'EXECUTED';
                return (
                  <div key={rec.id} className={`p-4 rounded-xl border transition space-y-3 text-xs ${
                    isExecuted ? 'bg-slate-50 border-slate-200 opacity-80' : 'bg-white border-slate-300 shadow-sm'
                  }`}>
                    <div className="flex justify-between items-center">
                      <span className="font-bold text-slate-900 text-sm">{rec.title}</span>
                      <Badge status={isExecuted ? 'success' : 'warning'}>
                        {rec.approvalStatus}
                      </Badge>
                    </div>

                    <p className="text-slate-700">{rec.reason}</p>
                    
                    <div className="grid grid-cols-3 gap-4 p-3 bg-slate-50 border border-slate-200 rounded-lg">
                      <div>
                        <span className="text-[10px] text-slate-400 block font-semibold">EXPECTED IMPACT</span>
                        <span className="font-semibold text-slate-800">{rec.expectedImpact}</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-slate-400 block font-semibold">RISK LEVEL</span>
                        <span className="font-semibold text-slate-800">{rec.riskLevel}</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-slate-400 block font-semibold">CONFIDENCE SCORE</span>
                        <span className="font-mono font-bold text-indigo-600">{Math.round(rec.confidenceScore * 100)}%</span>
                      </div>
                    </div>

                    <div className="flex justify-end pt-1">
                      {!isExecuted ? (
                        <Button variant="primary" onClick={() => handleApproveRecommendation(rec.id)}>
                          <CheckCircle2 className="w-4 h-4" />
                          <span>Approve & Execute Action</span>
                        </Button>
                      ) : (
                        <span className="text-[11px] text-slate-400 italic">Approved & executed by {rec.approvedBy}</span>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* 4. AI COPILOT */}
        {activeTab === 'copilot' && (
          <div className="flex-1 flex flex-col overflow-hidden">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <MessageSquare className="w-4 h-4 text-indigo-600" />
              <span>Manufacturing AI Natural Language Assistant</span>
            </h3>

            {/* Prompt Form */}
            <form onSubmit={handleCopilotSubmit} className="flex space-x-3 mb-4 shrink-0">
              <Input
                type="text"
                placeholder="Ask AI Copilot: e.g. 'Which orders are at risk of missing delivery?'"
                value={copilotQuery}
                onChange={(e) => setCopilotQuery(e.target.value)}
                className="flex-1"
              />
              <Button type="submit" variant="primary" disabled={copilotLoading}>
                <Send className="w-4 h-4" />
                <span>Query</span>
              </Button>
            </form>

            {/* Answer Feed */}
            <div className="flex-1 overflow-y-auto pr-2 space-y-4 text-xs">
              {copilotHistory.map((item, idx) => (
                <div key={idx} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
                  <div className="flex justify-between items-center">
                    <span className="font-bold text-indigo-600 font-mono">Query: "{item.query}"</span>
                    <Badge status="info">Confidence: {item.confidence}</Badge>
                  </div>
                  <p className="text-slate-800 leading-relaxed bg-white p-3 border border-slate-200 rounded-lg">{item.answer}</p>
                </div>
              ))}

              {copilotHistory.length === 0 && (
                <p className="text-center py-12 text-slate-400 italic">Ask a question above to query tenant operational data in natural language.</p>
              )}
            </div>
          </div>
        )}

        {/* 5. AUDIT TRAIL */}
        {activeTab === 'audits' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Clock className="w-4 h-4 text-indigo-600" />
              <span>AI Execution & Model Audit Trail</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Timestamp</th>
                  <th className="pb-3">Actor</th>
                  <th className="pb-3">Request Type</th>
                  <th className="pb-3">Model Provider</th>
                  <th className="pb-3">Confidence</th>
                  <th className="pb-3">Summary</th>
                </TableHead>
                <TableBody>
                  {auditLogs.map(log => (
                    <tr key={log.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                      <td className="py-3 text-slate-500 font-mono">{new Date(log.timestamp).toLocaleTimeString()}</td>
                      <td className="py-3 font-semibold text-slate-800">{log.actorId}</td>
                      <td className="py-3 font-bold text-indigo-600">{log.requestType}</td>
                      <td className="py-3 font-mono text-slate-600">{log.modelProvider}</td>
                      <td className="py-3 font-mono font-bold text-slate-900">{Math.round(log.confidenceScore * 100)}%</td>
                      <td className="py-3 text-slate-700 truncate max-w-[200px]">{log.responseSummary}</td>
                    </tr>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

      </div>
    </div>
  );
}
