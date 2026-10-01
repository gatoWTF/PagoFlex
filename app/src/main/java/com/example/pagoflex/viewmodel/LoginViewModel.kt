package com.example.pagoflex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pagoflex.data.UsuarioRepository
import com.example.pagoflex.model.Usuario

class LoginViewModel(
    private val repo: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    var correo by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var usuario by mutableStateOf<Usuario?>(null)
        private set
}