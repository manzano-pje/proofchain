import { cn } from "@/lib/utils";
import { motion, AnimatePresence } from "framer-motion";
import { X, Loader2, CheckCircle } from "lucide-react";

export const Button = ({ variant = "primary", isLoading, className, ...props }: any) => (
  <button className={cn("min-h-[44px] flex items-center justify-center gap-2 font-semibold px-6 py-3 rounded-2xl transition-all", variant === "primary" ? "bg-primary hover:bg-primaryHover text-white shadow-sm disabled:opacity-50" : variant === "secondary" ? "bg-white border border-gray-200 text-dark hover:bg-gray-50" : "bg-danger hover:bg-red-600 text-white", className)} disabled={isLoading} {...props}>
    {isLoading ? <Loader2 className="w-4 h-4 animate-spin"/> : props.children}
  </button>
);
export const Input = (props: any) => <input className="input-field" {...props} />;
export const Modal = ({ open, onClose, title, children }: any) => (
  <AnimatePresence>{open && <div className="fixed inset-0 bg-dark/60 backdrop-blur-sm flex items-center justify-center z-50 p-4"><motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.95 }} className="bg-white rounded-2xl p-6 w-full max-w-md shadow-xl"><div className="flex justify-between items-center mb-4"><h3 className="text-xl font-bold">{title}</h3><button onClick={onClose} className="p-2 hover:bg-gray-100 rounded-full"><X size={20}/></button></div>{children}</motion.div></div>}</AnimatePresence>
);
export const Toast = () => {
  const { toast, hideToast } = useStore();
  return <AnimatePresence>{toast.show && <motion.div initial={{ y: -20, opacity: 0 }} animate={{ y: 0, opacity: 1 }} exit={{ y: -20, opacity: 0 }} className={`fixed top-4 right-4 z-50 px-4 py-3 rounded-xl text-white font-medium flex items-center gap-2 ${toast.type === "success" ? "bg-accent" : "bg-danger"}`}><CheckCircle size={18}/>{toast.msg}</motion.div>}</AnimatePresence>;
};
export const Skeleton = ({ className }: any) => <div className={cn("animate-pulse bg-gray-200 rounded-lg h-5", className)} />;