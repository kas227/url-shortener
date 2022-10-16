package de.klamtluk.urlshortener.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerTest {

    public static final int MAX_LENGTH = 7;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnBadRequestWhenUrlMissing() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnBadRequestWhenUrlMalformed() throws Exception {
        this.mockMvc.perform(get("/*="))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailForNonExistingUrl() throws Exception {
        final String nonExistingUrl = "123";
        this.mockMvc.perform(get("/" + nonExistingUrl))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldShortenUrl() throws Exception {
        final var longUrl = "https://localhost/long-url";
        final var shortenedUrl = this.mockMvc.perform(post("/")
                        .content(String.format("{\"url\" : \"%s\"}", longUrl))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(shortenedUrl.length() <= MAX_LENGTH);
    }

    @Test
    public void shouldRedirectedForExistingUrl() throws Exception {
        final String existingUrl = this.mockMvc.perform(post("/")
                        .content("""
                                {"url" : "https://localhost/long-url"}
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        this.mockMvc.perform(get("/" + existingUrl))
                .andExpect(status().isPermanentRedirect());
    }

    @Test
    public void shouldCreateAliasForUrl() throws Exception {
        final var longUrl = "https://localhost/long-url";
        final var alias = "mercedes";
        this.mockMvc.perform(post("/")
                        .content(String.format("""
                                {
                                    "url": "%s",
                                    "alias": "%s"
                                }
                                """, longUrl, alias))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(alias));
    }

    @Test
    public void shouldRedirectForAlias() throws Exception {
        final var longUrl = "https://localhost/long-url";
        final var alias = "mercedes";
        this.mockMvc.perform(post("/")
                        .content(String.format("""
                                {
                                    "url": "%s",
                                    "alias": "%s"
                                }
                                """, longUrl, alias))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/" + alias))
                .andExpect(status().isPermanentRedirect());
    }
}
