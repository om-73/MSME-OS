import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  GitFork, 
  KanbanSquare, 
  Building, 
  Bell, 
  LogOut, 
  User, 
  Boxes,
  History,
  ClipboardCheck,
  ChevronLeft,
  ChevronRight,
  Globe,
  Truck
} from 'lucide-react';

interface SidebarProps {
  currentTab: string;
  setCurrentTab: (tab: string) => void;
  user: any;
  onLogout: () => void;
}

export default function Sidebar({ currentTab, setCurrentTab, user, onLogout }: SidebarProps) {
  const [isCollapsed, setIsCollapsed] = useState(false);

  const tabs = [
    { id: 'dashboard', name: 'Executive Dashboard', icon: LayoutDashboard, roles: ['ROLE_FACTORY_OWNER'] },
    { id: 'builder', name: 'Workflow Designer', icon: GitFork, roles: ['ROLE_FACTORY_OWNER'] },
    { id: 'kanban', name: 'Production Control', icon: KanbanSquare, roles: ['ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_QUALITY_INSPECTOR'] },
    { id: 'worker-app', name: 'Worker Task App', icon: ClipboardCheck, roles: ['ROLE_OPERATOR', 'ROLE_QUALITY_INSPECTOR', 'ROLE_FACTORY_OWNER'] },
    { id: 'dispatch', name: 'Logistics Dispatch', icon: Truck, roles: ['ROLE_FACTORY_OWNER', 'ROLE_OPERATOR'] },
    { id: 'inventory-dashboard', name: 'Inventory Analytics', icon: Boxes, roles: ['ROLE_FACTORY_OWNER', 'ROLE_OPERATOR'] },
    { id: 'inventory-ledger', name: 'Stock Master Ledger', icon: History, roles: ['ROLE_FACTORY_OWNER', 'ROLE_OPERATOR'] },
    { id: 'inventory-audit', name: 'Physical Stocktakes', icon: ClipboardCheck, roles: ['ROLE_FACTORY_OWNER', 'ROLE_OPERATOR'] },
    { id: 'brand', name: 'Client Portal', icon: Building, roles: ['ROLE_BRAND_CLIENT'] },
    { id: 'notifications', name: 'Workspace Notifications', icon: Bell, roles: ['ROLE_FACTORY_OWNER', 'ROLE_OPERATOR', 'ROLE_QUALITY_INSPECTOR', 'ROLE_BRAND_CLIENT'] },
  ];

  const visibleTabs = tabs.filter(tab => tab.roles.includes(user.role));

  const getRoleLabel = (role: string) => {
    switch (role) {
      case 'ROLE_FACTORY_OWNER': return 'Factory Owner';
      case 'ROLE_OPERATOR': return 'Floor Operator';
      case 'ROLE_QUALITY_INSPECTOR': return 'QC Lead';
      case 'ROLE_BRAND_CLIENT': return 'Client Brand Rep';
      default: return 'User';
    }
  };

  return (
    <div className={`bg-white border-r border-slate-200 flex flex-col justify-between h-screen sticky top-0 transition-all duration-150 shrink-0 ${
      isCollapsed ? 'w-16' : 'w-64'
    }`}>
      <div>
        {/* Brand header */}
        <div className="p-4 border-b border-slate-200 flex items-center justify-between">
          <div className="flex items-center space-x-3 overflow-hidden">
            <div className="w-8 h-8 rounded bg-indigo-600 flex items-center justify-center font-extrabold text-white text-base shrink-0">
              M
            </div>
            {!isCollapsed && (
              <div>
                <h1 className="font-bold text-slate-900 leading-none text-xs tracking-tight">MfgOS Workspace</h1>
                <span className="text-[9px] text-slate-400 font-semibold tracking-wider uppercase">Enterprise Suite</span>
              </div>
            )}
          </div>

          <button
            onClick={() => setIsCollapsed(!isCollapsed)}
            className="p-1 hover:bg-slate-100 text-slate-400 hover:text-slate-650 rounded transition"
          >
            {isCollapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
          </button>
        </div>

        {/* Organization Switcher */}
        {!isCollapsed && (
          <div className="px-3 pt-3">
            <div className="p-2.5 bg-slate-50 border border-slate-200 rounded-lg flex items-center justify-between text-xs">
              <div className="flex items-center space-x-2">
                <Globe className="w-4 h-4 text-slate-500" />
                <span className="font-semibold text-slate-700">Apex Textiles Ltd.</span>
              </div>
              <span className="text-[9px] font-bold text-indigo-600 bg-indigo-50 border border-indigo-200 px-1.5 py-0.2 rounded uppercase">
                Active
              </span>
            </div>
          </div>
        )}

        {/* User profile brief */}
        {!isCollapsed && (
          <div className="p-3 mx-3 my-2 bg-slate-50 border border-slate-200 rounded-lg flex items-center space-x-3">
            <div className="w-8 h-8 rounded-full bg-slate-200 flex items-center justify-center border border-slate-350 shrink-0">
              <User className="w-4 h-4 text-slate-600" />
            </div>
            <div className="overflow-hidden">
              <p className="text-xs font-bold text-slate-800 truncate leading-tight">{user.fullName}</p>
              <p className="text-[10px] text-slate-500 truncate mt-0.5">{getRoleLabel(user.role)}</p>
            </div>
          </div>
        )}

        {/* Navigation list */}
        <nav className="px-3 mt-4 space-y-0.5">
          {visibleTabs.map(tab => {
            const Icon = tab.icon;
            const isActive = currentTab === tab.id;
            return (
              <button
                key={tab.id}
                onClick={() => setCurrentTab(tab.id)}
                className={`w-full flex items-center px-3 py-2 rounded-lg text-xs font-semibold transition ${
                  isCollapsed ? 'justify-center' : 'space-x-3'
                } ${
                  isActive 
                    ? 'bg-slate-100 text-indigo-600 border border-slate-200' 
                    : 'text-slate-600 hover:text-slate-900 hover:bg-slate-50'
                }`}
                title={isCollapsed ? tab.name : undefined}
              >
                <Icon className={`w-4 h-4 shrink-0 ${isActive ? 'text-indigo-650' : 'text-slate-500'}`} />
                {!isCollapsed && <span>{tab.name}</span>}
              </button>
            );
          })}
        </nav>
      </div>

      {/* Logout button */}
      <div className="p-3 border-t border-slate-200">
        <button
          onClick={onLogout}
          className={`w-full flex items-center py-2 rounded-lg text-xs font-bold text-slate-500 hover:text-rose-600 hover:bg-rose-50 transition ${
            isCollapsed ? 'justify-center' : 'space-x-3 px-3'
          }`}
          title={isCollapsed ? 'Exit Workspace' : undefined}
        >
          <LogOut className="w-4 h-4 shrink-0" />
          {!isCollapsed && <span>Exit Workspace</span>}
        </button>
      </div>
    </div>
  );
}
