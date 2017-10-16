/*
 * Created on 31-mrt-2005
 * Copyright 2013 Henk van den Berg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * see license.txt
 *
 */
package nl.fountain.xelem.excel;

/**
 * Represents the Comment element.
 */
public interface Comment extends XLElement {

    void setAuthor(String author);

    String getAuthor();

    void setShowAlways(boolean show);

    boolean showsAlways();

    void setData(String data);

    String getData();

    /**
     * Gets the content of the data element stripped of the author (if
     * there was any).
     * 
     * @return the content of the data element stripped of the author
     */
    String getDataClean();

}
