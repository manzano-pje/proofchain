import { Routes, Route } from "react-router-dom";
import Landing from "./routes/Landing";
import Login from "./routes/Login";
import Validate from "./routes/Validate";
import MainLayout from "./layouts/MainLayout";
import Protected from "./routes/Protected";
import Dashboard from "./routes/Dashboard";
import Students from "./routes/Students";
import Teachers from "./routes/Teachers";
import Courses from "./routes/Courses";
import Reports from "./routes/Reports";
import AdminDashboard from "./routes/AdminDashboard";
import AdminInstitutions from "./routes/AdminInstitutions";
import AdminSettings from "./routes/AdminSettings";
import { Toast } from "./components/ui/Base";
import { CertificatePreviewModal } from "./components/CertificatePreview";

export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Landing/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/validate" element={<Validate/>}/>
        <Route element={<Protected/>}>
          <Route path="/dashboard" element={<MainLayout/>}>
            <Route index element={<Dashboard/>}/>
            <Route path="students" element={<Students/>}/>
            <Route path="teachers" element={<Teachers/>}/>
            <Route path="courses" element={<Courses/>}/>
            <Route path="reports" element={<Reports/>}/>
          </Route>
        </Route>
        <Route element={<Protected role="ADMIN"/>}>
          <Route path="/admin" element={<MainLayout/>}>
            <Route index element={<AdminDashboard/>}/>
            <Route path="institutions" element={<AdminInstitutions/>}/>
            <Route path="settings" element={<AdminSettings/>}/>
          </Route>
        </Route>
      </Routes>
      <Toast />
      <CertificatePreviewModal />
    </>
  );
}