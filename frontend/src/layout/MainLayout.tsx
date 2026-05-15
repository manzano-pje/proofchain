import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { useStore } from "@/store/useStore";
import { LayoutDashboard, Users, GraduationCap, BookOpen, BarChart3, Shield, Settings, LogOut, Menu } from "lucide-react";
import { cn } from "@/lib/utils";

export default function MainLayout() {
  const { sidebarOpen, toggleSidebar } = useStore();
  const navigate = useNavigate();
  const location = useLocation();
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  const isAdmin = user.role === "ADMIN";

  const links = isAdmin
    ? [{ to: "/admin", icon: Shield, label: "Admin" }, { to: "/admin/institutions", icon: Users, label: "Instituições" }, { to: "/admin/settings", icon: Settings, label: "Configurações" }]
    : [{ to: "/dashboard", icon: LayoutDashboard, label: "Dashboard" }, { to: "/dashboard/students", icon: Users, label: "Alunos" }, { to: "/dashboard/teachers", icon: GraduationCap, label: "Professores" }, { to: "/dashboard/courses", icon: BookOpen, label: "Cursos" }, { to: "/dashboard/reports", icon: BarChart3, label: "Relatórios" }];

  return (
    <div className="min-h-screen flex bg-gray-50">
      <aside className={cn("fixed md:relative z-40 h-full w-64 bg-dark text-white transition-all duration-300", sidebarOpen ? "translate-x-0" : "-translate-x-full md:translate-x-0 md:w-16")}>
        <div className="p-6 flex items-center justify-between"><span className="font-bold text-xl text-primary">🔷 Proofchain</span><button onClick={toggleSidebar} className="md:hidden"><Menu/></button></div>
        <nav className="mt-4 space-y-1 px-2">{links.map(l => <button key={l.to} onClick={() => navigate(l.to)} className={cn("w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-white/10 rounded-xl transition-colors", location.pathname === l.to && "bg-white/10 text-primary")}>
          <l.icon size={18}/><span className={sidebarOpen ? "block" : "hidden"}>{l.label}</span>
        </button>)}</nav>
        <div className="absolute bottom-4 left-0 right-0 px-2"><button onClick={() => { localStorage.clear(); navigate("/"); }} className="w-full flex items-center gap-3 px-4 py-3 text-sm hover:bg-white/10 rounded-xl transition-colors"><LogOut size={18}/><span className={sidebarOpen ? "block" : "hidden"}>Sair</span></button></div>
      </aside>
      <main className="flex-1 p-6 md:p-8 overflow-auto"><Outlet /></main>
    </div>
  );
}