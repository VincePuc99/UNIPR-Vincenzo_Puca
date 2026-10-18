<template>
  <v-app>
    <AppBanner
      v-model="banner.show"
      :message="banner.message"
      :type="banner.type"
    />

    <v-main>
      <v-container fluid class="pa-0 fill-height content-container">
        <v-card class="rounded-box dashboard-card elevation-16">
          
          <!-- Header -->
          <div class="dashboard-header mb-6">
            <h1 class="app-headline dashboard-title">Le mie Todo</h1>
            <v-btn
              class="custom-btn"
              @click="logout"
            >
              <v-icon left>mdi-logout</v-icon>
              Logout
            </v-btn>
          </div>

          <!-- Pulsante Aggiungi in alto -->
          <div class="mb-6 add-btn-container">
            <v-btn
              @click="openAddDialog"
              class="custom-btn add-btn"
            >
              <v-icon left>mdi-plus</v-icon>
              Aggiungi Todo
            </v-btn>
          </div>

          <!-- Lista Todo -->
          <v-list v-if="todos.length > 0" two-line class="todo-list">
            <v-list-item
              v-for="todo in sortedTodos"
              :key="todo.idTodo"
              class="todo-item mb-2 pa-4"
            >
              <!-- Contenuto todo -->
              <v-list-item-content>
                <div class="todo-content">
                  <div
                    class="todo-title font-weight-bold"
                    :class="{ 'todo-title-completed': todo.completed }"
                  >
                    {{ todo.description }}
                  </div>
                  <div
                    class="todo-description text-caption"
                    :class="todo.completed ? 'todo-status-completed' : 'todo-status-progress'"
                  >
                    {{ todo.completed ? 'Completata' : 'In corso' }}
                  </div>
                </div>
              </v-list-item-content>

              <v-list-item-action>
                <div class="action-buttons">
                  <v-btn
                    icon
                    small
                    class="edit-gradient-btn"
                    @click="openEditDialog(todo)"
                    title="Modifica"
                  >
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn
                    icon
                    small
                    class="delete-gradient-btn"
                    @click="openDeleteDialog(todo)"
                    title="Elimina"
                  >
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </div>
              </v-list-item-action>
            </v-list-item>
          </v-list>

          <!-- Stato vuoto -->
          <div v-else class="text-center py-12">
            <v-icon size="64" color="grey">mdi-checkbox-marked-circle-outline</v-icon>
            <p class="text-grey mt-4">Nessuna todo per il momento. Aggiungine una!</p>
          </div>

        </v-card>
      </v-container>
    </v-main>

    <!-- Dialog Aggiungi/Modifica -->
    <v-dialog
      v-model="showDialog"
      max-width="420"
      transition="dialog-bottom-transition"
      persistent
    >
      <v-card class="edit-box elevation-16">
        <v-card-title class="justify-center item-box">
          <h2 class="edit-title">{{ isEditMode ? 'Modifica Todo' : 'Aggiungi Todo' }}</h2>
        </v-card-title>

        <v-card-text>
          <v-form ref="form" @submit.prevent="saveTodo">
            <v-text-field
              v-model="currentTodo.description"
              label="Descrizione"
              outlined
              class="field-spacing"
              :rules="[v => !!v || 'Descrizione obbligatoria']"
              required
            />
            <v-checkbox
              v-model="currentTodo.completed"
              label="Completata"
            />
          </v-form>
        </v-card-text>

        <v-card-actions class="justify-center item-box edit-actions">
          <v-btn
            class="edit-btn save-btn"
            @click="saveTodo"
          >
            <v-icon left>mdi-content-save</v-icon>
            {{ isEditMode ? 'Aggiorna' : 'Salva' }}
          </v-btn>
          <v-btn
            class="edit-btn cancel-btn"
            @click="closeDialog"
          >
            <v-icon left>mdi-close</v-icon>
            Annulla
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog
      v-model="showDeleteDialog"
      max-width="420"
      transition="dialog-bottom-transition"
      persistent
    >
      <v-card class="edit-box elevation-16">
        <v-card-title class="justify-center item-box">
          <h2 class="edit-title">Elimina Todo</h2>
        </v-card-title>

        <v-card-text class="text-center">
          Sei sicuro di voler eliminare questa todo?
        </v-card-text>

        <v-card-actions class="justify-center item-box edit-actions">
          <v-btn
            class="edit-btn add-btn"
            @click="deleteTodo"
          >
            <v-icon left>mdi-check</v-icon>
            Conferma
          </v-btn>
          <v-btn
            class="edit-btn cancel-btn"
            @click="closeDeleteDialog"
          >
            <v-icon left>mdi-close</v-icon>
            Annulla
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>
import axios from 'axios';
import AppBanner from '../components/AppBanner.vue';

