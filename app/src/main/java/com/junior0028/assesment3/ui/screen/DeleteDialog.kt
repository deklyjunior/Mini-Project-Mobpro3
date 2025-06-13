package com.junior0028.assesment3.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.junior0028.assesment3.R
import com.junior0028.assesment3.model.Menu

@Composable
fun DeleteDialog(
    menu: Menu,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.hapus_menu_title))
        },
        text = {
            Text(text = stringResource(R.string.hapus_menu_body, menu.judul))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.batal))
            }
        }
    )
}