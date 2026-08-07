import React from 'react';
import { 
  Boxes, 
  TrendingUp, 
  DollarSign, 
  AlertTriangle, 
  Warehouse, 
  BellRing, 
  ClipboardCheck, 
  ArrowUpRight 
} from 'lucide-react';

interface InventoryDashboardProps {
  inventory: any[];
}

export default function InventoryDashboard({ inventory }: InventoryDashboardProps) {
  const totalItems = inventory.length;
  const totalValuation = inventory.reduce((sum, item) => sum + ((item.currentStock || 0) * (item.purchasePrice || 0)), 0);
  const lowStockItems = inventory.filter(item => item.isLowStock);
  const clientSuppliedCount = inventory.filter(item => item.category === 'CLIENT_SUPPLIED').length;

  const categoryCounts: Record<string, number> = {};
  inventory.forEach(item => {
    categoryCounts[item.category] = (categoryCounts[item.category] || 0) + 1;
  });

  const getCategoryLabel = (cat: string) => {
    switch (cat) {
      case 'RAW_MATERIAL': return 'Raw Materials';
      case 'FINISHED_GOODS': return 'Finished Goods';
      case 'WIP': return 'Work In Progress';
      case 'CLIENT_SUPPLIED': return 'Client-Supplied';
      case 'SCRAP': return 'Scrap Inventory';
      case 'REJECTED': return 'Rejected Materials';
      default: return cat;
    }
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 overflow-y-auto font-sans text-slate-900">
      
      {/* Page Header */}
      <div className="flex justify-between items-center mb-8 shrink-0">
        <div>
          <h2 className="text-xl font-bold text-slate-900 tracking-tight uppercase">Inventory Analytics</h2>
          <p className="text-xs text-slate-500 mt-0.5">Corporate inventory balances, warehouse load, and low stock alarms</p>
        </div>
      </div>

      {/* KPI Stats Scorecards */}
      <div className="grid grid-cols-4 gap-6 mb-8">
        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">SKUs Tracked</span>
            <Boxes className="w-4 h-4 text-indigo-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">{totalItems}</p>
          <div className="text-[10px] text-slate-500 mt-2">
            Active items in warehouse
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Asset Valuation</span>
            <DollarSign className="w-4 h-4 text-emerald-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">${totalValuation.toLocaleString(undefined, { minimumFractionDigits: 2 })}</p>
          <div className="flex items-center space-x-1.5 mt-2 text-[10px] text-emerald-600 font-bold">
            <TrendingUp className="w-3.5 h-3.5" />
            <span>Valued at purchase cost</span>
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Sourcing Shortages</span>
            <AlertTriangle className="w-4 h-4 text-rose-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">{lowStockItems.length}</p>
          <div className="text-[10px] text-rose-500 font-bold mt-2">
            Below safety compliance limits
          </div>
        </div>

        <div className="bg-white border border-slate-200 rounded-xl p-5 shadow-sm">
          <div className="flex justify-between items-start mb-4">
            <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Client Stock</span>
            <Warehouse className="w-4 h-4 text-cyan-600" />
          </div>
          <p className="text-2xl font-bold text-slate-900 font-mono">{clientSuppliedCount}</p>
          <div className="text-[10px] text-slate-500 mt-2">
            Isolated customer assets
          </div>
        </div>
      </div>

      {/* Structured Split Grid */}
      <div className="grid grid-cols-3 gap-8">
        
        {/* Urgent Threshold Alerts list */}
        <div className="col-span-2 bg-white border border-slate-200 rounded-xl p-6 shadow-sm flex flex-col justify-between">
          <div>
            <div className="flex items-center space-x-2 mb-6">
              <BellRing className="w-4 h-4 text-indigo-650" />
              <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider">Material Shortage Flags</h3>
            </div>

            <div className="space-y-3 text-xs">
              {lowStockItems.length === 0 ? (
                <div className="p-4 bg-emerald-50 border border-emerald-250 text-emerald-700 rounded-lg flex items-center space-x-2">
                  <ClipboardCheck className="w-4 h-4 text-emerald-600" />
                  <span>All material levels currently exceed safety limits.</span>
                </div>
              ) : (
                lowStockItems.map(item => (
                  <div key={item.id} className="p-3.5 bg-rose-50 border border-rose-200 rounded-lg flex justify-between items-center">
                    <div>
                      <span className="font-bold text-slate-800 block">{item.name}</span>
                      <span className="text-[10px] text-slate-400">Bin: {item.warehouseName || 'General store'} ({item.rackLocation})</span>
                    </div>

                    <div className="text-right">
                      <span className="text-rose-600 font-bold block">{item.currentStock} {item.unit}</span>
                      <span className="text-[10px] text-slate-400">Limit: {item.safetyStock} {item.unit}</span>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>

        {/* Category distribution splits card */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 shadow-sm">
          <div className="flex items-center space-x-2 mb-6">
            <Boxes className="w-4 h-4 text-indigo-600" />
            <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider">Categories Breakdown</h3>
          </div>

          <div className="space-y-4 text-xs">
            {Object.entries(categoryCounts).map(([cat, count]) => {
              const pct = (count / totalItems) * 100;
              return (
                <div key={cat} className="space-y-1">
                  <div className="flex justify-between font-semibold">
                    <span className="text-slate-600">{getCategoryLabel(cat)}</span>
                    <span className="text-indigo-600 font-mono">{count} SKUs</span>
                  </div>
                  <div className="h-1.5 w-full bg-slate-100 rounded-full overflow-hidden">
                    <div 
                      className="h-full bg-indigo-600 rounded-full transition-all duration-300"
                      style={{ width: `${pct}%` }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}
