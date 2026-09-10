package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

@Composable
fun QuiziaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = QuiziaTheme.typography.bodyMedium) },
        placeholder = placeholder?.let { { Text(text = it, style = QuiziaTheme.typography.bodyMedium) } },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        enabled = enabled,
        shape = QuiziaTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = QuiziaTheme.colorScheme.primary,
            unfocusedBorderColor = QuiziaTheme.colorScheme.outline,
            focusedLabelColor = QuiziaTheme.colorScheme.primary,
            unfocusedLabelColor = QuiziaTheme.colorScheme.onSurfaceVariant,
            cursorColor = QuiziaTheme.colorScheme.primary,
        ),
        modifier = modifier,
    )
}

@Composable
fun QuiziaPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
) {
    var isVisible by remember { mutableStateOf(false) }
    QuiziaTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            androidx.compose.ui.text.input.PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
        ),
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    imageVector = if (isVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                    contentDescription = null,
                    tint = QuiziaTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        modifier = modifier,
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaTextFieldPreview() {
    QuiziaTheme {
        QuiziaTextField(
            value = "sk-abc123",
            onValueChange = {},
            label = "API Key",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(name = "Password – Light", showBackground = true)
@Preview(name = "Password – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaPasswordTextFieldPreview() {
    QuiziaTheme {
        QuiziaPasswordTextField(
            value = "sk-secret",
            onValueChange = {},
            label = "API Key",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
