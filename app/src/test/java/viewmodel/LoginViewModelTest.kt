package viewmodel

import com.example.pagoflex.viewmodel.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel


    @Before
    fun preparar(){
        viewModel = LoginViewModel()
    }

    @Test
    fun iniciarSesion_conCorreoInvalido_mostrarError(){
        viewModel.cambiarCorreo("camila")
        viewModel.cambiarContrasena("Clave123")

        viewModel.iniciarSesion()

        assertEquals("Correo no válido", viewModel.errorCorreo)
    }
}