package org.benetech.model.formDef;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class FormDef {
    public Specification specification;

    // the 'xlsx' property would go here
    // use JsonAnySetter to handle other formDef source, as per spec
    public Map<String, ObjectNode> sources;
    @JsonAnySetter
    public void anySetter(String key, ObjectNode value) {
        sources.put(key, value);
    }

    @JsonCreator
    public FormDef(@JsonProperty(required = true, value = "specification") Specification specification) {
        this.specification = specification;

        this.sources = new HashMap<>();
    }
}
