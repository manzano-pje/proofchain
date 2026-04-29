import { useState } from "react";
import { motion } from "framer-motion";
import { Search, AlertCircle, CheckCircle2 } from "lucide-react";
import { validation } from "@/services/api";
import { Button } from "@/components/ui/Base";
import { useStore } from "@/store/useStore";
import { useQuery } from "@tanstack/react-query";
export default function Validate() {
  const [id, setId] = useState("");
  const { setProofData } = useStore();
  const { data, isLoading, error, refetch } = useQuery({ queryKey: ["validate", id], queryFn: () => validation.check(id), enabled: false });
  const handleValidate = () => { if (!id.trim()) return; refetch(); };
  const res = data?.data;
  return (<section className="min-h-screen bg-gradient-to-b from-light to-gray-50 py-16 px-4 flex flex-col items-center"><div className="w-full max-w-2xl text-center space-y-6"><h1 className="text-4xl md:text-5xl font-bold text-dark">Valide certificados digitais</h1><p className="text-lg text-gray-600">Confirme autenticidade em segundos com registro criptográfico verificável</p><div className="bg-white p-2 rounded-2xl shadow-lg border border-gray-100 flex flex-col md:flex-row gap-2"><input value={id} onChange={e => setId(e.target.value.toUpperCase())} placeholder="Ex: CERT-001" className="flex-1 px-4 py-3 bg-transparent outline-none text-dark font-medium input-field"/><Button onClick={handleValidate} isLoading={isLoading}><Search size={18}/> Validar</Button></div>{error && <p className="text-danger text-sm">⚠️ Erro na rede.</p>}{isLoading && !data && <p className="text-gray-500 animate-pulse">Buscando registro...</p>}{res && (<motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="bg-white rounded-2xl p-6 shadow-md border border-gray-100 text-left">{res.status === "success" ? (<div className="space-y-4"><div className="flex items-center gap-3 text-accent"><CheckCircle2/><span className="font-semibold">Certificado Válido</span></div><div className="grid grid-cols-2 gap-4 text-sm"><div><span className="text-gray-500">Nome:</span> {res.certificate!.name}</div><div><span className="text-gray-500">Curso:</span> {res.certificate!.course}</div><div><span className="text-gray-500">Instituição:</span> {res.certificate!.institution}</div><div><span className="text-gray-500">Data:</span> {res.certificate!.date}</div></div><Button variant="secondary" className="w-full mt-2" onClick={() => setProofData(res)}>🔍 Ver prova de autenticidade</Button></div>) : (<div className="flex items-center gap-3 text-danger justify-center py-4"><AlertCircle/><span className="font-semibold">Certificado não encontrado</span></div>)}</motion.div>)}</div></section>);
}