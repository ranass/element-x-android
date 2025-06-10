package io.element.android.libraries.textcomposer.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Represents a detected URL with its metadata for preview display
 */
data class DetectedUrl(
    val url: String,
    val title: String? = null,
    val description: String? = null,
    val isLoading: Boolean = false,
)

/**
 * State for managing URL previews in the text composer
 */
data class UrlPreviewState(
    val detectedUrls: ImmutableList<DetectedUrl> = persistentListOf(),
    val isEnabled: Boolean = true,
) {
    /**
     * Whether there are any URLs to display
     */
    val hasUrls: Boolean get() = detectedUrls.isNotEmpty()

    /**
     * Whether any URLs are currently loading
     */
    val isLoading: Boolean get() = detectedUrls.any { it.isLoading }
}
