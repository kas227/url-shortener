package de.klamtluk.urlshortener.api;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UrlShortenerControllerTest {

    static final String URL_PARAM = "url";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnBadRequestWhenUrlMissing() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(URL_PARAM)));
    }

    @Test
    public void shouldReturnBadRequestWhenUrlMalformed() throws Exception {
        this.mockMvc.perform(get("/").queryParam(URL_PARAM, "not a url"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(URL_PARAM)));
    }

    @Test
    public void shouldReturnBadRequestWhenUrlEmpty() throws Exception {
        this.mockMvc.perform(get("/").queryParam(URL_PARAM, ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(URL_PARAM)));
    }

    @Test
    public void shouldRedirectedForExistingUrl() throws Exception {
        final String existingUrl = createNewShortUrl();

        this.mockMvc.perform(get("/").queryParam(URL_PARAM, existingUrl))
                .andExpect(status().isPermanentRedirect());
    }

    private String createNewShortUrl() throws Exception {
        return this.mockMvc.perform(post("/")
                        .content("""
                                {"url" : "https://localhost/long-url"}
                                """))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void shouldFailForNotExistingUrl() throws Exception {
        final String nonExistingUrl = "123";
        this.mockMvc.perform(get("/").queryParam(URL_PARAM, nonExistingUrl))
                .andExpect(status().isNotFound());
    }
}