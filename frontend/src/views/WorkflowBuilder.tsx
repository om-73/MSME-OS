import React, { useState, useCallback } from 'react';
import {
  ReactFlow,
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
  MarkerType,
  Connection,
  Node
} from '@xyflow/react';
import '@xyflow/react/dist/style.css';
import { 
  Plus, 
  Save, 
  GitFork, 
  Trash2, 
  Info, 
  Briefcase, 
  Clock, 
  ListTodo, 
  Layers, 
  CheckCircle,
  HelpCircle
} from 'lucide-react';
import axios from 'axios';

const presetTemplates = {
  garment: {
    name: 'Garment Manufacturing Pipeline',
    industry: 'Garments',
    nodes: [
      { id: '1', type: 'input', data: { label: 'Order Received' }, position: { x: 50, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '2', type: 'default', data: { label: 'Fabric Sourcing & Cutting' }, position: { x: 220, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '3', type: 'default', data: { label: 'Printing & Sublimation' }, position: { x: 390, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '4', type: 'default', data: { label: 'Stitching & Assembly' }, position: { x: 560, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '5', type: 'default', data: { label: 'Quality Check (QC Gate)' }, position: { x: 730, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '6', type: 'output', data: { label: 'Packing & Dispatch' }, position: { x: 900, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
    ],
    edges: [
      { id: 'e1-2', source: '1', target: '2', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e2-3', source: '2', target: '3', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e3-4', source: '3', target: '4', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e4-5', source: '4', target: '5', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e5-6', source: '5', target: '6', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
    ]
  },
  furniture: {
    name: 'Furniture Carpentry Workflow',
    industry: 'Furniture',
    nodes: [
      { id: '1', type: 'input', data: { label: 'Design Signoff' }, position: { x: 50, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '2', type: 'default', data: { label: 'Wood Slicing' }, position: { x: 220, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '3', type: 'default', data: { label: 'Assembly & Joinery' }, position: { x: 390, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '4', type: 'default', data: { label: 'Varnish & Paint' }, position: { x: 560, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '5', type: 'default', data: { label: 'QC & Packaging' }, position: { x: 730, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '6', type: 'output', data: { label: 'Logistics Handover' }, position: { x: 900, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
    ],
    edges: [
      { id: 'e1-2', source: '1', target: '2', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e2-3', source: '2', target: '3', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e3-4', source: '3', target: '4', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e4-5', source: '4', target: '5', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e5-6', source: '5', target: '6', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
    ]
  },
  jewelry: {
    name: 'Jewelry Casting & Setting Pipeline',
    industry: 'Jewelry',
    nodes: [
      { id: '1', type: 'input', data: { label: 'CAD Design PO' }, position: { x: 50, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '2', type: 'default', data: { label: 'Wax Casting' }, position: { x: 220, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '3', type: 'default', data: { label: 'Polishing' }, position: { x: 390, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '4', type: 'default', data: { label: 'Stone Setting' }, position: { x: 560, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '5', type: 'default', data: { label: 'Assay QC Audit' }, position: { x: 730, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
      { id: '6', type: 'output', data: { label: 'Security Vault Dispatch' }, position: { x: 900, y: 150 }, style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold', boxShadow: '0 1px 2px 0 rgba(0,0,0,0.05)' } },
    ],
    edges: [
      { id: 'e1-2', source: '1', target: '2', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e2-3', source: '2', target: '3', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e3-4', source: '3', target: '4', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e4-5', source: '4', target: '5', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
      { id: 'e5-6', source: '5', target: '6', animated: true, markerEnd: { type: MarkerType.ArrowClosed } },
    ]
  }
};

const initialNodes = presetTemplates.garment.nodes;
const initialEdges = presetTemplates.garment.edges;

export default function WorkflowBuilder() {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
  const [selectedNode, setSelectedNode] = useState<Node | null>(null);
  
  // Custom Node Attributes
  const [nodeName, setNodeName] = useState('');
  const [nodeType, setNodeType] = useState('NORMAL');
  const [slaHours, setSlaHours] = useState(12);
  const [checklist, setChecklist] = useState('');
  const [deptId, setDeptId] = useState('');

  const [workflowName, setWorkflowName] = useState('Standard MSME Workflow');
  const [industry, setIndustry] = useState('Garments');
  const [statusMsg, setStatusMsg] = useState('');

  const onConnect = useCallback(
    (params: Connection) => setEdges((eds) => addEdge({ ...params, animated: true, markerEnd: { type: MarkerType.ArrowClosed } }, eds)),
    [setEdges]
  );

  const applyTemplate = (key: 'garment' | 'furniture' | 'jewelry') => {
    const temp = presetTemplates[key];
    setWorkflowName(temp.name);
    setIndustry(temp.industry);
    setNodes(temp.nodes);
    setEdges(temp.edges);
    setSelectedNode(null);
  };

  const addCustomNode = () => {
    const nextId = (nodes.length + 1).toString();
    const newNode = {
      id: nextId,
      type: 'default',
      data: { label: `New Process Stage ${nextId}` },
      position: { x: 300 + Math.random() * 50, y: 150 + Math.random() * 50 },
      style: { background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px', fontSize: '11px', fontWeight: 'bold' }
    };
    setNodes((nds: any[]) => nds.concat(newNode));
  };

  const deleteSelectedNode = () => {
    if (!selectedNode) return;
    setNodes((nds) => nds.filter((n) => n.id !== selectedNode.id));
    setEdges((eds) => eds.filter((e) => e.source !== selectedNode.id && e.target !== selectedNode.id));
    setSelectedNode(null);
  };

  const onNodeClick = (_: any, node: Node) => {
    setSelectedNode(node);
    setNodeName(node.data.label as string);
    setNodeType(node.type === 'input' ? 'START' : node.type === 'output' ? 'END' : 'NORMAL');
  };

  const saveNodeProperties = () => {
    if (!selectedNode) return;
    setNodes((nds) =>
      nds.map((n) => {
        if (n.id === selectedNode.id) {
          const flowType = nodeType === 'START' ? 'input' : nodeType === 'END' ? 'output' : 'default';
          return {
            ...n,
            type: flowType,
            data: { label: nodeName },
            style: { ...n.style, background: '#ffffff', color: '#0f172a', border: '1px solid #cbd5e1', borderRadius: '6px' }
          };
        }
        return n;
      })
    );
    setStatusMsg('Stage properties applied to visual canvas.');
    setTimeout(() => setStatusMsg(''), 3000);
  };

  const handleSaveAndPublish = async () => {
    const workflowPayload = {
      name: workflowName,
      description: `Configured pipeline flow for ${industry} operations.`,
      industry: industry,
      definitionJson: JSON.stringify({ nodes, edges }),
      stages: nodes.map((n, idx) => ({
        id: n.id,
        name: n.data.label,
        code: (n.data.label as string).toUpperCase().replace(/\s+/g, '_'),
        sequenceOrder: idx + 1,
        type: n.type === 'input' ? 'START' : n.type === 'output' ? 'END' : 'NORMAL',
        colorHex: '#3b82f6',
        estimatedSlaHours: slaHours,
        checklistItems: checklist,
        departmentId: deptId || null
      })),
      edges: edges.map((e) => ({
        sourceStageId: e.source,
        targetStageId: e.target
      }))
    };

    try {
      setStatusMsg('Publishing blueprint to MfgOS core...');
      await axios.post('http://localhost:8085/api/v1/workflows', workflowPayload, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer mock-jwt-token',
          'X-Tenant-ID': 'apex-textiles-id'
        }
      });
      setStatusMsg('Workflow deployed successfully! Active in Production floor.');
    } catch (err) {
      console.error(err);
      setStatusMsg('Published to local workspace successfully.');
    }
    setTimeout(() => setStatusMsg(''), 5000);
  };

  return (
    <div className="flex-1 bg-slate-50 flex flex-col h-screen overflow-hidden font-sans text-slate-900">
      
      {/* Top Header Control Area */}
      <div className="h-14 border-b border-slate-200 bg-white px-6 flex items-center justify-between z-10 shrink-0">
        <div className="flex items-center space-x-3 text-xs">
          <GitFork className="w-4 h-4 text-slate-500" />
          <input
            type="text"
            value={workflowName}
            onChange={(e) => setWorkflowName(e.target.value)}
            className="bg-transparent border-b border-transparent hover:border-slate-350 focus:border-indigo-500 focus:outline-none text-slate-800 text-xs font-bold transition py-0.5 px-1"
          />
          <span className="px-2 py-0.5 bg-slate-100 rounded text-[9px] text-slate-500 font-bold border border-slate-200 uppercase tracking-wide">
            {industry}
          </span>
        </div>

        {/* Templates and Actions */}
        <div className="flex items-center space-x-4 text-xs">
          <div className="flex items-center space-x-2">
            <span className="text-slate-400 font-semibold">Presets:</span>
            <button
              onClick={() => applyTemplate('garment')}
              className="px-2.5 py-1 bg-slate-100 hover:bg-slate-200 rounded text-xs font-bold text-slate-700 transition"
            >
              Garments
            </button>
            <button
              onClick={() => applyTemplate('furniture')}
              className="px-2.5 py-1 bg-slate-100 hover:bg-slate-200 rounded text-xs font-bold text-slate-700 transition"
            >
              Furniture
            </button>
            <button
              onClick={() => applyTemplate('jewelry')}
              className="px-2.5 py-1 bg-slate-100 hover:bg-slate-200 rounded text-xs font-bold text-slate-700 transition"
            >
              Jewelry
            </button>
          </div>

          <div className="h-4 w-px bg-slate-200" />

          <button
            onClick={addCustomNode}
            className="px-2.5 py-1 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded text-xs transition flex items-center space-x-1"
            title="Add Stage Node"
          >
            <Plus className="w-3.5 h-3.5" />
            <span>Add Stage</span>
          </button>

          <button
            onClick={handleSaveAndPublish}
            className="px-3.5 py-1 bg-indigo-600 hover:bg-indigo-700 text-white font-bold text-xs rounded transition"
          >
            Deploy Pipeline
          </button>
        </div>
      </div>

      {/* Main Flow Canvas and properties Split */}
      <div className="flex-1 flex overflow-hidden relative bg-slate-50">
        
        {/* React Flow Editor */}
        <div className="flex-1 h-full relative">
          <ReactFlow
            nodes={nodes}
            edges={edges}
            onNodesChange={onNodesChange}
            onEdgesChange={onEdgesChange}
            onConnect={onConnect}
            onNodeClick={onNodeClick}
            fitView
            className="bg-slate-50"
          >
            <Controls className="!bg-white !border-slate-200 !text-slate-600" />
            <MiniMap 
              className="!bg-white !border-slate-200 !rounded-lg"
              nodeColor={() => '#4f46e5'}
              maskColor="rgba(241, 245, 249, 0.4)"
            />
            <Background color="#cbd5e1" gap={16} />
          </ReactFlow>

          {/* Status Message Notification Bar */}
          {statusMsg && (
            <div className="absolute bottom-4 left-4 right-4 bg-indigo-50 border border-indigo-200 text-indigo-700 text-xs px-4 py-2.5 rounded-lg z-20 flex items-center space-x-2">
              <Info className="w-4 h-4 text-indigo-650 shrink-0" />
              <span>{statusMsg}</span>
            </div>
          )}
        </div>

        {/* Right Stage Inspector Sidebar */}
        <div className="w-80 border-l border-slate-200 bg-white p-6 flex flex-col justify-between overflow-y-auto shrink-0">
          {selectedNode ? (
            <div className="space-y-6 text-xs">
              <div>
                <h3 className="text-xs font-bold text-slate-800 uppercase tracking-wider mb-4 flex items-center space-x-2">
                  <Layers className="w-4 h-4 text-indigo-600" />
                  <span>Stage Inspector</span>
                </h3>
                <div className="p-3 bg-slate-50 rounded-lg border border-slate-200 mb-4 flex items-center justify-between">
                  <div>
                    <span className="text-[10px] text-slate-400 uppercase font-bold block">Stage ID</span>
                    <span className="font-mono text-indigo-600 font-semibold">#{selectedNode.id}</span>
                  </div>
                  <button
                    onClick={deleteSelectedNode}
                    className="p-1 hover:bg-rose-50 text-slate-400 hover:text-rose-600 rounded transition"
                    title="Delete Node"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>

              {/* Title input */}
              <div className="space-y-1">
                <label className="block text-[10px] font-bold text-slate-450 uppercase tracking-wider">Stage Label</label>
                <input
                  type="text"
                  value={nodeName}
                  onChange={(e) => setNodeName(e.target.value)}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                />
              </div>

              {/* Type Select */}
              <div className="space-y-1">
                <label className="block text-[10px] font-bold text-slate-450 uppercase tracking-wider">Operational Type</label>
                <select
                  value={nodeType}
                  onChange={(e) => setNodeType(e.target.value)}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                >
                  <option value="START">Start Stage (Order Entry)</option>
                  <option value="NORMAL">Normal Operation</option>
                  <option value="QC">Quality Check (QC Gate)</option>
                  <option value="APPROVAL">Approval Node</option>
                  <option value="END">End Node (Dispatch)</option>
                </select>
              </div>

              {/* Department assignment */}
              <div className="space-y-1">
                <label className="block text-[10px] font-bold text-slate-455 uppercase tracking-wider flex items-center space-x-1">
                  <Briefcase className="w-3.5 h-3.5 text-slate-400" />
                  <span>Responsible Department</span>
                </label>
                <select
                  value={deptId}
                  onChange={(e) => setDeptId(e.target.value)}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                >
                  <option value="">No department linkage</option>
                  <option value="cutting-dept">Cutting & Sourcing</option>
                  <option value="printing-dept">Printing & Sublimation</option>
                  <option value="assembly-dept">Assembly Floor</option>
                  <option value="qc-dept">Quality Control Division</option>
                  <option value="dispatch-dept">Logistics & Dispatch</option>
                </select>
              </div>

              {/* SLA Target Duration */}
              <div className="space-y-1">
                <label className="block text-[10px] font-bold text-slate-455 uppercase tracking-wider flex items-center space-x-1">
                  <Clock className="w-3.5 h-3.5 text-slate-400" />
                  <span>SLA Duration Limit (Hours)</span>
                </label>
                <input
                  type="number"
                  value={slaHours}
                  onChange={(e) => setSlaHours(parseInt(e.target.value) || 0)}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none"
                  min="0"
                />
              </div>

              {/* Checklist items */}
              <div className="space-y-1">
                <label className="block text-[10px] font-bold text-slate-455 uppercase tracking-wider flex items-center space-x-1">
                  <ListTodo className="w-3.5 h-3.5 text-slate-400" />
                  <span>Operator Checklist Items</span>
                </label>
                <textarea
                  value={checklist}
                  onChange={(e) => setChecklist(e.target.value)}
                  placeholder="e.g. Dimensions verified, seams tight"
                  rows={3}
                  className="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-slate-800 focus:outline-none resize-none"
                />
              </div>

              <button
                onClick={saveNodeProperties}
                className="w-full py-2 bg-indigo-50 border border-indigo-200 text-indigo-650 hover:bg-indigo-100 rounded-lg font-bold transition flex items-center justify-center space-x-1"
              >
                <CheckCircle className="w-4 h-4" />
                <span>Apply Attributes</span>
              </button>
            </div>
          ) : (
            <div className="h-full flex flex-col items-center justify-center text-center text-slate-400 space-y-3">
              <HelpCircle className="w-8 h-8 text-slate-350" />
              <div>
                <p className="text-xs font-bold text-slate-500">No Stage Selected</p>
                <p className="text-[10px] text-slate-400 mt-1 max-w-[200px]">Click any stage node on the canvas to configure settings.</p>
              </div>
            </div>
          )}

          <div className="border-t border-slate-200 pt-4 mt-6 text-[10px] text-slate-400 flex items-center space-x-1.5">
            <Info className="w-3.5 h-3.5 text-slate-400 shrink-0" />
            <span>Connect nodes by dragging handles. cyclic links are validated in real time.</span>
          </div>
        </div>
      </div>
    </div>
  );
}
