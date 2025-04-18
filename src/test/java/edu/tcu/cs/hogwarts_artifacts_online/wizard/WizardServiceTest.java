package edu.tcu.cs.hogwarts_artifacts_online.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifacts_online.system.ObjectNotFoundException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    private List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setName("Albus Dumbledore");
        w1.setId(1);

        Wizard w2 = new Wizard();
        w2.setName("Harry Potter");
        w1.setId(2);

        Wizard w3 = new Wizard();
        w3.setName("Neville Longbottom");
        w1.setId(3);
        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
        this.wizards.clear();
    }

    @Test
    void testFindAllSuccess() {
        given(this.wizardRepository.findAll()).willReturn(wizards);
        List<Wizard> actualWizards = this.wizardService.findAll();
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        Wizard w1 = new Wizard();
        w1.setName("Albus Dumbledore");
        w1.setId(1);
        w1.setArtifacts(Collections.emptyList());
        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w1));
        Wizard actualWizard = this.wizardService.findById(1);
        assertThat(actualWizard.getId()).isEqualTo(w1.getId());
        assertThat(actualWizard.getName()).isEqualTo(w1.getName());
        assertThat(actualWizard.getNumberOfArtifacts()).isEqualTo(0);
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        given(this.wizardRepository.findById(Mockito.anyInt())).willReturn(Optional.empty());

        Throwable thrown = Assertions.catchThrowable(() -> {
            Wizard wizard = this.wizardService.findById(1);
        });

        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 1 :(");

        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        Wizard w1 = new Wizard();
        w1.setName("Albus Dumbledore");
        w1.setId(1);
        w1.setArtifacts(Collections.emptyList());

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w1));
        doNothing().when(wizardRepository).deleteById(1);

        wizardService.delete(1);
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.delete(1);
        });
        verify(wizardRepository, times(1)).findById(1);
    }

    @Test
    void testsaveSuccess() {

        Wizard newWizard = new Wizard();
        newWizard.setName("Wizard 3");
        newWizard.setArtifacts(Collections.emptyList());

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);
        Wizard savedWizard = this.wizardService.save(newWizard);
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSucess() {
        Wizard oldWizard = new Wizard();
        oldWizard.setId(4);
        oldWizard.setName("Albus Dumbledore");
        oldWizard.setArtifacts(Collections.emptyList());

        Wizard update = new Wizard();
        update.setId(4);
        update.setName("Albinus D. Embercloak");
        update.setArtifacts(Collections.emptyList());
        given(this.wizardRepository.findById(4)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        Wizard updatedWizard = this.wizardService.update(4, update);
        assertThat(update.getId()).isEqualTo(updatedWizard.getId());
        assertThat(update.getName()).isEqualTo(updatedWizard.getName());
        verify(wizardRepository, times(1)).findById(4);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        Wizard update = new Wizard();
        update.setId(4);
        update.setName("Albus Dumbledore");
        update.setArtifacts(Collections.emptyList());
        given(this.wizardRepository.findById(4)).willReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> this.wizardService.update(4, update));
        verify(this.wizardRepository, times(1)).findById(4);
    }

    @Test
    void testAddArtifactToWizardSuccess() {

        Wizard w3 = new Wizard();
        w3.setId(10);
        w3.setName("Neville Longbottom");

        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription(
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        given(this.wizardRepository.findById(10)).willReturn(Optional.of(w3));
        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
        given(this.wizardRepository.save(w3)).willReturn(w3);

        Wizard updateWizard = this.wizardService.addArtifactToWizard(10, "1250808601744904191");

        assertThat(updateWizard.getArtifacts().contains(a1));
        assertThat(a1.getOwner()).isEqualTo(updateWizard);

        verify(this.wizardRepository, times(1)).findById(10);
        verify(this.artifactRepository, times(1)).findById("1250808601744904191");
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        // Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.addArtifactToWizard(3, "1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 3 :(");
        assertThat(a.getOwner().getId()).isEqualTo(2);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        // Given
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.addArtifactToWizard(3, "1250808601744904192");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
    }

}
