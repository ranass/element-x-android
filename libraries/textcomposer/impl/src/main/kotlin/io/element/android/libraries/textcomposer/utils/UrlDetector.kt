package io.element.android.libraries.textcomposer.utils

import android.util.Patterns

/**
 * Utility class for detecting URLs in text with support for custom display text.
 * Provides comprehensive URL detection following the established patterns in Element X Android.
 */
object UrlDetector {
    
    /**
     * Data class representing a detected URL with its position and optional custom display text.
     */
    data class UrlMatch(
        val url: String,
        val start: Int,
        val end: Int,
        val displayText: String = url
    )
    
    /**
     * Detects all URLs in the given text.
     * @param text The text to search for URLs
     * @return List of UrlMatch objects representing found URLs
     */
    fun detectUrls(text: CharSequence): List<UrlMatch> {
        if (text.isEmpty()) return emptyList()
        
        val matches = mutableListOf<UrlMatch>()
        
        // Use Android's built-in web URL pattern as primary detector
        val matcher = Patterns.WEB_URL.matcher(text)
        
        while (matcher.find()) {
            val url = matcher.group()
            val start = matcher.start()
            val end = matcher.end()
            
            // Validate the URL before adding
            if (isValidUrl(url)) {
                matches.add(UrlMatch(url, start, end))
            }
        }

        // Sort by start position and remove overlapping matches
        return matches.sortedBy { it.start }
            .fold(mutableListOf<UrlMatch>()) { acc, match ->
                if (acc.isEmpty() || acc.last().end <= match.start) {
                    acc.add(match)
                }
                acc
            }
    }
    
    /**
     * Validates if a string is a properly formed URL.
     * @param url The URL string to validate
     * @return true if the URL is valid, false otherwise
     */
    fun isValidUrl(url: String): Boolean {
        if (url.isBlank()) return false
        
        // Check minimum length
        if (url.length < 4) return false
        
        // Check for valid scheme or domain pattern
        return when {
            // URLs with protocol
            url.matches(Regex("^(https?)://.*", RegexOption.IGNORE_CASE)) -> {
                validateUrlWithProtocol(url)
            }
            else -> false
        }
    }

    /**
     * Validates URLs that include a protocol.
     */
    private fun validateUrlWithProtocol(url: String): Boolean {
        // Check for basic pattern validation
        if (!UrlPatterns.BASIC_URL_REGEX.matcher(url).matches()) return false
        
        // Extract scheme
        val scheme = url.substringBefore("://").lowercase()
        if (scheme !in UrlPatterns.SUPPORTED_SCHEMES) return false
        
        // Extract domain part
        val domainPart = url.substringAfter("://").substringBefore("/")
        return domainPart.isNotEmpty() && domainPart.contains('.')
    }
}
