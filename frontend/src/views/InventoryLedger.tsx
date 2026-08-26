import React, { useState } from 'react';
import { 
  Plus, 
  Search, 
  ArrowDownLeft, 
  ArrowUpRight, 
  Clock, 
  Warehouse, 
  QrCode, 
  User 
} from 'lucide-react';
import { api } from '../api/client';
import { 
  Button, 
  Card, 
  Badge, 
  Input, 
  Select, 
  Dialog,
  Table,
  TableHead,
  TableBody
} from '../components/DesignSystem';

interface InventoryLedgerProps {
  inventory: any[];
  ledger: any[];
  brands: any[];
  user: any;
  onRefresh: () => void;
  onUpdateInventoryItem: (item: any) => void;
  onAddMovement: (m: any) => void;
}

export default function InventoryLedger({ inventory, ledger, brands, user, onRefresh, onUpdateInventoryItem, onAddMovement }: InventoryLedgerProps) {
  const [activeSubTab, setActiveSubTab] = useState<'master' | 'ledger'>('master');
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('ALL');

  // Modal triggers
  const [showReceiveModal, setShowReceiveModal] = useState(false);
  const [showIssueModal, setShowIssueModal] = useState(false);
  const [selectedItemForIssue, setSelectedItemForIssue] = useState<any | null>(null);
  
  // Barcode / QR display modal
  const [showBarcodeModal, setShowBarcodeModal] = useState<any | null>(null);

  // Form states
  const [receiveForm, setReceiveForm] = useState({
    name: 'Heavy Cotton Stitching Thread',
    code: 'RM-TH-02',
    sku: 'SKU-RM-TH-02',
    barcode: '',
    category: 'RAW_MATERIAL',
    supplierName: 'Trims & Thread Corp',
    unit: 'spools',
    purchasePrice: 2.20,
    currentStock: 150.0,
    warehouseName: 'Main Raw Warehouse',
    rackLocation: 'Rack B-1',
    batchNumber: 'B-THREAD-112',
    safetyStock: 30.0,
    minStockAlert: 10.0,
    maxStockAlert: 1000.0,
    clientBrandId: ''
  });

  const [issueQuantity, setIssueQuantity] = useState(10.0);
  const [issueRemarks, setIssueRemarks] = useState('');
  const [issueOrderId, setIssueOrderId] = useState('');

  const handleReceiveStock = async (e: React.FormEvent) => {
    e.preventDefault();
    const barcodeVal = receiveForm.barcode || 'BC-' + Date.now();
    const payload = {
      ...receiveForm,
      barcode: barcodeVal
    };

    try {
      const res = await api.post('/inventory/receive', payload);
      if (res.data) {
        onUpdateInventoryItem(res.data);
        onAddMovement({
          id: Math.random().toString(),
          inventoryItemId: res.data.id,
          inventoryItemName: res.data.name,
          inventoryItemCode: res.data.code,
          movementType: 'RECEIVE',
          quantity: payload.currentStock,
          toWarehouse: payload.warehouseName,
          operatorName: user.fullName,
          remarks: 'Goods Receipt Note (GRN) logged via Inventory Master',
          timestamp: new Date().toISOString()
        });
      }
    } catch (err) {
      console.error('Failed to receive inventory in database:', err);
    }

    setShowReceiveModal(false);
    onRefresh();
  };

  const handleIssueStock = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedItemForIssue) return;

    try {
      await api.post('/inventory/adjust', {
        itemId: selectedItemForIssue.id,
        quantity: issueQuantity,
        movementType: 'ISSUE',
        remarks: issueRemarks || `Issued for production order ${issueOrderId}`
      });

      const updatedItem = {
        ...selectedItemForIssue,
        currentStock: Math.max(0, selectedItemForIssue.currentStock - issueQuantity),
        availableStock: Math.max(0, (selectedItemForIssue.availableStock || selectedItemForIssue.currentStock) - issueQuantity)
      };
      onUpdateInventoryItem(updatedItem);
      onAddMovement({
        id: Math.random().toString(),
        inventoryItemId: updatedItem.id,
        inventoryItemName: updatedItem.name,
        inventoryItemCode: updatedItem.code,
        movementType: 'ISSUE',
        quantity: issueQuantity,
        fromWarehouse: updatedItem.warehouseName,
        orderId: issueOrderId,
        operatorName: user.fullName,
        remarks: issueRemarks || `Issued for production order ${issueOrderId}`,
        timestamp: new Date().toISOString()
      });
    } catch (err) {
      console.error('Failed to issue inventory in database:', err);
    }

    setShowIssueModal(false);
    setSelectedItemForIssue(null);
    setIssueRemarks('');
    onRefresh();
  };

  const getCategoryLabel = (cat: string) => {
    switch (cat) {
      case 'RAW_MATERIAL': return 'Raw Material';
      case 'FINISHED_GOODS': return 'Finished Goods';
      case 'WIP': return 'Work In Progress';
      case 'CLIENT_SUPPLIED': return 'Client-Supplied';
      case 'SCRAP': return 'Scrap Inventory';
      case 'REJECTED': return 'Rejected';
      default: return cat;
    }
  };

  const filteredItems = inventory.filter(item => {
    const matchesSearch = item.name.toLowerCase().includes(searchTerm.toLowerCase()) || item.code.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCat = categoryFilter === 'ALL' || item.category === categoryFilter;
    return matchesSearch && matchesCat;
  });

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Page Header toolbar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Inventory Master & Ledger</h2>
            <p className="text-xs text-slate-500 mt-0.5">Manage material quantities, bins, and GRN intakes</p>
          </div>
          
          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg">
            <button
              onClick={() => setActiveSubTab('master')}
              className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                activeSubTab === 'master' ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              Stock Master
            </button>
            <button
              onClick={() => setActiveSubTab('ledger')}
              className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                activeSubTab === 'ledger' ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              Movement Ledger
            </button>
          </div>
        </div>

        <Button variant="primary" onClick={() => setShowReceiveModal(true)}>
          <Plus className="w-4 h-4" />
          <span>Goods Receipt (GRN)</span>
        </Button>
      </div>

      {/* Filters bar */}
      {activeSubTab === 'master' && (
        <div className="mb-6 flex space-x-4 shrink-0 text-xs">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-2.5 w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Search by material code or name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 bg-white border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
            />
          </div>

          <Select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            className="w-48 bg-white"
          >
            <option value="ALL">All Categories</option>
            <option value="RAW_MATERIAL">Raw Materials</option>
            <option value="FINISHED_GOODS">Finished Goods</option>
            <option value="CLIENT_SUPPLIED">Client-Supplied</option>
            <option value="SCRAP">Scrap</option>
          </Select>
        </div>
      )}

      {/* Main Table Grid */}
      <div className="flex-1 overflow-hidden bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm">
        {activeSubTab === 'master' ? (
          <div className="flex-1 overflow-y-auto pr-2">
            <Table>
              <TableHead>
                <th className="pb-3">Code / Name</th>
                <th className="pb-3">Category</th>
                <th className="pb-3">Bin Location</th>
                <th className="pb-3">Batch Number</th>
                <th className="pb-3 text-right">Available Stock</th>
                <th className="pb-3 text-right">Safety Limit</th>
                <th className="pb-3 text-center">Actions</th>
              </TableHead>
              <TableBody>
                {filteredItems.map(item => (
                  <tr key={item.id} className="hover:bg-slate-50/55 transition text-slate-705 border-b border-slate-100">
                    <td className="py-3 flex items-center space-x-3">
                      <div>
                        <span className="font-mono text-indigo-650 block font-semibold">{item.code}</span>
                        <span className="text-[11px] text-slate-500">{item.name}</span>
                      </div>
                      {item.isLowStock && (
                        <Badge status="error">Low Stock</Badge>
                      )}
                    </td>
                    <td className="py-3">
                      <Badge status="default">
                        {getCategoryLabel(item.category)}
                      </Badge>
                    </td>
                    <td className="py-3 text-slate-600">
                      <div className="flex items-center space-x-1">
                        <Warehouse className="w-3.5 h-3.5 text-slate-400" />
                        <span>{item.warehouseName || 'General store'} ({item.rackLocation})</span>
                      </div>
                    </td>
                    <td className="py-3 font-mono text-slate-500">{item.batchNumber}</td>
                    <td className="py-3 text-right font-semibold font-mono text-slate-900">
                      {item.availableStock} <span className="text-[10px] text-slate-400 font-normal">{item.unit}</span>
                    </td>
                    <td className="py-3 text-right font-mono text-slate-500">
                      {item.safetyStock} {item.unit}
                    </td>
                    <td className="py-3 text-center">
                      <div className="flex items-center justify-center space-x-2">
                        <Button 
                          variant="outline" 
                          onClick={() => {
                            setSelectedItemForIssue(item);
                            setShowIssueModal(true);
                          }}
                          className="!py-1"
                        >
                          Issue Item
                        </Button>
                        <button
                          onClick={() => setShowBarcodeModal(item)}
                          className="p-1.5 hover:bg-slate-100 text-slate-400 hover:text-indigo-600 rounded transition"
                          title="View Barcode Label"
                        >
                          <QrCode className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </TableBody>
            </Table>
          </div>
        ) : (
          /* TRANSACTION LEDGER */
          <div className="flex-1 overflow-y-auto pr-2">
            <div className="space-y-3">
              {ledger.map(log => {
                const isReceive = log.movementType === 'RECEIVE';
                return (
                  <div key={log.id} className="p-4 bg-slate-50 border border-slate-200 rounded-lg flex justify-between items-center text-xs">
                    <div className="flex items-center space-x-3">
                      <div className={`p-2 rounded-lg border shrink-0 ${
                        isReceive 
                          ? 'bg-emerald-50 border-emerald-200 text-emerald-600' 
                          : 'bg-rose-50 border-rose-200 text-rose-600'
                      }`}>
                        {isReceive ? <ArrowDownLeft className="w-4 h-4" /> : <ArrowUpRight className="w-4 h-4" />}
                      </div>
                      <div>
                        <div className="flex items-center space-x-2">
                          <span className="font-bold text-slate-850">{log.inventoryItemName}</span>
                          <span className="text-[10px] text-slate-400 font-mono">({log.inventoryItemCode})</span>
                        </div>
                        <p className="text-[11px] text-slate-500 mt-1">{log.remarks}</p>
                        
                        <div className="flex items-center space-x-3 mt-2 text-[10px] text-slate-400">
                          <span className="flex items-center space-x-1">
                            <User className="w-3 h-3 text-slate-400" />
                            <span>Keeper: {log.operatorName || 'System'}</span>
                          </span>
                          <span className="flex items-center space-x-1">
                            <Clock className="w-3 h-3 text-slate-400" />
                            <span>{new Date(log.timestamp).toLocaleDateString()} {new Date(log.timestamp).toLocaleTimeString()}</span>
                          </span>
                        </div>
                      </div>
                    </div>

                    <div className="text-right font-mono font-bold text-slate-800">
                      {isReceive ? '+' : '-'}{log.quantity}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </div>

      {/* 1. Goods Receipt Note Dialog */}
      <Dialog isOpen={showReceiveModal} onClose={() => setShowReceiveModal(false)} title="Goods Receipt (GRN) Sourcing Intake">
        <form onSubmit={handleReceiveStock} className="space-y-4 text-xs">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Material Name</label>
              <Input
                type="text"
                value={receiveForm.name}
                onChange={(e) => setReceiveForm({ ...receiveForm, name: e.target.value })}
                required
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Material Unique Code</label>
              <Input
                type="text"
                value={receiveForm.code}
                onChange={(e) => setReceiveForm({ ...receiveForm, code: e.target.value })}
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Category Type</label>
              <Select
                value={receiveForm.category}
                onChange={(e) => setReceiveForm({ ...receiveForm, category: e.target.value })}
              >
                <option value="RAW_MATERIAL">Raw Material</option>
                <option value="FINISHED_GOODS">Finished Goods</option>
                <option value="CLIENT_SUPPLIED">Client-Supplied</option>
                <option value="SCRAP">Scrap</option>
              </Select>
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Unit Metric</label>
              <Input
                type="text"
                value={receiveForm.unit}
                onChange={(e) => setReceiveForm({ ...receiveForm, unit: e.target.value })}
                placeholder="meters, kg, spools, pcs"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Quantity Received</label>
              <Input
                type="number"
                value={receiveForm.currentStock}
                onChange={(e) => setReceiveForm({ ...receiveForm, currentStock: parseFloat(e.target.value) || 0 })}
                min="0"
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Safety Stock Threshold</label>
              <Input
                type="number"
                value={receiveForm.safetyStock}
                onChange={(e) => setReceiveForm({ ...receiveForm, safetyStock: parseFloat(e.target.value) || 0 })}
                min="0"
              />
            </div>
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Warehouse</label>
              <Input
                type="text"
                value={receiveForm.warehouseName}
                onChange={(e) => setReceiveForm({ ...receiveForm, warehouseName: e.target.value })}
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Rack Location</label>
              <Input
                type="text"
                value={receiveForm.rackLocation}
                onChange={(e) => setReceiveForm({ ...receiveForm, rackLocation: e.target.value })}
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Batch No</label>
              <Input
                type="text"
                value={receiveForm.batchNumber}
                onChange={(e) => setReceiveForm({ ...receiveForm, batchNumber: e.target.value })}
              />
            </div>
          </div>

          {receiveForm.category === 'CLIENT_SUPPLIED' && (
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Associative Client Brand</label>
              <Select
                value={receiveForm.clientBrandId}
                onChange={(e) => setReceiveForm({ ...receiveForm, clientBrandId: e.target.value })}
              >
                <option value="">Select client...</option>
                <option value="nike-brand">Nike MSME Partner</option>
                <option value="adidas-brand">Adidas Performance</option>
              </Select>
            </div>
          )}

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowReceiveModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Record GRN Intake
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Issue Material Dialog */}
      <Dialog isOpen={showIssueModal} onClose={() => { setShowIssueModal(false); setSelectedItemForIssue(null); }} title="Issue Material to Floor">
        {selectedItemForIssue && (
          <form onSubmit={handleIssueStock} className="space-y-4 text-xs">
            <p className="text-slate-500">
              Issuing material <span className="font-bold text-indigo-650">{selectedItemForIssue.name}</span>. Currently available stock: {selectedItemForIssue.availableStock} {selectedItemForIssue.unit}.
            </p>
            <div>
              <label className="block text-slate-505 mb-1 font-semibold">Issue Quantity</label>
              <Input
                type="number"
                value={issueQuantity}
                onChange={(e) => setIssueQuantity(parseFloat(e.target.value) || 0)}
                max={selectedItemForIssue.availableStock}
                min="0.1"
                step="any"
                required
              />
            </div>

            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Active Production Order Link ID (Optional)</label>
              <Input
                type="text"
                placeholder="e.g. ORD-2026-101"
                value={issueOrderId}
                onChange={(e) => setIssueOrderId(e.target.value)}
              />
            </div>

            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Remarks / Storekeeping Notes</label>
              <textarea
                value={issueRemarks}
                onChange={(e) => setIssueRemarks(e.target.value)}
                placeholder="e.g. Dye run #3 release"
                rows={3}
                className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none resize-none text-xs"
              />
            </div>

            <div className="flex space-x-3 pt-4 justify-end">
              <Button type="button" onClick={() => { setShowIssueModal(false); setSelectedItemForIssue(null); }}>
                Cancel
              </Button>
              <Button type="submit" variant="primary">
                Confirm Material Issue
              </Button>
            </div>
          </form>
        )}
      </Dialog>

      {/* 3. Barcode QR display dialog */}
      <Dialog isOpen={!!showBarcodeModal} onClose={() => setShowBarcodeModal(null)} title="Print Bin Identifier Card">
        {showBarcodeModal && (
          <div className="text-center space-y-4">
            <div className="p-4 bg-white border border-slate-200 rounded-xl inline-block shadow-sm">
              <QrCode className="w-36 h-36 text-slate-900 mx-auto" />
            </div>
            
            <p className="text-xs font-mono font-bold text-slate-800">{showBarcodeModal.barcode}</p>
            <p className="text-[10px] text-slate-500 font-semibold">Bin: {showBarcodeModal.warehouseName} | Rack: {showBarcodeModal.rackLocation}</p>

            <Button onClick={() => setShowBarcodeModal(null)} className="w-full mt-4">
              Close Identifier
            </Button>
          </div>
        )}
      </Dialog>
    </div>
  );
}
