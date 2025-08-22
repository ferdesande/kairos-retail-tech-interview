package com.kairos.pricing.infrastructure.web.dto;

/**
 * Standard error response for API endpoints.
 * <p>
 * For production environments, consider implementing RFC 7807 Problem Details
 * with additional fields: title, instance, timestamp, and extensions.
 * Reference: <a href="https://tools.ietf.org/html/rfc7807">RFC 7807</a>
 */
public record ErrorResponse(
        String type,
        String detail,
        int status
) {
}