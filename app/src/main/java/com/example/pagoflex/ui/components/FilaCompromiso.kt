package com.example.pagoflex.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.pagoflex.R
import com.example.pagoflex.model.Compromiso
import com.example.pagoflex.model.EstadoCompromiso
import com.example.pagoflex.ui.styles.EstilosTarjeta
import com.example.pagoflex.ui.theme.Dimens
import com.example.pagoflex.ui.theme.PagoFlexTheme
import com.example.pagoflex.utils.FormatoMoneda

// Fila que muestra un compromiso en la lista. Al tocarla abre el detalle.
@Composable
fun FilaCompromiso(
    compromiso: Compromiso,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = EstilosTarjeta.forma,
        colors = EstilosTarjeta.colores(),
        elevation = EstilosTarjeta.elevacion()
    ) {
        Row(
            modifier = Modifier.padding(Dimens.espacioMedio),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.tamanoIconoCategoria)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_otro),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(Dimens.tamanoIcono)
                )
            }
            Spacer(Modifier.width(Dimens.espacioMedio))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = compromiso.concepto,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = compromiso.empresa + " · " + compromiso.fechaVencimiento,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(Dimens.espacioChico))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = FormatoMoneda.clp(compromiso.totalAPagar),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.size(Dimens.espacioMini))
                EtiquetaEstado(estado = compromiso.estado)
            }
        }
    }
}

@Preview(showBackground = true, name = "Claro")
@Preview(showBackground = true, name = "Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilaCompromisoPreview() {
    PagoFlexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            FilaCompromiso(
                compromiso = Compromiso(
                    folio = "CP-2026-0102",
                    empresa = "Club Los Halcones",
                    rubro = "Club deportivo",
                    concepto = "Cuota social octubre",
                    cuota = "10/12",
                    monto = 15000,
                    fechaVencimiento = "05-10-2026",
                    estado = EstadoCompromiso.VENCIDO,
                    recargo = 2000
                ),
                onClick = {},
                modifier = Modifier.padding(Dimens.espacioMedio)
            )
        }
    }
}
