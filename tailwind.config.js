/** @type {import('tailwindcss').Config} */
export default {
	content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
		extend: {
      colors: {
        white: {
          DEFAULT: "#FFF",
        },
        black: {
          DEFAULT: "#000",
        },
        prime: {
          DEFAULT: "#0b66c2",
          2: "#1682FD",
        },
        bg: {
          DEFAULT: "#FAFAFA",
        },
        border: {
          DEFAULT: "#EAEAEA",
        },
        gray: {
          DEFAULT: "rgb(156 163 175)",
          font: "#808080",
        },
      },
      borderRadius: {
        base: "20px",
      },
      boxShadow: {
        base: "0 2px 4px 0 rgba(0,0,0,0.25)",
      },
		},
		fontFamily: {
			body: ['Comfortaa', 'sans-serif'],
			heading: ['Righteous', 'sans-serif'],
		},
		plugins: [],
	}
}
