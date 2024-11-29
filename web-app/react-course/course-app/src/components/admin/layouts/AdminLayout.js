// AdminLayout.js
import React from "react";
import AdminHeader from "../components/AdminHeader";
import AdminSidebar from "../components/AdminSidebar";
import AdminFooter from "../components/AdminFooter";
import { Outlet } from "react-router-dom";
import "../css/AdminLayout.css";

const AdminLayout = () => {
  return (
    <div className="admin-app">
      {" "}
      {/* Thêm lớp admin-app */}
      <AdminSidebar />
      <div className="wrapper d-flex flex-column min-vh-100">
        <AdminHeader />
        <div className="body flex-grow-1">
          <Outlet /> {/* Nội dung con sẽ được hiển thị ở đây */}
        </div>
        <AdminFooter />
      </div>
    </div>
  );
};

export default AdminLayout;
