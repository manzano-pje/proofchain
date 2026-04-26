import { useQuery } from "@tanstack/react-query";
import { dashboard } from "@/services/api";
import { Skeleton, Button } from "@/components/ui/Base";
import { FileText, Users, GraduationCap, TrendingUp } from "lucide-react";
export default function Dashboard() {
  const { data, isLoading } = useQuery({ queryKey: ["summary"], queryFn: dashboard.summary });
  if (isLoading) return <div className="grid grid-cols-1 md:grid-cols-4 gap-4"><Skeleton className="h-32"/><Skeleton className="h-32"/><Skeleton className="h-32"/><Skeleton className="h-32"/></div>;
  return (<div className="space-y-6"><h1 className="text-2xl font-bold">Dashboard do Usuário</h1><div className="grid grid-cols-1 md:grid-cols-4 gap-4"><Card icon={<FileText size={24}/>} title="Certificados" value={data?.data.certs}/><Card icon={<Users size={24}/>} title="Alunos" value={data?.data.students}/><Card icon={<GraduationCap size={24}/>} title="Professores" value={data?.data.teachers}/><Card icon={<TrendingUp size={24}/>} title="Receita" value={`R$ ${data?.data.revenue}`}/></div></div>);
}
const Card = ({ icon, title, value }: any) => (<div className="card flex items-center gap-4"><div className="w-12 h-12 bg-primary/10 text-primary rounded-xl flex items-center justify-center">{icon}</div><div><p className="text-gray-500 text-sm">{title}</p><p className="text-2xl font-bold text-dark">{value}</p></div></div>);