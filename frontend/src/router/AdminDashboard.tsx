import { useQuery } from "@tanstack/react-query";
import { dashboard, institutions } from "@/services/api";
import { Skeleton, Button } from "@/components/ui/Base";
import { Shield, Users, TrendingUp, FileText } from "lucide-react";
export default function AdminDashboard() {
  const { data, isLoading } = useQuery({ queryKey: ["admin-summary"], queryFn: dashboard.summary });
  if (isLoading) return <Skeleton className="h-48"/>;
  return (<div className="space-y-6"><h1 className="text-2xl font-bold flex items-center gap-2"><Shield size={24}/> Painel Admin</h1><div className="grid grid-cols-1 md:grid-cols-4 gap-4"><Card icon={<Users size={24}/>} title="Instituições" value="24"/><Card icon={<FileText size={24}/>} title="Certificados Globais" value={data?.data.certs}/><Card icon={<TrendingUp size={24}/>} title="Faturamento" value={`R$ ${data?.data.revenue.toLocaleString()}`}/><Card icon={<Shield size={24}/>} title="Planos Ativos" value="18"/></div></div>);
}
const Card = ({ icon, title, value }: any) => (<div className="bg-dark text-white p-6 rounded-2xl"><p className="text-gray-400 text-sm mb-2">{title}</p><p className="text-3xl font-bold">{value}</p></div>);