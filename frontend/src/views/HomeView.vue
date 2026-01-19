<script setup>
import { Panel } from 'primevue'
import { Card } from 'primevue'
</script>

<template>
  <div class="home-container">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="main-title">
          <span class="title-accent">Добро</span><span class="title-main">Дело</span>
        </h1>
        <p class="hero-subtitle">
          Платформа для тех, кто хочет менять мир к лучшему
        </p>
        <p class="hero-description">
          Единое пространство, где встречаются те, кто нуждается в поддержке, и те, кто готов ее оказать
        </p>
      </div>
      <div class="hero-image">
        <img src="@/assets/images/volunteer-hero.svg" alt="Волонтерство" />
      </div>
    </div>

    <!-- About Section -->
    <div class="about-section">
      <Card class="about-card">
        <template #content>
          <h2 class="section-title">Кто мы?</h2>
          <p class="about-text">
            Наша система создана для того, чтобы упростить организацию волонтерской деятельности и сделать помощь более доступной. Присоединяйтесь, и добрые дела станет делать проще, организованнее и эффективнее!
          </p>
        </template>
      </Card>
    </div>

    <!-- Features Section -->
    <div class="features-section">
      <h2 class="section-title">Что решает наша система?</h2>
      <div class="features-grid">
        <Card class="feature-card">
          <template #content>
            <div class="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="64" height="64">
                <circle cx="26" cy="26" r="18" fill="none" stroke="currentColor" stroke-width="4"/>
                <path d="M 38 38 L 52 52" stroke="currentColor" stroke-width="4" stroke-linecap="round"/>
                <circle cx="56" cy="56" r="6" fill="currentColor"/>
              </svg>
            </div>
            <h3>Централизованный поиск</h3>
            <p>
              Больше не нужно искать объявления в разных соцсетях и чатах. Все актуальные возможности для волонтерства собраны в одном месте.
            </p>
          </template>
        </Card>

        <Card class="feature-card">
          <template #content>
            <div class="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="64" height="64">
                <circle cx="32" cy="32" r="28" fill="none" stroke="currentColor" stroke-width="4"/>
                <path d="M 18 32 L 28 42 L 48 22" stroke="currentColor" stroke-width="4" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
              </svg>
            </div>
            <h3>Четкий процесс</h3>
            <p>
              Мы заменяем хаотичное общение четким процессом: от публикации задачи до фиксации ее выполнения в цифровой форме.
            </p>
          </template>
        </Card>

        <Card class="feature-card">
          <template #content>
            <div class="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="64" height="64">
                <rect x="12" y="8" width="40" height="52" rx="2" fill="none" stroke="currentColor" stroke-width="3"/>
                <line x1="20" y1="20" x2="44" y2="20" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <line x1="20" y1="28" x2="44" y2="28" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <line x1="20" y1="36" x2="38" y2="36" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <line x1="20" y1="44" x2="40" y2="44" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
            <h3>Электронная книжка</h3>
            <p>
              Электронная волонтерская книжка становится надежным подтверждением опыта и активной гражданской позиции.
            </p>
          </template>
        </Card>
      </div>
    </div>

    <!-- For Volunteers Section -->
    <Panel class="info-panel volunteers-panel">
      <template #header>
        <div class="panel-header">
          <span class="panel-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="40" height="40">
              <circle cx="20" cy="18" r="8" fill="currentColor"/>
              <path d="M 8 48 Q 8 32 20 32 Q 32 32 32 48 L 32 52 L 8 52 Z" fill="currentColor"/>
              <circle cx="44" cy="18" r="8" fill="currentColor" opacity="0.7"/>
              <path d="M 32 48 Q 32 32 44 32 Q 56 32 56 48 L 56 52 L 32 52 Z" fill="currentColor" opacity="0.7"/>
            </svg>
          </span>
          <h2>Для волонтеров</h2>
        </div>
      </template>
      <ul class="benefits-list">
        <li>
          <span class="bullet">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
              <path d="M 12 2 L 14.5 9.5 L 22 10 L 16.5 15 L 18.5 22 L 12 18 L 5.5 22 L 7.5 15 L 2 10 L 9.5 9.5 Z" fill="currentColor"/>
            </svg>
          </span>
          Единая доска с актуальными объявлениями — от масштабных мероприятий до небольших бытовых просьб
        </li>
        <li>
          <span class="bullet">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
              <path d="M 12 2 L 14.5 9.5 L 22 10 L 16.5 15 L 18.5 22 L 12 18 L 5.5 22 L 7.5 15 L 2 10 L 9.5 9.5 Z" fill="currentColor"/>
            </svg>
          </span>
          Подача заявки на интересующее событие в несколько кликов
        </li>
        <li>
          <span class="bullet">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
              <path d="M 12 2 L 14.5 9.5 L 22 10 L 16.5 15 L 18.5 22 L 12 18 L 5.5 22 L 7.5 15 L 2 10 L 9.5 9.5 Z" fill="currentColor"/>
            </svg>
          </span>
          Все ваши достижения автоматически фиксируются в личной электронной волонтерской книжке
        </li>
      </ul>
    </Panel>

    <!-- For Organizers Section -->
    <Panel class="info-panel organizers-panel">
      <template #header>
        <div class="panel-header">
          <span class="panel-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64" width="40" height="40">
              <rect x="8" y="24" width="48" height="32" rx="2" fill="currentColor"/>
              <rect x="12" y="20" width="8" height="4" fill="currentColor"/>
              <rect x="24" y="16" width="8" height="8" fill="currentColor"/>
              <rect x="36" y="20" width="8" height="4" fill="currentColor"/>
              <rect x="48" y="18" width="4" height="6" fill="currentColor"/>
              <rect x="24" y="32" width="16" height="16" rx="1" fill="var(--surface-0)" opacity="0.3"/>
            </svg>
          </span>
          <h2>Для организаторов</h2>
        </div>
      </template>
      <ul class="benefits-list">
        <li>
          <span class="bullet">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
              <circle cx="12" cy="12" r="10" fill="none" stroke="currentColor" stroke-width="2"/>
              <circle cx="12" cy="12" r="3" fill="currentColor"/>
              <line x1="12" y1="2" x2="12" y2="6" stroke="currentColor" stroke-width="2"/>
              <line x1="12" y1="18" x2="12" y2="22" stroke="currentColor" stroke-width="2"/>
              <line x1="2" y1="12" x2="6" y2="12" stroke="currentColor" stroke-width="2"/>
              <line x1="18" y1="12" x2="22" y2="12" stroke="currentColor" stroke-width="2"/>
            </svg>
          </span>
          Создавайте подробные объявления с указанием всех необходимых деталей
        </li>
        <li>
          <span class="bullet">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
              <circle cx="12" cy="12" r="10" fill="none" stroke="currentColor" stroke-width="2"/>
              <circle cx="12" cy="12" r="3" fill="currentColor"/>
              <line x1="12" y1="2" x2="12" y2="6" stroke="currentColor" stroke-width="2"/>
              <line x1="12" y1="18" x2="12" y2="22" stroke="currentColor" stroke-width="2"/>
              <line x1="2" y1="12" x2="6" y2="12" stroke="currentColor" stroke-width="2"/>
              <line x1="18" y1="12" x2="22" y2="12" stroke="currentColor" stroke-width="2"/>
            </svg>
          </span>
          Отслеживайте, кто записался на ваше мероприятие или предложил помощь
        </li>
        <li>
          <span class="bullet">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
              <circle cx="12" cy="12" r="10" fill="none" stroke="currentColor" stroke-width="2"/>
              <circle cx="12" cy="12" r="3" fill="currentColor"/>
              <line x1="12" y1="2" x2="12" y2="6" stroke="currentColor" stroke-width="2"/>
              <line x1="12" y1="18" x2="12" y2="22" stroke="currentColor" stroke-width="2"/>
              <line x1="2" y1="12" x2="6" y2="12" stroke="currentColor" stroke-width="2"/>
              <line x1="18" y1="12" x2="22" y2="12" stroke="currentColor" stroke-width="2"/>
            </svg>
          </span>
          Оставляйте обратную связь волонтерам за проделанную работу
        </li>
      </ul>
    </Panel>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Roboto', sans-serif;
}

