package org.benetech.model.formDef;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class DisplayCollection {
    public Display prompt;
    public Display hint;
    public Display constraint_message;

    public Map<String, Display> allDisplays;
    @JsonAnySetter
    public void anySetter(String key, Display value) {
        allDisplays.put(key, value);
    }

    @JsonCreator
    public DisplayCollection(@JsonProperty(value = "prompt") Display prompt,
                             @JsonProperty(value = "hint") Display hint,
                             @JsonProperty(value = "constraint_message") Display constraint_message) {
        this.prompt = prompt;
        this.hint = hint;
        this.constraint_message = constraint_message;

        this.allDisplays = new HashMap<>();
        this.allDisplays.put("prompt", prompt);
        this.allDisplays.put("hint", hint);
        this.allDisplays.put("constraint_message", constraint_message);
    }
}
