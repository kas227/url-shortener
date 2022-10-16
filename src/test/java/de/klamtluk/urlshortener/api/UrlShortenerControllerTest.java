package de.klamtluk.urlshortener.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerTest {

    static final String URL_PARAM = "url";
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
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(URL_PARAM)));
    }

    @Test
    public void shouldShortenUrl() throws Exception {
        final var longUrl = "https://localhost/long-url";
        final var shortenedUrl = this.mockMvc.perform(post("/")
                        .content(String.format("{\"url\" : \"%s\"}", longUrl))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(shortenedUrl.length() <= MAX_LENGTH);
    }

    @Test
    public void shouldRedirectedForExistingUrl() throws Exception {
        final String existingUrl = createNewShortUrl();

        this.mockMvc.perform(get("/"+existingUrl))
                .andExpect(status().isPermanentRedirect());
    }

    private String createNewShortUrl() throws Exception {
        return this.mockMvc.perform(post("/")
                        .content("""
                                {"url" : "https://localhost/long-url"}
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void shouldFailForNonExistingUrl() throws Exception {
        final String nonExistingUrl = "123";
        this.mockMvc.perform(get("/" + nonExistingUrl))
                .andExpect(status().isNotFound());
    }
}