import { useQuery } from "@tanstack/react-query";
import { teachers } from "@/services/api";
import { Skeleton, Input } from "@/components/ui/Base";
import { Search } from "lucide-react";
import { useState } from "react";
export default function Teachers() {
  const { data, isLoading } = useQuery({ queryKey: ["teachers"], queryFn: teachers.list });
  const [search, setSearch] = useState("");
  if (isLoading) return <Skeleton className="h-64"/>;
  const filtered = data?.data.filter((t: any) => t.name.toLowerCase().includes(search.toLowerCase()));
  return (<div className="space-y-6"><h1 className="text-2xl font-bold">Professores</h1><div className="bg-white p-4 rounded-xl border border-gray-100"><div className="relative"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18}/><Input value={search} onChange={e => setSearch(e.target.value)} placeholder="Buscar professor..." className="pl-10"/></div></div><div className="bg-white rounded-xl border border-gray-100 overflow-hidden"><table className="w-full text-left"><thead className="bg-gray-50 text-gray-600 text-sm"><tr><th className="p-4">Nome</th><th className="p-4">Email</th><th className="p-4">Cursos</th></tr></thead><tbody className="divide-y">{filtered?.map((t: any) => <tr key={t.id} className="hover:bg-gray-50/50"><td className="p-4 font-medium">{t.name}</td><td className="p-4 text-gray-600">{t.email}</td><td className="p-4 text-gray-500 text-sm">{t.courses} ativos</td></tr>)}</tbody></table></div></div>);
}