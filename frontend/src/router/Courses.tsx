import { useQuery } from "@tanstack/react-query";
import { courses } from "@/services/api";
import { Skeleton, Button, Modal, Input } from "@/components/ui/Base";
import { Plus, Clock } from "lucide-react";
import { useState } from "react";
export default function Courses() {
  const { data, isLoading, refetch } = useQuery({ queryKey: ["courses"], queryFn: courses.list });
  const [open, setOpen] = useState(false);
  if (isLoading) return <Skeleton className="h-64"/>;
  return (<div className="space-y-6"><div className="flex justify-between items-center"><h1 className="text-2xl font-bold">Cursos</h1><Button onClick={() => setOpen(true)}><Plus size={18}/> Novo</Button></div><div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">{data?.data.map((c: any) => <div key={c.id} className="bg-white p-5 rounded-xl border border-gray-100 shadow-sm hover:shadow-md transition"><div className="flex justify-between items-start"><h3 className="font-bold text-lg text-dark">{c.title}</h3><span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded-full">{c.status}</span></div><div className="mt-4 flex items-center gap-2 text-sm text-gray-600"><Clock size={14}/><span>{c.hours} horas • {c.students} alunos</span></div></div>)}</div><Modal open={open} onClose={() => setOpen(false)} title="Criar Curso"><form onSubmit={() => { refetch(); setOpen(false); }} className="space-y-4"><Input placeholder="Título do curso"/><Input type="number" placeholder="Carga horária"/><Button type="submit" className="w-full">Salvar Curso</Button></form></Modal></div>);
}