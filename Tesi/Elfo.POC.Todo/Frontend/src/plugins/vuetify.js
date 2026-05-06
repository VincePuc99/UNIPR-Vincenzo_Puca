// plugins/vuetify.js
import { createVuetify } from 'vuetify'
import 'vuetify/styles'

import '@mdi/font/css/materialdesignicons.css'; 

import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// Importa le icone Material Design Icons
import { aliases, mdi } from 'vuetify/lib/iconsets/mdi'

const vuetify = createVuetify({
  components,   
  directives,   
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    defaultTheme: 'light', 
  },
})

export default vuetify