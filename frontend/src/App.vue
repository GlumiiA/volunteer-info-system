<script setup>
import { RouterView } from 'vue-router'
import { Toast, ScrollPanel } from 'primevue'
import ViewToolbar from '@/components/ViewToolbar.vue'
</script>

<template>
  <Toast />
  <div class="main-panel">
    <div class="toolbar-container">
      <ViewToolbar />
    </div>
    <div class="content-container">
      <ScrollPanel class="scroll-panel">
        <RouterView :key="$route.fullPath" />
      </ScrollPanel>
    </div>
  </div>
</template>

<style>
@import './assets/css/variables.css';

@import url('https://fonts.googleapis.com/css2?family=Nunito:ital,wght@0,200..1000;1,200..1000&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Roboto+Flex:opsz,wght,XOPQ,XTRA,YOPQ,YTDE,YTFI,YTLC,YTUC@8..144,100..1000,96,468,79,-203,738,514,712&display=swap');

html {
  overflow: hidden;
}

.p-button span {
  font-family: Nunito, sans-serif;
}

.main-panel {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.toolbar-container {
  flex-shrink: 0;
  margin: var(--space-s);
}

.content-container {
  flex-grow: 1;
  min-height: 0;
  margin: var(--space-s) var(--space-s) var(--space-m);
}

.scroll-panel {
  height: 100%;
  width: 100%;
}
</style>
