package com.jujodevs.invitta.core.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import com.jujodevs.invitta.core.designsystem.theme.dimens

@Composable
fun InvittaButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(MaterialTheme.dimens.mediumLarge),
        contentPadding =
            PaddingValues(
                horizontal = MaterialTheme.dimens.medium,
                vertical = MaterialTheme.dimens.extraSmall,
            ),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon()
            Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.mediumSmall),
                text = label,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun InvittaButtonPreview() {
    InvittaTheme {
        InvittaButton(
            label = "Test",
            icon = {
                Icon(Icons.Default.Add, contentDescription = "Add")
            },
            onClick = {},
        )
    }
}