/* Hero Section */
.hero-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 3rem;
  align-items: center;
  margin-bottom: 4rem;
  padding: 3rem 0;
}

.hero-content {
  animation: fadeInLeft 0.8s ease-out;
}

.main-title {
  font-size: 4rem;
  font-weight: 800;
  margin: 0 0 1rem 0;
  line-height: 1.1;
}

.title-accent {
  color: var(--primary-color);
  display: inline-block;
}

.title-main {
  color: var(--text-color);
  display: inline-block;
}

.hero-subtitle {
  font-size: 1.5rem;
  color: var(--primary-color);
  margin: 1rem 0;
  font-weight: 500;
}

.hero-description {
  font-size: 1.1rem;
  color: var(--text-secondary-color);
  line-height: 1.6;
  margin-top: 1rem;
}

.hero-image {
  animation: fadeInRight 0.8s ease-out;
  display: flex;
  justify-content: center;
  align-items: center;
}

.hero-image img {
  width: 100%;
  max-width: 500px;
  height: auto;
  filter: drop-shadow(0 10px 30px rgba(0, 0, 0, 0.1));
}

/* Features Section */
.features-section {
  margin: 4rem 0;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-color);
  margin-bottom: 3rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 2rem;
  margin-bottom: 4rem;
}

.feature-card {
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  background: var(--surface-card);
  border-radius: 12px;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 1rem;
  height: 80px;
  color: var(--primary-color);
}

