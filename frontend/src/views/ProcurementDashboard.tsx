import React, { useState, useEffect } from 'react';
import { 
  Plus, 
  Search, 
  CheckCircle, 
  ArrowDownLeft, 
  FileText, 
  User, 
  Globe,
  Sparkles,
  ClipboardCheck,
  Scale
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

interface ProcurementDashboardProps {
  user: any;
  inventory: any[];
}

export default function ProcurementDashboard({ user, inventory }: ProcurementDashboardProps) {
  const [purchaseOrders, setPurchaseOrders] = useState<any[]>([]);
  const [vendors, setVendors] = useState<any[]>([]);
  const [selectedPo, setSelectedPo] = useState<any | null>(null);

  // Subtabs
  const [activeTab, setActiveTab] = useState<'pos' | 'vendors'>('pos');

  // Modals
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showReceiveModal, setShowReceiveModal] = useState(false);
  const [showVendorModal, setShowVendorModal] = useState(false);

  // Form states
  const [vendorName, setVendorName] = useState('Trims & Thread Corp');
  const [selectedMaterialId, setSelectedMaterialId] = useState('');
  const [orderQty, setOrderQty] = useState(500.0);
  const [unitPrice, setUnitPrice] = useState(1.50);

  // Vendor Form
  const [newVendor, setNewVendor] = useState({
    name: 'Sourcing Fabric Ltd',
    code: 'VEN-FAB-99',
    email: 'sales@fabricltd.com',
    phone: '555-0199',
    address: 'Textile Zone Block C'
  });

  // Partial receiving inputs
  const [receiptQuantities, setReceiptQuantities] = useState<Record<string, number>>({});
  const [invoiceNo, setInvoiceNo] = useState('');

  const [statusMsg, setStatusMsg] = useState('');

  const fetchProcurementData = async () => {
    try {
      const [poRes, venRes] = await Promise.all([
        api.get('/procurement/orders'),
        api.get('/procurement/vendors')
      ]);
      setPurchaseOrders(poRes.data || []);
      setVendors(venRes.data || []);
    } catch (err) {
      console.error('Failed to fetch procurement data from database:', err);
    }
  };

  useEffect(() => {
    fetchProcurementData();
    if (inventory.length > 0) {
      setSelectedMaterialId(inventory[0].id);
    }
  }, [inventory]);

  const handleCreatePo = async (e: React.FormEvent) => {
    e.preventDefault();
    const material = inventory.find(i => i.id === selectedMaterialId);
    if (!material) return;

    const payload = {
      vendorName,
      items: [
        {
          materialId: material.id,
          materialName: material.name,
          quantityOrdered: orderQty,
          unitPrice: unitPrice
        }
      ]
    };

    try {
      await api.post('/procurement/orders', payload);
      setStatusMsg('Purchase request logged successfully.');
      fetchProcurementData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to create purchase order in database.');
    }

    setShowCreateModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleApprovePo = async (poId: number) => {
    try {
      await api.post(`/procurement/orders/${poId}/approve`, {});
      setStatusMsg('Purchase order approved & sent.');
      fetchProcurementData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to approve purchase order in database.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handlePartialReceive = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedPo) return;

    try {
      await api.post(`/procurement/orders/${selectedPo.id}/receive`, {
        receipts: receiptQuantities,
        invoiceNumber: invoiceNo
      });
      setStatusMsg('GRN logged. Stock quantities resolved.');
      fetchProcurementData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to log GRN receipts in database.');
    }

    setShowReceiveModal(false);
    setSelectedPo(null);
    setReceiptQuantities({});
    setInvoiceNo('');
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleCreateVendor = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/procurement/vendors', newVendor);
      setStatusMsg('Supplier profile registered.');
      fetchProcurementData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to register vendor in database.');
    }

    setShowVendorModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Page Header */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Procurement Workspace</h2>
            <p className="text-xs text-slate-550 mt-0.5">Approve purchase requests, track supplier invoices & record Goods Receipt Notes (GRN)</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            <button
              onClick={() => setActiveTab('pos')}
              className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                activeTab === 'pos' ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              Purchase Orders
            </button>
            <button
              onClick={() => setActiveTab('vendors')}
              className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                activeTab === 'vendors' ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
              }`}
            >
              Vendors Ledger
            </button>
          </div>
        </div>

        <div className="flex space-x-2">
          {activeTab === 'vendors' ? (
            <Button variant="primary" onClick={() => setShowVendorModal(true)}>
              <Plus className="w-4 h-4" />
              <span>Register Vendor</span>
            </Button>
          ) : (
            <Button variant="primary" onClick={() => setShowCreateModal(true)}>
              <Plus className="w-4 h-4" />
              <span>Create Purchase Request</span>
            </Button>
          )}
        </div>
      </div>

      {statusMsg && (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-750 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      )}

      {/* Main Grid View */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm overflow-hidden">
        {activeTab === 'pos' ? (
          <div className="flex-1 overflow-y-auto pr-2">
            <Table>
              <TableHead>
                <th className="pb-3">PO Number</th>
                <th className="pb-3">Vendor / Supplier</th>
                <th className="pb-3">Total Amount</th>
                <th className="pb-3">Invoice Mapping</th>
                <th className="pb-3">Status</th>
                <th className="pb-3 text-center">Actions</th>
              </TableHead>
              <TableBody>
                {purchaseOrders.map(po => {
                  const isPending = po.status === 'PENDING_APPROVAL';
                  const isApproved = po.status === 'APPROVED';
                  const isPartial = po.status === 'PARTIAL_RECEIVED';
                  const isCompleted = po.status === 'COMPLETED';
                  return (
                    <tr key={po.id} className="hover:bg-slate-50/50 transition border-b border-slate-100">
                      <td className="py-3 font-mono font-bold text-indigo-600">{po.poNumber}</td>
                      <td className="py-3 font-semibold text-slate-800">{po.vendorName}</td>
                      <td className="py-3 font-mono font-semibold">${po.totalAmount.toLocaleString()}</td>
                      <td className="py-3 font-mono text-slate-550">{po.invoiceNumber || 'Pending'}</td>
                      <td className="py-3">
                        <Badge status={isCompleted ? 'success' : isPending ? 'default' : 'warning'}>
                          {po.status}
                        </Badge>
                      </td>
                      <td className="py-3 text-center">
                        <div className="flex items-center justify-center space-x-2">
                          {isPending && user.role === 'ROLE_FACTORY_OWNER' && (
                            <Button variant="primary" onClick={() => handleApprovePo(po.id)} className="!py-1">
                              Approve
                            </Button>
                          )}
                          {(isApproved || isPartial) && (
                            <Button 
                              variant="outline" 
                              onClick={() => {
                                setSelectedPo(po);
                                const qts: Record<string, number> = {};
                                po.items.forEach((it: any) => {
                                  qts[it.materialId] = it.quantityOrdered - it.quantityReceived;
                                });
                                setReceiptQuantities(qts);
                                setShowReceiveModal(true);
                              }}
                              className="!py-1"
                            >
                              Receive GRN
                            </Button>
                          )}
                          {isCompleted && (
                            <span className="text-[10px] text-slate-400 font-bold block">Intake Completed</span>
                          )}
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </TableBody>
            </Table>
          </div>
        ) : (
          /* VENDOR CREDIT LEDGER */
          <div className="flex-1 overflow-y-auto pr-2">
            <Table>
              <TableHead>
                <th className="pb-3">Code</th>
                <th className="pb-3">Name</th>
                <th className="pb-3">Email</th>
                <th className="pb-3">Phone</th>
                <th className="pb-3">Address</th>
                <th className="pb-3 text-right">Outstanding Credit</th>
              </TableHead>
              <TableBody>
                {vendors.map(v => (
                  <tr key={v.id} className="hover:bg-slate-50/50 transition border-b border-slate-100">
                    <td className="py-3 font-mono font-bold text-slate-500">{v.code}</td>
                    <td className="py-3 font-semibold text-slate-800">{v.name}</td>
                    <td className="py-3">{v.email}</td>
                    <td className="py-3 font-mono">{v.phone}</td>
                    <td className="py-3 text-slate-550">{v.address}</td>
                    <td className="py-3 text-right font-mono font-bold text-slate-800">${v.outstandingBalance.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                  </tr>
                ))}
              </TableBody>
            </Table>
          </div>
        )}
      </div>

      {/* 1. Create Purchase Order Request Dialog */}
      <Dialog isOpen={showCreateModal} onClose={() => setShowCreateModal(false)} title="Create Purchase Request">
        <form onSubmit={handleCreatePo} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Vendor / Supplier</label>
            <Select value={vendorName} onChange={(e) => setVendorName(e.target.value)}>
              <option value="Trims & Thread Corp">Trims & Thread Corp</option>
              <option value="Sourcing Fabric Ltd">Sourcing Fabric Ltd</option>
            </Select>
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div className="col-span-2">
              <label className="block text-slate-500 mb-1 font-semibold">Target SKU Material</label>
              <Select value={selectedMaterialId} onChange={(e) => setSelectedMaterialId(e.target.value)}>
                {inventory.map(item => (
                  <option key={item.id} value={item.id}>{item.code} - {item.name}</option>
                ))}
              </Select>
            </div>
            <div>
              <label className="block text-slate-550 mb-1 font-semibold">UOM Metric</label>
              <div className="px-3 py-1.5 bg-slate-100 border border-slate-200 rounded-lg text-slate-500 font-bold">
                {inventory.find(i => i.id === selectedMaterialId)?.unit || 'unit'}
              </div>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Request Quantity</label>
              <Input
                type="number"
                value={orderQty}
                onChange={(e) => setOrderQty(parseFloat(e.target.value) || 0)}
              />
            </div>
            <div>
              <label className="block text-slate-505 mb-1 font-semibold">Estimated Unit Price ($)</label>
              <Input
                type="number"
                value={unitPrice}
                onChange={(e) => setUnitPrice(parseFloat(e.target.value) || 0)}
                step="any"
              />
            </div>
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowCreateModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Log Purchase Order
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Goods Receipt Note Intake Dialog */}
      <Dialog isOpen={showReceiveModal} onClose={() => { setShowReceiveModal(false); setSelectedPo(null); }} title="Goods Receipt (GRN) Intake">
        {selectedPo && (
          <form onSubmit={handlePartialReceive} className="space-y-4 text-xs">
            <p className="text-slate-500">
              Recording delivery intakes for purchase order <span className="font-mono text-indigo-650 font-semibold">{selectedPo.poNumber}</span>.
            </p>

            <div className="space-y-3">
              {selectedPo.items.map((it: any) => (
                <div key={it.materialId} className="p-3 bg-slate-50 border border-slate-200 rounded-lg flex items-center justify-between">
                  <div>
                    <span className="font-bold text-slate-800 block">{it.materialName}</span>
                    <span className="text-[10px] text-slate-500">Ordered: {it.quantityOrdered} | Received: {it.quantityReceived}</span>
                  </div>
                  <div className="w-32">
                    <Input
                      type="number"
                      value={receiptQuantities[it.materialId] ?? 0}
                      onChange={(e) => setReceiptQuantities({ ...receiptQuantities, [it.materialId]: parseFloat(e.target.value) || 0 })}
                      max={it.quantityOrdered - it.quantityReceived}
                      min="0"
                    />
                  </div>
                </div>
              ))}
            </div>

            <div>
              <label className="block text-slate-500 mb-1 font-semibold">Vendor Invoice Attachment Code</label>
              <Input
                type="text"
                placeholder="e.g. INV-90112"
                value={invoiceNo}
                onChange={(e) => setInvoiceNo(e.target.value)}
                required
              />
            </div>

            <div className="flex space-x-3 pt-4 justify-end">
              <Button type="button" onClick={() => { setShowReceiveModal(false); setSelectedPo(null); }}>
                Cancel
              </Button>
              <Button type="submit" variant="primary">
                Record Delivery Intake
              </Button>
            </div>
          </form>
        )}
      </Dialog>

      {/* 3. Register Vendor Dialog */}
      <Dialog isOpen={showVendorModal} onClose={() => setShowVendorModal(false)} title="Register Supplier Profile">
        <form onSubmit={handleCreateVendor} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-505 mb-1 font-semibold">Vendor Company Name</label>
            <Input
              type="text"
              value={newVendor.name}
              onChange={(e) => setNewVendor({ ...newVendor, name: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="block text-slate-505 mb-1 font-semibold">Unique Vendor Code</label>
            <Input
              type="text"
              value={newVendor.code}
              onChange={(e) => setNewVendor({ ...newVendor, code: e.target.value })}
              required
            />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-slate-505 mb-1 font-semibold">Contact Email</label>
              <Input
                type="email"
                value={newVendor.email}
                onChange={(e) => setNewVendor({ ...newVendor, email: e.target.value })}
                required
              />
            </div>
            <div>
              <label className="block text-slate-505 mb-1 font-semibold">Phone Number</label>
              <Input
                type="text"
                value={newVendor.phone}
                onChange={(e) => setNewVendor({ ...newVendor, phone: e.target.value })}
                required
              />
            </div>
          </div>
          <div>
            <label className="block text-slate-505 mb-1 font-semibold">Corporate Address</label>
            <Input
              type="text"
              value={newVendor.address}
              onChange={(e) => setNewVendor({ ...newVendor, address: e.target.value })}
              required
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowVendorModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Register Vendor
            </Button>
          </div>
        </form>
      </Dialog>

    </div>
  );
}
