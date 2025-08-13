import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  include: ['chart.js', 'react-chartjs-2',
      '@codemirror/state',
      '@codemirror/view',
      '@codemirror/lang-javascript',
      '@codemirror/lang-python',
      '@codemirror/lang-java',
      '@codemirror/lang-cpp',
      '@codemirror/lang-go',
      '@codemirror/lang-php',
      '@codemirror/lang-html',
      '@codemirror/lang-css',
      '@codemirror/lang-json',
      '@codemirror/autocomplete'
  ]
  

})
