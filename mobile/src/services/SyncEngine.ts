import { useStore, PendingSyncItem } from '../store/useStore';
import axios from 'axios';

const API_BASE = 'http://localhost:8085/api/v1';

export class SyncEngine {
  public static async processSyncQueue(): Promise<number> {
    const state = useStore.getState();
    if (!state.isOnline || state.pendingSyncQueue.length === 0) {
      return 0;
    }

    const queue = [...state.pendingSyncQueue];
    let syncedCount = 0;

    for (const item of queue) {
      try {
        await this.syncItem(item);
        syncedCount++;
      } catch (error) {
        console.warn(`Failed to sync item ${item.id}, will retry on next connection event.`, error);
        break; // Pause remaining queue processing to preserve operation ordering
      }
    }

    if (syncedCount > 0) {
      const remaining = queue.slice(syncedCount);
      useStore.setState({ pendingSyncQueue: remaining });
    }

    return syncedCount;
  }

  private static async syncItem(item: PendingSyncItem): Promise<void> {
    const headers = {
      'Authorization': 'Bearer mock-jwt-token',
      'X-Tenant-ID': 'apex-textiles-id'
    };

    switch (item.type) {
      case 'TASK_COMPLETE':
        await axios.post(`${API_BASE}/worker/tasks/${item.payload.taskId}/complete`, item.payload, { headers });
        break;
      case 'TASK_START':
        await axios.post(`${API_BASE}/worker/tasks/${item.payload.taskId}/start`, item.payload, { headers });
        break;
      case 'TASK_PAUSE':
        await axios.post(`${API_BASE}/worker/tasks/${item.payload.taskId}/pause`, item.payload, { headers });
        break;
      case 'REPORT_ISSUE':
        await axios.post(`${API_BASE}/worker/tasks/${item.payload.taskId}/report-issue`, item.payload, { headers });
        break;
      case 'DISPATCH_SIGN':
        await axios.post(`${API_BASE}/dispatch/${item.payload.dispatchId}/deliver`, item.payload, { headers });
        break;
      default:
        console.log('Processed offline action: ' + item.type);
    }
  }
}
