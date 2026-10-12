import { useEffect, useState } from "react";

import ProductList from "./components/ProductList";
import Cart from "./components/Cart";
import CheckoutForm from "./components/CheckoutForm";
import SignupPage from "./pages/SignupPage";
import SigninPage from "./pages/SigninPage";
import Navbar from "./components/Navbar";

import AdminDashboard from "./pages/AdminDashboard";
import UserDashboard from "./pages/UserDashboard";

import { useAuthStore } from "./store/authStore";

function App() {
  const user = useAuthStore((state) => state.user);

  const [page, setPage] = useState(
    window.location.pathname
  );

  useEffect(() => {
    const handleNavigation = () => {
      setPage(window.location.pathname);
    };

    window.addEventListener(
      "popstate",
      handleNavigation
    );

    return () => {
      window.removeEventListener(
        "popstate",
        handleNavigation
      );
    };
  }, []);

  return (
    <div className="container">
      <Navbar />

      <h1>
        E-Commerce Product Management System
      </h1>

      {!user ? (
        <>
          {page === "/signup" ? (
            <SignupPage />
          ) : page === "/signin" ? (
            <SigninPage setPage={setPage} />
          ) : (
            <>
              <p className="subtitle">
                Browse Products
              </p>

              <ProductList />

              <Cart />

              <CheckoutForm />
            </>
          )}
        </>
      ) : user.role === "ADMIN" ? (
        <AdminDashboard />
      ) : (
        <UserDashboard />
      )}
    </div>
  );
}

export default App;