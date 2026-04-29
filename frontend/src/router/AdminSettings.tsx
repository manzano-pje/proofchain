import { Button, Input } from "@/components/ui/Base";
import { useStore } from "@/store/useStore";
import { Save, Mail, Phone, Building2 } from "lucide-react";
export default function AdminSettings() {
  const { showToast } = useStore();
  const handleSave = async () => { showToast("Configurações salvas"); };
  return (<div className="max-w-2xl space-y-6"><h1 className="text-2xl font-bold">Configurações da Plataforma</h1><div className="bg-white p-6 rounded-2xl border border-gray-100 space-y-6"><h3 className="font-semibold">Dados Globais</h3><div className="grid md:grid-cols-2 gap-4"><div className="relative"><Building2 className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18}/><Input className="pl-10" placeholder="Nome da Plataforma" defaultValue="Proofchain"/></div><div className="relative"><Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18}/><Input className="pl-10" placeholder="Email de Suporte" defaultValue="suporte@proofchain.com"/></div></div><Button onClick={handleSave} className="w-full"><Save size={16}/> Salvar Alterações</Button></div></div>);
}