package de.klamtluk.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class UrlEncoder {
    public static final int RADIX = 36;

    /**
     * Encode the given number to a capitalized alphanumeric string using base36.
     *
     * <p>
     * Take a look at {@link #decode} for the reverse operation.
     * Example:
     * <code>
     * encode(10232L) == "7w8";
     * </code>
     * </p>
     *
     * @param input source number to encode
     * @return encoded string
     */
    public String encode(Long input) {
        return Long.toString(input, RADIX);
    }

    /**
     * Decode the given encoded string to its long value.
     *
     * <p>
     * Take a look at {@link #encode} for the reverse operation.
     * Example:
     * <code>
     * decode("7w8") == 10232L;
     * </code>
     * </p>
     *
     * @param longUrl encoded input to decode
     * @return decoded value
     */
    public Long decode(String longUrl) {
        final var lowerCasedUrl = longUrl.toLowerCase();
        return Long.valueOf(lowerCasedUrl, RADIX);

    }
}
