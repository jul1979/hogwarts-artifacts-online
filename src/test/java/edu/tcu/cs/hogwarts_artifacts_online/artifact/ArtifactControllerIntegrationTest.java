package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Artifact API endpoints")
@Tag("integration")
public class ArtifactControllerIntegrationTest {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper objectMapper;

        String token;

        @BeforeEach
        void SetUp() throws Exception {
                ResultActions resultActions = this.mockMvc
                                .perform(MockMvcRequestBuilders.post("/users/login").with(httpBasic("john", "123456")));
                MvcResult mvcResult = resultActions.andDo(MockMvcResultHandlers.print()).andReturn();
                String contentAsString = mvcResult.getResponse().getContentAsString();
                JSONObject json = new JSONObject(contentAsString);
                this.token = "Bearer " + json.getJSONObject("data").getString("token");
        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)//resets the database
        void testFindAllArtifactsSuccess() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders.get("/artifacts").accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(6)));
        }

        @Test
        @DisplayName("Check addArtifact with valid input (POST)")
        void testAddArtifactSuccess() throws Exception {
                Artifact a = new Artifact();
                a.setName("Remembrall");
                a.setDescription(
                                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
                a.setImageUrl("ImageUrl");

                String json = this.objectMapper.writeValueAsString(a);
                this.mockMvc
                                .perform(
                                                MockMvcRequestBuilders.post("/artifacts")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .header("Authorization", this.token)
                                                                .content(json).accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Remembrall"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(
                                                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered."))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl").value("ImageUrl"));
                this.mockMvc
                                .perform(MockMvcRequestBuilders.get("/artifacts").accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(7)));
        }
}
