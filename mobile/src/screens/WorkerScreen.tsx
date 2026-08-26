import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, SafeAreaView, ScrollView, Alert } from 'react-native';
import { useStore } from '../store/useStore';
import { SyncEngine } from '../services/SyncEngine';

export default function WorkerScreen() {
  const user = useStore((state) => state.user);
  const isOnline = useStore((state) => state.isOnline);
  const setOnlineStatus = useStore((state) => state.setOnlineStatus);
  const pendingSyncQueue = useStore((state) => state.pendingSyncQueue);
  const addToSyncQueue = useStore((state) => state.addToSyncQueue);

  const [activeTask, setActiveTask] = useState<any>({
    id: 't-101',
    orderNumber: 'ORD-2026-101',
    productName: 'Premium Dri-FIT Running Tops',
    stageName: 'Stitching & Cuffs',
    status: 'IN_PROGRESS'
  });

  const [statusText, setStatusText] = useState('');

  const handleCompleteTask = () => {
    const payload = {
      taskId: activeTask.id,
      remarks: 'Completed via mobile terminal.',
      operatorName: user?.fullName || 'Worker'
    };

    if (isOnline) {
      setStatusText('Job complete! Synced directly to factory queue.');
    } else {
      addToSyncQueue({
        id: Date.now().toString(),
        type: 'TASK_COMPLETE',
        payload,
        timestamp: new Date().toISOString()
      });
      setStatusText('Offline mode: Action saved to device queue.');
    }

    setActiveTask({
      id: 't-102',
      orderNumber: 'ORD-2026-102',
      productName: 'Eco Cotton Summer Polos',
      stageName: 'Stitching & Cuffs',
      status: 'PENDING'
    });
  };

  const toggleNetworkMode = async () => {
    const newStatus = !isOnline;
    setOnlineStatus(newStatus);
    if (newStatus) {
      const count = await SyncEngine.processSyncQueue();
      Alert.alert('Network Restored', `Synchronized ${count} pending offline actions back to backend.`);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Network Status Toggle Header */}
      <TouchableOpacity 
        style={[styles.networkBanner, { backgroundColor: isOnline ? '#10b981' : '#f59e0b' }]}
        onPress={toggleNetworkMode}
      >
        <Text style={styles.networkText}>
          {isOnline ? 'ONLINE (Tap to test offline queue)' : `OFFLINE (${pendingSyncQueue.length} items queued for sync)`}
        </Text>
      </TouchableOpacity>

      <ScrollView contentContainerStyle={styles.content}>
        <View style={styles.header}>
          <Text style={styles.workerTitle}>Operator Tasks</Text>
          <Text style={styles.workerSubtitle}>Worker: {user?.fullName}</Text>
        </View>

        {/* Shift Target Meter */}
        <View style={styles.targetCard}>
          <View style={styles.targetRow}>
            <Text style={styles.targetLabel}>Today's Target Progress</Text>
            <Text style={styles.targetVal}>18 / 25 units</Text>
          </View>
          <View style={styles.progressTrack}>
            <View style={[styles.progressFill, { width: '72%' }]} />
          </View>
        </View>

        {statusText ? (
          <View style={styles.msgBox}>
            <Text style={styles.msgText}>{statusText}</Text>
          </View>
        ) : null}

        {/* Active Task Workspace Card */}
        {activeTask ? (
          <View style={styles.taskCard}>
            <Text style={styles.orderRef}>{activeTask.orderNumber}</Text>
            <Text style={styles.productName}>{activeTask.productName}</Text>
            <Text style={styles.stageName}>STAGE: {activeTask.stageName}</Text>

            {/* BIG CONTROL BUTTONS */}
            <View style={styles.buttonGroup}>
              <TouchableOpacity style={styles.btnStart}>
                <Text style={styles.btnText}>START</Text>
              </TouchableOpacity>

              <TouchableOpacity style={styles.btnPause}>
                <Text style={styles.btnText}>PAUSE</Text>
              </TouchableOpacity>
            </View>

            <TouchableOpacity style={styles.btnComplete} onPress={handleCompleteTask}>
              <Text style={styles.btnText}>COMPLETE JOB</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.btnIssue}>
              <Text style={styles.btnIssueText}>REPORT FLOOR ISSUE</Text>
            </TouchableOpacity>
          </View>
        ) : null}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f8fafc' },
  networkBanner: { padding: 10, alignItems: 'center' },
  networkText: { color: '#ffffff', fontWeight: '800', fontSize: 11, textTransform: 'uppercase' },
  content: { padding: 20 },
  header: { marginBottom: 16 },
  workerTitle: { fontSize: 22, fontWeight: '800', color: '#0f172a' },
  workerSubtitle: { fontSize: 12, color: '#64748b', marginTop: 2 },
  targetCard: { backgroundColor: '#ffffff', borderWidth: 1, borderColor: '#cbd5e1', borderRadius: 12, padding: 16, marginBottom: 16 },
  targetRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 8 },
  targetLabel: { fontSize: 12, fontWeight: '700', color: '#334155' },
  targetVal: { fontSize: 12, fontWeight: '800', color: '#4f46e5' },
  progressTrack: { height: 8, backgroundColor: '#f1f5f9', borderRadius: 4, overflow: 'hidden' },
  progressFill: { height: '100%', backgroundColor: '#4f46e5' },
  msgBox: { backgroundColor: '#e0e7ff', padding: 12, borderRadius: 10, marginBottom: 16 },
  msgText: { color: '#3730a3', fontSize: 12, fontWeight: '700' },
  taskCard: { backgroundColor: '#ffffff', borderWidth: 1, borderColor: '#cbd5e1', borderRadius: 16, padding: 20 },
  orderRef: { fontSize: 12, fontWeight: '800', color: '#4f46e5', marginBottom: 4 },
  productName: { fontSize: 16, fontWeight: '800', color: '#0f172a', marginBottom: 4 },
  stageName: { fontSize: 11, fontWeight: '700', color: '#64748b', uppercase: true, marginBottom: 20 },
  buttonGroup: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 12 },
  btnStart: { flex: 1, backgroundColor: '#4f46e5', padding: 20, borderRadius: 12, alignItems: 'center', marginRight: 6 },
  btnPause: { flex: 1, backgroundColor: '#f59e0b', padding: 20, borderRadius: 12, alignItems: 'center', marginLeft: 6 },
  btnComplete: { backgroundColor: '#10b981', padding: 22, borderRadius: 12, alignItems: 'center', marginBottom: 12 },
  btnText: { color: '#ffffff', fontWeight: '900', fontSize: 16 },
  btnIssue: { backgroundColor: '#fff1f2', borderWidth: 1, borderColor: '#fecdd3', padding: 14, borderRadius: 12, alignItems: 'center' },
  btnIssueText: { color: '#e11d48', fontWeight: '800', fontSize: 12 }
});
