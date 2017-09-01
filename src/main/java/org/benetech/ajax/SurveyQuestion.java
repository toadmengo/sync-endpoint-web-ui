package org.benetech.ajax;

public class SurveyQuestion {
  private String type;
  private String name;
  private String displayText;
  private Integer rowNum;

  public SurveyQuestion(String type, String name, String displayText, Integer rowNum) {
    this.type = type;
    this.name = name;
    this.displayText = displayText;
    this.rowNum = rowNum;
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDisplayText() {
    return displayText;
  }
  public void setDisplayText(String displayText) {
    this.displayText = displayText;
  }
  public Integer getRowNum() {
    return rowNum;
  }
  public void setRowNum(Integer rowNum) {
    this.rowNum = rowNum;
  }
}
