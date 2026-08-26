import React, { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import Onboarding from './views/Onboarding';
import Dashboard from './views/Dashboard';
import WorkflowBuilder from './views/WorkflowBuilder';
import KanbanBoard from './views/KanbanBoard';
import BrandPortal from './views/BrandPortal';
import NotificationCenter from './views/NotificationCenter';
import InventoryDashboard from './views/InventoryDashboard';
import InventoryLedger from './views/InventoryLedger';
import InventoryAuditManager from './views/InventoryAuditManager';
import WorkerApp from './views/WorkerApp';
import DispatchDashboard from './views/DispatchDashboard';
import ProcurementDashboard from './views/ProcurementDashboard';
import WarehouseManager from './views/WarehouseManager';
import BrandMaterialManager from './views/BrandMaterialManager';
import AnalyticsDashboard from './views/AnalyticsDashboard';
import TenantBillingDashboard from './views/TenantBillingDashboard';
import SaaSAdminDashboard from './views/SaaSAdminDashboard';
import EnterpriseIntelligenceCenter from './views/EnterpriseIntelligenceCenter';
import EnterpriseIntegrationConsole from './views/EnterpriseIntegrationConsole';
import EnterpriseSecurityConsole from './views/EnterpriseSecurityConsole';
import MobileWorkerAppConsole from './views/MobileWorkerAppConsole';
import EnterpriseDocumentConsole from './views/EnterpriseDocumentConsole';
import EnterpriseIoTConsole from './views/EnterpriseIoTConsole';
import EnterpriseClientPortalConsole from './views/EnterpriseClientPortalConsole';
import { api } from './api/client';

export default function App() {
  const [user, setUser] = useState<any | null>(() => {
    try {
      const saved = localStorage.getItem('mfgos_user');
      return saved ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  });
  const [currentTab, setCurrentTab] = useState('dashboard');
  
  // Real Database State
  const [orders, setOrders] = useState<any[]>([]);
  const [stages, setStages] = useState<any[]>([]);
  const [notifications, setNotifications] = useState<any[]>([]);
  const [inventory, setInventory] = useState<any[]>([]);
  const [ledger, setLedger] = useState<any[]>([]);
  const [audits, setAudits] = useState<any[]>([]);
  const [brands, setBrands] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  const loadDataFromBackend = async () => {
    if (!user) return;
    setLoading(true);
    try {
      const [ordersRes, workflowRes, notifRes, invRes, ledgerRes, auditRes] = await Promise.allSettled([
        api.get('/orders'),
        api.get('/workflows/default'),
        api.get('/notifications'),
        api.get('/inventory'),
        api.get('/inventory/ledger'),
        api.get('/inventory/audit')
      ]);

      if (ordersRes.status === 'fulfilled') {
        setOrders(ordersRes.value.data || []);
      }
      if (workflowRes.status === 'fulfilled' && workflowRes.value.data?.stages) {
        setStages(workflowRes.value.data.stages || []);
      }
      if (notifRes.status === 'fulfilled') {
        setNotifications(notifRes.value.data || []);
      }
      if (invRes.status === 'fulfilled') {
        setInventory(invRes.value.data || []);
      }
      if (ledgerRes.status === 'fulfilled') {
        setLedger(ledgerRes.value.data || []);
      }
      if (auditRes.status === 'fulfilled') {
        setAudits(auditRes.value.data || []);
      }
    } catch (err) {
      console.error('Failed to load database records from backend:', err);
    } finally {
      setLoading(false);
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
    localStorage.removeItem('mfgos_user');
    setUser(null);
    setOrders([]);
    setStages([]);
    setNotifications([]);
    setInventory([]);
    setLedger([]);
    setAudits([]);
  };

  // Notification Actions
  const handleMarkNotificationRead = async (id: string) => {
    try {
      await api.patch(`/notifications/${id}/read`);
      setNotifications(prev => prev.map(n => n.id === id ? { ...n, readStatus: true } : n));
    } catch (err) {
      console.error('Failed to mark notification read:', err);
    }
  };

  const handleMarkAllNotificationsRead = async () => {
    try {
      await api.post('/notifications/read-all');
      setNotifications(prev => prev.map(n => ({ ...n, readStatus: true })));
    } catch (err) {
      console.error('Failed to mark all notifications read:', err);
    }
  };

  const handleTriggerSimulatedEvent = async (category: string) => {
    let title = 'System Alert';
    let message = 'An operational alert has been logged.';
    let orderNumber = orders[0]?.orderNumber || 'ORD-2026-101';

    if (category === 'MATERIAL_SHORTAGE') {
      title = 'Material Shortage Alert';
      message = 'Low raw material threshold reached for active production run.';
    } else if (category === 'QC_FAILURE') {
      title = 'QC Checkpoint Flag';
      message = 'Order moved to BLOCKED state for rework inspection.';
    } else if (category === 'DELAY') {
      title = 'SLA Bottleneck Alert';
      message = 'Floor cycle duration exceedance warning.';
    } else if (category === 'DISPATCH') {
      title = 'Logistics Dispatch Cleared';
      message = 'Consignment handed over to courier.';
    }

    try {
      const res = await api.post('/notifications', {
        category,
        title,
        message,
        orderNumber,
        readStatus: false
      });
      if (res.data) {
        setNotifications(prev => [res.data, ...prev]);
      }
    } catch (err) {
      console.error('Failed to publish notification to database:', err);
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
              <span>{user.tenantName || 'Apex Apparel & Textiles'}</span>
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
          {currentTab === 'client-portal' && (
            <EnterpriseClientPortalConsole user={user} />
          )}
          {currentTab === 'machines-iot' && (
            <EnterpriseIoTConsole user={user} />
          )}
          {currentTab === 'documents' && (
            <EnterpriseDocumentConsole user={user} />
          )}
          {currentTab === 'mobile-app' && (
            <MobileWorkerAppConsole user={user} />
          )}
          {currentTab === 'security' && (
            <EnterpriseSecurityConsole user={user} />
          )}
          {currentTab === 'integrations' && (
            <EnterpriseIntegrationConsole user={user} />
          )}
          {currentTab === 'ai-intelligence' && (
            <EnterpriseIntelligenceCenter user={user} />
          )}
          {currentTab === 'tenant-billing' && (
            <TenantBillingDashboard user={user} />
          )}
          {currentTab === 'saas-admin' && (
            <SaaSAdminDashboard />
          )}
          {currentTab === 'analytics' && (
            <AnalyticsDashboard />
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
          {currentTab === 'procurement' && (
            <ProcurementDashboard user={user} inventory={inventory} />
          )}
          {currentTab === 'warehouse' && (
            <WarehouseManager 
              inventory={inventory} 
              onRefresh={loadDataFromBackend} 
              onUpdateInventoryItem={handleUpdateInventoryItem}
              onAddMovement={handleAddMovement}
            />
          )}
          {currentTab === 'brand-materials' && (
            <BrandMaterialManager 
              inventory={inventory} 
              onRefresh={loadDataFromBackend} 
              onUpdateInventoryItem={handleUpdateInventoryItem}
            />
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
          {(currentTab === 'notifications' || currentTab === 'notification-center') && (
            <NotificationCenter user={user} />
          )}
        </div>
      </div>
    </div>
  );
}
