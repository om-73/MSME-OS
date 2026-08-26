import React, { useState } from 'react';
import { 
  Warehouse, 
  ArrowRightLeft, 
  Scale, 
  Search, 
  QrCode,
  Sparkles,
  ClipboardList
} from 'lucide-react';
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

interface WarehouseManagerProps {
  inventory: any[];
  onRefresh: () => void;
  onUpdateInventoryItem: (item: any) => void;
  onAddMovement: (m: any) => void;
}

export default function WarehouseManager({ inventory, onRefresh, onUpdateInventoryItem, onAddMovement }: WarehouseManagerProps) {
  const [showTransferModal, setShowTransferModal] = useState(false);
  const [showAdjustModal, setShowAdjustModal] = useState(false);

  // Forms
  const [selectedMaterialId, setSelectedMaterialId] = useState(inventory[0]?.id || '');
  const [transferQty, setTransferQty] = useState(20.0);
  const [targetLocation, setTargetLocation] = useState('Dyeing Block B');

  const [adjustQty, setAdjustQty] = useState(5.0);
  const [adjustType, setAdjustType] = useState('ADJUSTMENT'); // ADJUSTMENT, WASTE, DAMAGE
  const [remarks, setRemarks] = useState('');

  const [statusMsg, setStatusMsg] = useState('');

  const handleTransfer = (e: React.FormEvent) => {
    e.preventDefault();
    const material = inventory.find(i => i.id === selectedMaterialId);
    if (!material) return;

    if (material.availableStock < transferQty) {
      setStatusMsg('Error: Transfer quantity exceeds available stock.');
      setTimeout(() => setStatusMsg(''), 3000);
      return;
    }

    const updated = {
      ...material,
      rackLocation: targetLocation
    };
    onUpdateInventoryItem(updated);

    onAddMovement({
      id: Math.random().toString(),
      inventoryItemId: material.id,
      inventoryItemName: material.name,
      inventoryItemCode: material.code,
      movementType: 'TRANSFER',
      quantity: transferQty,
      fromWarehouse: material.warehouseName,
      toWarehouse: targetLocation,
      remarks: 'Warehouse internal transfer',
      timestamp: new Date().toISOString()
    });

    setStatusMsg(`Transferred ${transferQty} ${material.unit} to ${targetLocation}.`);
    setShowTransferModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleAdjust = (e: React.FormEvent) => {
    e.preventDefault();
    const material = inventory.find(i => i.id === selectedMaterialId);
    if (!material) return;

    const finalQty = adjustType === 'ADJUSTMENT' 
      ? material.currentStock + adjustQty 
      : Math.max(0, material.currentStock - adjustQty);

    const updated = {
      ...material,
      currentStock: finalQty,
      availableStock: finalQty - material.reservedStock
    };
    onUpdateInventoryItem(updated);

    onAddMovement({
      id: Math.random().toString(),
      inventoryItemId: material.id,
      inventoryItemName: material.name,
      inventoryItemCode: material.code,
      movementType: adjustType,
      quantity: adjustQty,
      remarks: remarks || 'Cycle counting adjustment',
      timestamp: new Date().toISOString()
    });

    setStatusMsg(`Stock level adjusted for SKU: ${material.code}.`);
    setShowAdjustModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div>
          <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Warehouse Management</h2>
          <p className="text-xs text-slate-550 mt-0.5">Control warehouse locations, map shelf bins, and transfer stock units</p>
        </div>

        <div className="flex space-x-2">
          <Button variant="outline" onClick={() => setShowAdjustModal(true)}>
            <Scale className="w-4 h-4" />
            <span>Cycle Count Adjustment</span>
          </Button>
          <Button variant="primary" onClick={() => setShowTransferModal(true)}>
            <ArrowRightLeft className="w-4 h-4" />
            <span>Internal Transfer</span>
          </Button>
        </div>
      </div>

      {statusMsg && (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-750 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      )}

      {/* Structured Split columns grid */}
      <div className="flex-1 grid grid-cols-3 gap-8 overflow-hidden">
        
        {/* Warehouse bins and location capacities */}
        <div className="col-span-2 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
            <Warehouse className="w-4 h-4 text-indigo-600" />
            <span>Location Bins</span>
          </h3>

          <div className="flex-1 overflow-y-auto pr-2">
            <Table>
              <TableHead>
                <th className="pb-3">SKU Code</th>
                <th className="pb-3">Name</th>
                <th className="pb-3">Store Room Location</th>
                <th className="pb-3">Rack Bin</th>
                <th className="pb-3 text-right">Current Stock</th>
              </TableHead>
              <TableBody>
                {inventory.map(item => (
                  <tr key={item.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                    <td className="py-3 font-mono font-bold text-indigo-600">{item.code}</td>
                    <td className="py-3 font-semibold text-slate-800">{item.name}</td>
                    <td className="py-3">{item.warehouseName || 'Main Raw Warehouse'}</td>
                    <td className="py-3 font-mono text-slate-550">{item.rackLocation || 'Rack A-0'}</td>
                    <td className="py-3 text-right font-mono font-bold">{item.currentStock} {item.unit}</td>
                  </tr>
                ))}
              </TableBody>
            </Table>
          </div>
        </div>

        {/* Audit Log / Cycle actions side-card */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
            <ClipboardList className="w-4 h-4 text-indigo-600" />
            <span>Cycle Count Statistics</span>
          </h3>

          <div className="space-y-4 text-xs">
            <div className="p-4 bg-slate-50 border border-slate-200 rounded-lg">
              <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Total Location Bins</span>
              <span className="text-xl font-bold text-slate-800 font-mono">14 active racks</span>
            </div>

            <div className="p-4 bg-slate-50 border border-slate-200 rounded-lg">
              <span className="text-[10px] text-slate-455 uppercase font-bold block mb-1">Cycle Variance rate</span>
              <span className="text-xl font-bold text-slate-800 font-mono">0.05% margin</span>
            </div>

            <p className="text-[11px] text-slate-450 leading-relaxed">
              Standard warehouse cycle count counts should be performed weekly to verify theoretical book stocks.
            </p>
          </div>
        </div>
      </div>

      {/* 1. Internal Transfer Dialog */}
      <Dialog isOpen={showTransferModal} onClose={() => setShowTransferModal(false)} title="Warehouse Stock Transfer">
        <form onSubmit={handleTransfer} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Select Material SKU</label>
            <Select value={selectedMaterialId} onChange={(e) => setSelectedMaterialId(e.target.value)}>
              {inventory.map(item => (
                <option key={item.id} value={item.id}>{item.code} - {item.name}</option>
              ))}
            </Select>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Target Warehouse / Rack Bin</label>
            <Input
              type="text"
              value={targetLocation}
              onChange={(e) => setTargetLocation(e.target.value)}
              placeholder="e.g. Rack C-4"
              required
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Transfer Quantity</label>
            <Input
              type="number"
              value={transferQty}
              onChange={(e) => setTransferQty(parseFloat(e.target.value) || 0)}
              min="0.1"
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowTransferModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Confirm Transfer
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Stock Adjustment Dialog */}
      <Dialog isOpen={showAdjustModal} onClose={() => setShowAdjustModal(false)} title="Cycle Counting Stock Adjustment">
        <form onSubmit={handleAdjust} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Select Material SKU</label>
            <Select value={selectedMaterialId} onChange={(e) => setSelectedMaterialId(e.target.value)}>
              {inventory.map(item => (
                <option key={item.id} value={item.id}>{item.code} - {item.name}</option>
              ))}
            </Select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Adjustment Type</label>
              <Select value={adjustType} onChange={(e) => setAdjustType(e.target.value)}>
                <option value="ADJUSTMENT">Reconcile Stock (Add)</option>
                <option value="WASTE">Scrap (Deduct)</option>
                <option value="DAMAGED">Damaged (Deduct)</option>
              </Select>
            </div>
            <div>
              <label className="block text-slate-505 mb-1 font-semibold">Adjustment Quantity</label>
              <Input
                type="number"
                value={adjustQty}
                onChange={(e) => setAdjustQty(parseFloat(e.target.value) || 0)}
                min="0.1"
              />
            </div>
          </div>

          <div>
            <label className="block text-slate-505 mb-1 font-semibold">Remarks</label>
            <Input
              type="text"
              value={remarks}
              onChange={(e) => setRemarks(e.target.value)}
              placeholder="e.g. Found on floor audit..."
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowAdjustModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Apply Adjustment
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
