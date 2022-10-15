package de.klamtluk.urlshortener.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnBadRequestWhenUrlMissing() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().string(containsString("url")));
    }

    @Test
    public void shouldReturnBadRequestWhenUrlMalformed() throws Exception {
        this.mockMvc.perform(get("/")
                        .queryParam("url", "not a url"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("url")));
    }

    @Test
    public void shouldReturnBadRequestWhenUrlEmpty() throws Exception {
        this.mockMvc.perform(get("/")
                        .queryParam("url", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("url")));
    }
}