const API_BASE = 'http://localhost:5035/api/todo';

export default {
  name: 'Dashboard',

  components: {
    AppBanner
  },

  data() {
    return {
      todos: [],
      showDialog: false,
      showDeleteDialog: false,
      isEditMode: false,
      currentTodo: {
        idTodo: null,
        description: '',
        completed: false,
        isEnabled: true
      },
      todoToDelete: null,
      banner: {
        show: false,
        message: '',
        type: 'info'
      },
      isAuthenticated: false
    };
  },

  computed: {
    sortedTodos() {
      return [...this.todos].sort((leftTodo, rightTodo) => Number(leftTodo.completed) - Number(rightTodo.completed));
    }
  },

  mounted() {
    this.verifyToken();
  },

  methods: {
    showBanner(message, type = 'error') {
      this.banner.message = message;
      this.banner.type = type;
      this.banner.show = false;

      this.$nextTick(() => {
        this.banner.show = true;
      });
    },

    getAuthHeaders() {
      const token = localStorage.getItem('jwt_token');
      return { Authorization: `Bearer ${token}` };
    },

    async verifyToken() {
      const authenticated = await this.ensureAuthenticated();
      if (!authenticated) return;
      await this.loadTodos();
    },

    async ensureAuthenticated() {
      const token = localStorage.getItem('jwt_token');
      if (!token) {
        this.isAuthenticated = false;
        this.showLogoutMessage();
        return false;
      }

      try {
        await axios.get(`${API_BASE}/verify`, { headers: { Authorization: `Bearer ${token}` } });
        this.isAuthenticated = true;
        return true;
      } catch {
        localStorage.removeItem('jwt_token');
        this.isAuthenticated = false;
        this.showLogoutMessage();
        return false;
      }
    },

    showLogoutMessage() {
      this.showBanner('Sessione non valida. Effettua di nuovo il login.');
      window.setTimeout(() => {
        this.$router.push('/');
      }, 900);
    },

    async loadTodos() {
      try {
        const res = await axios.get(API_BASE, { headers: this.getAuthHeaders() });
        this.todos = (res.data || []).filter(t => t.isEnabled);
      } catch (err) {
        console.error('Errore caricamento todo:', err);
      }
    },

    async openAddDialog() {
      if (!(await this.ensureAuthenticated())) return;
      this.isEditMode = false;
      this.currentTodo = { idTodo: null, description: '', completed: false, isEnabled: true };
      this.showDialog = true;
    },

    async openEditDialog(todo) {
      if (!(await this.ensureAuthenticated())) return;
      this.isEditMode = true;
      this.currentTodo = { ...todo };
      this.showDialog = true;
    },

    closeDialog() {
      this.showDialog = false;
      this.currentTodo = { idTodo: null, description: '', completed: false, isEnabled: true };
    },

    async openDeleteDialog(todo) {
      if (!(await this.ensureAuthenticated())) return;
      this.todoToDelete = todo;
      this.showDeleteDialog = true;
    },

    closeDeleteDialog() {
      this.showDeleteDialog = false;
      this.todoToDelete = null;
    },

    async saveTodo() {
      if (!(await this.ensureAuthenticated())) return;

      if (!this.currentTodo.description.trim()) {
        this.showBanner('La descrizione non può essere vuota');
        return;
      }
      const payload = {
        description: this.currentTodo.description,
        completed: this.currentTodo.completed,
        isEnabled: this.currentTodo.isEnabled
      };
      try {
        if (this.isEditMode) {
          await axios.put(`${API_BASE}/${this.currentTodo.idTodo}`, payload, { headers: this.getAuthHeaders() });
        } else {
          await axios.post(API_BASE, payload, { headers: this.getAuthHeaders() });
        }
        await this.loadTodos();
        this.closeDialog();
        this.showBanner(this.isEditMode ? 'Todo aggiornata con successo' : 'Todo aggiunta con successo', 'success');
      } catch (err) {
        console.error('Errore salvataggio todo:', err);
        this.showBanner('Errore nel salvataggio della todo');
      }
    },

    async deleteTodo() {
      if (!(await this.ensureAuthenticated())) return;

      if (!this.todoToDelete) return;
      try {
        await axios.delete(`${API_BASE}/${this.todoToDelete.idTodo}`, { headers: this.getAuthHeaders() });
        await this.loadTodos();
        this.closeDeleteDialog();
        this.showBanner('Todo eliminata con successo', 'success');
      } catch (err) {
        console.error('Errore eliminazione todo:', err);
        this.showBanner("Errore nell'eliminazione della todo");
      }
    },

    logout() {
      localStorage.removeItem('jwt_token');
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.dashboard-header {
  position: relative;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  min-height: 44px;
  margin-top: 8px;
}

.dashboard-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin: 0;
}
</style>