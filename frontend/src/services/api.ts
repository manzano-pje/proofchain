import { MOCK_CERTS, MOCK_STUDENTS, MOCK_TEACHERS, MOCK_COURSES, MOCK_INSTITUTIONS, validateMock } from "@/mocks/data";
const delay = (ms: number) => new Promise(r => setTimeout(r, ms));

export const auth = {
  login: async (email: string) => { await delay(500); localStorage.setItem("token", `mock_${Date.now()}`); localStorage.setItem("user", JSON.stringify({ email, role: email.includes("admin") ? "ADMIN" : "USER" })); return { success: true }; }
};
export const validation = { check: async (id: string) => { await delay(600); return {  validateMock(id) }; } };
export const dashboard = { summary: async () => { await delay(400); return { data: { certs: 142, students: 89, teachers: 12, revenue: 8450 } }; } };
export const students = { list: async () => { await delay(300); return {  MOCK_STUDENTS }; } };
export const teachers = { list: async () => { await delay(300); return {  MOCK_TEACHERS }; } };
export const courses = { list: async () => { await delay(300); return {  MOCK_COURSES }; }, create: async () => { await delay(400); return { data: { success: true } }; } };
export const institutions = { list: async () => { await delay(300); return {  MOCK_INSTITUTIONS }; } };