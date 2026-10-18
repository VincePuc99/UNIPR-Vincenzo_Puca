<template>
  <transition name="banner-pop">
    <div v-if="modelValue && message" class="app-banner-layer">
      <v-card class="app-banner" :class="`app-banner--${type}`" elevation="18">
        <div class="app-banner__content">
          <v-icon size="20">{{ bannerIcon }}</v-icon>
          <span>{{ message }}</span>
        </div>
      </v-card>
    </div>
  </transition>
</template>

<script>
export default {
  name: 'AppBanner',

  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    message: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'info'
    },
    timeout: {
      type: Number,
      default: 2600
    }
  },

  emits: ['update:modelValue'],

  data() {
    return {
      timerId: null
    };
  },

  computed: {
    bannerIcon() {
      if (this.type === 'success') return 'mdi-check-circle-outline';
      if (this.type === 'error') return 'mdi-alert-circle-outline';
      return 'mdi-information-outline';
    }
  },

  watch: {
    modelValue(value) {
      if (value) {
        this.startTimer();
        return;
      }

      this.clearTimer();
    }
  },

  beforeUnmount() {
    this.clearTimer();
  },

  methods: {
    startTimer() {
      this.clearTimer();
      this.timerId = window.setTimeout(() => {
        this.$emit('update:modelValue', false);
      }, this.timeout);
    },

    clearTimer() {
      if (!this.timerId) return;
      window.clearTimeout(this.timerId);
      this.timerId = null;
    }
  }
};
</script>