.feature-icon svg {
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-icon svg {
  transform: scale(1.1);
}

.feature-card h3 {
  font-size: 1.4rem;
  color: var(--primary-color);
  margin: 1rem 0;
  font-weight: 600;
}

.feature-card p {
  color: var(--text-secondary-color);
  line-height: 1.6;
  font-size: 1rem;
}

/* Info Panels */
.info-panel {
  margin: 2rem 0;
  border-radius: 12px;
  transition: transform 0.2s ease;
}

.info-panel:hover {
  transform: scale(1.01);
}

.panel-header {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.panel-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.panel-header h2 {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 600;
}

.volunteers-panel {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.05) 0%, rgba(168, 85, 247, 0.05) 100%);
}

.organizers-panel {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.05) 0%, rgba(16, 185, 129, 0.05) 100%);
}

.benefits-list {
  list-style: none;
  padding: 0;
  margin: 1rem 0 0 0;
}

.benefits-list li {
  padding: 1rem 0;
  font-size: 1.1rem;
  line-height: 1.6;
  color: var(--text-secondary-color);
  display: flex;
  align-items: flex-start;
  gap: 1rem;
}

.bullet {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 2px;
  color: var(--primary-color);
}

/* About Section */
.about-section {
  margin: 4rem 0 2rem;
}

.about-card {
  text-align: center;
  padding: 2rem;
  background: linear-gradient(135deg, rgba(251, 191, 36, 0.05) 0%, rgba(239, 68, 68, 0.05) 100%);
  border-radius: 12px;
}

.about-text {
  font-size: 1.2rem;
  line-height: 1.8;
  color: var(--text-secondary-color);
  max-width: 800px;
  margin: 1rem auto 0;
}

/* Animations */
@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* Responsive Design */
@media (max-width: 968px) {
  .hero-section {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .main-title {
    font-size: 3rem;
  }

  .hero-image {
    order: -1;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .home-container {
    padding: 1rem;
  }

  .main-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .section-title {
    font-size: 2rem;
  }
}
</style>
