import React, { useState, useEffect } from 'react';
import { Building2, Mail, Lock, LogIn, Loader2, Server, Settings, Check } from 'lucide-react';
import { Button, Select, Card } from '../components/DesignSystem';
import { api, getApiBaseUrl } from '../api/client';

interface OnboardingProps {
  onLoginSuccess: (user: any) => void;
}

export default function Onboarding({ onLoginSuccess }: OnboardingProps) {
  const [isRegister, setIsRegister] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showServerConfig, setShowServerConfig] = useState(false);
  const [customServerUrl, setCustomServerUrl] = useState(getApiBaseUrl());
  const [serverSavedMsg, setServerSavedMsg] = useState('');
  const [formData, setFormData] = useState({
    companyName: 'Apex Apparel & Textile Solutions',
    subdomain: 'apex-textiles',
    industry: 'Garments',
    adminEmail: 'owner@apex.com',
    adminPassword: 'password123',
    adminFullName: 'Rajesh Kumar',
  });
  const [error, setError] = useState('');

  const handleSaveServerUrl = (e: React.FormEvent) => {
    e.preventDefault();
    if (customServerUrl.trim()) {
      localStorage.setItem('mfgos_api_url', customServerUrl.trim());
      setServerSavedMsg('Backend URL updated successfully!');
      setTimeout(() => setServerSavedMsg(''), 3000);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleDemoLogin = async (role: string) => {
    setError('');
    setLoading(true);

    let email = 'owner@apex.com';
    let password = 'password123';

    if (role === 'ROLE_OPERATOR') {
      email = 'operator@apex.com';
    } else if (role === 'ROLE_QUALITY_INSPECTOR') {
      email = 'qc@apex.com';
    } else if (role === 'ROLE_BRAND_CLIENT') {
      email = 'owner@apex.com'; // Owner with brand portal capability
    }

    try {
      const response = await api.post('/auth/login', {
        email,
        password
      });

      const userData = {
        ...response.data,
        token: response.data.token,
        tenantId: response.data.tenantId,
        role: role === 'ROLE_BRAND_CLIENT' ? 'ROLE_BRAND_CLIENT' : response.data.role,
        brandId: role === 'ROLE_BRAND_CLIENT' ? 'nike-brand' : response.data.brandId
      };

      localStorage.setItem('mfgos_user', JSON.stringify(userData));
      onLoginSuccess(userData);
    } catch (err: any) {
      console.error('Database login error:', err);
      const msg = err.response?.data?.message || err.message || 'Failed to authenticate with database';
      setError(`Authentication error: ${msg}`);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!formData.adminEmail || !formData.adminPassword) {
      setError('Please fill all required credentials');
      return;
    }

    setLoading(true);
    try {
      let response;
      if (isRegister) {
        response = await api.post('/auth/register-tenant', {
          companyName: formData.companyName,
          subdomain: formData.subdomain,
          industry: formData.industry,
          adminEmail: formData.adminEmail,
          adminPassword: formData.adminPassword,
          adminFullName: formData.adminFullName
        });
      } else {
        response = await api.post('/auth/login', {
          email: formData.adminEmail,
          password: formData.adminPassword
        });
      }

      const userData = {
        ...response.data,
        token: response.data.token,
        tenantId: response.data.tenantId
      };

      localStorage.setItem('mfgos_user', JSON.stringify(userData));
      onLoginSuccess(userData);
    } catch (err: any) {
      console.error('Database auth error:', err);
      const msg = err.response?.data?.message || err.message || 'Database connection error';
      setError(`Database Error: ${msg}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col items-center justify-center p-6 relative font-sans text-slate-900">
      <Card className="w-full max-w-md bg-white border border-slate-200 rounded-2xl p-8 shadow-md relative z-10">
        <div className="text-center mb-8">
          <div className="w-10 h-10 rounded bg-indigo-650 flex items-center justify-center font-bold text-white tracking-widest text-lg mx-auto mb-4">
            M
          </div>
          <h2 className="text-xl font-bold text-slate-900 tracking-tight">MfgOS Workspace</h2>
          <p className="text-xs text-slate-500 mt-1">Multi-Tenant PostgreSQL Connected Platform</p>
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
                    required
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
                    required
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

              <div>
                <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-1">Admin Full Name</label>
                <input
                  type="text"
                  name="adminFullName"
                  value={formData.adminFullName}
                  onChange={handleInputChange}
                  className="w-full px-3 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                  placeholder="Full Name"
                  required
                />
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

          <Button type="submit" variant="primary" disabled={loading} className="w-full py-2 flex items-center justify-center space-x-1.5">
            {loading ? (
              <Loader2 className="w-4 h-4 animate-spin" />
            ) : (
              <>
                <LogIn className="w-4 h-4" />
                <span>{isRegister ? 'Register & Initialize Database' : 'Sign In to Workspace'}</span>
              </>
            )}
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

        {/* Database Quick Authenticator */}
        <div className="mt-8 pt-6 border-t border-slate-200 text-xs">
          <span className="text-[10px] text-slate-400 uppercase font-bold tracking-wider block mb-3 text-center">
            Database Seeded Accounts
          </span>
          <div className="grid grid-cols-2 gap-2">
            <button
              onClick={() => handleDemoLogin('ROLE_FACTORY_OWNER')}
              disabled={loading}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700 disabled:opacity-50"
            >
              Factory Owner
            </button>
            <button
              onClick={() => handleDemoLogin('ROLE_OPERATOR')}
              disabled={loading}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700 disabled:opacity-50"
            >
              Floor Operator
            </button>
            <button
              onClick={() => handleDemoLogin('ROLE_QUALITY_INSPECTOR')}
              disabled={loading}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700 disabled:opacity-50"
            >
              QC Lead Inspector
            </button>
            <button
              onClick={() => handleDemoLogin('ROLE_BRAND_CLIENT')}
              disabled={loading}
              className="px-2.5 py-1.5 bg-slate-50 border border-slate-200 hover:bg-slate-100 rounded-lg transition text-[11px] font-semibold text-slate-700 disabled:opacity-50"
            >
              Nike Brand Portal
            </button>
          </div>
        </div>

        {/* Server Endpoint Settings */}
        <div className="mt-4 pt-3 border-t border-slate-100 text-center">
          <button
            onClick={() => setShowServerConfig(!showServerConfig)}
            className="text-[11px] text-slate-400 hover:text-slate-700 flex items-center justify-center space-x-1.5 mx-auto transition"
          >
            <Server className="w-3.5 h-3.5" />
            <span>Backend Server Endpoint: <code className="text-slate-600">{customServerUrl}</code></span>
            <Settings className="w-3 h-3 text-slate-400" />
          </button>

          {showServerConfig && (
            <form onSubmit={handleSaveServerUrl} className="mt-3 p-3 bg-slate-50 border border-slate-200 rounded-xl text-left">
              <label className="block text-[10px] font-bold text-slate-600 uppercase tracking-wider mb-1">
                API Base URL (e.g. Render / Koyeb URL)
              </label>
              <div className="flex space-x-2">
                <input
                  type="text"
                  value={customServerUrl}
                  onChange={(e) => setCustomServerUrl(e.target.value)}
                  placeholder="https://your-backend.onrender.com/api/v1"
                  className="flex-1 px-3 py-1.5 bg-white border border-slate-200 rounded-lg text-xs text-slate-800 focus:outline-none"
                  required
                />
                <Button type="submit" variant="primary" className="px-3 py-1 text-xs">
                  Save
                </Button>
              </div>
              {serverSavedMsg && (
                <p className="text-[11px] text-emerald-600 font-semibold mt-1.5 flex items-center space-x-1">
                  <Check className="w-3.5 h-3.5" />
                  <span>{serverSavedMsg}</span>
                </p>
              )}
            </form>
          )}
        </div>
      </Card>
    </div>
  );
}

