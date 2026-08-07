import React from 'react';
import { 
  Bell, 
  Check, 
  Flame, 
  AlertTriangle, 
  Clock, 
  Truck, 
  DollarSign, 
  Info,
  Sparkles
} from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle } from '../components/DesignSystem';

interface NotificationsProps {
  notifications: any[];
  onMarkRead: (id: string) => void;
  onMarkAllRead: () => void;
  onTriggerEvent: (category: string) => void;
}

export default function Notifications({ notifications, onMarkRead, onMarkAllRead, onTriggerEvent }: NotificationsProps) {
  
  const getCategoryDetails = (cat: string) => {
    switch (cat) {
      case 'MATERIAL_SHORTAGE':
        return { icon: Flame, badge: 'error', label: 'Shortage' };
      case 'QC_FAILURE':
        return { icon: AlertTriangle, badge: 'error', label: 'QC Defect' };
      case 'DELAY':
        return { icon: Clock, badge: 'warning', label: 'Delay' };
      case 'DISPATCH':
        return { icon: Truck, badge: 'info', label: 'Dispatch' };
      case 'PAYMENT':
        return { icon: DollarSign, badge: 'success', label: 'Payment' };
      default:
        return { icon: Info, badge: 'default', label: 'Notice' };
    }
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex space-x-8 h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Notifications list feed */}
      <div className="flex-1 flex flex-col h-full bg-white border border-slate-200 rounded-2xl p-6 shadow-sm overflow-hidden">
        <div className="flex justify-between items-center mb-6 shrink-0">
          <div className="flex items-center space-x-2">
            <Bell className="w-5 h-5 text-indigo-650" />
            <h2 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Alert Feed</h2>
          </div>

          {notifications.some(n => !n.readStatus) && (
            <button
              onClick={onMarkAllRead}
              className="text-xs text-indigo-650 hover:underline flex items-center space-x-1 font-bold"
            >
              <Check className="w-3.5 h-3.5" />
              <span>Mark all read</span>
            </button>
          )}
        </div>

        {/* Scroll feed */}
        <div className="flex-1 overflow-y-auto space-y-4 pr-2">
          {notifications.map(notif => {
            const { icon: Icon, badge, label } = getCategoryDetails(notif.category);
            return (
              <div
                key={notif.id}
                className={`p-4 rounded-xl border transition flex justify-between items-start ${
                  notif.readStatus 
                    ? 'bg-slate-50 border-slate-205 opacity-60' 
                    : 'bg-white border-slate-200 hover:border-slate-300 shadow-sm'
                }`}
              >
                <div className="flex space-x-3 items-start text-xs">
                  <div className="p-2 bg-slate-55 text-slate-500 rounded-lg border border-slate-200 shrink-0">
                    <Icon className="w-4 h-4" />
                  </div>
                  <div>
                    <div className="flex items-center space-x-2">
                      <span className="font-bold text-slate-900">{notif.title}</span>
                      <Badge status={badge as any}>
                        {label}
                      </Badge>
                    </div>
                    <p className="text-slate-500 mt-1 max-w-xl leading-relaxed">{notif.message}</p>
                    {notif.orderNumber && (
                      <span className="text-[10px] text-indigo-600 font-mono font-bold mt-2 inline-block">
                        Ref: {notif.orderNumber}
                      </span>
                    )}
                  </div>
                </div>

                {!notif.readStatus && (
                  <button
                    onClick={() => onMarkRead(notif.id)}
                    className="p-1 text-slate-400 hover:text-indigo-600 rounded transition shrink-0"
                    title="Mark as Read"
                  >
                    <Check className="w-4 h-4" />
                  </button>
                )}
              </div>
            );
          })}

          {notifications.length === 0 && (
            <div className="h-full flex flex-col items-center justify-center text-slate-400 py-12 text-center">
              <Bell className="w-8 h-8 text-slate-300 mb-2" />
              <p className="text-xs font-bold text-slate-500">All notifications cleared</p>
            </div>
          )}
        </div>
      </div>

      {/* Simulator Side Panel */}
      <div className="w-80 bg-white border border-slate-200 rounded-2xl p-6 flex flex-col shrink-0 shadow-sm">
        <div className="flex items-center space-x-2 mb-6">
          <Sparkles className="w-4 h-4 text-indigo-600" />
          <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider">Event Simulator</h3>
        </div>

        <p className="text-xs text-slate-500 mb-6 leading-relaxed">
          Manufacturing ops require visual testing of pipeline dispatches. Use these quick events to trigger workspace alerts.
        </p>

        <div className="space-y-3 flex-1 text-xs">
          <button
            onClick={() => onTriggerEvent('MATERIAL_SHORTAGE')}
            className="w-full py-2 bg-rose-50 border border-rose-200 hover:bg-rose-100 text-rose-650 font-bold rounded-lg transition flex items-center justify-between px-4"
          >
            <span>Material Shortage</span>
            <Flame className="w-4 h-4 text-rose-500" />
          </button>

          <button
            onClick={() => onTriggerEvent('QC_FAILURE')}
            className="w-full py-2 bg-rose-50 border border-rose-200 hover:bg-rose-100 text-rose-650 font-bold rounded-lg transition flex items-center justify-between px-4"
          >
            <span>Quality Control Failure</span>
            <AlertTriangle className="w-4 h-4 text-rose-500" />
          </button>

          <button
            onClick={() => onTriggerEvent('DELAY')}
            className="w-full py-2 bg-amber-50 border border-amber-200 hover:bg-amber-100 text-amber-650 font-bold rounded-lg transition flex items-center justify-between px-4"
          >
            <span>Timeline Delay</span>
            <Clock className="w-4 h-4 text-amber-550" />
          </button>

          <button
            onClick={() => onTriggerEvent('DISPATCH')}
            className="w-full py-2 bg-indigo-50 border border-indigo-200 hover:bg-indigo-100 text-indigo-650 font-bold rounded-lg transition flex items-center justify-between px-4"
          >
            <span>Dispatch Waybill</span>
            <Truck className="w-4 h-4 text-indigo-550" />
          </button>

          <button
            onClick={() => onTriggerEvent('PAYMENT')}
            className="w-full py-2 bg-emerald-50 border border-emerald-200 hover:bg-emerald-100 text-emerald-655 font-bold rounded-lg transition flex items-center justify-between px-4"
          >
            <span>Stripe Payment Confirmed</span>
            <DollarSign className="w-4 h-4 text-emerald-500" />
          </button>
        </div>
      </div>
    </div>
  );
}
