package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifacts_online.system.StatusCode;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.dto.WizardDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WizardControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockitoBean
        WizardService wizardService;

        @InjectMocks
        WizardController wizardController;

        List<Wizard> wizards;

        @Autowired
        ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                this.wizards = new ArrayList<>();
                Wizard w1 = new Wizard();
                w1.setName("Albus Dumbledore");
                w1.setId(1);

                Wizard w2 = new Wizard();
                w2.setName("Harry Potter");
                w2.setId(2);

                Wizard w3 = new Wizard();
                w3.setName("Neville Longbottom");
                w3.setId(3);

                w1.setArtifacts(Collections.emptyList());
                w2.setArtifacts(Collections.emptyList());
                w3.setArtifacts(Collections.emptyList());

                this.wizards.add(w1);
                this.wizards.add(w2);
                this.wizards.add(w3);
        }

        @AfterEach
        void tearDown() {
                this.wizards.clear();
        }

        @Test
        void testFindAllWizardsSuccess() throws Exception {
                given(this.wizardService.findAll()).willReturn(this.wizards);
                this.mockMvc.perform(MockMvcRequestBuilders.get("/wizards")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find All Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data",
                                                Matchers.hasSize(this.wizards.size())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Albus Dumbledore"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].numberOfArtifacts").value(0))

                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name")
                                                .value("Harry Potter"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].numberOfArtifacts").value(0))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name")
                                                .value("Neville Longbottom"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].numberOfArtifacts").value(0));
        }

        @Test
        void findWizardByIdSuccess() throws Exception {
                given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));
                this.mockMvc.perform(MockMvcRequestBuilders.get("/wizards/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Find One Success"))

                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("Albus Dumbledore"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.numberOfArtifacts").value(0))

                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1));
        }

        @Test
        void testFindWizardByIdNotFound() throws Exception {
                given(this.wizardService.findById(1)).willThrow(new ObjectNotFoundException("wizard", 1));
                this.mockMvc.perform(MockMvcRequestBuilders.get("/wizards/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find wizard with Id 1 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void deleteWizardSuccess() throws Exception {
                doNothing().when(wizardService).delete(1);
                this.mockMvc.perform(MockMvcRequestBuilders.delete("/wizards/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Delete Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void testDeleteWizardErrorWithNonExistentId() throws Exception {
                doThrow(new ObjectNotFoundException("wizard", 1)).when(this.wizardService)
                                .delete(1);
                this.mockMvc.perform(MockMvcRequestBuilders.delete("/wizards/1")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find wizard with Id 1 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void testAddWizardSuccess() throws Exception {
                WizardDto wizardDto = new WizardDto(null, "Hermione Granger", 0);
                String json = this.objectMapper.writeValueAsString(wizardDto);
                Wizard savedWizard = new Wizard();
                savedWizard.setName("Hermione Granger");
                savedWizard.setArtifacts(Collections.emptyList());
                savedWizard.setId(4);
                given(this.wizardService.save(any(Wizard.class))).willReturn(savedWizard);

                this.mockMvc.perform(MockMvcRequestBuilders.post("/wizards")
                                .content(json).contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Add Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.numberOfArtifacts").value(0))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(savedWizard.getName()));
        }

        @Test
        void testUpdateWizardSuccess() throws Exception {
                WizardDto wizardDto = new WizardDto(4, "Albinus D. Embercloak", null);
                String json = objectMapper.writeValueAsString(wizardDto);

                Wizard updatedWizard = new Wizard();
                updatedWizard.setId(4);
                updatedWizard.setName("Albinus D. Embercloak");
                updatedWizard.setArtifacts(Collections.emptyList());
                given(this.wizardService.update(eq(4), Mockito.any(Wizard.class)))
                                .willReturn(updatedWizard);
                this.mockMvc.perform(MockMvcRequestBuilders.put("/wizards/4")
                                .content(json).contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Update Success"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.numberOfArtifacts").value(0))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name")
                                                .value(updatedWizard.getName()));
        }

        @Test
        void testAddArtifactToWizardSuccess() throws Exception {
                Wizard w3 = new Wizard();
                w3.setId(10);
                w3.setName("Neville Longbottom");

                Artifact a1 = new Artifact();
                a1.setId("1250808601744904191");
                a1.setName("Deluminator");
                a1.setDescription(
                                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
                a1.setImageUrl("ImageUrl");

                Wizard updatedWizard = new Wizard();// A vérifier,code peut être unitile?
                updatedWizard.setId(10);
                updatedWizard.setName("Neville Longbottom");
                updatedWizard.addArtifact(a1);

                given(this.wizardService.addArtifactToWizard(eq(10), eq("1250808601744904191")))
                                .willReturn(updatedWizard);// A vérifier,code peut être unitile?
                this.mockMvc.perform(
                                MockMvcRequestBuilders.put("/wizards/10/artifacts/1250808601744904191")
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(true))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.SUCCESS))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Artifact Assignment Success"));
        }

        @Test
        void testAssignArtifactErrorWithNonExistentWizardId() throws Exception {
                // Given
                doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).addArtifactToWizard(5,
                                "1250808601744904191");

                // When and then
                this.mockMvc.perform(
                                MockMvcRequestBuilders.put("/wizards/5/artifacts/1250808601744904191")
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find wizard with Id 5 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

        @Test
        void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
                // Given
                doThrow(new ObjectNotFoundException("artifact", "1250808601744904199")).when(this.wizardService)
                                .addArtifactToWizard(2, "1250808601744904199");

                // When and then
                this.mockMvc.perform(
                                MockMvcRequestBuilders.put("/wizards/2/artifacts/1250808601744904199")
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.flag").value(false))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusCode.NOT_FOUND))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                                                .value("Could not find artifact with Id 1250808601744904199 :("))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        }

}
