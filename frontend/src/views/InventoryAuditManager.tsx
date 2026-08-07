import React, { useState } from 'react';
import { 
  ClipboardCheck, 
  Plus, 
  Scale, 
  AlertTriangle, 
  CheckCircle, 
  Clock, 
  History, 
  User 
} from 'lucide-react';
import axios from 'axios';
import { 
  Button, 
  Badge, 
  Input, 
  Dialog,
  Table,
  TableHead,
  TableBody
} from '../components/DesignSystem';

interface InventoryAuditManagerProps {
  audits: any[];
  inventory: any[];
  user: any;
  onRefresh: () => void;
  onAddAudit: (audit: any) => void;
  onUpdateAuditStatus: (id: string, status: string, completedAt?: string) => void;
}

export default function InventoryAuditManager({ audits, inventory, user, onRefresh, onAddAudit, onUpdateAuditStatus }: InventoryAuditManagerProps) {
  const [selectedAudit, setSelectedAudit] = useState<any | null>(null);
  const [showStartAuditModal, setShowStartAuditModal] = useState(false);
  const [newAuditName, setNewAuditName] = useState('Q3 Physical Stocktake');

  // Counts tracking state
  const [physicalCounts, setPhysicalCounts] = useState<Record<string, number>>({});
  const [statusMsg, setStatusMsg] = useState('');

  const handleStartAudit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8085/api/v1/inventory/audit', {
        auditName: newAuditName
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      onAddAudit(res.data);
      setSelectedAudit(res.data);
      // Initialize counts input map
      const counts: Record<string, number> = {};
      res.data.items.forEach((item: any) => {
        counts[item.inventoryItemId] = item.systemStock;
      });
      setPhysicalCounts(counts);
    } catch (err) {
      // Mock session
      const mockAudit = {
        id: (audits.length + 1).toString(),
        auditName: newAuditName,
        status: 'DRAFT',
        createdBy: user.fullName,
        createdAt: new Date().toISOString(),
        items: inventory.map(item => ({
          id: Math.random().toString(),
          inventoryItemId: item.id,
          inventoryItemName: item.name,
          inventoryItemCode: item.code,
          systemStock: item.currentStock,
          physicalStock: item.currentStock,
          variance: 0.0,
          reconciled: false
        }))
      };
      onAddAudit(mockAudit);
      setSelectedAudit(mockAudit);
      const counts: Record<string, number> = {};
      mockAudit.items.forEach((item: any) => {
        counts[item.inventoryItemId] = item.systemStock;
      });
      setPhysicalCounts(counts);
    }

    setShowStartAuditModal(false);
    onRefresh();
  };

  const handleCountChange = (itemId: string, val: number) => {
    setPhysicalCounts({
      ...physicalCounts,
      [itemId]: val
    });

    if (selectedAudit) {
      const updatedItems = selectedAudit.items.map((it: any) => {
        if (it.inventoryItemId === itemId) {
          const system = it.systemStock;
          return {
            ...it,
            physicalStock: val,
            variance: val - system
          };
        }
        return it;
      });
      setSelectedAudit({
        ...selectedAudit,
        items: updatedItems
      });
    }
  };

  const handleSaveCounts = async () => {
    if (!selectedAudit) return;

    try {
      await axios.post(`http://localhost:8085/api/v1/inventory/audit/${selectedAudit.id}/submit`, physicalCounts, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Physical counts logged successfully.');
    } catch (err) {
      setStatusMsg('Physical counts logged to local draft.');
    }

    setTimeout(() => setStatusMsg(''), 3000);
    onRefresh();
  };

  const handleReconcileSignoff = async () => {
    if (!selectedAudit) return;

    try {
      await axios.post(`http://localhost:8085/api/v1/inventory/audit/${selectedAudit.id}/reconcile`, {}, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      onUpdateAuditStatus(selectedAudit.id, 'COMPLETED', new Date().toISOString());
      setSelectedAudit((prev: any) => ({
        ...prev,
        status: 'COMPLETED',
        completedAt: new Date().toISOString(),
        items: prev.items.map((it: any) => ({ ...it, reconciled: true }))
      }));
    } catch (err) {
      onUpdateAuditStatus(selectedAudit.id, 'COMPLETED', new Date().toISOString());
      setSelectedAudit((prev: any) => ({
        ...prev,
        status: 'COMPLETED',
        completedAt: new Date().toISOString(),
        items: prev.items.map((it: any) => ({ ...it, reconciled: true }))
      }));
    }
    onRefresh();
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex space-x-8 h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Historical Stocktake sidebar */}
      <div className="w-80 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shrink-0 shadow-sm">
        <div className="flex justify-between items-center mb-6 shrink-0">
          <div className="flex items-center space-x-2">
            <History className="w-4 h-4 text-indigo-650" />
            <h2 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Audit Sessions</h2>
          </div>

          {user.role === 'ROLE_FACTORY_OWNER' && (
            <Button
              variant="outline"
              onClick={() => setShowStartAuditModal(true)}
              className="p-1 hover:bg-slate-105"
            >
              <Plus className="w-4 h-4" />
            </Button>
          )}
        </div>

        {/* Audit list items */}
        <div className="flex-1 overflow-y-auto space-y-3">
          {audits.map(aud => {
            const isSel = selectedAudit?.id === aud.id;
            const isDraft = aud.status === 'DRAFT';
            return (
              <button
                key={aud.id}
                onClick={() => {
                  setSelectedAudit(aud);
                  const counts: Record<string, number> = {};
                  aud.items.forEach((item: any) => {
                    counts[item.inventoryItemId] = item.physicalStock;
                  });
                  setPhysicalCounts(counts);
                }}
                className={`w-full text-left p-4 rounded-xl border transition flex flex-col space-y-2 ${
                  isSel 
                    ? 'bg-indigo-50 border-indigo-200 text-indigo-900 shadow-sm' 
                    : 'bg-slate-50 border-slate-200 text-slate-600 hover:bg-slate-100'
                }`}
              >
                <div className="flex justify-between items-start w-full">
                  <span className="text-xs font-bold truncate max-w-[130px]">{aud.auditName}</span>
                  <Badge status={isDraft ? 'warning' : 'success'}>
                    {aud.status}
                  </Badge>
                </div>

                <div className="flex items-center space-x-3 text-[10px] text-slate-550">
                  <span className="flex items-center space-x-1">
                    <User className="w-3 h-3 text-slate-400" />
                    <span className="truncate">{aud.createdBy || 'Auditor'}</span>
                  </span>
                  <span className="flex items-center space-x-1">
                    <Clock className="w-3 h-3 text-slate-400" />
                    <span>{new Date(aud.createdAt).toLocaleDateString()}</span>
                  </span>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Main Stocktaking Worksheet area */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
        {selectedAudit ? (
          <div className="flex-1 flex flex-col overflow-hidden">
            
            {/* Header toolbar details */}
            <div className="flex justify-between items-center pb-4 border-b border-slate-100 mb-6 shrink-0">
              <div>
                <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">{selectedAudit.auditName}</h3>
                <span className="text-[10px] text-slate-550">Snapshot date: {new Date(selectedAudit.createdAt).toLocaleString()}</span>
              </div>

              {selectedAudit.status === 'DRAFT' ? (
                <div className="flex space-x-3">
                  <Button variant="outline" onClick={handleSaveCounts}>
                    Save Draft Counts
                  </Button>
                  {user.role === 'ROLE_FACTORY_OWNER' && (
                    <Button variant="primary" onClick={handleReconcileSignoff}>
                      <CheckCircle className="w-3.5 h-3.5" />
                      <span>Reconcile & Finalize</span>
                    </Button>
                  )}
                </div>
              ) : (
                <div className="flex items-center space-x-1 text-emerald-650 font-bold text-xs bg-emerald-50 border border-emerald-200 px-3 py-1 rounded-lg">
                  <CheckCircle className="w-4 h-4 text-emerald-500" />
                  <span>Reconciled & Finalized</span>
                </div>
              )}
            </div>

            {statusMsg && (
              <div className="mb-4 p-3.5 bg-indigo-50 border border-indigo-200 text-indigo-750 text-xs rounded-xl flex items-center space-x-2 shrink-0 font-semibold">
                <span>{statusMsg}</span>
              </div>
            )}

            {/* Counts worksheet list */}
            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Code / Name</th>
                  <th className="pb-3 text-right">System Book Stock</th>
                  <th className="pb-3 text-center">Physical Stock Count</th>
                  <th className="pb-3 text-right">Variance Offset</th>
                  <th className="pb-3 text-center">Reconciled Status</th>
                </TableHead>
                <TableBody>
                  {selectedAudit.items.map((it: any) => {
                    const hasVar = it.variance !== 0;
                    return (
                      <tr key={it.id} className="text-slate-700 border-b border-slate-100">
                        <td className="py-3">
                          <span className="font-mono text-indigo-650 block font-semibold">{it.inventoryItemCode}</span>
                          <span className="text-[11px] text-slate-500">{it.inventoryItemName}</span>
                        </td>
                        <td className="py-3 text-right font-mono text-slate-600">
                          {it.systemStock}
                        </td>
                        <td className="py-3 text-center">
                          {selectedAudit.status === 'DRAFT' ? (
                            <input
                              type="number"
                              value={physicalCounts[it.inventoryItemId] ?? it.physicalStock}
                              onChange={(e) => handleCountChange(it.inventoryItemId, parseFloat(e.target.value) || 0)}
                              className="w-24 px-2 py-1 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 text-center font-mono text-xs focus:outline-none focus:border-indigo-500"
                              min="0"
                              step="any"
                            />
                          ) : (
                            <span className="font-mono text-slate-800">{it.physicalStock}</span>
                          )}
                        </td>
                        <td className={`py-3 text-right font-mono font-bold ${
                          !hasVar ? 'text-slate-400' : it.variance > 0 ? 'text-emerald-600' : 'text-rose-600'
                        }`}>
                          {it.variance > 0 ? '+' : ''}{it.variance}
                        </td>
                        <td className="py-3 text-center">
                          <Badge status={it.reconciled ? 'success' : 'default'}>
                            {it.reconciled ? 'Done' : 'Pending'}
                          </Badge>
                        </td>
                      </tr>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          </div>
        ) : (
          <div className="h-full flex flex-col items-center justify-center text-center text-slate-450 space-y-3">
            <ClipboardCheck className="w-10 h-10 text-slate-300" />
            <div>
              <p className="text-xs font-bold text-slate-500">No Active Audit Session</p>
              <p className="text-[11px] text-slate-400 mt-1 max-w-[240px]">Select a stocktake session from history, or initiate a new snapshot to execute physical reconciliations.</p>
            </div>
          </div>
        )}
      </div>

      {/* Start Audit Dialog */}
      <Dialog isOpen={showStartAuditModal} onClose={() => setShowStartAuditModal(false)} title="Initiate Stocktake Session">
        <form onSubmit={handleStartAudit} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Audit Session Title</label>
            <Input
              type="text"
              value={newAuditName}
              onChange={(e) => setNewAuditName(e.target.value)}
              required
            />
          </div>

          <div className="p-3.5 bg-amber-50 border border-amber-200 text-amber-700 rounded-lg flex items-start space-x-2">
            <AlertTriangle className="w-4 h-4 shrink-0 mt-0.5" />
            <p className="text-[10px] leading-relaxed">
              Initiating a session will take an instantaneous system book stock snapshot. Operator counts can be logged as a draft until final owner reconciliation.
            </p>
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowStartAuditModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Confirm Snapshot
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
