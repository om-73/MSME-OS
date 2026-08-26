import React, { useState } from 'react';
import { 
  AlertOctagon, 
  CheckSquare, 
  Plus, 
  FileText 
} from 'lucide-react';
import { api } from '../api/client';
import { Button, Badge, Input, Select, Dialog } from '../components/DesignSystem';

interface KanbanBoardProps {
  orders: any[];
  stages: any[];
  user: any;
  onRefresh: () => void;
  onAddOrder: (order: any) => void;
}

export default function KanbanBoard({ orders, stages, user, onRefresh, onAddOrder }: KanbanBoardProps) {
  const [draggedOrderId, setDraggedOrderId] = useState<string | null>(null);
  
  // QC Modals and properties
  const [showQcModal, setShowQcModal] = useState(false);
  const [selectedOrderForQc, setSelectedOrderForQc] = useState<any | null>(null);
  const [selectedTargetStageId, setSelectedTargetStageId] = useState<string | null>(null);
  const [qcPassed, setQcPassed] = useState(true);
  const [defectType, setDefectType] = useState('Ink Smudge & Color Bleed');
  const [defectCount, setDefectCount] = useState(3);
  const [qcRemarks, setQcRemarks] = useState('');

  // Add Order Form Modal
  const [showAddOrder, setShowAddOrder] = useState(false);
  const [newOrder, setNewOrder] = useState({
    brandId: 'nike-brand',
    productName: 'Eco Cotton Summer Polos',
    quantity: 1200,
    priority: 'HIGH',
    totalContractValue: 24500.0,
    notes: 'Urgent premium contract run'
  });

  const handleDragStart = (e: React.DragEvent, id: string) => {
    setDraggedOrderId(id);
    e.dataTransfer.setData('text/plain', id);
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
  };

  const handleDrop = async (e: React.DragEvent, targetStageId: string) => {
    e.preventDefault();
    const orderId = e.dataTransfer.getData('text/plain') || draggedOrderId;
    if (!orderId) return;

    const order = orders.find(o => o.id === orderId);
    const targetStage = stages.find(s => s.id === targetStageId);

    if (!order || !targetStage) return;

    if (targetStage.type === 'QC' || targetStage.code === 'QC') {
      setSelectedOrderForQc(order);
      setSelectedTargetStageId(targetStageId);
      setShowQcModal(true);
      return;
    }

    try {
      await api.post(`/orders/${orderId}/transition`, {
        targetStageId,
        notes: `Moved stage to ${targetStage.name} by floor operator.`
      });
    } catch (err) {
      console.warn('Transition error:', err);
    }
    onRefresh();
  };

  const submitQcForm = async () => {
    if (!selectedOrderForQc || !selectedTargetStageId) return;

    try {
      await api.post('/orders/qc-outcome', {
        orderId: selectedOrderForQc.id,
        stageId: selectedTargetStageId,
        passed: qcPassed,
        defectType: qcPassed ? null : defectType,
        sampleSize: 100,
        defectCount: qcPassed ? 0 : defectCount,
        remarks: qcRemarks
      });

      await api.post(`/orders/${selectedOrderForQc.id}/transition`, {
        targetStageId: selectedTargetStageId,
        notes: qcPassed ? 'QC Passed' : `QC Failed: ${defectType}`
      });
    } catch (err) {
      console.warn('QC submission error:', err);
    }

    setShowQcModal(false);
    setSelectedOrderForQc(null);
    setSelectedTargetStageId(null);
    onRefresh();
  };

  const createNewOrder = async () => {
    try {
      const res = await api.post('/orders', newOrder);
      if (res.data) {
        onAddOrder(res.data);
      }
    } catch (err) {
      console.error('Failed to create order in database:', err);
    }
    setShowAddOrder(false);
    onRefresh();
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Kanban header */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div>
          <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Production Floor</h2>
          <p className="text-xs text-slate-500 mt-0.5">Drag and drop batches to proceed stages</p>
        </div>

        {user.role === 'ROLE_FACTORY_OWNER' && (
          <Button variant="primary" onClick={() => setShowAddOrder(true)}>
            <Plus className="w-4 h-4" />
            <span>Create Production Order</span>
          </Button>
        )}
      </div>

      {/* Grid columns */}
      <div className="flex-1 flex space-x-4 overflow-x-auto pb-4 items-stretch select-none">
        {stages.map(stage => {
          const stageOrders = orders.filter(o => o.currentStageId === stage.id);
          return (
            <div
              key={stage.id}
              onDragOver={handleDragOver}
              onDrop={(e) => handleDrop(e, stage.id)}
              className="w-72 bg-slate-100/60 border border-slate-200 rounded-xl p-4 flex flex-col shrink-0"
            >
              {/* Stage title header */}
              <div className="flex items-center justify-between mb-4 border-b border-slate-200 pb-2">
                <div className="flex items-center space-x-2">
                  <span 
                    className="w-2.5 h-2.5 rounded-full inline-block" 
                    style={{ background: stage.colorHex || '#3B82F6' }} 
                  />
                  <span className="text-xs font-bold text-slate-800 truncate max-w-[150px]">{stage.name}</span>
                </div>
                <span className="text-[10px] text-slate-500 font-mono font-bold bg-white border border-slate-200 px-2 py-0.5 rounded-full">
                  {stageOrders.length}
                </span>
              </div>

              {/* Orders cards slots */}
              <div className="flex-1 overflow-y-auto space-y-3">
                {stageOrders.map(order => {
                  const isBlocked = order.status === 'BLOCKED';
                  return (
                    <div
                      key={order.id}
                      draggable={!isBlocked}
                      onDragStart={(e) => handleDragStart(e, order.id)}
                      className={`p-4 rounded-lg border transition cursor-grab active:cursor-grabbing shadow-sm bg-white ${
                        isBlocked 
                          ? 'border-rose-300'
                          : 'border-slate-200 hover:border-slate-350'
                      }`}
                    >
                      <div className="flex justify-between items-start mb-2">
                        <span className="text-[10px] text-indigo-600 font-mono font-bold">{order.orderNumber}</span>
                        <Badge status={order.priority === 'HIGH' ? 'error' : 'warning'}>
                          {order.priority}
                        </Badge>
                      </div>

                      <h4 className="text-xs font-bold text-slate-850 truncate">{order.productName}</h4>
                      <p className="text-[10px] text-slate-400 mt-1">Client: {order.brandName}</p>

                      <div className="mt-3 pt-3 border-t border-slate-100 flex items-center justify-between text-[10px] text-slate-500">
                        <div className="flex items-center space-x-1">
                          <CheckSquare className="w-3.5 h-3.5 text-slate-405" />
                          <span className="font-mono">{order.quantity} pcs</span>
                        </div>

                        {isBlocked && (
                          <div className="flex items-center space-x-1 text-rose-600 font-bold">
                            <AlertOctagon className="w-3.5 h-3.5 text-rose-500" />
                            <span>QC Blocked</span>
                          </div>
                        )}
                      </div>
                    </div>
                  );
                })}

                {stageOrders.length === 0 && (
                  <div className="h-full flex items-center justify-center text-center text-slate-400 py-8 border border-dashed border-slate-200 rounded-xl">
                    <p className="text-[10px]">Drag batches here</p>
                  </div>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {/* 1. QC Audit Inspection Dialog */}
      <Dialog isOpen={showQcModal} onClose={() => { setShowQcModal(false); setSelectedOrderForQc(null); }} title="QC Inspection Audit">
        <p className="text-xs text-slate-500 mb-2">
          Executing check for batch <span className="text-indigo-600 font-semibold">{selectedOrderForQc?.orderNumber}</span>.
        </p>

        <div className="space-y-4 text-xs">
          <div className="flex space-x-4">
            <button
              onClick={() => setQcPassed(true)}
              type="button"
              className={`flex-1 py-2 rounded-lg border text-xs font-bold transition ${
                qcPassed 
                  ? 'bg-emerald-50 border-emerald-350 text-emerald-700' 
                  : 'bg-slate-50 border-slate-200 text-slate-500'
              }`}
            >
              QC Passed
            </button>
            <button
              onClick={() => setQcPassed(false)}
              type="button"
              className={`flex-1 py-2 rounded-lg border text-xs font-bold transition ${
                !qcPassed 
                  ? 'bg-rose-50 border-rose-350 text-rose-700' 
                  : 'bg-slate-50 border-slate-200 text-slate-500'
              }`}
            >
              QC Failed
            </button>
          </div>

          {!qcPassed && (
            <>
              <div className="space-y-1">
                <label className="block text-slate-500">Defect Category</label>
                <Select
                  value={defectType}
                  onChange={(e) => setDefectType(e.target.value)}
                >
                  <option value="Ink Smudge & Color Bleed">Ink Smudge & Color Bleed</option>
                  <option value="Dimension Tolerance Exceeded">Dimension Tolerance Exceeded</option>
                  <option value="Fabric Thread Stitching Misalignment">Stitching Misalignment</option>
                  <option value="Fabric Surface Stain">Surface Stain / Mark</option>
                </Select>
              </div>

              <div className="space-y-1">
                <label className="block text-slate-500">Defect Count</label>
                <Input
                  type="number"
                  value={defectCount}
                  onChange={(e) => setDefectCount(parseInt(e.target.value) || 0)}
                  min="1"
                />
              </div>
            </>
          )}

          <div className="space-y-1">
            <label className="block text-slate-500">Remarks / Notes</label>
            <textarea
              value={qcRemarks}
              onChange={(e) => setQcRemarks(e.target.value)}
              placeholder="Remarks..."
              rows={3}
              className="w-full px-3 py-2 bg-slate-55 border border-slate-200 rounded-lg text-slate-800 focus:outline-none resize-none text-xs"
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => { setShowQcModal(false); setSelectedOrderForQc(null); }}>
              Cancel
            </Button>
            <Button type="button" variant="primary" onClick={submitQcForm}>
              Submit Audit
            </Button>
          </div>
        </div>
      </Dialog>

      {/* 2. Create Order Dialog */}
      <Dialog isOpen={showAddOrder} onClose={() => setShowAddOrder(false)} title="Create Production Order">
        <div className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1">Target Client Brand</label>
            <Select
              value={newOrder.brandId}
              onChange={(e) => setNewOrder({ ...newOrder, brandId: e.target.value })}
            >
              <option value="nike-brand">Nike MSME Partner</option>
              <option value="adidas-brand">Adidas Performance</option>
              <option value="zara-brand">Zara Global Sourcing</option>
            </Select>
          </div>

          <div>
            <label className="block text-slate-500 mb-1">Product Description</label>
            <Input
              type="text"
              value={newOrder.productName}
              onChange={(e) => setNewOrder({ ...newOrder, productName: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-505 mb-1">Quantity (pcs)</label>
            <Input
              type="number"
              value={newOrder.quantity}
              onChange={(e) => setNewOrder({ ...newOrder, quantity: parseInt(e.target.value) || 0 })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1">Priority Rank</label>
            <Select
              value={newOrder.priority}
              onChange={(e) => setNewOrder({ ...newOrder, priority: e.target.value })}
            >
              <option value="HIGH">High Priority</option>
              <option value="MEDIUM">Medium Priority</option>
              <option value="LOW">Low Priority</option>
            </Select>
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowAddOrder(false)}>
              Cancel
            </Button>
            <Button type="button" variant="primary" onClick={createNewOrder}>
              Deploy Order
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
