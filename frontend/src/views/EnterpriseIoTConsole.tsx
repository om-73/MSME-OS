import React, { useState, useEffect } from 'react';
import { 
  Cpu, 
  Activity, 
  Zap, 
  Wrench, 
  AlertTriangle, 
  CheckCircle2, 
  XCircle, 
  Plus, 
  Sparkles, 
  Radio, 
  Clock, 
  Gauge, 
  Layers, 
  RefreshCw,
  HardDrive
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

interface EnterpriseIoTConsoleProps {
  user?: any;
}

export default function EnterpriseIoTConsole({ user }: EnterpriseIoTConsoleProps) {
  const [machines, setMachines] = useState<any[]>([]);
  const [devices, setDevices] = useState<any[]>([]);
  const [downtimes, setDowntimes] = useState<any[]>([]);
  const [maintenances, setMaintenances] = useState<any[]>([]);

  // Subtabs
  const [activeTab, setActiveTab] = useState<'machines' | 'oee' | 'devices' | 'maintenance' | 'downtime'>('machines');

  // Modals
  const [showMachineModal, setShowMachineModal] = useState(false);
  const [showMaintenanceModal, setShowMaintenanceModal] = useState(false);
  const [selectedMachine, setSelectedMachine] = useState<any | null>(null);
  const [statusMsg, setStatusMsg] = useState('');

  // Machine Form
  const [machineForm, setMachineForm] = useState({ machineCode: 'STITCH-005', name: 'Juki Heavy Duty Sewing', machineType: 'INDUSTRIAL_SEWING', department: 'Stitching', manufacturer: 'Juki' });

  // Maintenance Form
  const [maintenanceForm, setMaintenanceForm] = useState({ maintenanceType: 'PREVENTIVE', description: 'Routine motor alignment and oil filter change', sparePartsUsed: 'RM-OIL-FILTER-01, RM-BELT-A12' });

  const fetchIoTData = async () => {
    try {
      const [machRes, devRes, downRes, maintRes] = await Promise.all([
        api.get('/machines'),
        api.get('/machines/devices'),
        api.get('/machines/downtime'),
        api.get('/machines/maintenance')
      ]);
      setMachines(machRes.data || []);
      setDevices(devRes.data || []);
      setDowntimes(downRes.data || []);
      setMaintenances(maintRes.data || []);
    } catch (err) {
      console.error('Failed to fetch IoT and machine data from database:', err);
    }
  };

  useEffect(() => {
    fetchIoTData();
  }, []);

  const handleRegisterMachine = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/machines', machineForm);
      setStatusMsg('New machine registered in shop-floor inventory.');
      fetchIoTData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to register machine.');
    }
    setShowMachineModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleScheduleMaintenance = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedMachine) return;
    try {
      await api.post(`/machines/${selectedMachine.id}/maintenance`, maintenanceForm);
      setStatusMsg(`Maintenance task scheduled for ${selectedMachine.machineCode}.`);
      fetchIoTData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to schedule maintenance.');
    }
    setShowMaintenanceModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleSimulateTelemetry = async (machineId: number, isOverheat: boolean) => {
    try {
      await api.post(`/machines/${machineId}/telemetry`, {
        productionCount: 200,
        cycleTimeSeconds: 4.0,
        temperatureCelsius: isOverheat ? 92.5 : 68.0
      });
      setStatusMsg(isOverheat ? 'Critical Overheating Telemetry Ingested -> Machine Stopped & Alert Fired.' : 'Telemetry Ingested -> Normal Operation.');
      fetchIoTData();
    } catch (err) {
      console.error(err);
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Machine, IoT & Shop-Floor Equipment Integration</h2>
            <p className="text-xs text-slate-550 mt-0.5">Industry 4.0 telemetry ingestion, OEE metrics calculator, debounced downtime & Module 7 spare parts maintenance</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'machines', label: 'Machine Floor Grid' },
              { id: 'oee', label: 'OEE Analytics' },
              { id: 'devices', label: 'IoT Devices & PLCs' },
              { id: 'maintenance', label: 'Maintenance Desk' },
              { id: 'downtime', label: 'Downtime Logs' }
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

        <Button variant="primary" onClick={() => setShowMachineModal(true)}>
          <Plus className="w-4 h-4" />
          <span>Register Machine</span>
        </Button>
      </div>

      {statusMsg ? (
        <div className="mb-4 p-3 bg-indigo-50 border border-indigo-200 text-indigo-755 text-xs rounded-xl font-bold flex items-center space-x-2 shrink-0">
          <Sparkles className="w-4 h-4 text-indigo-650" />
          <span>{statusMsg}</span>
        </div>
      ) : null}

      {/* Main Workspace */}
      <div className="flex-1 bg-white border border-slate-200 rounded-xl p-6 flex flex-col shadow-sm overflow-hidden">
        
        {/* 1. MACHINE FLOOR GRID */}
        {activeTab === 'machines' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Cpu className="w-4 h-4 text-indigo-600" />
              <span>Shop-Floor Machine Status & Active Order Bindings</span>
            </h3>

            <div className="grid grid-cols-3 gap-6 overflow-y-auto pr-2">
              {machines.map(m => {
                const isRunning = m.status === 'RUNNING';
                const isStopped = m.status === 'STOPPED';
                const isMaint = m.status === 'MAINTENANCE';

                return (
                  <div key={m.id} className="p-5 bg-slate-50 border border-slate-200 rounded-2xl space-y-3 shadow-sm">
                    <div className="flex justify-between items-center">
                      <div>
                        <span className="font-bold text-slate-900 text-sm block">{m.name}</span>
                        <span className="text-[10px] text-indigo-600 font-mono font-bold">{m.machineCode}</span>
                      </div>
                      <Badge status={isRunning ? 'success' : isStopped ? 'error' : isMaint ? 'warning' : 'default'}>
                        {m.status}
                      </Badge>
                    </div>

                    <div className="grid grid-cols-2 gap-2 text-[11px] p-3 bg-white border border-slate-200 rounded-xl font-mono">
                      <div>
                        <span className="text-[10px] text-slate-400 block font-semibold">DEPARTMENT</span>
                        <span className="font-bold text-slate-800">{m.department}</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-slate-400 block font-semibold">ACTIVE ORDER</span>
                        <span className="font-bold text-indigo-600">{m.currentOrderId || 'None'}</span>
                      </div>
                      <div className="col-span-2 pt-1 border-t border-slate-100 flex justify-between items-center">
                        <span className="text-[10px] text-slate-400 font-semibold">OEE SCORE</span>
                        <span className="font-bold text-emerald-600 text-sm">{m.oeeScorePct}%</span>
                      </div>
                    </div>

                    <div className="flex justify-between items-center pt-2">
                      <Button variant="outline" onClick={() => { setSelectedMachine(m); setShowMaintenanceModal(true); }} className="!py-1">
                        <Wrench className="w-3.5 h-3.5" />
                        <span>Maintenance</span>
                      </Button>

                      <Button variant="outline" onClick={() => handleSimulateTelemetry(m.id, true)} className="!py-1 border-rose-200 text-rose-600 hover:bg-rose-50">
                        <AlertTriangle className="w-3.5 h-3.5" />
                        <span>Simulate Overheat</span>
                      </Button>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* 2. OEE ANALYTICS */}
        {activeTab === 'oee' && (
          <div className="flex-1 flex flex-col space-y-6 overflow-y-auto pr-2 text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider flex items-center space-x-1.5">
              <Gauge className="w-4 h-4 text-indigo-600" />
              <span>Overall Equipment Effectiveness (OEE) Metrics</span>
            </h3>

            <div className="grid grid-cols-4 gap-6">
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Plant Overall OEE</span>
                <span className="text-3xl font-bold text-emerald-650 font-mono">83.9%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Availability Rate</span>
                <span className="text-3xl font-bold text-slate-900 font-mono">90.7%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Performance Efficiency</span>
                <span className="text-3xl font-bold text-indigo-650 font-mono">95.0%</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Quality Pass Rate</span>
                <span className="text-3xl font-bold text-slate-800 font-mono">97.8%</span>
              </div>
            </div>
          </div>
        )}

        {/* 3. IOT DEVICES */}
        {activeTab === 'devices' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Radio className="w-4 h-4 text-indigo-600" />
              <span>Connected IoT Devices, PLCs & Gateways</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">Device Identifier</th>
                  <th className="pb-3">Device Type</th>
                  <th className="pb-3">Protocol Adapter</th>
                  <th className="pb-3">Network IP</th>
                  <th className="pb-3">Firmware</th>
                  <th className="pb-3">Status</th>
                </TableHead>
                <TableBody>
                  {devices.map(d => (
                    <tr key={d.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                      <td className="py-3 font-bold text-indigo-600 font-mono">{d.deviceId}</td>
                      <td className="py-3 font-semibold text-slate-800">{d.deviceType}</td>
                      <td className="py-3 font-mono text-slate-600">{d.protocol}</td>
                      <td className="py-3 font-mono text-slate-500">{d.ipAddress}</td>
                      <td className="py-3 font-mono">{d.firmwareVersion}</td>
                      <td className="py-3"><Badge status="success">{d.status}</Badge></td>
                    </tr>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

        {/* 4. MAINTENANCE DESK */}
        {activeTab === 'maintenance' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Wrench className="w-4 h-4 text-indigo-600" />
              <span>Maintenance Records & Module 7 Spare Parts Usage</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {maintenances.map(m => (
                <div key={m.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2 text-xs">
                  <div className="flex justify-between items-center">
                    <span className="font-bold text-slate-900">{m.description}</span>
                    <Badge status="success">{m.status}</Badge>
                  </div>
                  <p className="text-slate-600">Type: <strong>{m.maintenanceType}</strong> | Technician: {m.technicianName}</p>
                  <p className="text-slate-600 font-mono">Module 7 Spare Parts Used: <strong className="text-indigo-600">{m.sparePartsUsed}</strong></p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 5. DOWNTIME LOGS */}
        {activeTab === 'downtime' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Clock className="w-4 h-4 text-indigo-600" />
              <span>Debounced Machine Downtime Audit Logs</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {downtimes.map(dt => (
                <div key={dt.id} className="p-4 bg-rose-50 border border-rose-200 rounded-xl space-y-2 text-rose-900">
                  <div className="flex justify-between items-center font-bold">
                    <span>Reason: {dt.downtimeReason}</span>
                    <Badge status="error">{dt.durationMinutes} Mins Duration</Badge>
                  </div>
                  <p className="text-xs text-rose-800">{dt.notes}</p>
                </div>
              ))}
            </div>
          </div>
        )}

      </div>

      {/* 1. Register Machine Modal */}
      <Dialog isOpen={showMachineModal} onClose={() => setShowMachineModal(false)} title="Register Shop-Floor Machine">
        <form onSubmit={handleRegisterMachine} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Machine Code</label>
            <Input
              type="text"
              value={machineForm.machineCode}
              onChange={(e) => setMachineForm({ ...machineForm, machineCode: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Machine Name</label>
            <Input
              type="text"
              value={machineForm.name}
              onChange={(e) => setMachineForm({ ...machineForm, name: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Department</label>
            <Select value={machineForm.department} onChange={(e) => setMachineForm({ ...machineForm, department: e.target.value })}>
              <option value="Stitching">Stitching</option>
              <option value="Cutting">Cutting</option>
              <option value="Pressing">Pressing</option>
              <option value="QC">QC Inspection</option>
            </Select>
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowMachineModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Register Machine
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Maintenance Modal */}
      <Dialog isOpen={showMaintenanceModal} onClose={() => setShowMaintenanceModal(false)} title={`Schedule Maintenance: ${selectedMachine?.machineCode}`}>
        <form onSubmit={handleScheduleMaintenance} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Maintenance Type</label>
            <Select value={maintenanceForm.maintenanceType} onChange={(e) => setMaintenanceForm({ ...maintenanceForm, maintenanceType: e.target.value })}>
              <option value="PREVENTIVE">Preventive Maintenance</option>
              <option value="CORRECTIVE">Corrective Repair</option>
              <option value="EMERGENCY">Emergency Maintenance</option>
            </Select>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Description</label>
            <Input
              type="text"
              value={maintenanceForm.description}
              onChange={(e) => setMaintenanceForm({ ...maintenanceForm, description: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Module 7 Spare Parts Used</label>
            <Input
              type="text"
              value={maintenanceForm.sparePartsUsed}
              onChange={(e) => setMaintenanceForm({ ...maintenanceForm, sparePartsUsed: e.target.value })}
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowMaintenanceModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Schedule & Set Status
            </Button>
          </div>
        </form>
      </Dialog>
    </div>
  );
}
