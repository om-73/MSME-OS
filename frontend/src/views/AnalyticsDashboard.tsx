import React, { useState, useEffect } from 'react';
import { 
  TrendingUp, 
  Download, 
  Calendar, 
  Cpu, 
  AlertTriangle, 
  BarChart3, 
  Activity, 
  User, 
  Zap, 
  Clock,
  Sparkles,
  ClipboardList
} from 'lucide-react';
import { api, API_BASE_URL } from '../api/client';
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

export default function AnalyticsDashboard() {
  const [executiveStats, setExecutiveStats] = useState<any | null>(null);
  const [forecastStats, setForecastStats] = useState<any | null>(null);

  // Subtabs
  const [activeTab, setActiveTab] = useState<'cockpit' | 'ai' | 'performance' | 'export'>('cockpit');

  // Reports
  const [reportType, setReportType] = useState('worker-performance');
  const [exportFormat, setExportFormat] = useState('CSV');

  const [statusMsg, setStatusMsg] = useState('');

  const fetchAnalytics = async () => {
    try {
      const [execRes, foreRes] = await Promise.all([
        api.get('/analytics/executive'),
        api.get('/analytics/forecast')
      ]);
      setExecutiveStats(execRes.data);
      setForecastStats(foreRes.data);
    } catch (err) {
      console.error('Failed to fetch analytics from database:', err);
    }
  };

  useEffect(() => {
    fetchAnalytics();
  }, []);

  const handleDownloadReport = () => {
    const url = `${API_BASE_URL}/analytics/export?reportType=${reportType}`;
    // Trigger download
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `${reportType}-report.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    setStatusMsg(`Downloading ${reportType} report as ${exportFormat}...`);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  if (!executiveStats || !forecastStats) {
    return (
      <div className="flex-1 bg-slate-50 p-8 flex items-center justify-center font-bold text-slate-550 text-xs">
        Compiling BI data models...
      </div>
    );
  }

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header toolbar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Intelligence Command Center</h2>
            <p className="text-xs text-slate-550 mt-0.5">Real-time bottleneck analysis, AI material consumption curves & cycle forecasts</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {['cockpit', 'ai', 'performance', 'export'].map(tab => (
              <button
                key={tab}
                onClick={() => setActiveTab(tab as any)}
                className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                  activeTab === tab ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                {tab === 'cockpit' ? 'Executive Cockpit' : tab === 'ai' ? 'AI Forecasting' : tab === 'performance' ? 'QC & Performance' : 'Report Exports'}
              </button>
            ))}
          </div>
        </div>
      </div>

      {statusMsg && (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      )}

      {/* Main Container */}
      <div className="flex-1 flex flex-col overflow-hidden">
        
        {activeTab === 'cockpit' && (
          <div className="flex-1 flex flex-col space-y-6 overflow-y-auto pr-2 pb-6">
            {/* KPI Cards Grid */}
            <div className="grid grid-cols-4 gap-6">
              <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Active Batches</span>
                <span className="text-2xl font-bold text-slate-850 font-mono">{executiveStats.activeOrders} orders</span>
              </div>
              <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-455 uppercase font-bold block mb-1">On-Time Delivery</span>
                <span className="text-2xl font-bold text-emerald-650 font-mono">{executiveStats.onTimeDeliveryRate}%</span>
              </div>
              <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-455 uppercase font-bold block mb-1">Total Revenue</span>
                <span className="text-2xl font-bold text-slate-850 font-mono">${executiveStats.totalRevenue.toLocaleString()}</span>
              </div>
              <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-455 uppercase font-bold block mb-1">Floor Efficiency</span>
                <span className="text-2xl font-bold text-indigo-650 font-mono">{executiveStats.productionEfficiency}%</span>
              </div>
            </div>

            {/* High-Fidelity SVG Line Graph */}
            <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm flex flex-col h-80 shrink-0">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
                <TrendingUp className="w-4 h-4 text-indigo-600" />
                <span>Revenue & Profit Performance Trend ($)</span>
              </h3>
              
              <div className="flex-1 relative">
                {/* SVG Area graph */}
                <svg className="w-full h-full" viewBox="0 0 500 150" preserveAspectRatio="none">
                  {/* Grid Lines */}
                  <line x1="0" y1="30" x2="500" y2="30" stroke="#f1f5f9" strokeWidth="1" />
                  <line x1="0" y1="75" x2="500" y2="75" stroke="#f1f5f9" strokeWidth="1" />
                  <line x1="0" y1="120" x2="500" y2="120" stroke="#f1f5f9" strokeWidth="1" />

                  {/* Revenue Curve */}
                  <path
                    d="M 0 120 L 100 90 L 200 98 L 300 60 L 400 40 L 500 20"
                    fill="none"
                    stroke="#4f46e5"
                    strokeWidth="2.5"
                    strokeLinecap="round"
                  />
                  {/* Profit Curve */}
                  <path
                    d="M 0 140 L 100 125 L 200 128 L 300 100 L 400 90 L 500 80"
                    fill="none"
                    stroke="#10b981"
                    strokeWidth="2.5"
                    strokeLinecap="round"
                  />
                </svg>

                {/* X Axis Labels */}
                <div className="flex justify-between text-[9px] text-slate-400 font-bold uppercase mt-2 font-mono">
                  <span>Mon (12k)</span>
                  <span>Tue (15k)</span>
                  <span>Wed (14k)</span>
                  <span>Thu (18k)</span>
                  <span>Fri (21k)</span>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'ai' && (
          <div className="flex-1 grid grid-cols-3 gap-8 overflow-hidden">
            
            {/* Left Col: Consumption curve and shortages risk */}
            <div className="col-span-2 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
                <Cpu className="w-4 h-4 text-indigo-600" />
                <span>AI Material Consumption Projections</span>
              </h3>

              <div className="flex-1 flex flex-col justify-between">
                {/* SVG Area graph for consumption projections */}
                <div className="h-44 relative">
                  <svg className="w-full h-full" viewBox="0 0 500 120" preserveAspectRatio="none">
                    <path
                      d="M 0 110 L 125 90 L 250 80 L 375 95 L 500 60 L 500 120 L 0 120 Z"
                      fill="rgba(79, 70, 229, 0.08)"
                    />
                    <path
                      d="M 0 110 L 125 90 L 250 80 L 375 95 L 500 60"
                      fill="none"
                      stroke="#4f46e5"
                      strokeWidth="2.5"
                      strokeDasharray="4 2"
                    />
                  </svg>
                  <div className="flex justify-between text-[9px] text-slate-400 font-bold font-mono mt-2 uppercase">
                    <span>T+1 Day</span>
                    <span>T+2 Days</span>
                    <span>T+3 Days</span>
                    <span>T+4 Days</span>
                    <span>T+5 Days</span>
                  </div>
                </div>

                <div className="border-t border-slate-100 pt-4 mt-4">
                  <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-3">Critical Shortage Hazards</h4>
                  <div className="space-y-3">
                    {forecastStats.shortageRisks.map((r: any) => (
                      <div key={r.materialCode} className="p-3 bg-rose-50 border border-rose-100 rounded-lg flex justify-between items-center text-xs">
                        <div className="flex items-center space-x-2">
                          <AlertTriangle className="w-4 h-4 text-rose-550 shrink-0" />
                          <span className="font-bold text-rose-700">{r.materialCode}</span>
                          <span className="text-slate-500 font-semibold">{r.materialName}</span>
                        </div>
                        <span className="font-mono text-rose-700 font-bold">Depletion in {r.daysToZeroStock} days</span>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>

            {/* Right Col: AI parameters and recommendations */}
            <div className="bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
                <Zap className="w-4 h-4 text-indigo-600" />
                <span>Decision Intelligence</span>
              </h3>

              <div className="space-y-6 text-xs text-slate-600">
                <div className="p-4 bg-slate-50 border border-slate-200 rounded-lg">
                  <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Delay Hazard Rating</span>
                  <span className="text-lg font-bold text-slate-800 font-mono">{forecastStats.deliveryDelayRiskPct}% Risk</span>
                </div>

                <div className="p-4 bg-slate-50 border border-slate-200 rounded-lg">
                  <span className="text-[10px] text-slate-455 uppercase font-bold block mb-1">Detected Process Bottleneck</span>
                  <span className="text-xs font-bold text-slate-800 block mt-1">{forecastStats.machineBottleneckRisk}</span>
                </div>

                <div className="p-4 bg-indigo-50 border border-indigo-200 text-indigo-750 rounded-lg">
                  <span className="font-bold block mb-1 text-indigo-800">AI Recommendation:</span>
                  Optimize stitching assignments to resolve task buffer loads and reorder thread SKU <span className="font-mono font-bold text-indigo-700">RM-TH-02</span> today.
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'performance' && (
          <div className="flex-1 grid grid-cols-3 gap-8 overflow-hidden">
            
            {/* QC / Rework Rate logs */}
            <div className="col-span-2 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
                <BarChart3 className="w-4 h-4 text-indigo-600" />
                <span>Department QC Rework Ratios</span>
              </h3>

              <div className="flex-1 overflow-y-auto pr-2">
                <Table>
                  <TableHead>
                    <th className="pb-3">Department</th>
                    <th className="pb-3 text-right">Rework Count</th>
                    <th className="pb-3 text-right">Rework Rate (%)</th>
                    <th className="pb-3">Quality Status</th>
                  </TableHead>
                  <TableBody>
                    <tr className="hover:bg-slate-50/50 border-b border-slate-100 text-xs">
                      <td className="py-3 font-semibold text-slate-800">Stitching Block</td>
                      <td className="py-3 text-right font-mono font-bold">6 batches</td>
                      <td className="py-3 text-right font-mono font-bold text-rose-550">4.2%</td>
                      <td className="py-3"><Badge status="error">Check Required</Badge></td>
                    </tr>
                    <tr className="hover:bg-slate-50/50 border-b border-slate-100 text-xs">
                      <td className="py-3 font-semibold text-slate-800">Cutting Table</td>
                      <td className="py-3 text-right font-mono font-bold">1 batch</td>
                      <td className="py-3 text-right font-mono font-bold">0.5%</td>
                      <td className="py-3"><Badge status="success">Excellent</Badge></td>
                    </tr>
                    <tr className="hover:bg-slate-50/50 border-b border-slate-100 text-xs">
                      <td className="py-3 font-semibold text-slate-800">Screen Printing</td>
                      <td className="py-3 text-right font-mono font-bold">3 batches</td>
                      <td className="py-3 text-right font-mono font-bold">3.1%</td>
                      <td className="py-3"><Badge status="warning">Warning</Badge></td>
                    </tr>
                  </TableBody>
                </Table>
              </div>
            </div>

            {/* Operator Scorecard rankings */}
            <div className="bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
              <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
                <User className="w-4 h-4 text-indigo-600" />
                <span>Operator Leaderboards</span>
              </h3>

              <div className="flex-1 overflow-y-auto space-y-3 pr-2">
                {[
                  { name: 'Amir Khan', completed: 18, efficiency: '94.2%' },
                  { name: 'Linh Tran', completed: 15, efficiency: '91.8%' },
                  { name: 'John Doe', completed: 12, efficiency: '89.5%' }
                ].map(op => (
                  <div key={op.name} className="p-3 bg-slate-50 border border-slate-200 rounded-lg flex justify-between items-center text-xs">
                    <div>
                      <span className="font-bold text-slate-800 block">{op.name}</span>
                      <span className="text-[10px] text-slate-450 uppercase font-semibold">Completed: {op.completed} tasks</span>
                    </div>
                    <span className="font-mono text-indigo-650 font-bold">{op.efficiency}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {activeTab === 'export' && (
          <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-6 flex items-center space-x-1.5">
              <Download className="w-4 h-4 text-indigo-600" />
              <span>Report Exporters</span>
            </h3>

            <div className="max-w-md space-y-5 text-xs text-slate-600">
              <div>
                <label className="block text-slate-500 mb-1 font-semibold">Select Report Type</label>
                <Select value={reportType} onChange={(e) => setReportType(e.target.value)}>
                  <option value="worker-performance">Worker Performance Report</option>
                  <option value="inventory-aging">Inventory Aging Report</option>
                  <option value="rework-analysis">Quality Rework Log Report</option>
                  <option value="revenue-analysis">Revenue & Profit Audit Report</option>
                </Select>
              </div>

              <div>
                <label className="block text-slate-550 mb-1 font-semibold">Select Format</label>
                <Select value={exportFormat} onChange={(e) => setExportFormat(e.target.value)}>
                  <option value="CSV">Comma Separated Values (.csv)</option>
                  <option value="EXCEL">Microsoft Excel Workbook (.xlsx)</option>
                  <option value="PDF">Adobe Acrobat Document (.pdf)</option>
                </Select>
              </div>

              <div className="pt-4 border-t border-slate-100 flex justify-end">
                <Button variant="primary" onClick={handleDownloadReport}>
                  <Download className="w-4 h-4" />
                  <span>Download Report</span>
                </Button>
              </div>
            </div>
          </div>
        )}

      </div>

    </div>
  );
}
