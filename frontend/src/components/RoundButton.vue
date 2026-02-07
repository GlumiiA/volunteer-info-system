<script setup>
import { Button } from 'primevue'

defineProps({
  icon: {
    type: String,
    default: '',
  },
  severity: {
    type: String,
    default: '',
    validator: (value) =>
      ['', 'secondary', 'success', 'info', 'warn', 'help', 'danger', 'contrast'].includes(value),
  },
})

const emit = defineEmits(['click'])
</script>

<template>
  <Button class="round-button" :severity="severity" @click="emit('click')" rounded>
    <slot>
      <i v-if="icon" :class="icon" />
    </slot>
  </Button>
</template>

<style scoped>
.round-button {
  width: var(--space-l);
  height: var(--space-l);
}
</style>
