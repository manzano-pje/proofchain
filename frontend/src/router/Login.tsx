import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { auth } from "@/services/api";
import { Button, Input } from "@/components/ui/Base";
import { useStore } from "@/store/useStore";
export default function Login() {
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { showToast } = useStore();
  const handleSubmit = async (e: any) => { e.preventDefault(); setLoading(true); try { await auth.login(email); showToast("Login realizado"); navigate("/dashboard"); } catch { showToast("Erro ao logar", "error"); } finally { setLoading(false); } };
  return (<div className="min-h-screen flex items-center justify-center bg-gray-50 px-4"><div className="bg-white p-8 rounded-2xl shadow-lg w-full max-w-md"><h1 className="text-2xl font-bold mb-2 text-dark">Acesso à Plataforma</h1><p className="text-gray-600 mb-6">Use qualquer email. Ex: `admin@test.com` para painel Admin</p><form onSubmit={handleSubmit} className="space-y-4"><Input type="email" placeholder="seu@email.com" value={email} onChange={e => setEmail(e.target.value)} required/><Button type="submit" isLoading={loading} className="w-full">Entrar</Button></form></div></div>);
}