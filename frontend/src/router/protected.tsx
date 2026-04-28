import { Navigate, Outlet } from "react-router-dom";
export default function Protected({ role }: { role?: "ADMIN" }) {
  const user = JSON.parse(localStorage.getItem("user") || "{}");
  if (!user.email) return <Navigate to="/login"/>;
  if (role === "ADMIN" && user.role !== "ADMIN") return <Navigate to="/dashboard"/>;
  return <Outlet/>;
}