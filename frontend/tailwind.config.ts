import type { Config } from "tailwindcss";

export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: { primary: "#16A34A", primaryHover: "#15803D", dark: "#020617", light: "#F8FAFC", accent: "#22C55E", danger: "#EF4444" },
      fontFamily: { sans: ["Inter", "system-ui", "sans-serif"] },
      borderRadius: { xl: "1rem", "2xl": "1.5rem" }
    },
  },
  plugins: [],
} satisfies Config;