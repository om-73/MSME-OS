import React from 'react';

// ==========================================
// 1. TYPOGRAPHY & TEXT HEADINGS
// ==========================================
export function PageHeader({ title, description, actions }: { title: string; description?: string; actions?: React.ReactNode }) {
  return (
    <div className="flex justify-between items-center mb-6 pb-4 border-b border-slate-200 shrink-0">
      <div>
        <h2 className="text-lg font-bold text-slate-900 uppercase tracking-tight">{title}</h2>
        {description && <p className="text-xs text-slate-500 mt-0.5">{description}</p>}
      </div>
      {actions && <div className="flex space-x-2">{actions}</div>}
    </div>
  );
}

// ==========================================
// 2. BUTTON VARIANTS (Stripe inspired)
// ==========================================
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'danger';
  children: React.ReactNode;
}

export function Button({ variant = 'secondary', children, className = '', ...props }: ButtonProps) {
  const baseStyle = "px-3 py-1.5 rounded-lg text-xs font-bold transition flex items-center justify-center space-x-1.5 focus:outline-none";
  let variantStyle = "";

  if (variant === 'primary') {
    variantStyle = "bg-indigo-600 hover:bg-indigo-700 text-white shadow-sm";
  } else if (variant === 'secondary') {
    variantStyle = "bg-slate-100 hover:bg-slate-200 text-slate-700";
  } else if (variant === 'outline') {
    variantStyle = "border border-slate-200 hover:bg-slate-50 text-slate-650";
  } else if (variant === 'danger') {
    variantStyle = "bg-rose-50 border border-rose-200 text-rose-600 hover:bg-rose-100";
  }

  return (
    <button className={`${baseStyle} ${variantStyle} ${className}`} {...props}>
      {children}
    </button>
  );
}

// ==========================================
// 3. ENTERPRISE CARDS (shadcn/ui style)
// ==========================================
export function Card({ children, className = '' }: { children: React.ReactNode; className?: string }) {
  return (
    <div className={`bg-white border border-slate-200 rounded-xl shadow-sm p-6 ${className}`}>
      {children}
    </div>
  );
}

export function CardHeader({ children, className = '' }: { children: React.ReactNode; className?: string }) {
  return (
    <div className={`flex justify-between items-center mb-5 pb-3 border-b border-slate-100 ${className}`}>
      {children}
    </div>
  );
}

export function CardTitle({ children, className = '' }: { children: React.ReactNode; className?: string }) {
  return (
    <h3 className={`text-xs font-bold text-slate-800 uppercase tracking-wider ${className}`}>
      {children}
    </h3>
  );
}

// ==========================================
// 4. DATA TABLES (Github style)
// ==========================================
export function Table({ children }: { children: React.ReactNode }) {
  return (
    <div className="overflow-x-auto w-full">
      <table className="w-full text-left text-xs border-collapse">
        {children}
      </table>
    </div>
  );
}

export function TableHead({ children }: { children: React.ReactNode }) {
  return (
    <thead>
      <tr className="border-b border-slate-200 text-slate-500 font-bold uppercase tracking-wider text-[10px]">
        {children}
      </tr>
    </thead>
  );
}

export function TableBody({ children }: { children: React.ReactNode }) {
  return (
    <tbody className="divide-y divide-slate-100 text-slate-700">
      {children}
    </tbody>
  );
}

// ==========================================
// 5. STATUS BADGES
// ==========================================
export function Badge({ status = 'default', children }: { status?: 'success' | 'warning' | 'error' | 'info' | 'default'; children: React.ReactNode }) {
  let badgeStyle = "text-[9px] px-2 py-0.5 rounded font-bold uppercase tracking-wider border ";

  if (status === 'success') {
    badgeStyle += "bg-emerald-50 border-emerald-250 text-emerald-600";
  } else if (status === 'warning') {
    badgeStyle += "bg-amber-50 border-amber-250 text-amber-600";
  } else if (status === 'error') {
    badgeStyle += "bg-rose-50 border-rose-255 text-rose-600";
  } else if (status === 'info') {
    badgeStyle += "bg-indigo-50 border-indigo-200 text-indigo-600";
  } else {
    badgeStyle += "bg-slate-50 border-slate-200 text-slate-500";
  }

  return (
    <span className={badgeStyle}>
      {children}
    </span>
  );
}

// ==========================================
// 6. FORM FIELDS & COMMAND INPUTS
// ==========================================
export function Input({ className = '', ...props }: React.InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input 
      className={`w-full px-3 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 text-xs focus:outline-none focus:border-indigo-500 transition ${className}`}
      {...props} 
    />
  );
}

export function Select({ className = '', children, ...props }: React.SelectHTMLAttributes<HTMLSelectElement>) {
  return (
    <select 
      className={`w-full px-3 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 text-xs focus:outline-none focus:border-indigo-500 transition ${className}`}
      {...props}
    >
      {children}
    </select>
  );
}

// ==========================================
// 7. DIALOGS & SHEET WRAPPERS
// ==========================================
export function Dialog({ isOpen, onClose, title, children }: { isOpen: boolean; onClose: () => void; title: string; children: React.ReactNode }) {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 bg-slate-950/40 z-50 flex items-center justify-center p-6 backdrop-blur-sm">
      <div className="w-full max-w-md bg-white border border-slate-200 rounded-2xl p-6 shadow-2xl space-y-4">
        <div className="flex justify-between items-center pb-2 border-b border-slate-100">
          <h3 className="text-sm font-bold text-slate-900 uppercase tracking-wider">{title}</h3>
          <button onClick={onClose} className="text-slate-400 hover:text-slate-600 text-xs font-bold">Close</button>
        </div>
        {children}
      </div>
    </div>
  );
}
