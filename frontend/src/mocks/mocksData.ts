import { Certificate, Student, Teacher, Course, Institution } from "@/types";
export const MOCK_CERTS: Certificate[] = [
  { id: "CERT-001", name: "Ana Silva", course: "React Avançado", date: "2024-11-10", institution: "Proofchain Academy", loadHours: 40, status: "ISSUED" },
  { id: "CERT-002", name: "Carlos Souza", course: "Web3 Fundamentals", date: "2024-12-01", institution: "Proofchain Academy", loadHours: 20, status: "ISSUED" }
];
export const MOCK_STUDENTS: Student[] = [
  { id: "S001", name: "Ana Silva", email: "ana@email.com", enrolled: "2024-10-01" },
  { id: "S002", name: "João Lima", email: "joao@email.com", enrolled: "2024-09-15" }
];
export const MOCK_TEACHERS: Teacher[] = [
  { id: "T001", name: "Prof. Mendes", email: "mendes@email.com", courses: 3 },
  { id: "T002", name: "Profa. Costa", email: "costa@email.com", courses: 2 }
];
export const MOCK_COURSES: Course[] = [
  { id: "C001", title: "React Avançado", hours: 40, students: 24, status: "ATIVO" },
  { id: "C002", title: "Node.js API", hours: 30, students: 18, status: "ATIVO" }
];
export const MOCK_INSTITUTIONS: Institution[] = [
  { id: "I001", name: "Tech Academy", plans: 2, status: "ACTIVE" },
  { id: "I002", name: "Educa Digital", plans: 1, status: "SUSPENDED" }
];
export const validateMock = (id: string) => {
  const cert = MOCK_CERTS.find(c => c.id === id);
  if (!cert) return { status: "not_found" };
  return { status: "success", certificate: cert, proof: { hash: `0x${Math.random().toString(16).slice(2)}a3b`, txHash: `0x${Math.random().toString(16).slice(2)}c9d`, timestamp: new Date().toISOString(), integrity: "VALID" } };
};