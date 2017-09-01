package org.benetech.model.formDef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specification {
    public Map<String, Section> sections;

    @JsonCreator
    public Specification(@JsonProperty(required = true, value = "sections") Map<String, Section> sections) {
        this.sections = sections;
    }
}
