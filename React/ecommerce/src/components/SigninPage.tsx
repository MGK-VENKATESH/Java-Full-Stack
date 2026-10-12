import { useState } from "react";
import { signin } from "../api/authApi";
import { useAuthStore } from "../store/authStore";

function SigninPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const login = useAuthStore((state) => state.login);

  const handleSignin = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await signin({
        email,
        password
      });

      login(response);
      setMessage(response.message);
    } catch {
      setMessage("Invalid Email or Password");
    }
  };

  return (
    <div className="auth-container">
      <h2>Sign In</h2>

      <form onSubmit={handleSignin}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        <button type="submit">
          Sign In
        </button>
      </form>

      {message && <p>{message}</p>}
    </div>
  );
}

export default SigninPage;