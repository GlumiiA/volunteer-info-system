import { ref, onMounted } from 'vue'

export function useThemeMode() {
  const isDark = ref(false)

  const toggleTheme = () => {
    isDark.value = !isDark.value
    document.documentElement.classList.toggle('my-app-dark')
  }

  onMounted(() => {
    const isSystemThemeDark = globalThis.matchMedia?.('(prefers-color-scheme: dark)').matches
    if (isSystemThemeDark) {
      document.documentElement.classList.add('my-app-dark')
      isDark.value = true
    }
  })

  return { isDark, toggleTheme }
}
