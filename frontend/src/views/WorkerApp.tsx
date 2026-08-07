import React, { useState, useEffect } from 'react';
import { 
  Play, 
  Pause, 
  CheckCircle, 
  AlertTriangle, 
  QrCode, 
  Camera,
  ClipboardList,
  CheckSquare,
  HelpCircle,
  Clock,
  Sparkles
} from 'lucide-react';
import axios from 'axios';
import { Button, Card, Badge, Dialog, Input, Select } from '../components/DesignSystem';

interface WorkerAppProps {
  user: any;
}

export default function WorkerApp({ user }: WorkerAppProps) {
  const [tasks, setTasks] = useState<any[]>([]);
  const [selectedTask, setSelectedTask] = useState<any | null>(null);
  
  // Scanners / Modals
  const [showScanner, setShowScanner] = useState(false);
  const [scanCode, setScanCode] = useState('');
  const [showIssueModal, setShowIssueModal] = useState(false);
  const [showCompleteModal, setShowCompleteModal] = useState(false);

  // Issues forms
  const [issueType, setIssueType] = useState('Machine Breakdown');
  const [issueRemarks, setIssueRemarks] = useState('');

  // Complete task check-offs
  const [checklistValues, setChecklistValues] = useState<Record<string, boolean>>({
    'Visual check OK': false,
    'Dimensions verify': false,
    'Quantity match confirmation': false
  });
  const [remarks, setRemarks] = useState('');
  const [photoUrl, setPhotoUrl] = useState('');

  const [statusMsg, setStatusMsg] = useState('');

  const fetchWorkerTasks = async () => {
    try {
      const res = await axios.get(`http://localhost:8085/api/v1/worker/tasks/my/${user.email || 'operator@apex.com'}`, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setTasks(res.data);
    } catch (err) {
      // Mock seeder
      setTasks([
        { id: 1, orderId: 'ord-1', orderNumber: 'ORD-2026-101', productName: 'Premium Dri-FIT Running Tops', stageId: 's-2', stageName: 'Stitching & Cuffs', status: 'ASSIGNED', remarks: 'Standard run cuffs spec', safetyStock: 0 },
        { id: 2, orderId: 'ord-2', orderNumber: 'ORD-2026-102', productName: 'Eco Cotton Summer Polos', stageId: 's-2', stageName: 'Stitching & Cuffs', status: 'PENDING', remarks: 'High thread density stitching', safetyStock: 0 }
      ]);
    }
  };

  useEffect(() => {
    fetchWorkerTasks();
  }, [user]);

  const handleStartTask = async (task: any) => {
    try {
      const res = await axios.post(`http://localhost:8085/api/v1/worker/tasks/${task.id}/start`, {
        operatorName: user.fullName
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Job started. Timer is active.');
      setSelectedTask(res.data);
      fetchWorkerTasks();
    } catch (err) {
      const updated = { ...task, status: 'IN_PROGRESS', startTime: new Date().toISOString() };
      setSelectedTask(updated);
      setTasks(prev => prev.map(t => t.id === task.id ? updated : t));
      setStatusMsg('Job started. (Mock)');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handlePauseTask = async (task: any) => {
    try {
      const res = await axios.post(`http://localhost:8085/api/v1/worker/tasks/${task.id}/pause`, {
        operatorName: user.fullName
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Job paused.');
      setSelectedTask(res.data);
      fetchWorkerTasks();
    } catch (err) {
      const updated = { ...task, status: 'PAUSED' };
      setSelectedTask(updated);
      setTasks(prev => prev.map(t => t.id === task.id ? updated : t));
      setStatusMsg('Job paused. (Mock)');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleCompleteTaskSubmit = async () => {
    if (!selectedTask) return;

    try {
      await axios.post(`http://localhost:8085/api/v1/worker/tasks/${selectedTask.id}/complete`, {
        remarks: remarks || 'Checked and validated.',
        photoUrl: photoUrl || 'https://images.unsplash.com/photo-1558449028-b53a39d105fc',
        operatorName: user.fullName
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Job complete! Order moved automatically to next queue.');
    } catch (err) {
      setStatusMsg('Job complete! Order queue updated. (Mock)');
    }

    setTasks(prev => prev.filter(t => t.id !== selectedTask.id));
    setSelectedTask(null);
    setShowCompleteModal(false);
    setRemarks('');
    setPhotoUrl('');
    setChecklistValues({
      'Visual check OK': false,
      'Dimensions verify': false,
      'Quantity match confirmation': false
    });
    setTimeout(() => setStatusMsg(''), 4000);
  };

  const handleReportIssueSubmit = async () => {
    if (!selectedTask) return;

    try {
      await axios.post(`http://localhost:8085/api/v1/worker/tasks/${selectedTask.id}/report-issue`, {
        issueType,
        remarks: issueRemarks,
        operatorName: user.fullName
      }, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Floor issue logged to Department Manager.');
    } catch (err) {
      setStatusMsg('Floor issue logged. (Mock)');
    }

    const updated = { ...selectedTask, status: 'BLOCKED', remarks: `Blocked: ${issueType}. detail: ${issueRemarks}` };
    setSelectedTask(updated);
    setTasks(prev => prev.map(t => t.id === selectedTask.id ? updated : t));
    setShowIssueModal(false);
    setIssueRemarks('');
    setTimeout(() => setStatusMsg(''), 4050);
  };

  const handleSimulateScan = () => {
    const task = tasks.find(t => t.orderNumber.toLowerCase() === scanCode.toLowerCase().trim());
    if (task) {
      setSelectedTask(task);
      setStatusMsg(`Scanned waybill successfully: ${task.orderNumber}`);
    } else {
      setStatusMsg(`Order tag ID not found in queue: ${scanCode}`);
    }
    setShowScanner(false);
    setScanCode('');
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-6 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Simplified Mobile-Like Header */}
      <div className="flex justify-between items-center bg-white border border-slate-200 rounded-xl p-4 mb-6 shadow-sm shrink-0">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-full bg-slate-100 flex items-center justify-center border border-slate-205">
            <ClipboardList className="w-5 h-5 text-indigo-650" />
          </div>
          <div>
            <h2 className="text-sm font-bold text-slate-805 leading-none">Task Console</h2>
            <span className="text-[10px] text-slate-400 font-semibold uppercase mt-0.5 block">Logged: {user.fullName}</span>
          </div>
        </div>

        <button
          onClick={() => setShowScanner(true)}
          className="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-bold rounded-lg transition flex items-center space-x-2 shadow-sm"
        >
          <QrCode className="w-4 h-4" />
          <span>Scan Waybill QR</span>
        </button>
      </div>

      {statusMsg && (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-700 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4" />
          <span>{statusMsg}</span>
        </div>
      )}

      {/* Main Grid: Mobile workflow task cards & current active workspace */}
      <div className="flex-1 flex space-x-6 overflow-hidden">
        
        {/* Left Side: Tasks queue */}
        <div className="w-96 bg-white border border-slate-200 rounded-xl p-5 flex flex-col shrink-0 overflow-hidden shadow-sm">
          <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider mb-4">Assigned Operations</h3>
          
          <div className="flex-1 overflow-y-auto space-y-3">
            {tasks.map(t => {
              const isSelected = selectedTask?.id === t.id;
              const isBlocked = t.status === 'BLOCKED';
              return (
                <button
                  key={t.id}
                  onClick={() => setSelectedTask(t)}
                  className={`w-full text-left p-4 rounded-xl border transition flex flex-col space-y-2 ${
                    isSelected 
                      ? 'bg-indigo-50 border-indigo-200 text-indigo-900 shadow-sm' 
                      : 'bg-slate-50 border-slate-200 text-slate-600 hover:bg-slate-100'
                  }`}
                >
                  <div className="flex justify-between items-start w-full text-xs">
                    <span className="font-mono font-bold text-indigo-650">{t.orderNumber}</span>
                    <Badge status={isBlocked ? 'error' : t.status === 'IN_PROGRESS' ? 'info' : 'default'}>
                      {t.status}
                    </Badge>
                  </div>
                  <h4 className="text-xs font-bold text-slate-850 truncate">{t.productName}</h4>
                  <span className="text-[10px] text-slate-400 font-semibold uppercase">{t.stageName}</span>
                </button>
              );
            })}

            {tasks.length === 0 && (
              <p className="text-center py-12 text-slate-400 text-xs">All assignments completed.</p>
            )}
          </div>
        </div>

        {/* Right Side: Active Workspace console with BIG buttons */}
        <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col overflow-hidden shadow-sm">
          {selectedTask ? (
            <div className="flex-1 flex flex-col justify-between overflow-y-auto">
              
              {/* Task descriptions */}
              <div className="space-y-6">
                <div>
                  <Badge status="info">Active Workload</Badge>
                  <h3 className="text-lg font-bold text-slate-900 mt-2">{selectedTask.productName}</h3>
                  <span className="text-xs text-slate-550 font-mono">Order ref: {selectedTask.orderNumber} | Current stage: {selectedTask.stageName}</span>
                </div>

                {selectedTask.remarks && (
                  <div className="p-4 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-600">
                    <span className="font-bold text-slate-700 block mb-1">Supervisor Notes:</span>
                    {selectedTask.remarks}
                  </div>
                )}

                {/* Big Checklist checks */}
                <div className="space-y-3">
                  <h4 className="text-xs font-bold text-slate-700 uppercase tracking-wider flex items-center space-x-1">
                    <CheckSquare className="w-4 h-4 text-indigo-605" />
                    <span>Operations Checks</span>
                  </h4>
                  {Object.keys(checklistValues).map(key => (
                    <label key={key} className="flex items-center space-x-3 p-3 bg-slate-50 border border-slate-200 rounded-lg cursor-pointer hover:bg-slate-100 text-xs font-semibold">
                      <input
                        type="checkbox"
                        checked={checklistValues[key]}
                        onChange={() => setChecklistValues({ ...checklistValues, [key]: !checklistValues[key] })}
                        className="w-4 h-4 rounded text-indigo-600 border-slate-300 focus:ring-indigo-500"
                      />
                      <span>{key}</span>
                    </label>
                  ))}
                </div>
              </div>

              {/* Big buttons console block */}
              <div className="mt-8 pt-6 border-t border-slate-200 space-y-4">
                {selectedTask.status === 'BLOCKED' ? (
                  <div className="p-4 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-xl flex items-center space-x-2">
                    <AlertTriangle className="w-5 h-5 text-rose-500 shrink-0" />
                    <span>Task blocked. supervisor has been notified to inspect workspace.</span>
                  </div>
                ) : (
                  <div className="grid grid-cols-2 gap-4">
                    {selectedTask.status === 'IN_PROGRESS' ? (
                      <button
                        onClick={() => handlePauseTask(selectedTask)}
                        className="py-5 bg-amber-500 hover:bg-amber-600 text-white rounded-xl font-extrabold text-sm transition flex flex-col items-center justify-center space-y-2 shadow-sm"
                      >
                        <Pause className="w-6 h-6" />
                        <span>PAUSE JOB</span>
                      </button>
                    ) : (
                      <button
                        onClick={() => handleStartTask(selectedTask)}
                        className="py-5 bg-indigo-600 hover:bg-indigo-755 text-white rounded-xl font-extrabold text-sm transition flex flex-col items-center justify-center space-y-2 shadow-sm"
                      >
                        <Play className="w-6 h-6 animate-pulse" />
                        <span>START JOB</span>
                      </button>
                    )}

                    <button
                      onClick={() => setShowCompleteModal(true)}
                      disabled={selectedTask.status !== 'IN_PROGRESS'}
                      className={`py-5 text-white rounded-xl font-extrabold text-sm transition flex flex-col items-center justify-center space-y-2 shadow-sm ${
                        selectedTask.status === 'IN_PROGRESS' ? 'bg-emerald-600 hover:bg-emerald-700' : 'bg-slate-300 cursor-not-allowed'
                      }`}
                    >
                      <CheckCircle className="w-6 h-6" />
                      <span>COMPLETE JOB</span>
                    </button>
                  </div>
                )}

                <button
                  onClick={() => setShowIssueModal(true)}
                  disabled={selectedTask.status === 'BLOCKED'}
                  className={`w-full py-3.5 border font-extrabold rounded-xl transition flex items-center justify-center space-x-2 text-xs shadow-sm ${
                    selectedTask.status === 'BLOCKED' ? 'bg-slate-100 border-slate-200 text-slate-400 cursor-not-allowed' : 'border-rose-200 bg-rose-50 hover:bg-rose-100 text-rose-600'
                  }`}
                >
                  <AlertTriangle className="w-4 h-4" />
                  <span>REPORT OPERATIONAL ISSUE</span>
                </button>
              </div>

            </div>
          ) : (
            <div className="h-full flex flex-col items-center justify-center text-center text-slate-400 space-y-3">
              <QrCode className="w-12 h-12 text-slate-300" />
              <div>
                <p className="text-xs font-bold text-slate-500">Workspace Empty</p>
                <p className="text-[11px] text-slate-400 mt-1 max-w-[240px]">Scan a batch waybill QR card or select an active operation task from the assignment list.</p>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* 1. Barcode QR Scanner Simulation Dialog */}
      <Dialog isOpen={showScanner} onClose={() => setShowScanner(false)} title="Scan Waybill QR Tag">
        <div className="space-y-4 text-xs">
          <div className="p-6 bg-slate-100 rounded-xl border border-slate-205 text-center flex flex-col items-center justify-center space-y-3">
            <Camera className="w-8 h-8 text-slate-400" />
            <span className="text-slate-500 font-semibold">Camera scanner ready...</span>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Input Waybill Code (e.g. ORD-2026-101)</label>
            <Input
              type="text"
              placeholder="Type order tag..."
              value={scanCode}
              onChange={(e) => setScanCode(e.target.value)}
            />
          </div>

          <div className="flex space-x-3 pt-2 justify-end">
            <Button type="button" onClick={() => setShowScanner(false)}>
              Cancel
            </Button>
            <Button type="button" variant="primary" onClick={handleSimulateScan}>
              Simulate Scan Check
            </Button>
          </div>
        </div>
      </Dialog>

      {/* 2. Issue Reporting Dialog */}
      <Dialog isOpen={showIssueModal} onClose={() => setShowIssueModal(false)} title="Report Operational Issue">
        <div className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Issue Category</label>
            <Select
              value={issueType}
              onChange={(e) => setIssueType(e.target.value)}
            >
              <option value="Machine Breakdown">Machine Breakdown</option>
              <option value="Material Shortage">Material Shortage</option>
              <option value="QC Defect Check">Quality Issue / Defect Check</option>
              <option value="Missing trims/accessories">Missing Components</option>
              <option value="Supervisor Help Required">Need Supervisor Assist</option>
            </Select>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Detail explanation</label>
            <textarea
              value={issueRemarks}
              onChange={(e) => setIssueRemarks(e.target.value)}
              placeholder="e.g. Stitching motor overheating..."
              rows={3}
              className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none resize-none text-xs"
              required
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowIssueModal(false)}>
              Cancel
            </Button>
            <Button type="button" variant="danger" onClick={handleReportIssueSubmit}>
              Submit Issue Alert
            </Button>
          </div>
        </div>
      </Dialog>

      {/* 3. Job Completion Sign-Off Dialog */}
      <Dialog isOpen={showCompleteModal} onClose={() => setShowCompleteModal(false)} title="Job Completion Sign-Off">
        <div className="space-y-4 text-xs">
          <p className="text-slate-505 leading-relaxed">
            Please type remarks and attach quality validation photo to route order to next department.
          </p>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Operator Comments</label>
            <Input
              type="text"
              placeholder="All seams aligned, ready."
              value={remarks}
              onChange={(e) => setRemarks(e.target.value)}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Verification Photo URL (Optional)</label>
            <Input
              type="text"
              placeholder="https://images.unsplash.com/photo..."
              value={photoUrl}
              onChange={(e) => setPhotoUrl(e.target.value)}
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowCompleteModal(false)}>
              Cancel
            </Button>
            <Button type="button" variant="primary" onClick={handleCompleteTaskSubmit}>
              Submit Sign-Off
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
