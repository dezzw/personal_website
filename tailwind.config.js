/** @type {import('tailwindcss').Config} */
import typography from '@tailwindcss/typography';

export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx,cljs}'],
  theme: {
    extend: {
      colors: {
        bg: {
          DEFAULT: '#F5F5F7', // Apple-style light gray background
        },
        card: {
          DEFAULT: '#FFFFFF',
        },
        text: {
          primary: '#1D1D1F', // Apple-style dark gray/black
          secondary: '#86868B', // Apple-style lighter gray
        },
        accent: {
          blue: '#0071e3', // Apple blue
        },
        border: {
          DEFAULT: '#D2D2D7',
        },
      },
      borderRadius: {
        '3xl': '1.5rem', // Large rounded corners
      },
      boxShadow: {
        sm: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
        md: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
      },
      fontFamily: {
        sans: [
          '-apple-system',
          'BlinkMacSystemFont',
          '"Segoe UI"',
          'Roboto',
          'Helvetica',
          'Arial',
          'sans-serif',
          '"Apple Color Emoji"',
          '"Segoe UI Emoji"',
          '"Segoe UI Symbol"',
        ],
      },
    },
  },
  plugins: [typography],
};
