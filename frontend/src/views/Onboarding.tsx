import React, { useState } from 'react';
import { Building2, Mail, Lock, LogIn } from 'lucide-react';
import { Button, Input, Select, Badge, Card } from '../components/DesignSystem';

interface OnboardingProps {
  onLoginSuccess: (user: any) => void;
}

export default function Onboarding({ onLoginSuccess }: OnboardingProps) {
  const [isRegister, setIsRegister] = useState(false);
  const [formData, setFormData] = useState({
    companyName: 'Apex Apparel & Textile Solutions',
    subdomain: 'apex-textiles',
    industry: 'Garments',
    adminEmail: 'owner@apex.com',
    adminPassword: 'password123',
    adminFullName: 'Rajesh Kumar',
  });
  const [error, setError] = useState('');

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleDemoLogin = (role: string) => {
    let email = 'owner@apex.com';
    let fullName = 'Rajesh Kumar';
    let brandId = undefined;

    if (role === 'ROLE_OPERATOR') {
      email = 'operator@apex.com';
      fullName = 'Ramesh Sharma';
    } else if (role === 'ROLE_QUALITY_INSPECTOR') {
      email = 'qc@apex.com';
      fullName = 'Priya Verma';
    } else if (role === 'ROLE_BRAND_CLIENT') {
      email = 'brand@nike.com';
      fullName = 'Sarah Jenkins (Nike Client)';
      brandId = 'nike-brand';
    }

    onLoginSuccess({
      email,
      fullName,
      role,
      tenantId: 'apex-textiles-id',
      tenantName: 'Apex Apparel & Textile Solutions',
      brandId,
      token: 'mock-jwt-token'
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.adminEmail || !formData.adminPassword) {
      setError('Please fill all required credentials');
      return;
    }

    onLoginSuccess({
      email: formData.adminEmail,
      fullName: formData.adminFullName,
      role: 'ROLE_FACTORY_OWNER',
      tenantId: formData.subdomain + '-id',
      tenantName: formData.companyName,
      token: 'mock-jwt-token'
    });
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col items-center justify-center p-6 relative font-sans text-slate-900">
      <Card className="w-full max-w-md bg-white border border-slate-200 rounded-2xl p-8 shadow-md relative z-10">
        <div className="text-center mb-8">
          <div className="w-10 h-10 rounded bg-indigo-650 flex items-center justify-center font-bold text-white tracking-widest text-lg mx-auto mb-4">
            M
          </div>
          <h2 className="text-xl font-bold text-slate-900 tracking-tight">MfgOS Workspace</h2>
          <p className="text-xs text-slate-500 mt-1">Multi-Tenant Manufacturing Operating System</p>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-rose-50 border border-rose-200 text-rose-600 text-xs rounded-xl">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4 text-xs">
          {isRegister && (
            <>
              <div>
                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-1">Company Name</label>
                <div className="relative">
                  <Building2 className="absolute left-3 top-2.5 w-4 h-4 text-slate-400" />
                  <input
                    type="text"
                    name="companyName"
                    value={formData.companyName}
                    onChange={handleInputChange}
                    className="w-full pl-10 pr-4 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                    placeholder="Enter company name"
                  />
                </div>
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-1">Workspace Domain</label>
                <div className="relative flex items-center">
                  <input
                    type="text"
                    name="subdomain"
                    value={formData.subdomain}
                    onChange={handleInputChange}
                    className="w-full pl-4 pr-32 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                    placeholder="workspace-handle"
                  />
                  <span className="absolute right-3 text-xs text-indigo-600 font-bold">.mfgos.com</span>
                </div>
              </div>

              <div>
                <label className="block text-[10px] font-bold text-slate-550 uppercase tracking-wider mb-1">Industry Sector</label>
                <Select
                  name="industry"
                  value={formData.industry}
                  onChange={handleInputChange}
                >
                  <option value="Garments">Garment Manufacturing</option>
                  <option value="Furniture">Furniture Assembly</option>
                  <option value="Packaging">Industrial Packaging</option>
                  <option value="Jewelry">Jewelry & Casting</option>
                  <option value="Plastic">Plastic Molding</option>
                  <option value="Engineering">Precision Engineering</option>
                </Select>
              </div>
            </>
          )}

          <div>
            <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-1">Email Address</label>
            <div className="relative text-xs">
              <Mail className="absolute left-3 top-2.5 w-4 h-4 text-slate-400" />
              <input
                type="email"
                name="adminEmail"
                value={formData.adminEmail}
                onChange={handleInputChange}
                className="w-full pl-10 pr-4 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                placeholder="you@domain.com"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-1">Password Credentials</label>
            <div className="relative">
              <Lock className="absolute left-3 top-2.5 w-4 h-4 text-slate-400" />
              <input
                type="password"
                name="adminPassword"
                value={formData.adminPassword}
                onChange={handleInputChange}
                className="w-full pl-10 pr-4 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                placeholder="••••••••"
                required
              />
            </div>
          </div>

          <Button type="submit" variant="primary" className="w-full py-2 flex items-center justify-center space-x-1.5">
            <LogIn className="w-4 h-4" />
            <span>{isRegister ? 'Register Account' : 'Sign In'}</span>
          </Button>
        </form>

        <div className="mt-6 pt-4 border-t border-slate-200 text-center">
          <button
            onClick={() => setIsRegister(!isRegister)}
            className="text-xs text-indigo-650 hover:underline font-bold"
          >
            {isRegister ? 'Already have an account? Sign In' : 'Need a new tenant workspace? Register'}
          </button>
        </div>

        {/* Corporate Quick Switcher */}
        <div className="mt-8 pt-6 border-t border-slate-200 text-xs">
          <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider block mb-3 text-center">
            Demo Sandbox Logins
          </span>
          <div className="grid grid-cols-2 gap-2">
            <button
              onClick={() => handleDemoLogin('ROLE_FACTORY_OWNER')}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700"
            >
              Factory Owner
            </button>
            <button
              onClick={() => handleDemoLogin('ROLE_OPERATOR')}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700"
            >
              Floor Operator
            </button>
            <button
              onClick={() => handleDemoLogin('ROLE_QUALITY_INSPECTOR')}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700"
            >
              QC Lead Inspector
            </button>
            <button
              onClick={() => handleDemoLogin('ROLE_BRAND_CLIENT')}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700"
            >
              Nike Brand Portal
            </button>
          </div>
        </div>
      </Card>
    </div>
  );
}
