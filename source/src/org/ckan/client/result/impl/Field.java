package org.ckan.client.result.impl;

/**
 * Represents a field
 *
 * @author      Ross Jones <ross.jones@okfn.org>, Jay Guo <jguo144@gmail.com>
 * @version     1.8
 * @since       2016-01-01
 */
public class Field {

    private String id;
    private String type;

    public Field() {}
    public Field(String key, String value) {
        id = key;
        type = value;
    }
    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public void setType(String v) {
        type = v;
    }

    public String toString() {
        return "<id: " + this.getId() + ", type: " + this.getType() + ">";
    }
}