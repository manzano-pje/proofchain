import { useQuery } from "@tanstack/react-query";
import { students } from "@/services/api";
import { Skeleton, Input } from "@/components/ui/Base";
import { Search } from "lucide-react";
import { useState } from "react";
export default function Students() {
  const { data, isLoading } = useQuery({ queryKey: ["students"], queryFn: students.list });
  const [search, setSearch] = useState("");
  if (isLoading) return <Skeleton className="h-64"/>;
  const filtered = data?.data.filter((s: any) => s.name.toLowerCase().includes(search.toLowerCase()));
  return (<div className="space-y-6"><div className="flex justify-between items-center"><h1 className="text-2xl font-bold">Alunos</h1><button className="btn-primary">+ Novo Aluno</button></div><div className="bg-white p-4 rounded-xl border border-gray-100"><div className="relative"><Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18}/><Input value={search} onChange={e => setSearch(e.target.value)} placeholder="Buscar aluno..." className="pl-10"/></div></div><div className="bg-white rounded-xl border border-gray-100 overflow-hidden"><table className="w-full text-left"><thead className="bg-gray-50 text-gray-600 text-sm"><tr><th className="p-4">Nome</th><th className="p-4">Email</th><th className="p-4">Matrícula</th></tr></thead><tbody className="divide-y">{filtered?.map((s: any) => <tr key={s.id} className="hover:bg-gray-50/50"><td className="p-4 font-medium">{s.name}</td><td className="p-4 text-gray-600">{s.email}</td><td className="p-4 text-gray-500 text-sm">{s.enrolled}</td></tr>)}</tbody></table></div></div>);
}