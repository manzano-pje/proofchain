export interface Certificate { id: string; name: string; course: string; date: string; institution: string; loadHours: number; status: "ISSUED" | "REVOKED"; }
export interface Proof { hash: string; txHash: string; timestamp: string; integrity: "VALID" | "INVALID"; }
export interface ValidationResult { status: "success" | "not_found"; certificate?: Certificate; proof?: Proof; }
export interface Subscription { plan: string; used: number; limit: number; expiresAt: string; }
export interface Student { id: string; name: string; email: string; enrolled: string; }
export interface Teacher { id: string; name: string; email: string; courses: number; }
export interface Course { id: string; title: string; hours: number; students: number; status: string; }
export interface Institution { id: string; name: string; plans: number; status: "ACTIVE" | "SUSPENDED"; }