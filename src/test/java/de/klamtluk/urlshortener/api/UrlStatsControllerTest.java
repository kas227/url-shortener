package de.klamtluk.urlshortener.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFailWithNotFoundForNonExisting() throws Exception {
        final var key = "1231";
        this.mockMvc.perform(get("/api/stats")
                        .queryParam("key", key))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReportStatsAfterOpening() throws Exception {
        final var key = createShortUrl("https://locahost.org");
        openUrl(key);
        expectClicks(key, 1);
        openUrl(key);
        expectClicks(key, 2);
    }

    private void expectClicks(String key, int count) throws Exception {
        this.mockMvc.perform(get("/api/stats")
                        .queryParam("key", key))
                .andExpect(jsonPath("$.clicks", Matchers.equalTo(count)));
    }

    private void openUrl(String key) throws Exception {
        this.mockMvc.perform(get("/" + key))
                .andExpect(status().isPermanentRedirect());
    }

    private String createShortUrl(String longUrl) throws Exception {
        return this.mockMvc.perform(post("/")
                        .content(String.format("{\"url\" : \"%s\"}", longUrl))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}