import React, { useState, useEffect } from 'react';
import { 
  Truck, 
  CheckCircle, 
  AlertTriangle, 
  Plus, 
  QrCode, 
  FileText, 
  MapPin,
  Calendar,
  Sparkles
} from 'lucide-react';
import axios from 'axios';
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

export default function DispatchDashboard() {
  const [dispatchQueue, setDispatchQueue] = useState<any[]>([]);
  const [selectedRecord, setSelectedRecord] = useState<any | null>(null);

  // Modals
  const [showCourierModal, setShowCourierModal] = useState(false);
  const [showVerifyModal, setShowVerifyModal] = useState(false);

  // Forms
  const [vehicleNo, setVehicleNo] = useState('KA-03-ME-9011');
  const [courierName, setCourierName] = useState('DHL Express Cargo');
  const [trackingNumber, setTrackingNumber] = useState('DHL-890214-X');
  const [invoiceNumber, setInvoiceNumber] = useState('INV-2026-092');

  const [checklistPassed, setChecklistPassed] = useState(false);
  const [barcodeVerified, setBarcodeVerified] = useState(false);

  const [statusMsg, setStatusMsg] = useState('');

  const fetchDispatchQueue = async () => {
    try {
      const res = await axios.get('http://localhost:8085/api/v1/dispatch', {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setDispatchQueue(res.data);
    } catch (err) {
      // Mock seeder
      setDispatchQueue([
        { id: 1, orderId: 'ord-1', orderNumber: 'ORD-2026-101', productName: 'Premium Dri-FIT Running Tops', vehicleNo: 'KA-03-ME-9011', courierName: 'DHL Express Cargo', trackingNumber: 'DHL-890214-X', status: 'READY', checklistPassed: false, invoiceNumber: 'INV-2026-092', barcodeVerified: false, deliveryConfirmationTime: null },
        { id: 2, orderId: 'ord-2', orderNumber: 'ORD-2026-102', productName: 'Eco Cotton Summer Polos', vehicleNo: null, courierName: null, trackingNumber: null, status: 'READY', checklistPassed: false, invoiceNumber: null, barcodeVerified: false, deliveryConfirmationTime: null }
      ]);
    }
  };

  useEffect(() => {
    fetchDispatchQueue();
  }, []);

  const handleCourierSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedRecord) return;

    try {
      const res = await axios.post(`http://localhost:8085/api/v1/dispatch/${selectedRecord.id}/courier`, {
        vehicleNo,
        courierName,
        trackingNumber,
        invoiceNumber
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Courier waybill mapped successfully.');
      fetchDispatchQueue();
    } catch (err) {
      const updated = { ...selectedRecord, vehicleNo, courierName, trackingNumber, invoiceNumber };
      setDispatchQueue(prev => prev.map(d => d.id === selectedRecord.id ? updated : d));
      setStatusMsg('Courier waybill mapped. (Mock)');
    }

    setShowCourierModal(false);
    setSelectedRecord(null);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleVerifySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedRecord) return;

    try {
      const res = await axios.post(`http://localhost:8085/api/v1/dispatch/${selectedRecord.id}/verify`, {
        checklistPassed,
        barcodeVerified
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Checklist and barcode verified.');
      fetchDispatchQueue();
    } catch (err) {
      const updated = { ...selectedRecord, checklistPassed, barcodeVerified };
      setDispatchQueue(prev => prev.map(d => d.id === selectedRecord.id ? updated : d));
      setStatusMsg('Checklist and barcode verified. (Mock)');
    }

    setShowVerifyModal(false);
    setSelectedRecord(null);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleShip = async (record: any) => {
    try {
      await axios.post(`http://localhost:8085/api/v1/dispatch/${record.id}/ship`, {}, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Shipment dispatched. Client notification sent.');
      fetchDispatchQueue();
    } catch (err) {
      if (!record.checklistPassed || !record.barcodeVerified) {
        setStatusMsg('Cannot ship: checklist or barcode must be verified.');
        setTimeout(() => setStatusMsg(''), 3500);
        return;
      }
      const updated = { ...record, status: 'DISPATCHED' };
      setDispatchQueue(prev => prev.map(d => d.id === record.id ? updated : d));
      setStatusMsg('Shipment dispatched. (Mock)');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleDeliver = async (record: any) => {
    try {
      await axios.post(`http://localhost:8085/api/v1/dispatch/${record.id}/deliver`, {}, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Shipment delivery confirmed by client signature.');
      fetchDispatchQueue();
    } catch (err) {
      const updated = { ...record, status: 'DELIVERED', deliveryConfirmationTime: new Date().toISOString() };
      setDispatchQueue(prev => prev.map(d => d.id === record.id ? updated : d));
      setStatusMsg('Shipment delivered. (Mock)');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div>
          <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Logistics & Dispatch Control</h2>
          <p className="text-xs text-slate-500 mt-0.5">Approve waybills, verify packaging checklist barcodes, and assign courier freights</p>
        </div>
      </div>

      {statusMsg && (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-750 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      )}

      {/* Main queue card layout */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm overflow-hidden">
        <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-2">
          <Truck className="w-4 h-4 text-indigo-600" />
          <span>Dispatch Waybill Queue</span>
        </h3>

        <div className="flex-1 overflow-y-auto pr-2">
          <Table>
            <TableHead>
              <th className="pb-3">Order Ref</th>
              <th className="pb-3">Product Name</th>
              <th className="pb-3">Courier Logistics Details</th>
              <th className="pb-3 text-center">Checks</th>
              <th className="pb-3">Invoice</th>
              <th className="pb-3">Status</th>
              <th className="pb-3 text-center">Actions</th>
            </TableHead>
            <TableBody>
              {dispatchQueue.map(item => {
                const isReady = item.status === 'READY';
                const isDispatched = item.status === 'DISPATCHED';
                const isDelivered = item.status === 'DELIVERED';
                return (
                  <tr key={item.id} className="hover:bg-slate-50/50 transition border-b border-slate-100">
                    <td className="py-3 font-mono font-bold text-indigo-600">{item.orderNumber}</td>
                    <td className="py-3 font-semibold text-slate-800">{item.productName}</td>
                    <td className="py-3">
                      {item.courierName ? (
                        <div className="text-[11px] space-y-0.5 text-slate-600">
                          <span className="font-semibold">{item.courierName}</span>
                          <span className="text-[10px] text-slate-400 block">Tracking: {item.trackingNumber}</span>
                          <span className="text-[10px] text-slate-400 block">Vehicle: {item.vehicleNo}</span>
                        </div>
                      ) : (
                        <span className="text-xs text-rose-500 font-semibold italic">Unassigned</span>
                      )}
                    </td>
                    <td className="py-3 text-center">
                      <div className="flex flex-col space-y-1 items-center">
                        <Badge status={item.checklistPassed ? 'success' : 'default'}>
                          Checklist: {item.checklistPassed ? 'OK' : 'Pending'}
                        </Badge>
                        <Badge status={item.barcodeVerified ? 'success' : 'default'}>
                          Barcode: {item.barcodeVerified ? 'OK' : 'Pending'}
                        </Badge>
                      </div>
                    </td>
                    <td className="py-3 font-mono text-slate-500">{item.invoiceNumber || 'N/A'}</td>
                    <td className="py-3">
                      <Badge status={isDelivered ? 'success' : isDispatched ? 'warning' : 'info'}>
                        {item.status}
                      </Badge>
                    </td>
                    <td className="py-3 text-center">
                      <div className="flex items-center justify-center space-x-2">
                        {isReady && (
                          <>
                            <Button 
                              variant="outline" 
                              onClick={() => {
                                setSelectedRecord(item);
                                setVehicleNo(item.vehicleNo || '');
                                setCourierName(item.courierName || '');
                                setTrackingNumber(item.trackingNumber || '');
                                setInvoiceNumber(item.invoiceNumber || '');
                                setShowCourierModal(true);
                              }}
                              className="!py-1"
                            >
                              Assign Waybill
                            </Button>
                            <Button 
                              variant="outline" 
                              onClick={() => {
                                setSelectedRecord(item);
                                setChecklistPassed(item.checklistPassed);
                                setBarcodeVerified(item.barcodeVerified);
                                setShowVerifyModal(true);
                              }}
                              className="!py-1"
                            >
                              Verify Package
                            </Button>
                            <Button 
                              variant="primary" 
                              onClick={() => handleShip(item)}
                              className="!py-1"
                            >
                              Ship Items
                            </Button>
                          </>
                        )}
                        {isDispatched && (
                          <Button 
                            variant="primary" 
                            onClick={() => handleDeliver(item)}
                            className="!py-1 bg-emerald-650 hover:bg-emerald-700 border-none"
                          >
                            Mark Delivered
                          </Button>
                        )}
                        {isDelivered && (
                          <span className="text-[10px] text-slate-400 font-bold block">
                            Delivered: {item.deliveryConfirmationTime ? new Date(item.deliveryConfirmationTime).toLocaleDateString() : 'Yes'}
                          </span>
                        )}
                      </div>
                    </td>
                  </tr>
                );
              })}
            </TableBody>
          </Table>
        </div>
      </div>

      {/* 1. Assign Courier Dialog */}
      <Dialog isOpen={showCourierModal} onClose={() => { setShowCourierModal(false); setSelectedRecord(null); }} title="Map Courier Logistics Details">
        <form onSubmit={handleCourierSubmit} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Courier / Carrier Name</label>
            <Input
              type="text"
              value={courierName}
              onChange={(e) => setCourierName(e.target.value)}
              placeholder="e.g. DHL Express Cargo"
              required
            />
          </div>
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Logistics Tracking Reference ID</label>
            <Input
              type="text"
              value={trackingNumber}
              onChange={(e) => setTrackingNumber(e.target.value)}
              placeholder="e.g. DHL-890214-X"
              required
            />
          </div>
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Dispatch Vehicle Registration Number</label>
            <Input
              type="text"
              value={vehicleNo}
              onChange={(e) => setVehicleNo(e.target.value)}
              placeholder="e.g. KA-03-ME-9011"
              required
            />
          </div>
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Invoice Number Link</label>
            <Input
              type="text"
              value={invoiceNumber}
              onChange={(e) => setInvoiceNumber(e.target.value)}
              placeholder="e.g. INV-2026-092"
              required
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => { setShowCourierModal(false); setSelectedRecord(null); }}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Save Waybill Mapping
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Package Verification Dialog */}
      <Dialog isOpen={showVerifyModal} onClose={() => { setShowVerifyModal(false); setSelectedRecord(null); }} title="Audit Package Verification">
        <form onSubmit={handleVerifySubmit} className="space-y-4 text-xs">
          <p className="text-slate-500">
            Sign-off checklist controls and verify packing box waybill barcode scanner.
          </p>

          <label className="flex items-center space-x-3 p-3 bg-slate-50 border border-slate-200 rounded-lg cursor-pointer hover:bg-slate-100 font-semibold">
            <input
              type="checkbox"
              checked={checklistPassed}
              onChange={() => setChecklistPassed(!checklistPassed)}
              className="w-4 h-4 rounded text-indigo-650 border-slate-300 focus:ring-indigo-500"
            />
            <span>Packing checklist items verified & items counted correctly</span>
          </label>

          <label className="flex items-center space-x-3 p-3 bg-slate-50 border border-slate-200 rounded-lg cursor-pointer hover:bg-slate-100 font-semibold">
            <input
              type="checkbox"
              checked={barcodeVerified}
              onChange={() => setBarcodeVerified(!barcodeVerified)}
              className="w-4 h-4 rounded text-indigo-650 border-slate-300 focus:ring-indigo-500"
            />
            <span>Box waybill barcode matches system packing slip order number</span>
          </label>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => { setShowVerifyModal(false); setSelectedRecord(null); }}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Submit Sign-Off Audit
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
