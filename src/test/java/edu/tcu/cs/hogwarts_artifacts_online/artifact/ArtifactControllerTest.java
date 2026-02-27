package edu.tcu.cs.hogwarts_artifacts_online.artifact;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "dev")
public class ArtifactControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockitoBean
        private ArtifactService artifactService;

        @Autowired
        ObjectMapper objectMapper;

        List<Artifact> artifacts;

        @BeforeEach
        void setUp() {
                artifacts = new ArrayList<>();

                // Artifact a1
                Artifact a1 = new Artifact();
                a1.setId("1250808601744904191");
                a1.setName("Deluminator");
                a1.setDescription(
                                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
                a1.setImageUrl("ImageUrl");

                // Artifact a2
                Artifact a2 = new Artifact();
                a2.setId("1250808601744904192");
                a2.setName("Invisibility Cloak");
                a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
                a2.setImageUrl("ImageUrl");

                // Artifact a3
                Artifact a3 = new Artifact();
                a3.setId("1250808601744904193");
                a3.setName("Elder Wand");
                a3.setDescription(
                                "The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
                a3.setImageUrl("ImageUrl");

                // Artifact a4
                Artifact a4 = new Artifact();
                a4.setId("1250808601744904194");
                a4.setName("The Marauder's Map");
                a4.setDescription(
                                "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
                a4.setImageUrl("ImageUrl");

                // Artifact a5
                Artifact a5 = new Artifact();
                a5.setId("1250808601744904195");
                a5.setName("The Sword Of Gryffindor");
                a5.setDescription(
                                "A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
                a5.setImageUrl("ImageUrl");

                artifacts.add(a1);
                artifacts.add(a2);
                artifacts.add(a3);
                artifacts.add(a4);
                artifacts.add(a5);
        }

        @AfterEach
        void tearDown() {
        }

        @Test
        void testFindArtifactById() throws Exception {

                given(this.artifactService.findById("1250808601744904191")).willReturn(this.artifacts.get(0));

                this.mockMvc.perform(
                                MockMvcRequestBuilders.get("/artifacts/1250808601744904191")
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find One Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904191"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Deluminator"));
        }

        @Test
        void testFindArtifactByIdNotFound() throws Exception {

                given(this.artifactService.findById("1250808601744904191"))
                                .willThrow(new ObjectNotFoundException("artifact", "1250808601744904191"));

                this.mockMvc.perform(
                                MockMvcRequestBuilders.get("/artifacts/1250808601744904191")
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find artifact with Id 1250808601744904191 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void testFindAllArtifactsSuccess() throws Exception {
                given(this.artifactService.findAll()).willReturn(this.artifacts);
                this.mockMvc.perform(MockMvcRequestBuilders.get("/artifacts")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data",
                                                Matchers.hasSize(this.artifacts.size())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value("1250808601744904191"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Deluminator"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value("1250808601744904192"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name")
                                                .value("Invisibility Cloak"));
        }

        @Test
        void testAddArtifactsSuccess() throws Exception {
                // Given

                ArtifactDto artifactDto = new ArtifactDto(null, "Remembrall",
                                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
                                "ImageUrl", null);

                String json = this.objectMapper.writeValueAsString(artifactDto);

                Artifact savedArtifact = new Artifact();
                savedArtifact.setId("1250808601744904197");
                savedArtifact.setName("Remembrall");
                savedArtifact.setDescription(
                                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
                savedArtifact.setImageUrl("ImageUrl");

                given(this.artifactService.save(any(Artifact.class))).willReturn(savedArtifact);

                // When and then
                this.mockMvc.perform(MockMvcRequestBuilders.post("/artifacts")
                                .content(json).contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(savedArtifact.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description")
                                                .value(savedArtifact.getDescription()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl")
                                                .value(savedArtifact.getImageUrl()));
        }

        @Test
        void testUpdateArtifactSuccess() throws Exception {
                // Given
                ArtifactDto artifactDto = new ArtifactDto("1250808601744904192",
                                "Invisibility Cloak",
                                "A new description.",
                                "ImageUrl",
                                null);
                String json = this.objectMapper.writeValueAsString(artifactDto);

                Artifact updatedArtifact = new Artifact();
                updatedArtifact.setId("1250808601744904192");
                updatedArtifact.setName("Invisibility Cloak");
                updatedArtifact.setDescription("A new description.");
                updatedArtifact.setImageUrl("ImageUrl");

                given(this.artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class)))
                                .willReturn(updatedArtifact);

                // When and then
                this.mockMvc.perform(MockMvcRequestBuilders.put("/artifacts/1250808601744904192")
                                .contentType(MediaType.APPLICATION_JSON).content(json)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value("1250808601744904192"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name")
                                                .value(updatedArtifact.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description")
                                                .value(updatedArtifact.getDescription()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageUrl")
                                                .value(updatedArtifact.getImageUrl()));
        }

        @Test
        void testUpdateArtifactErrorWithNonExistentId() throws Exception {
                // Given
                ArtifactDto artifactDto = new ArtifactDto("1250808601744904192",
                                "Invisibility Cloak",
                                "A new description.",
                                "ImageUrl",
                                null);
                String json = this.objectMapper.writeValueAsString(artifactDto);

                given(this.artifactService.update(eq("1250808601744904192"), Mockito.any(Artifact.class)))
                                .willThrow(new ObjectNotFoundException("artifact", "1250808601744904192"));

                // When and then
                this.mockMvc.perform(MockMvcRequestBuilders.put("/artifacts/1250808601744904192")
                                .contentType(MediaType.APPLICATION_JSON).content(json)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find artifact with Id 1250808601744904192 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void testDeleteArtifactSuccess() throws Exception {

                doNothing().when(artifactService).delete("1250808601744904192");
                this.mockMvc.perform(MockMvcRequestBuilders.delete("/artifacts/1250808601744904192")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Delete Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void testDeleteArtifactErrorWithNonExistentId() throws Exception {
                // Given
                doThrow(new ObjectNotFoundException("artifact", "1250808601744904191")).when(this.artifactService)
                                .delete("1250808601744904191");

                // When and then
                this.mockMvc.perform(MockMvcRequestBuilders.delete("/artifacts/1250808601744904191")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find artifact with Id 1250808601744904191 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

}
