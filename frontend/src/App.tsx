import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import Onboarding from './views/Onboarding';
import Dashboard from './views/Dashboard';
import WorkflowBuilder from './views/WorkflowBuilder';
import KanbanBoard from './views/KanbanBoard';
import BrandPortal from './views/BrandPortal';
import Notifications from './views/Notifications';
import InventoryDashboard from './views/InventoryDashboard';
import InventoryLedger from './views/InventoryLedger';
import InventoryAuditManager from './views/InventoryAuditManager';
import WorkerApp from './views/WorkerApp';
import DispatchDashboard from './views/DispatchDashboard';
import axios from 'axios';

export default function App() {
  const [user, setUser] = useState<any | null>(null);
  const [currentTab, setCurrentTab] = useState('dashboard');
  
  // App Wide Seed State (with fallback data seeder)
  const [orders, setOrders] = useState<any[]>([]);
  const [stages, setStages] = useState<any[]>([]);
  const [notifications, setNotifications] = useState<any[]>([]);
  const [inventory, setInventory] = useState<any[]>([]);
  const [ledger, setLedger] = useState<any[]>([]);
  const [audits, setAudits] = useState<any[]>([]);
  const [brands, setBrands] = useState<any[]>([
    { id: 'nike-brand', name: 'Nike MSME Partner' },
    { id: 'adidas-brand', name: 'Adidas Performance' }
  ]);

  // Seed fallback mock data instantly so the UI is immediately populated
  const loadMockData = () => {
    const mockStages = [
      { id: '1', name: 'Order Received', code: 'ORDER_RECEIVED', type: 'START', colorHex: '#3B82F6', sequenceOrder: 1, estimatedSlaHours: 2 },
      { id: '2', name: 'Fabric Cutting', code: 'CUTTING', type: 'NORMAL', colorHex: '#8B5CF6', sequenceOrder: 2, estimatedSlaHours: 12 },
      { id: '3', name: 'Printing & Sublimation', code: 'PRINTING', type: 'NORMAL', colorHex: '#EC4899', sequenceOrder: 3, estimatedSlaHours: 24 },
      { id: '4', name: 'Stitching & Assembly', code: 'ASSEMBLY', type: 'NORMAL', colorHex: '#F59E0B', sequenceOrder: 4, estimatedSlaHours: 36 },
      { id: '5', name: 'Quality Check (QC Gate)', code: 'QC', type: 'QC', colorHex: '#10B981', sequenceOrder: 5, estimatedSlaHours: 6 },
      { id: '6', name: 'Dispatch', code: 'DISPATCH', type: 'END', colorHex: '#64748B', sequenceOrder: 6, estimatedSlaHours: 2 }
    ];

    const mockOrders = [
      {
        id: '101',
        orderNumber: 'ORD-2026-101',
        brandId: 'nike-brand',
        brandName: 'Nike MSME Partner',
        productName: 'Dri-FIT Jerseys (Batch A)',
        quantity: 1200,
        priority: 'HIGH',
        status: 'IN_PROGRESS',
        currentStageId: '4',
        currentStageName: 'Stitching & Assembly',
        currentStageSequence: 4,
        totalContractValue: 38500.0,
        paymentStatus: 'PARTIAL',
        historyLogs: [],
        qcRecords: []
      },
      {
        id: '102',
        orderNumber: 'ORD-2026-102',
        brandId: 'nike-brand',
        brandName: 'Nike MSME Partner',
        productName: 'Pro Combat Compression Shorts',
        quantity: 850,
        priority: 'MEDIUM',
        status: 'IN_PROGRESS',
        currentStageId: '5',
        currentStageName: 'Quality Check (QC Gate)',
        currentStageSequence: 5,
        totalContractValue: 24200.0,
        paymentStatus: 'PAID',
        historyLogs: [],
        qcRecords: []
      },
      {
        id: '103',
        orderNumber: 'ORD-2026-103',
        brandId: 'adidas-brand',
        brandName: 'Adidas Performance',
        productName: 'Primegreen Performance Hoodies',
        quantity: 500,
        priority: 'HIGH',
        status: 'BLOCKED',
        currentStageId: '3',
        currentStageName: 'Printing & Sublimation',
        currentStageSequence: 3,
        totalContractValue: 31000.0,
        paymentStatus: 'PARTIAL',
        historyLogs: [],
        qcRecords: []
      }
    ];

    const mockNotifications = [
      {
        id: 'n1',
        category: 'MATERIAL_SHORTAGE',
        title: 'Cyan printing ink running low',
        message: 'Sublimation printing ink (Cyan #402) is running low for order ORD-2026-103. Stock remaining: 1.2 kg.',
        orderNumber: 'ORD-2026-103',
        readStatus: false
      },
      {
        id: 'n2',
        category: 'QC_FAILURE',
        title: 'QC Flag: Printing Defect Detected',
        message: 'Order ORD-2026-103 failed QC check due to Ink Smudge & Color Bleed. Order moved to BLOCKED state.',
        orderNumber: 'ORD-2026-103',
        readStatus: false
      }
    ];

    const mockInventory = [
      { id: '1', name: 'Dry-FIT Polyester Fabric (Roll)', code: 'RM-POLY-01', sku: 'SKU-RM-POLY-01', barcode: 'BC-9921441', category: 'RAW_MATERIAL', supplierName: 'Apex Mills Corp', unit: 'meters', purchasePrice: 8.50, currentStock: 800.0, reservedStock: 100.0, availableStock: 700.0, warehouseName: 'Main Raw Warehouse', rackLocation: 'Rack A-3', batchNumber: 'B-99214', safetyStock: 100.0, minStockAlert: 50.0, maxStockAlert: 2000.0, isLowStock: false },
      { id: '2', name: 'Sublimation Cyan Ink (kg)', code: 'RM-CYAN-INK', sku: 'SKU-RM-CYAN-INK', barcode: 'BC-882104', category: 'RAW_MATERIAL', supplierName: 'DyeTech Solutions', unit: 'kg', purchasePrice: 45.00, currentStock: 5.5, reservedStock: 0.0, availableStock: 5.5, warehouseName: 'Chemical Store Room', rackLocation: 'Chemical Cabinet B', batchNumber: 'B-7721', safetyStock: 10.0, minStockAlert: 5.0, maxStockAlert: 50.0, isLowStock: true },
      { id: '3', name: 'Nike Swoosh Branding Patches', code: 'CS-NIKE-PATCH', sku: 'SKU-CS-NIKE-PATCH', barcode: 'BC-1234567', category: 'CLIENT_SUPPLIED', supplierName: 'Nike Sourcing Division', unit: 'pcs', purchasePrice: 0.0, currentStock: 400.0, reservedStock: 0.0, availableStock: 400.0, warehouseName: 'Secured Trim Cage', rackLocation: 'Box C-12', clientBrandId: 'nike-brand', safetyStock: 50.0, minStockAlert: 10.0, maxStockAlert: 1000.0, isLowStock: false },
      { id: '4', name: 'Shredded Polyester Trims (Scrap)', code: 'SC-POLY-TRIM', sku: 'SKU-SC-POLY-TRIM', barcode: 'BC-SCRAP-99', category: 'SCRAP', supplierName: 'Internal Scrap Generation', unit: 'kg', purchasePrice: 0.0, currentStock: 35.0, reservedStock: 0.0, availableStock: 35.0, warehouseName: 'Scrap Yard', rackLocation: 'Bin #4', safetyStock: 0.0, minStockAlert: 0.0, maxStockAlert: 0.0, isLowStock: false }
    ];

    const mockLedger = [
      { id: 'l1', inventoryItemId: '1', inventoryItemName: 'Dry-FIT Polyester Fabric (Roll)', inventoryItemCode: 'RM-POLY-01', movementType: 'RECEIVE', quantity: 800.0, toWarehouse: 'Main Raw Warehouse', operatorName: 'Ramesh Sharma', remarks: 'Initial inventory seeder intake', timestamp: new Date().toISOString() },
      { id: 'l2', inventoryItemId: '2', inventoryItemName: 'Sublimation Cyan Ink (kg)', inventoryItemCode: 'RM-CYAN-INK', movementType: 'RECEIVE', quantity: 15.0, toWarehouse: 'Chemical Store Room', operatorName: 'Ramesh Sharma', remarks: 'Initial inventory seeder chemical intake', timestamp: new Date().toISOString() },
      { id: 'l3', inventoryItemId: '2', inventoryItemName: 'Sublimation Cyan Ink (kg)', inventoryItemCode: 'RM-CYAN-INK', movementType: 'CONSUME', quantity: 9.5, fromWarehouse: 'Chemical Store Room', operatorName: 'Ramesh Sharma', remarks: 'Batch run ORD-2026-101 cyan printing run consumption', timestamp: new Date().toISOString() }
    ];

    const mockAudits = [
      {
        id: 'a1',
        auditName: 'Q2 Raw Materials Audit',
        status: 'COMPLETED',
        createdBy: 'Ramesh Sharma',
        createdAt: new Date(Date.now() - 30 * 24 * 3600 * 1000).toISOString(),
        completedAt: new Date(Date.now() - 30 * 24 * 3600 * 1000 + 4 * 3600 * 1000).toISOString(),
        items: [
          { id: 'ai1', inventoryItemId: '1', inventoryItemName: 'Dry-FIT Polyester Fabric (Roll)', inventoryItemCode: 'RM-POLY-01', systemStock: 800.0, physicalStock: 800.0, variance: 0.0, reconciled: true }
        ]
      }
    ];

    setStages(mockStages);
    setOrders(mockOrders);
    setNotifications(mockNotifications);
    setInventory(mockInventory);
    setLedger(mockLedger);
    setAudits(mockAudits);
  };

  const loadDataFromBackend = async () => {
    try {
      const authHeader = {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      };

      const [ordersRes, workflowRes, notifRes, invRes, ledgerRes, auditRes] = await Promise.all([
        axios.get('http://localhost:8085/api/v1/orders', authHeader),
        axios.get('http://localhost:8085/api/v1/workflows/default', authHeader),
        axios.get('http://localhost:8085/api/v1/notifications', authHeader),
        axios.get('http://localhost:8085/api/v1/inventory', authHeader),
        axios.get('http://localhost:8085/api/v1/inventory/ledger', authHeader),
        axios.get('http://localhost:8085/api/v1/inventory/audit', authHeader)
      ]);

      setOrders(ordersRes.data);
      if (workflowRes.data && workflowRes.data.stages) {
        setStages(workflowRes.data.stages);
      }
      setNotifications(notifRes.data);
      setInventory(invRes.data);
      setLedger(ledgerRes.data);
      setAudits(auditRes.data);
    } catch (err) {
      // Backend offline or booting fallback silently
      console.warn('Backend server offline. MfgOS running in offline mock seeder mode.');
      loadMockData();
    }
  };

  useEffect(() => {
    if (user) {
      loadDataFromBackend();
    }
  }, [user]);

  const handleLoginSuccess = (userData: any) => {
    setUser(userData);
    if (userData.role === 'ROLE_BRAND_CLIENT') {
      setCurrentTab('brand');
    } else {
      setCurrentTab('dashboard');
    }
  };

  const handleLogout = () => {
    setUser(null);
  };

  // Callback triggers
  const handleMarkNotificationRead = async (id: string) => {
    try {
      await axios.patch(`http://localhost:8085/api/v1/notifications/${id}/read`, {}, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
    } catch (err) {
      console.warn(err);
    }
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, readStatus: true } : n));
  };

  const handleMarkAllNotificationsRead = async () => {
    try {
      await axios.post('http://localhost:8085/api/v1/notifications/read-all', {}, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
    } catch (err) {
      console.warn(err);
    }
    setNotifications(prev => prev.map(n => ({ ...n, readStatus: true })));
  };

  const handleTriggerSimulatedEvent = async (category: string) => {
    let title = 'System Alert';
    let message = 'An operational alert has been logged.';
    let orderNumber = 'ORD-2026-101';

    if (category === 'MATERIAL_SHORTAGE') {
      title = 'Material Shortage: Timber Stock Low';
      message = 'Oak wood lumber logs are running low for carpentry production ORD-2026-104. Stock remaining: 3 items.';
      orderNumber = 'ORD-2026-104';
    } else if (category === 'QC_FAILURE') {
      title = 'QC Flag: Dimensional Tolerance Check Failed';
      message = 'Order ORD-2026-102 failed QC check due to Stitching misalignment. Moved to BLOCKED for rework.';
      orderNumber = 'ORD-2026-102';
      // Block order 102
      setOrders(prev => prev.map(o => o.id === '102' ? { ...o, status: 'BLOCKED' } : o));
    } else if (category === 'DELAY') {
      title = 'SLA SLA Warning: Printing bottleneck';
      message = 'High print backlog at sublimation unit causing stage queues to backlog by 6+ hours.';
      orderNumber = 'ORD-2026-101';
    } else if (category === 'DISPATCH') {
      title = 'Order dispatched via DHL Express';
      message = 'Order ORD-2026-101 handed over to logistics carrier. Waybill: DHL-88210-IN.';
      orderNumber = 'ORD-2026-101';
      // Complete order 101
      setOrders(prev => prev.map(o => o.id === '101' ? { ...o, status: 'DISPATCHED' } : o));
    } else if (category === 'PAYMENT') {
      title = 'Stripe Payment Settlement Settled';
      message = 'Stripe payment advance of $15,000 cleared for order ORD-2026-101.';
      orderNumber = 'ORD-2026-101';
    }

    const newNotif = {
      id: Math.random().toString(),
      category,
      title,
      message,
      orderNumber,
      readStatus: false
    };

    setNotifications(prev => [newNotif, ...prev]);

    try {
      await axios.post('http://localhost:8085/api/v1/notifications', newNotif, {
        headers: {
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
    } catch (err) {
      console.warn(err);
    }
  };

  const handleAddOrder = (order: any) => {
    setOrders(prev => [order, ...prev]);
  };

  const handleUpdateInventoryItem = (item: any) => {
    setInventory(prev => {
      const idx = prev.findIndex(i => i.id === item.id);
      if (idx !== -1) {
        return prev.map(i => i.id === item.id ? item : i);
      }
      return [item, ...prev];
    });
  };

  const handleAddMovement = (m: any) => {
    setLedger(prev => [m, ...prev]);
  };

  const handleAddAudit = (a: any) => {
    setAudits(prev => [a, ...prev]);
  };

  const handleUpdateAuditStatus = (id: string, status: string, completedAt?: string) => {
    setAudits(prev => prev.map(a => a.id === id ? { ...a, status, completedAt } : a));
  };

  if (!user) {
    return <Onboarding onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <div className="flex h-screen bg-slate-50 overflow-hidden font-sans">
      <Sidebar 
        currentTab={currentTab} 
        setCurrentTab={setCurrentTab} 
        user={user} 
        onLogout={handleLogout} 
      />
      
      {/* Main View Area Container */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Workspace Toolbar Header */}
        <header className="h-14 border-b border-slate-200 bg-white flex justify-between items-center px-8 shrink-0 z-10">
          <div className="flex items-center space-x-2 text-xs">
            <span className="text-slate-400 font-semibold uppercase tracking-wider">Workspace</span>
            <span className="text-slate-300">/</span>
            <span className="text-slate-700 font-bold uppercase tracking-wider">{currentTab.replace(/-/g, ' ')}</span>
          </div>

          <div className="flex items-center space-x-4">
            <div className="relative w-64">
              <input
                type="text"
                placeholder="Search orders, SKU items..."
                className="w-full pl-8 pr-3 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-xs text-slate-700 focus:outline-none focus:border-indigo-500 transition"
              />
              <span className="absolute left-2.5 top-2 text-slate-400">
                <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </span>
            </div>

            <div className="flex items-center space-x-1.5 px-2.5 py-1 bg-slate-100 border border-slate-200 rounded-lg text-[10px] text-slate-600 font-bold uppercase tracking-wider">
              <span className="w-1.5 h-1.5 rounded-full bg-emerald-500"></span>
              <span>Apex Textiles</span>
            </div>
          </div>
        </header>

        {/* Content Pane */}
        <div className="flex-1 overflow-hidden flex flex-col bg-slate-50">
          {currentTab === 'dashboard' && (
            <Dashboard 
              orders={orders} 
              onTriggerShortage={() => handleTriggerSimulatedEvent('MATERIAL_SHORTAGE')} 
            />
          )}
          {currentTab === 'builder' && <WorkflowBuilder />}
          {currentTab === 'kanban' && (
            <KanbanBoard 
              orders={orders} 
              stages={stages} 
              user={user} 
              onRefresh={loadDataFromBackend}
              onAddOrder={handleAddOrder}
            />
          )}
          {currentTab === 'worker-app' && (
            <WorkerApp user={user} />
          )}
          {currentTab === 'dispatch' && (
            <DispatchDashboard />
          )}
          {currentTab === 'inventory-dashboard' && (
            <InventoryDashboard inventory={inventory} />
          )}
          {currentTab === 'inventory-ledger' && (
            <InventoryLedger 
              inventory={inventory} 
              ledger={ledger} 
              brands={brands}
              user={user} 
              onRefresh={loadDataFromBackend}
              onUpdateInventoryItem={handleUpdateInventoryItem}
              onAddMovement={handleAddMovement}
            />
          )}
          {currentTab === 'inventory-audit' && (
            <InventoryAuditManager 
              audits={audits} 
              inventory={inventory}
              user={user} 
              onRefresh={loadDataFromBackend}
              onAddAudit={handleAddAudit}
              onUpdateAuditStatus={handleUpdateAuditStatus}
            />
          )}
          {currentTab === 'brand' && (
            <BrandPortal 
              orders={orders} 
              user={user} 
            />
          )}
          {currentTab === 'notifications' && (
            <Notifications 
              notifications={notifications} 
              onMarkRead={handleMarkNotificationRead}
              onMarkAllRead={handleMarkAllNotificationsRead}
              onTriggerEvent={handleTriggerSimulatedEvent}
            />
          )}
        </div>
      </div>
    </div>
  );
}
