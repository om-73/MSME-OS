import { create } from 'zustand';

export interface PendingSyncItem {
  id: string;
  type: 'TASK_COMPLETE' | 'TASK_START' | 'TASK_PAUSE' | 'REPORT_ISSUE' | 'STOCK_TRANSFER' | 'QC_INSPECT' | 'DISPATCH_SIGN';
  payload: any;
  timestamp: string;
}

interface AppState {
  user: any | null;
  isOnline: boolean;
  pendingSyncQueue: PendingSyncItem[];
  setUser: (user: any | null) => void;
  setOnlineStatus: (status: boolean) => void;
  addToSyncQueue: (item: PendingSyncItem) => void;
  clearSyncQueue: () => void;
}

export const useStore = create<AppState>((set) => ({
  user: null,
  isOnline: true,
  pendingSyncQueue: [],
  setUser: (user) => set({ user }),
  setOnlineStatus: (isOnline) => set({ isOnline }),
  addToSyncQueue: (item) =>
    set((state) => ({
      pendingSyncQueue: [...state.pendingSyncQueue, item],
    })),
  clearSyncQueue: () => set({ pendingSyncQueue: [] }),
}));
