import { useQuery } from "@tanstack/react-query";
import { institutions } from "@/services/api";
import { Skeleton, Button } from "@/components/ui/Base";
export default function AdminInstitutions() {
  const { data, isLoading } = useQuery({ queryKey: ["institutions"], queryFn: institutions.list });
  if (isLoading) return <Skeleton className="h-64"/>;
  return (<div className="space-y-6"><h1 className="text-2xl font-bold">Gestão de Instituições</h1><div className="bg-white rounded-xl border border-gray-100 overflow-hidden"><table className="w-full text-left"><thead className="bg-gray-50 text-gray-600 text-sm"><tr><th className="p-4">Nome</th><th className="p-4">Planos</th><th className="p-4">Status</th><th className="p-4 text-right">Ações</th></tr></thead><tbody className="divide-y">{data?.data.map((i: any) => <tr key={i.id} className="hover:bg-gray-50/50"><td className="p-4 font-medium">{i.name}</td><td className="p-4 text-gray-600">{i.plans}</td><td className="p-4"><span className={`px-2 py-1 rounded-full text-xs font-semibold ${i.status === "ACTIVE" ? "bg-accent/10 text-accent" : "bg-danger/10 text-danger"}`}>{i.status}</span></td><td className="p-4 text-right"><Button variant="secondary" className="py-2 px-4 text-sm">Gerenciar</Button></td></tr>)}</tbody></table></div></div>);
}