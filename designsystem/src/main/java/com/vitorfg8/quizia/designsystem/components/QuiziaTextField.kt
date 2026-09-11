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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.R

@Composable
fun QuiziaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
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
        label = label?.let { { Text(text = it, style = QuiziaTheme.typography.bodyMedium) } },
        placeholder = placeholder?.let { { Text(text = it, style = QuiziaTheme.typography.bodyMedium) } },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        enabled = enabled,
        textStyle = QuiziaTheme.typography.bodyLarge,
        shape = QuiziaTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = QuiziaTheme.colorScheme.surface,
            unfocusedContainerColor = QuiziaTheme.colorScheme.surface,
            disabledContainerColor = QuiziaTheme.colorScheme.surface,
            focusedBorderColor = QuiziaTheme.colorScheme.primary,
            unfocusedBorderColor = QuiziaTheme.colorScheme.outline,
            disabledBorderColor = QuiziaTheme.colorScheme.outline,
            focusedLabelColor = QuiziaTheme.colorScheme.primary,
            unfocusedLabelColor = QuiziaTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = QuiziaTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = QuiziaTheme.colorScheme.onSurfaceVariant,
            cursorColor = QuiziaTheme.colorScheme.primary,
        ),
        modifier = modifier,
    )
}

/** Text field for secrets: the value is masked until the player asks to reveal it. */
@Composable
fun QuiziaPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
) {
    var isVisible by remember { mutableStateOf(false) }
    val toggleDescription = stringResource(
        id = if (isVisible) R.string.password_field_hide_value else R.string.password_field_show_value,
    )
    QuiziaTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    imageVector = if (isVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                    contentDescription = toggleDescription,
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
            value = "",
            onValueChange = {},
            placeholder = "Enter your API key",
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
            placeholder = "Enter your API key",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
