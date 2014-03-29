
package edu.morgan.studentUser;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Record {

    @SerializedName("Row")
    @Expose
    private Row row;

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

}
