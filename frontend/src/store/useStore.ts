import { create } from "zustand";
export const useStore = create<any>((set) => ({
  proofModal: false, setProofModal: (v: any) => set({ proofModal: v }),
  proofData: null, setProofData: (d: any) => set({ proofData: d, proofModal: true }),
  toast: { show: false, msg: "", type: "success" },
  showToast: (msg: string, type = "success") => set({ toast: { show: true, msg, type } }),
  hideToast: () => set({ toast: { show: false, msg: "", type: "success" } }),
  sidebarOpen: true, toggleSidebar: () => set(s => ({ sidebarOpen: !s.sidebarOpen }))
}));