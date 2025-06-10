package io.element.android.libraries.textcomposer.utils

import java.util.regex.Pattern

/**
 * Centralized regex patterns for URL detection.
 * Provides comprehensive URL pattern matching for various protocols and formats.
 */
object UrlPatterns {
    
    /**
     * Simple pattern for basic URL validation
     */
    val BASIC_URL_REGEX: Pattern = Pattern.compile(
        """^(https?)://[^\s/$.?#].[^\s]*$""",
        Pattern.CASE_INSENSITIVE
    )

    /**
     * Common URL schemes that should be recognized
     */
    val SUPPORTED_SCHEMES = setOf(
        "http", "https"
    )
}
