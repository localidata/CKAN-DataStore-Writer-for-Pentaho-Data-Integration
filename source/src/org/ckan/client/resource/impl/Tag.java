/*
CKANClient-J - Data Catalogue Software client in Java
Copyright (C) 2013 Newcastle University
Copyright (C) 2012 Open Knowledge Foundation

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.ckan.client.resource.impl;

import org.ckan.client.resource.CKANResource;

/**
 * Represents a tag
 *
 * @author      Andrew Martin <andrew.martin@ncl.ac.uk>, Ross Jones <ross.jones@okfn.org>
 * @version     1.8
 * @since       2012-05-01
 */
public class Tag extends CKANResource
{
    private String vocabulary_id;
    private String display_name;
    private String name;
    private String id;

    public String getVocabularyId() { return vocabulary_id; }
    public void setVolcabularyId(String v) { vocabulary_id = v; }

    public String getDisplayName() { return display_name; }
    public void setDisplayName(String d) { display_name = d; }

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    public String getId() { return id; }
    public void setId(String v) { id = v; }

    public Tag() {}

    public String toString() {
        return "<Tag:" + getName() + "/" + getDisplayName()  + ">";
    }
}