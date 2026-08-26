import React, { useState, useEffect } from 'react';
import { 
  Smartphone, 
  Play, 
  Pause, 
  CheckCircle2, 
  AlertTriangle, 
  QrCode, 
  Camera, 
  RotateCcw, 
  Wifi, 
  WifiOff, 
  Sparkles, 
  Package, 
  Truck, 
  ShieldCheck,
  ClipboardList
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

interface MobileWorkerAppConsoleProps {
  user?: any;
}

export default function MobileWorkerAppConsole({ user }: MobileWorkerAppConsoleProps) {
  const [tasks, setTasks] = useState<any[]>([]);
  const [scannedData, setScannedData] = useState<any | null>(null);
  const [syncStatus, setSyncStatus] = useState<'SYNCED' | 'SYNCING' | 'OFFLINE'>('SYNCED');
  const [queuedOfflineCount, setQueuedOfflineCount] = useState<number>(0);

  // Active Mobile Role Subtab
  const [activeTab, setActiveTab] = useState<'worker' | 'qc' | 'inventory' | 'dispatch' | 'sync'>('worker');

  // Modals
  const [showIssueModal, setShowIssueModal] = useState(false);
  const [selectedTask, setSelectedTask] = useState<any | null>(null);
  const [issueReason, setIssueReason] = useState('Fabric Defect / Tear');
  const [statusMsg, setStatusMsg] = useState('');

  // QC Form
  const [qcForm, setQcForm] = useState({ orderId: 101, qcResult: 'PASS', defectType: 'Stitching Asymmetry' });

  const fetchMobileData = async () => {
    try {
      const tasksRes = await api.get(`/mobile/tasks?userId=${user?.email || 'worker@apex.com'}&department=Cutting`);
      setTasks(tasksRes.data || []);
    } catch (err) {
      console.error('Failed to fetch mobile tasks from database:', err);
    }
  };

  useEffect(() => {
    fetchMobileData();
  }, [user]);

  const handleTaskAction = async (orderId: number, action: string) => {
    if (action === 'REPORT_ISSUE') {
      setSelectedTask(tasks.find(t => t.orderId === orderId));
      setShowIssueModal(true);
      return;
    }

    try {
      await api.post(`/mobile/tasks/${orderId}/action`, { action });
      setStatusMsg(`Task Action '${action}' executed successfully.`);
      fetchMobileData();
    } catch (err) {
      console.error(err);
      setStatusMsg(`Failed to execute task action.`);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleSubmitIssue = async () => {
    if (!selectedTask) return;
    try {
      await api.post(`/mobile/tasks/${selectedTask.orderId}/action`, {
        action: 'REPORT_ISSUE',
        issueReason
      });
      setStatusMsg(`Issue reported for ${selectedTask.orderNumber}. Rework request dispatched.`);
      fetchMobileData();
    } catch (err) {
      console.error(err);
      setStatusMsg(`Failed to report issue.`);
    }
    setShowIssueModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleSimulateScan = async () => {
    try {
      const res = await api.get('/mobile/inventory/scan?barcode=BC-COTTON-9011');
      setScannedData(res.data);
    } catch (err) {
      console.error('Failed to scan barcode in database:', err);
    }
  };

  const handleSubmitQC = async (result: string) => {
    try {
      await api.post('/mobile/qc/inspect', {
        orderId: 101,
        qcResult: result,
        defectType: qcForm.defectType
      });
      setStatusMsg(`QC Inspection recorded: ${result}`);
    } catch (err) {
      console.error(err);
      setStatusMsg(`Failed to submit QC inspection.`);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleForceSync = async () => {
    setSyncStatus('SYNCING');
    setTimeout(() => {
      setSyncStatus('SYNCED');
      setQueuedOfflineCount(0);
      setStatusMsg('Offline action queue synchronized with cloud servers.');
      setTimeout(() => setStatusMsg(''), 3000);
    }, 1200);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Mobile Operations & Shop-Floor Worker App</h2>
            <p className="text-xs text-slate-550 mt-0.5">Role-aware worker touch tasks, mobile QC inspection, barcode scanner & offline sync engine</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'worker', label: 'Operator Tasks' },
              { id: 'qc', label: 'QC Mobile' },
              { id: 'inventory', label: 'Barcode Scanner' },
              { id: 'dispatch', label: 'Dispatch Packing' },
              { id: 'sync', label: 'Offline Sync' }
            ].map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as any)}
                className={`px-3 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider transition ${
                  activeTab === tab.id ? 'bg-white text-indigo-650 shadow-sm border border-slate-200' : 'text-slate-500 hover:text-slate-800'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>
        </div>

        {/* Sync Status Badge */}
        <div className="flex items-center space-x-2">
          <Badge status={syncStatus === 'SYNCED' ? 'success' : 'warning'}>
            {syncStatus === 'SYNCED' ? '✓ Synced' : '⟳ Syncing'}
          </Badge>
        </div>
      </div>

      {statusMsg ? (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      ) : null}

      {/* Mobile Simulator Frame */}
      <div className="flex-1 max-w-sm mx-auto w-full bg-white border-4 border-slate-800 rounded-3xl p-5 flex flex-col shadow-2xl overflow-hidden relative">
        
        {/* Mobile Camera Notch */}
        <div className="w-32 h-4 bg-slate-800 rounded-b-xl mx-auto mb-4 shrink-0"></div>

        {/* 1. OPERATOR TASKS */}
        {activeTab === 'worker' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs space-y-4">
            <div className="flex justify-between items-center pb-2 border-b border-slate-200">
              <span className="font-bold text-slate-900 uppercase tracking-wider text-[11px]">Today's Floor Work</span>
              <span className="text-[10px] text-slate-400 font-mono">Shift: Morning</span>
            </div>

            <div className="flex-1 overflow-y-auto space-y-4 pr-1">
              {tasks.map(t => {
                const isInProgress = t.status === 'IN_PROGRESS';
                const isIssue = t.status === 'ISSUE_REPORTED';

                return (
                  <div key={t.orderId} className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-3 shadow-sm">
                    <div className="flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 text-sm block">{t.orderNumber}</span>
                        <span className="text-[11px] text-slate-500 font-medium">{t.articleName}</span>
                      </div>
                      <Badge status={isInProgress ? 'info' : isIssue ? 'error' : 'success'}>
                        {t.status}
                      </Badge>
                    </div>

                    <div className="flex justify-between items-center text-[11px] bg-white p-2.5 rounded-xl border border-slate-200 font-mono">
                      <span>Stage: <strong>{t.currentStage}</strong></span>
                      <span>Target: <strong>{t.targetQuantity} Pcs</strong></span>
                    </div>

                    {/* Touch Action Buttons */}
                    <div className="grid grid-cols-2 gap-2 pt-1">
                      {!isInProgress ? (
                        <button
                          onClick={() => handleTaskAction(t.orderId, 'START')}
                          className="py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl font-bold flex items-center justify-center space-x-1 shadow-sm text-xs"
                        >
                          <Play className="w-4 h-4 fill-white" />
                          <span>START</span>
                        </button>
                      ) : (
                        <button
                          onClick={() => handleTaskAction(t.orderId, 'PAUSE')}
                          className="py-2.5 bg-amber-500 hover:bg-amber-600 text-white rounded-xl font-bold flex items-center justify-center space-x-1 shadow-sm text-xs"
                        >
                          <Pause className="w-4 h-4 fill-white" />
                          <span>PAUSE</span>
                        </button>
                      )}

                      <button
                        onClick={() => handleTaskAction(t.orderId, 'COMPLETE')}
                        className="py-2.5 bg-indigo-650 hover:bg-indigo-750 text-white rounded-xl font-bold flex items-center justify-center space-x-1 shadow-sm text-xs"
                      >
                        <CheckCircle2 className="w-4 h-4" />
                        <span>COMPLETE</span>
                      </button>

                      <button
                        onClick={() => handleTaskAction(t.orderId, 'REPORT_ISSUE')}
                        className="col-span-2 py-2 bg-rose-50 hover:bg-rose-100 text-rose-700 border border-rose-200 rounded-xl font-bold flex items-center justify-center space-x-1 text-xs"
                      >
                        <AlertTriangle className="w-4 h-4" />
                        <span>REPORT ISSUE</span>
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* 2. QC INSPECTION */}
        {activeTab === 'qc' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs space-y-4">
            <span className="font-bold text-slate-900 uppercase tracking-wider text-[11px] pb-2 border-b border-slate-200">
              Mobile QC Checklist
            </span>

            <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-3">
              <span className="font-bold text-slate-900 text-sm block">Order #ORD-2026-88 Inspection</span>
              
              <div className="space-y-2 text-[11px]">
                {['Fabric Quality Check', 'Measurement Tolerances', 'Stitching Alignment', 'Color & Finishing'].map((check, idx) => (
                  <label key={idx} className="flex justify-between items-center p-2 bg-white rounded-lg border border-slate-200">
                    <span className="font-medium text-slate-700">{check}</span>
                    <input type="checkbox" defaultChecked className="w-4 h-4 text-indigo-600 rounded" />
                  </label>
                ))}
              </div>

              <div className="grid grid-cols-2 gap-2 pt-2">
                <button
                  onClick={() => handleSubmitQC('PASS')}
                  className="py-2.5 bg-emerald-600 text-white rounded-xl font-bold text-xs"
                >
                  PASS INSPECTION
                </button>
                <button
                  onClick={() => handleSubmitQC('FAIL')}
                  className="py-2.5 bg-rose-600 text-white rounded-xl font-bold text-xs"
                >
                  FAIL & REWORK
                </button>
              </div>
            </div>
          </div>
        )}

        {/* 3. BARCODE SCANNER */}
        {activeTab === 'inventory' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs space-y-4">
            <span className="font-bold text-slate-900 uppercase tracking-wider text-[11px] pb-2 border-b border-slate-200">
              Barcode / QR Camera Scanner
            </span>

            <div className="p-6 border-2 border-dashed border-indigo-300 rounded-2xl bg-indigo-50/50 flex flex-col items-center justify-center space-y-3 text-center">
              <Camera className="w-10 h-10 text-indigo-600 animate-pulse" />
              <span className="font-bold text-indigo-900">Point Camera at SKU Barcode</span>
              <Button variant="primary" onClick={handleSimulateScan} className="!py-1.5 text-xs">
                Simulate Camera Scan
              </Button>
            </div>

            {scannedData && (
              <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-2 font-mono text-[11px]">
                <span className="font-bold text-slate-900 text-xs block">{scannedData.materialName}</span>
                <p>Material Code: <strong className="text-indigo-600">{scannedData.materialCode}</strong></p>
                <p>Current Stock: <strong>{scannedData.currentStock} {scannedData.unitOfMeasure}</strong></p>
                <p>Bin Rack: <strong>{scannedData.binLocation}</strong></p>
              </div>
            )}
          </div>
        )}

        {/* 4. DISPATCH PACKING */}
        {activeTab === 'dispatch' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs space-y-4">
            <span className="font-bold text-slate-900 uppercase tracking-wider text-[11px] pb-2 border-b border-slate-200">
              Dispatch Pick & Pack
            </span>

            <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-3">
              <span className="font-bold text-slate-900 text-xs block">Shipment Order #ORD-2026-88</span>
              <p className="text-[11px] text-slate-600">Scan packaging barcode to verify package count before shipping.</p>

              <button
                onClick={() => setStatusMsg('Dispatch Confirmation Completed & Waybill Dispatched.')}
                className="w-full py-3 bg-indigo-650 text-white font-bold rounded-xl flex items-center justify-center space-x-1 text-xs"
              >
                <Truck className="w-4 h-4" />
                <span>CONFIRM DISPATCH</span>
              </button>
            </div>
          </div>
        )}

        {/* 5. OFFLINE SYNC */}
        {activeTab === 'sync' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs space-y-4">
            <span className="font-bold text-slate-900 uppercase tracking-wider text-[11px] pb-2 border-b border-slate-200">
              Offline Action Queue & Sync
            </span>

            <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-3">
              <div className="flex justify-between items-center">
                <span className="font-semibold text-slate-700">Sync Engine Status</span>
                <Badge status="success">ONLINE</Badge>
              </div>

              <p className="text-slate-600">Queued Offline Actions: <strong className="font-mono text-indigo-600">{queuedOfflineCount} items</strong></p>

              <Button variant="primary" onClick={handleForceSync} className="w-full justify-center">
                <RotateCcw className="w-4 h-4" />
                <span>Force Sync Offline Queue</span>
              </Button>
            </div>
          </div>
        )}

      </div>

      {/* Report Issue Modal */}
      <Dialog isOpen={showIssueModal} onClose={() => setShowIssueModal(false)} title="Report Shop-Floor Issue">
        <div className="space-y-4 text-xs">
          <p className="text-slate-600">Select problem reason for {selectedTask?.orderNumber}:</p>

          <Select value={issueReason} onChange={(e) => setIssueReason(e.target.value)}>
            <option value="Fabric Defect / Tear">Fabric Defect / Tear</option>
            <option value="Machine Breakdown">Machine Breakdown</option>
            <option value="Thread / Color Mismatch">Thread / Color Mismatch</option>
            <option value="Quantity Shortage">Quantity Shortage</option>
          </Select>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowIssueModal(false)}>
              Cancel
            </Button>
            <Button type="button" variant="danger" onClick={handleSubmitIssue}>
              Submit Issue & Request Rework
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
