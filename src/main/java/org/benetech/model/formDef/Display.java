package org.benetech.model.formDef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Display {
    public String text;
    public String audio;
    public String video;
    public String image;
    public String stringTokenRef;

    @JsonCreator
    public Display(JsonNode display) {
        if (display.isValueNode()) {
            stringTokenRef = display.textValue();
        }

        stringTokenRef = display.isValueNode() ? display.textValue() : null;

        text = display.path("text").asText();
        audio = display.path("audio").asText();
        video = display.path("video").asText();
        image = display.path("image").asText();
    }
}
