package org.benetech.model.formDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
    public Collection<Prompt> prompts;
}
