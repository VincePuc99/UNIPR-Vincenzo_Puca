<template>
  <v-app>
    <AppBanner
      v-model="banner.show"
      :message="banner.message"
      :type="banner.type"
    />

    <v-main>
      <v-container fluid class="pa-0 fill-height content-container">
        <v-card class="rounded-box elevation-16">
          <!-- Titolo -->
          <v-card-title class="justify-center item-box">
            <h1 class="headline">TodoList-App</h1>
          </v-card-title>

          <!-- Sottotitolo -->
          <v-card-subtitle class="justify-center d-flex item-box">
            <p class="subheading">Developed by V.Puca</p>
          </v-card-subtitle>

          <!-- Pulsanti -->
          <v-card-actions class="justify-center item-box">
           
            <v-btn
              class="custom-btn"
              small
              prepend-icon="mdi-account"
              @click="handleLoginClick"
            >
              Login
            </v-btn>
            
            <v-btn
              class="custom-btn"
              small
              prepend-icon="mdi-form-textbox"
              @click="showAlert"
            >
              Registrati
            </v-btn>
        
          </v-card-actions>
        </v-card>
      </v-container>

      <LoginDialog :show="loginDialog" @update:show="loginDialog = $event" />

    </v-main>
  </v-app>
</template>

<script>
import axios from 'axios';
import LoginDialog from "./Login.vue";
import AppBanner from '../components/AppBanner.vue';

const API_BASE = 'http://localhost:5035/api/todo';

export default {
  name: 'Home',
  components: { LoginDialog, AppBanner },
  data() {
    return {
      loginDialog: false,
      banner: {
        show: false,
        message: '',
        type: 'info'
      }
    };
  },
  methods: {
    showBanner(message, type = 'info') {
      this.banner.message = message;
      this.banner.type = type;
      this.banner.show = false;

      this.$nextTick(() => {
        this.banner.show = true;
      });
    },

    showAlert() {
      this.showBanner('Register to TodoList App!');
    },

    extractEmailFromToken(token) {
      try {
        const payload = token.split('.')[1];
        if (!payload) return '';

        const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
        const json = decodeURIComponent(
          atob(base64)
            .split('')
            .map((char) => `%${`00${char.charCodeAt(0).toString(16)}`.slice(-2)}`)
            .join('')
        );

        const parsed = JSON.parse(json);
        return parsed.email || parsed['http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress'] || '';
      } catch {
        return '';
      }
    },

    async handleLoginClick() {
      const token = localStorage.getItem('jwt_token');

      if (!token) {
        this.loginDialog = true;
        return;
      }

      try {
        await axios.get(`${API_BASE}/verify`, {
          headers: { Authorization: `Bearer ${token}` }
        });

        const email = this.extractEmailFromToken(token) || 'utente';
        this.showBanner(`Bentornato ${email}`, 'success');

        window.setTimeout(() => {
          this.$router.push('/dashboard');
        }, 900);
      } catch {
        localStorage.removeItem('jwt_token');
        this.loginDialog = true;
      }
    }
  },
};
</script>