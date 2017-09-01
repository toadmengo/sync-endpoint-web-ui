package org.benetech.model.formDef;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Prompt {
    public String name;
    public DisplayCollection display;
    public String type;
    public int _row_num;

    @JsonCreator
    public Prompt(@JsonProperty(value = "name") String name,
                  @JsonProperty(value = "type") String type,
                  @JsonProperty(required = true, value = "_row_num") int _row_num,
                  @JsonProperty(value = "display") DisplayCollection display) {
        this.name = name;
        this.display = display;
        this.type = type;
        this._row_num = _row_num;
    }
}
