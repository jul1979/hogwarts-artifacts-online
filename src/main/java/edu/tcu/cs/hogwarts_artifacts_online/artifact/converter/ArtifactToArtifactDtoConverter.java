package edu.tcu.cs.hogwarts_artifacts_online.artifact.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.Artifact;
import edu.tcu.cs.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwarts_artifacts_online.wizard.dto.WizardDto;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    private final WizardToWizardDtoConverter wizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardDtoConverter) {
        this.wizardDtoConverter = wizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        WizardDto wizardDto = source.getOwner() != null ? this.wizardDtoConverter.convert(source.getOwner()) : null;
        ArtifactDto artifactDto = new ArtifactDto(source.getId(), source.getName(), source.getDescription(),
                source.getImageUrl(), wizardDto);
        return artifactDto;
    }
}
