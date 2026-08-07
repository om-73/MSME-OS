import React from 'react';
import { 
  TrendingUp, 
  Activity, 
  Clock, 
  CheckCircle2, 
  AlertTriangle, 
  Users, 
  Gauge, 
  Flame, 
  TrendingDown, 
  CheckSquare, 
  ArrowUpRight, 
  Plus, 
  Layers,
  Sparkles
} from 'lucide-react';

interface DashboardProps {
  orders: any[];
  onTriggerShortage: () => void;
}

export default function Dashboard({ orders, onTriggerShortage }: DashboardProps) {
  const activeOrders = orders.filter(o => o.status === 'IN_PROGRESS' || o.status === 'BLOCKED').length;
  const completedOrders = orders.filter(o => o.status === 'COMPLETED' || o.status === 'DISPATCHED').length;
  const blockedOrders = orders.filter(o => o.status === 'BLOCKED').length;
  const totalContractVal = orders.reduce((sum, o) => sum + (o.totalContractValue || 0), 0);

  // Group by stage for pipeline analysis
  const stageCounts: Record<string, number> = {};
  orders.forEach(o => {
    if (o.status === 'IN_PROGRESS' || o.status === 'BLOCKED') {
      const stage = o.currentStageName || 'Unknown';
      stageCounts[stage] = (stageCounts[stage] || 0) + 1;
    }
  });

  // Department capacities
  const depts = [
    { name: 'Fabric Cutting & Prep', load: 2, limit: 5, color: 'bg-indigo-600' },
    { name: 'Sublimation & Printing', load: 1, limit: 4, color: 'bg-indigo-600' },
    { name: 'Stitching & Assembly', load: 4, limit: 6, color: 'bg-amber-600' },
    { name: 'Quality Inspection (QC)', load: 3, limit: 4, color: 'bg-indigo-600' },
    { name: 'Packing & Dispatch', load: 0, limit: 8, color: 'bg-slate-300' }
  ];

  return (
    <div className="flex-1 bg-slate-50 p-8 overflow-y-auto font-sans text-slate-900">
      
      {/* Executive Welcome Bar */}
      <div className="flex justify-between items-center mb-8 shrink-0">
        <div>
          <h2 className="text-xl font-bold text-slate-900 tracking-tight uppercase">Executive Cockpit</h2>
          <p className="text-xs text-slate-500 mt-0.5">Apex Textiles real-time floor load & procurement controls</p>
        </div>

        <div className="flex items-center space-x-3">
          <button
            onClick={onTriggerShortage}
            className="px-3.5 py-1.5 bg-rose-50 border border-rose-250 text-rose-600 text-xs font-bold rounded-lg transition hover:bg-rose-100 flex items-center space-x-1.5"
          >
            <Flame className="w-3.5 h-3.5 text-rose-500" />
            <span>Simulate Material Shortage</span>
          </button>
        </div>
      </div>

      {/* Corporate KPI Scorecards */}
      <div className="grid grid-cols-4 gap-6 mb-8">
        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Active Batches</span>
            <Activity className="w-4 h-4 text-indigo-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">{activeOrders}</p>
          <div className="flex items-center space-x-1 mt-2 text-[10px] text-emerald-600 font-bold">
            <TrendingUp className="w-3 h-3" />
            <span>+12.4% floor throughput</span>
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">QC Holds & Reworks</span>
            <AlertTriangle className="w-4 h-4 text-rose-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">{blockedOrders}</p>
          <div className="flex items-center space-x-1 mt-2 text-[10px] text-rose-500 font-bold">
            <span>Requires bypass override</span>
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Avg SLA Compliance</span>
            <Clock className="w-4 h-4 text-amber-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">96.8%</p>
          <div className="flex items-center space-x-1 mt-2 text-[10px] text-slate-500">
            <span>SLA Target Limit: 92.0%</span>
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Total Output Valuation</span>
            <CheckCircle2 className="w-4 h-4 text-emerald-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">${totalContractVal.toLocaleString()}</p>
          <div className="flex items-center space-x-1 mt-2 text-[10px] text-emerald-600 font-bold">
            <span>Active contract assets</span>
          </div>
        </div>
      </div>

      {/* Main visual panel layout grid */}
      <div className="grid grid-cols-3 gap-8">
        
        {/* Left Column: Workloads & Bottlenecks */}
        <div className="col-span-2 space-y-6">
          
          {/* Visual stage counts bar chart */}
          <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm">
            <div className="flex items-center space-x-2 mb-6">
              <Gauge className="w-4 h-4 text-indigo-600" />
              <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider">Active Pipeline Queue</h3>
            </div>

            <div className="space-y-4">
              {Object.keys(stageCounts).length === 0 ? (
                <p className="text-xs text-slate-500 text-center py-8">No orders currently active in pipeline stages.</p>
              ) : (
                Object.entries(stageCounts).map(([stage, count]) => {
                  const percentage = Math.min(100, (count / activeOrders) * 100);
                  return (
                    <div key={stage} className="space-y-1.5 text-xs">
                      <div className="flex justify-between font-semibold">
                        <span className="text-slate-700">{stage}</span>
                        <span className="text-indigo-600 font-mono">{count} {count === 1 ? 'batch' : 'batches'}</span>
                      </div>
                      <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
                        <div 
                          className="h-full bg-indigo-600 rounded-full transition-all duration-500" 
                          style={{ width: `${percentage}%` }}
                        />
                      </div>
                    </div>
                  );
                })
              )}
            </div>

            {/* Bottleneck Warning panel */}
            <div className="mt-6 p-4 bg-amber-50 border border-amber-200 rounded-lg flex items-start space-x-3 text-xs">
              <AlertTriangle className="w-4 h-4 text-amber-600 shrink-0 mt-0.5" />
              <div>
                <h4 className="font-bold text-amber-700 uppercase tracking-wide">Queue Lag Bottleneck Alert</h4>
                <p className="text-slate-500 mt-1">
                  The <span className="text-amber-700 font-semibold">Stitching & Assembly</span> stage is carrying 50% of the active floor capacity. Sourcing teams should delay trim releases.
                </p>
              </div>
            </div>
          </div>

          {/* Recent active orders table */}
          <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm">
            <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-4 flex items-center space-x-2">
              <Layers className="w-4 h-4 text-indigo-600" />
              <span>Recent Production Runs</span>
            </h3>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs border-collapse">
                <thead>
                  <tr className="border-b border-slate-200 text-slate-500 font-semibold">
                    <th className="pb-3 font-bold uppercase tracking-wider">Ref ID</th>
                    <th className="pb-3 font-bold uppercase tracking-wider">Product Description</th>
                    <th className="pb-3 font-bold uppercase tracking-wider text-right">Quantity</th>
                    <th className="pb-3 font-bold uppercase tracking-wider">Current Stage</th>
                    <th className="pb-3 font-bold uppercase tracking-wider text-right">Contract Val</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 text-slate-700">
                  {orders.slice(0, 3).map(o => (
                    <tr key={o.id} className="hover:bg-slate-50 transition">
                      <td className="py-3 font-mono font-semibold text-indigo-600">{o.orderNumber}</td>
                      <td className="py-3">{o.productName}</td>
                      <td className="py-3 text-right font-mono font-semibold">{o.quantity} pcs</td>
                      <td className="py-3">
                        <span className="text-[10px] font-bold text-slate-600 bg-slate-100 border border-slate-200 px-2 py-0.5 rounded-full uppercase">
                          {o.currentStageName || 'Finished QC'}
                        </span>
                      </td>
                      <td className="py-3 text-right font-mono font-semibold">${o.totalContractValue?.toLocaleString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* Right Column: Departments & Quick Controls */}
        <div className="space-y-6">
          {/* Department Loads */}
          <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm">
            <div className="flex items-center space-x-2 mb-6">
              <Users className="w-4 h-4 text-indigo-600" />
              <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider">Department Loads</h3>
            </div>

            <div className="space-y-4 text-xs">
              {depts.map((d) => {
                const pct = (d.load / d.limit) * 100;
                return (
                  <div key={d.name} className="space-y-1.5">
                    <div className="flex justify-between">
                      <span className="text-slate-700 font-semibold">{d.name}</span>
                      <span className="text-slate-400 font-mono">{d.load}/{d.limit} HP</span>
                    </div>
                    <div className="h-1.5 w-full bg-slate-100 rounded-full overflow-hidden">
                      <div 
                        className={`h-full rounded-full transition-all ${d.color}`} 
                        style={{ width: `${pct}%` }}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Quick Actions Panel */}
          <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm text-xs">
            <div className="flex items-center space-x-2 mb-4">
              <Sparkles className="w-4 h-4 text-indigo-600" />
              <h3 className="font-bold text-slate-700 uppercase tracking-wider">Quick Controls</h3>
            </div>

            <div className="space-y-2">
              <div className="p-3 bg-slate-50 border border-slate-200 rounded-lg flex items-center justify-between">
                <div>
                  <span className="font-bold text-slate-800 block">Sourcing Intake</span>
                  <span className="text-[10px] text-slate-500">Record a new GRN material receipt</span>
                </div>
                <Plus className="w-4 h-4 text-slate-400" />
              </div>

              <div className="p-3 bg-slate-50 border border-slate-200 rounded-lg flex items-center justify-between">
                <div>
                  <span className="font-bold text-slate-800 block">Physical Stocktake</span>
                  <span className="text-[10px] text-slate-500">Initiate an instantaneous audit snapshot</span>
                </div>
                <ArrowUpRight className="w-4 h-4 text-slate-400" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
