package edu.tcu.cs.hogwarts_artifacts_online.wizard.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import edu.tcu.cs.hogwarts_artifacts_online.wizard.Wizard;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {

    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id());
        wizard.setName(source.name());
        //wizard.setArtifacts(Collections.emptyList());// comparer avec binyang wei au moment de la correction.
        return wizard;
    }

}
