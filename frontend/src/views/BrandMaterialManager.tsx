import React, { useState } from 'react';
import { 
  Building2, 
  AlertTriangle, 
  Plus, 
  Layers, 
  CheckCircle,
  Sparkles
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

interface BrandMaterialManagerProps {
  inventory: any[];
  onRefresh: () => void;
  onUpdateInventoryItem: (item: any) => void;
}

export default function BrandMaterialManager({ inventory, onRefresh, onUpdateInventoryItem }: BrandMaterialManagerProps) {
  const [selectedBrand, setSelectedBrand] = useState('nike-brand');
  const [showAddModal, setShowAddModal] = useState(false);

  // Form states
  const [name, setName] = useState('Nike Custom Zip pullers');
  const [code, setCode] = useState('CS-NK-ZIP');
  const [qty, setQty] = useState(2500.0);
  const [safety, setSafety] = useState(1000.0);

  const [statusMsg, setStatusMsg] = useState('');

  // Filter client-supplied materials
  const clientMaterials = inventory.filter(item => 
    item.category === 'CLIENT_SUPPLIED' && item.clientBrandId === selectedBrand
  );

  const handleAddMaterial = (e: React.FormEvent) => {
    e.preventDefault();

    const newItem = {
      id: 'brand-' + Date.now(),
      name,
      code,
      sku: 'SKU-' + code,
      barcode: 'BC-' + Date.now(),
      category: 'CLIENT_SUPPLIED',
      supplierName: 'Client-Supplied',
      unit: 'pcs',
      purchasePrice: 0.0,
      currentStock: qty,
      reservedStock: 0.0,
      availableStock: qty,
      warehouseName: 'Client Vault A',
      rackLocation: 'Section B-3',
      batchNumber: 'LOT-' + (100 + Math.random() * 900),
      safetyStock: safety,
      clientBrandId: selectedBrand,
      isLowStock: qty < safety
    };

    onUpdateInventoryItem(newItem);
    setStatusMsg(`Brand material ${name} registered inside client repository.`);
    setShowAddModal(false);

    // Reset forms
    setName('');
    setCode('');
    setQty(1000.0);
    setSafety(500.0);

    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Brand Materials Repository</h2>
            <p className="text-xs text-slate-550 mt-0.5">Isolated customer-supplied accessories, care labels, packaging boxes & custom trims</p>
          </div>

          {/* Client Filter selector */}
          <Select 
            value={selectedBrand} 
            onChange={(e) => setSelectedBrand(e.target.value)}
            className="w-48 bg-white border border-slate-200"
          >
            <option value="nike-brand">Nike MSME Partner</option>
            <option value="adidas-brand">Adidas Performance</option>
            <option value="zara-brand">Zara Sourcing Group</option>
          </Select>
        </div>

        <Button variant="primary" onClick={() => setShowAddModal(true)}>
          <Plus className="w-4 h-4" />
          <span>Receive Brand Materials</span>
        </Button>
      </div>

      {statusMsg && (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      )}

      {/* Main Grid split */}
      <div className="flex-1 grid grid-cols-3 gap-8 overflow-hidden">
        
        {/* Table column */}
        <div className="col-span-2 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
            <Building2 className="w-4 h-4 text-indigo-600" />
            <span>Isolated Brand Trims Queue</span>
          </h3>

          <div className="flex-1 overflow-y-auto pr-2">
            <Table>
              <TableHead>
                <th className="pb-3">Code / ID</th>
                <th className="pb-3">Name</th>
                <th className="pb-3">Store Location</th>
                <th className="pb-3 text-right">Available Qty</th>
                <th className="pb-3 text-right">Safety Limit</th>
              </TableHead>
              <TableBody>
                {clientMaterials.map(item => {
                  const isLow = item.currentStock < item.safetyStock;
                  return (
                    <tr key={item.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                      <td className="py-3 flex items-center space-x-2">
                        <span className="font-mono font-bold text-indigo-600">{item.code}</span>
                        {isLow && <Badge status="error">Low</Badge>}
                      </td>
                      <td className="py-3 font-semibold text-slate-800">{item.name}</td>
                      <td className="py-3 text-slate-550">{item.warehouseName || 'Client Vault A'}</td>
                      <td className="py-3 text-right font-mono font-bold text-slate-805">{item.currentStock} {item.unit}</td>
                      <td className="py-3 text-right font-mono text-slate-450">{item.safetyStock} {item.unit}</td>
                    </tr>
                  );
                })}

                {clientMaterials.length === 0 && (
                  <tr>
                    <td colSpan={5} className="text-center py-12 text-slate-450 italic">
                      No client-supplied stock logged for this brand.
                    </td>
                  </tr>
                )}
              </TableBody>
            </Table>
          </div>
        </div>

        {/* Right Side instructions card */}
        <div className="bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm">
          <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
            <Layers className="w-4 h-4 text-indigo-600" />
            <span>Access Boundaries</span>
          </h3>

          <div className="space-y-4 text-xs leading-relaxed text-slate-550">
            <p>
              Customer-supplied materials belong directly to client brand registries and are isolated under multi-tenant boundaries.
            </p>
            <div className="p-3 bg-indigo-50 border border-indigo-200 text-indigo-750 rounded-lg flex items-start space-x-2">
              <AlertTriangle className="w-4 h-4 shrink-0 mt-0.5 text-indigo-650" />
              <span>Cross-brand sharing is disabled. items cannot be assigned to other clients' tasks.</span>
            </div>
          </div>
        </div>
      </div>

      {/* 1. Receive Client Trims Dialog */}
      <Dialog isOpen={showAddModal} onClose={() => setShowAddModal(false)} title="Receive Client-Supplied Materials">
        <form onSubmit={handleAddMaterial} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Material Description</label>
            <Input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="e.g. Neck branding labels"
              required
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Material Code ID</label>
              <Input
                type="text"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                placeholder="e.g. CS-NK-LBL"
                required
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Client Brand owner</label>
              <Select value={selectedBrand} onChange={(e) => setSelectedBrand(e.target.value)}>
                <option value="nike-brand">Nike MSME Partner</option>
                <option value="adidas-brand">Adidas Performance</option>
                <option value="zara-brand">Zara Sourcing Group</option>
              </Select>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Quantity Received (pcs)</label>
              <Input
                type="number"
                value={qty}
                onChange={(e) => setQty(parseFloat(e.target.value) || 0)}
              />
            </div>
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Minimum Safety stock</label>
              <Input
                type="number"
                value={safety}
                onChange={(e) => setSafety(parseFloat(e.target.value) || 0)}
              />
            </div>
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowAddModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Log Intake Receipt
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
