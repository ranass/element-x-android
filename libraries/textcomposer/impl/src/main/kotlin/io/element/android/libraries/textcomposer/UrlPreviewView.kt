package io.element.android.libraries.textcomposer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.element.android.compound.theme.ElementTheme
import io.element.android.compound.tokens.generated.CompoundIcons
import io.element.android.libraries.designsystem.preview.ElementPreview
import io.element.android.libraries.designsystem.preview.PreviewsDayNight
import io.element.android.libraries.designsystem.theme.components.CircularProgressIndicator
import io.element.android.libraries.designsystem.theme.components.Icon
import io.element.android.libraries.designsystem.theme.components.Text
import io.element.android.libraries.textcomposer.model.DetectedUrl
import io.element.android.libraries.textcomposer.model.UrlPreviewState
import io.element.android.libraries.ui.strings.CommonStrings
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun UrlPreviewView(
    urlPreviewState: UrlPreviewState,
    onRemoveUrl: (DetectedUrl) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = urlPreviewState.hasUrls,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            urlPreviewState.detectedUrls.forEach { detectedUrl ->
                UrlPreviewCard(
                    detectedUrl = detectedUrl,
                    onRemoveUrl = { onRemoveUrl(detectedUrl) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun UrlPreviewCard(
    detectedUrl: DetectedUrl,
    onRemoveUrl: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Favicon or Link Icon
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (detectedUrl.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = CompoundIcons.Link(),
                    contentDescription = null,
                    tint = ElementTheme.colors.iconSecondary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        // Content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (detectedUrl.isLoading) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = detectedUrl.url,
                    style = ElementTheme.typography.fontBodySmMedium,
                    color = ElementTheme.colors.textPrimary,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Clip
                )
            } else {
                if (detectedUrl.title != null) {
                    Text(
                        text = detectedUrl.title,
                        style = ElementTheme.typography.fontBodySmMedium,
                        color = ElementTheme.colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (detectedUrl.description != null) {
                    Text(
                        text = detectedUrl.description,
                        style = ElementTheme.typography.fontBodyXsRegular,
                        color = ElementTheme.colors.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Close button
        Icon(
            imageVector = CompoundIcons.Close(),
            contentDescription = stringResource(CommonStrings.action_close),
            tint = ElementTheme.colors.iconSecondary,
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    enabled = true,
                    onClick = onRemoveUrl,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                ),
        )
    }
}

@PreviewsDayNight
@Composable
internal fun UrlPreviewViewPreview(
    @PreviewParameter(UrlPreviewStateProvider::class) urlPreviewState: UrlPreviewState
) = ElementPreview {
    UrlPreviewView(
        urlPreviewState = urlPreviewState,
        onRemoveUrl = {},
        modifier = Modifier
            .background(ElementTheme.colors.bgSubtleSecondary)
            .padding(8.dp)
    )
}

internal class UrlPreviewStateProvider : androidx.compose.ui.tooling.preview.PreviewParameterProvider<UrlPreviewState> {
    override val values = sequenceOf(
        UrlPreviewState(),
        UrlPreviewState(
            detectedUrls = persistentListOf(
                DetectedUrl(
                    url = "https://www.example.com",
                    isLoading = true
                )
            )
        ),
        UrlPreviewState(
            detectedUrls = persistentListOf(
                DetectedUrl(
                    url = "https://www.github.com",
                    title = "GitHub: Let's build from here",
                    description = "GitHub is where over 100 million developers shape the future of software, together.",
                )
            )
        ),
        UrlPreviewState(
            detectedUrls = persistentListOf(
                DetectedUrl(
                    url = "https://www.example.com",
                    title = "Example Domain",
                    description = "This domain is for use in illustrative examples in documents.",
                ),
                DetectedUrl(
                    url = "https://matrix.org",
                    title = "Matrix.org",
                    description = "An open network for secure, decentralized communication",
                )
            )
        )
    )
}

