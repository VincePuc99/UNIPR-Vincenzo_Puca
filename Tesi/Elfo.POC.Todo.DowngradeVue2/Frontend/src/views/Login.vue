<template>
  <div>
    <AppBanner
      v-model="banner.show"
      :message="banner.message"
      :type="banner.type"
    />

    <v-dialog
      :value="show"
      @input="updateDialog"
      max-width="420"
      transition="dialog-bottom-transition"
      persistent
    >
      <v-card class="login-box elevation-16">

      <!-- titolo -->
        <v-card-title class="justify-center item-box">
          <h2 class="app-headline">Login</h2>
        </v-card-title>

      <!-- form -->
        <v-card-text>
          <v-form>
          <v-text-field
          v-model="email"
          label="Email"
          type="email"
          outlined
          :rules="showErrors ? [rules.required, rules.email] : []"
          class="field-spacing"
          />

          <v-text-field
          v-model="password"
          label="Password"
          type="password"
          outlined
          :rules="showErrors ? [rules.required] : []"
          />
          </v-form>
        </v-card-text>

      <!-- bottoni -->
        <v-card-actions class="justify-center item-box">
          <v-btn class="custom-btn" @click="login">
            <v-icon left>mdi-account</v-icon>
            Login
          </v-btn>
          <v-btn class="custom-btn cancel-btn" @click="close">
            <v-icon left>mdi-close</v-icon>
            Chiudi
          </v-btn>
        </v-card-actions>

      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import AppBanner from '../components/AppBanner.vue';

export default {
  name: "LoginDialog",

  components: {
    AppBanner
  },

  props: {
    show: Boolean
  },

  data() {
    return {
      email: "",
      password: "",
      showErrors: false,
      banner: {
        show: false,
        message: "",
        type: "info"
      },
      rules: {
        required: v => !!v || "Required",
        email: v => /.+@.+\..+/.test(v) || "Invalid email"
      }
    };
  },

  methods: {
    handleLoginError(err) {
      const status = err?.response?.status
      const apiMessage = err?.response?.data?.message

      switch (status) {
        case 400:
          this.showBanner(apiMessage || "Dati non validi")
          break
        case 401:
          this.showBanner("Email o password non corretti")
          break
        case 403:
          this.showBanner("Utente non autorizzato")
          break
        case 404:
          this.showBanner("Endpoint login non trovato")
          break
        case 500:
          this.showBanner("Errore interno del server")
          break
        default:
          this.showBanner(apiMessage || "Errore richiesta JWT")
          break
      }
    },

    showBanner(message, type = 'error') {
      this.banner.message = message
      this.banner.type = type
      this.banner.show = false

      this.$nextTick(() => {
        this.banner.show = true
      })
    },

    updateDialog(value) {
      this.$emit("update:show", value)
    },

    close() {
      this.email = ""
      this.password = ""
      this.showErrors = false
      this.$emit("update:show", false)
    },

/*     newrequest(){
    const token = localStorage.getItem('token'); // recupera il token
    axios.get('http://localhost:5000/api/protected-resource', {
    headers: {
        Authorization: `Bearer ${token}` // passa il token nell'header
    }
    })
    .then(response => {
    // Successo
    })
    .catch(error => {
    // Errore: token non valido o scaduto
    console.error(error);
    });
    }, */

 async login() {
      this.showErrors = true

      if (!this.email || !this.password) {
        this.showBanner("Compila email e password")
        return
      }
  
      try {
        await axios.post('http://localhost:5035/api/todo/login', {
          username: this.email,
          password: this.password,
          isEnabled: true
        }).then(res => {
            this.showBanner("Login effettuato con successo", 'success')
            localStorage.setItem('jwt_token', res.data.token); // Salva il token nel localStorage
            window.setTimeout(() => {
              this.$router.push('/dashboard');
              this.$emit("update:show", false)
            }, 900)
        }).catch(err => {
          console.error("Errore richiesta JWT:", err.response ? err.response.data : err.message)
          this.handleLoginError(err)
        })

      } catch (err) {
        console.error(err)
        this.showBanner("Errore di connessione al server")
      }
    }
  }
};
</script>

