import React, { useState, useEffect } from 'react';
import { 
  FileText, 
  Upload, 
  Download, 
  Share2, 
  CheckCircle2, 
  XCircle, 
  Clock, 
  History, 
  HardDrive, 
  Sparkles, 
  Tag, 
  Folder, 
  Search, 
  Plus, 
  ShieldCheck, 
  ExternalLink,
  Lock
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

interface EnterpriseDocumentConsoleProps {
  user?: any;
}

export default function EnterpriseDocumentConsole({ user }: EnterpriseDocumentConsoleProps) {
  const [documents, setDocuments] = useState<any[]>([]);
  const [quota, setQuota] = useState<any | null>(null);
  const [selectedDoc, setSelectedDoc] = useState<any | null>(null);
  const [versions, setVersions] = useState<any[]>([]);

  // Subtabs
  const [activeTab, setActiveTab] = useState<'center' | 'approvals' | 'expiring' | 'quota'>('center');
  const [selectedCategory, setSelectedCategory] = useState<string>('');

  // Modals
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showVersionModal, setShowVersionModal] = useState(false);
  const [showShareModal, setShowShareModal] = useState(false);
  const [shareLink, setShareLink] = useState<string | null>(null);
  const [statusMsg, setStatusMsg] = useState('');

  // Upload Form
  const [uploadForm, setUploadForm] = useState({ fileName: 'Men_Shirt_TechPack_V2.pdf', category: 'PRODUCTION', tags: 'TechPack,Garment', fileSizeBytes: 2450000 });

  // Version Form
  const [versionForm, setVersionForm] = useState({ changeDescription: 'Updated cuff & collar dimensions', fileSizeBytes: 2600000 });

  const fetchDocumentData = async () => {
    try {
      const [docsRes, quotaRes] = await Promise.all([
        api.get(`/documents${selectedCategory ? '?category=' + selectedCategory : ''}`),
        api.get('/documents/quota')
      ]);
      setDocuments(docsRes.data || []);
      setQuota(quotaRes.data || {});
    } catch (err) {
      console.error('Failed to fetch documents from database:', err);
    }
  };

  useEffect(() => {
    fetchDocumentData();
  }, [selectedCategory]);

  const handleUploadDocument = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/documents', uploadForm);
      setStatusMsg('Document uploaded & SHA-256 checksum recorded.');
      fetchDocumentData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to upload document.');
    }
    setShowUploadModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleUploadNewVersion = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedDoc) return;
    try {
      await api.post(`/documents/${selectedDoc.id}/versions`, versionForm);
      setStatusMsg(`New version uploaded for ${selectedDoc.fileName}.`);
      fetchDocumentData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to upload new version.');
    }
    setShowVersionModal(false);
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleApproveDoc = async (docId: number) => {
    try {
      await api.post(`/documents/${docId}/approve`, {});
      setStatusMsg('Document approved & notification dispatched.');
      fetchDocumentData();
    } catch (err) {
      console.error(err);
      setStatusMsg('Failed to approve document.');
    }
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleGenerateShareLink = async (docId: number) => {
    try {
      const res = await api.post(`/documents/${docId}/share`, { recipientEmail: 'client@brand.com', maxDownloads: 10 });
      setShareLink(`https://api.mfgos.com/v1/documents/share?token=${res.data.shareToken}`);
    } catch (err) {
      console.error(err);
    }
    setShowShareModal(true);
  };

  const handleViewVersions = async (doc: any) => {
    setSelectedDoc(doc);
    try {
      const res = await api.get(`/documents/${doc.id}/versions`);
      setVersions(res.data || []);
    } catch (err) {
      console.error(err);
    }
    setShowVersionModal(true);
  };

  return (
    <div className="flex-1 bg-slate-50 p-8 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Header bar */}
      <div className="flex justify-between items-center mb-6 shrink-0">
        <div className="flex space-x-6 items-center">
          <div>
            <h2 className="text-lg font-bold text-slate-900 uppercase tracking-wider">Enterprise Document Management & Records</h2>
            <p className="text-xs text-slate-550 mt-0.5">Centralized digital record repository, tech pack versioning, approvals & secure short-lived sharing links</p>
          </div>

          <div className="flex p-0.5 bg-slate-100 border border-slate-205 rounded-lg text-xs">
            {[
              { id: 'center', label: 'Document Center' },
              { id: 'approvals', label: 'Approval Queue' },
              { id: 'expiring', label: 'Expiring Certificates' },
              { id: 'quota', label: 'Storage Quotas' }
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

        <Button variant="primary" onClick={() => setShowUploadModal(true)}>
          <Upload className="w-4 h-4" />
          <span>Upload Document</span>
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
        
        {/* 1. DOCUMENT CENTER EXPLORER */}
        {activeTab === 'center' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <div className="flex justify-between items-center mb-4">
              <div className="flex space-x-2 items-center">
                <span className="font-bold text-slate-700">Category Filter:</span>
                <Select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)} className="w-44">
                  <option value="">All Categories</option>
                  <option value="PRODUCTION">Production Tech Packs</option>
                  <option value="PROCUREMENT">Procurement & Material</option>
                  <option value="QUALITY">Quality Certificates</option>
                  <option value="INVENTORY">Inventory Records</option>
                  <option value="DISPATCH">Dispatch & Packing</option>
                </Select>
              </div>
            </div>

            <div className="flex-1 overflow-y-auto pr-2">
              <Table>
                <TableHead>
                  <th className="pb-3">File Name</th>
                  <th className="pb-3">Category</th>
                  <th className="pb-3">Version</th>
                  <th className="pb-3">File Size</th>
                  <th className="pb-3">Status</th>
                  <th className="pb-3">Binding Ref</th>
                  <th className="pb-3 text-center">Actions</th>
                </TableHead>
                <TableBody>
                  {documents.map(doc => {
                    const isPending = doc.status === 'PENDING_REVIEW';
                    return (
                      <tr key={doc.id} className="hover:bg-slate-50/50 transition border-b border-slate-100 text-xs">
                        <td className="py-3 font-semibold text-slate-900 flex items-center space-x-2">
                          <FileText className="w-4 h-4 text-indigo-600 shrink-0" />
                          <span>{doc.fileName}</span>
                        </td>
                        <td className="py-3 font-bold text-indigo-600">{doc.category}</td>
                        <td className="py-3 font-mono font-bold">v{doc.currentVersion}</td>
                        <td className="py-3 font-mono text-slate-500">{(doc.fileSizeBytes / 1024 / 1024).toFixed(2)} MB</td>
                        <td className="py-3"><Badge status={isPending ? 'warning' : 'success'}>{doc.status}</Badge></td>
                        <td className="py-3 text-slate-600 font-mono">{doc.relatedEntityId || 'Unlinked'}</td>
                        <td className="py-3 text-center space-x-2">
                          <Button variant="outline" onClick={() => handleViewVersions(doc)} className="!py-1">
                            <History className="w-3.5 h-3.5" />
                            <span>Versions</span>
                          </Button>
                          <Button variant="outline" onClick={() => handleGenerateShareLink(doc.id)} className="!py-1">
                            <Share2 className="w-3.5 h-3.5" />
                            <span>Share</span>
                          </Button>
                        </td>
                      </tr>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          </div>
        )}

        {/* 2. APPROVAL QUEUE */}
        {activeTab === 'approvals' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <CheckCircle2 className="w-4 h-4 text-indigo-600" />
              <span>Pending Document Approval Queue</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {documents.filter(d => d.status === 'PENDING_REVIEW').map(doc => (
                <div key={doc.id} className="p-4 bg-slate-50 border border-slate-200 rounded-xl space-y-2">
                  <div className="flex justify-between items-center">
                    <span className="font-bold text-slate-900 text-sm">{doc.fileName}</span>
                    <Badge status="warning">Requires Manager Approval</Badge>
                  </div>
                  <p className="text-slate-600">Category: <strong>{doc.category}</strong> | Uploaded by {doc.uploadedBy}</p>
                  
                  <div className="flex justify-end space-x-2 pt-2">
                    <Button variant="primary" onClick={() => handleApproveDoc(doc.id)}>
                      Approve Document
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 3. EXPIRING CERTIFICATES */}
        {activeTab === 'expiring' && (
          <div className="flex-1 flex flex-col overflow-hidden text-xs">
            <h3 className="text-xs font-bold text-slate-705 uppercase tracking-wider mb-4 flex items-center space-x-1.5">
              <Clock className="w-4 h-4 text-indigo-600" />
              <span>Expiring Supplier & Compliance Certificates</span>
            </h3>

            <div className="flex-1 overflow-y-auto pr-2 space-y-4">
              {documents.filter(d => d.expirationDate).map(doc => (
                <div key={doc.id} className="p-4 bg-amber-50 border border-amber-200 rounded-xl space-y-2 text-amber-900">
                  <div className="flex justify-between items-center font-bold">
                    <span>{doc.fileName}</span>
                    <Badge status="warning">Expiring Soon</Badge>
                  </div>
                  <p className="text-xs">Expiration Date: {new Date(doc.expirationDate).toLocaleDateString()}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 4. STORAGE QUOTAS */}
        {activeTab === 'quota' && quota && (
          <div className="flex-1 flex flex-col space-y-6 overflow-y-auto pr-2 text-xs">
            <div className="grid grid-cols-3 gap-6">
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Total Managed Documents</span>
                <span className="text-2xl font-bold text-slate-900 font-mono">{quota.totalDocumentsCount}</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Storage Consumed</span>
                <span className="text-2xl font-bold text-indigo-650 font-mono">{quota.usedGb} GB</span>
              </div>
              <div className="bg-slate-50 border border-slate-200 rounded-xl p-5 shadow-sm">
                <span className="text-[10px] text-slate-450 uppercase font-bold block mb-1">Storage Plan Quota</span>
                <span className="text-2xl font-bold text-slate-800 font-mono">{quota.quotaGb} GB</span>
              </div>
            </div>
          </div>
        )}

      </div>

      {/* 1. Upload Document Modal */}
      <Dialog isOpen={showUploadModal} onClose={() => setShowUploadModal(false)} title="Upload Enterprise Digital Record">
        <form onSubmit={handleUploadDocument} className="space-y-4 text-xs">
          <div>
            <label className="block text-slate-500 mb-1 font-semibold">File Name</label>
            <Input
              type="text"
              value={uploadForm.fileName}
              onChange={(e) => setUploadForm({ ...uploadForm, fileName: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Category</label>
            <Select value={uploadForm.category} onChange={(e) => setUploadForm({ ...uploadForm, category: e.target.value })}>
              <option value="PRODUCTION">Production Tech Packs</option>
              <option value="PROCUREMENT">Procurement & Material</option>
              <option value="QUALITY">Quality Certificates</option>
              <option value="INVENTORY">Inventory Records</option>
              <option value="DISPATCH">Dispatch & Packing</option>
            </Select>
          </div>

          <div>
            <label className="block text-slate-500 mb-1 font-semibold">Tags (Comma-separated)</label>
            <Input
              type="text"
              value={uploadForm.tags}
              onChange={(e) => setUploadForm({ ...uploadForm, tags: e.target.value })}
            />
          </div>

          <div className="flex space-x-3 pt-4 justify-end">
            <Button type="button" onClick={() => setShowUploadModal(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="primary">
              Upload & Record Checksum
            </Button>
          </div>
        </form>
      </Dialog>

      {/* 2. Version Modal */}
      <Dialog isOpen={showVersionModal} onClose={() => setShowVersionModal(false)} title={`Version History: ${selectedDoc?.fileName}`}>
        <div className="space-y-4 text-xs">
          <form onSubmit={handleUploadNewVersion} className="p-3 bg-slate-50 border border-slate-200 rounded-xl space-y-3">
            <span className="font-bold text-slate-800 block text-[11px]">Upload New Version (Increments v{selectedDoc?.currentVersion})</span>
            <Input
              type="text"
              placeholder="Change description (e.g. Updated sleeve measurements)"
              value={versionForm.changeDescription}
              onChange={(e) => setVersionForm({ ...versionForm, changeDescription: e.target.value })}
            />
            <Button type="submit" variant="primary" className="!py-1">
              Upload New Version
            </Button>
          </form>

          <div className="space-y-2">
            <span className="font-bold text-slate-800 block">Version Progression Trail</span>
            {versions.map(v => (
              <div key={v.id} className="p-3 bg-white border border-slate-200 rounded-lg flex justify-between items-center">
                <div>
                  <span className="font-bold text-indigo-600 font-mono">Version {v.versionNumber}</span>
                  <p className="text-[11px] text-slate-600">{v.changeDescription}</p>
                </div>
                <span className="text-[10px] text-slate-400 font-mono">{new Date(v.createdAt).toLocaleDateString()}</span>
              </div>
            ))}
          </div>
        </div>
      </Dialog>

      {/* 3. Share Modal */}
      <Dialog isOpen={showShareModal} onClose={() => setShowShareModal(false)} title="Secure Document Share Link">
        <div className="space-y-4 text-xs">
          <p className="text-slate-600">Generated short-lived, signed download link for external partners:</p>
          <code className="bg-slate-50 p-3 border border-slate-200 rounded-lg font-mono text-[11px] block select-all text-indigo-600 font-bold">
            {shareLink}
          </code>
          <div className="flex justify-end">
            <Button type="button" variant="primary" onClick={() => setShowShareModal(false)}>
              Done
            </Button>
          </div>
        </div>
      </Dialog>
    </div>
  );
}
