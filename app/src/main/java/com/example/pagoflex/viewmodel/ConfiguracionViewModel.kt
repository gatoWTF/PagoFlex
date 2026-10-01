package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfiguracionViewModel : ViewModel() {

    var notificaciones by mutableStateOf(true)
        private set
    var modoOscuro by mutableStateOf(false)
        private set
    var diasAviso by mutableIntStateOf(3)
        private set
}