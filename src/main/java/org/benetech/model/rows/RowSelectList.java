package org.benetech.model.rows;


import java.util.ArrayList;
import java.util.List;

public class RowSelectList {
    // public String action

    private List<RowSelect> rows;


    public void addRow(RowSelect row) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.add(row);
    }


    /**
     * @return List<RowSelect> return the rows
     */
    public List<RowSelect> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<RowSelect> rows) {
        this.rows = rows;
    }

}
