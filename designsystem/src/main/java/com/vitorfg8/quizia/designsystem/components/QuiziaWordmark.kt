package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.R

/** The Quizia name written the way the brand does it: dark "Quiz" followed by a purple "ia". */
@Composable
fun QuiziaWordmark(
    modifier: Modifier = Modifier,
    style: TextStyle = QuiziaTheme.typography.titleLarge,
) {
    val lead = stringResource(id = R.string.brand_name_lead)
    val accent = stringResource(id = R.string.brand_name_accent)
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = QuiziaTheme.colorScheme.onBackground)) { append(lead) }
            withStyle(SpanStyle(color = QuiziaTheme.colorScheme.primary)) { append(accent) }
        },
        style = style,
        modifier = modifier,
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaWordmarkPreview() {
    QuiziaTheme {
        QuiziaWordmark(style = QuiziaTheme.typography.displaySmall)
    }
}
