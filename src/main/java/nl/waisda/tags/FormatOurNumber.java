/*  This file is part of Waisda 

    Copyright (c) 2012 Netherlands Institute for Sound and Vision
    https://github.com/beeldengeluid/waisda
	
    Waisda is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Waisda is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Waisda.  If not, see <http://www.gnu.org/licenses/>.
*/

package nl.waisda.tags;
 
import java.io.IOException;
import java.text.NumberFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import nl.waisda.model.Util;



 
@SuppressWarnings("serial")
public class FormatOurNumber extends TagSupport {
    
	private String number;
    
    private static final NumberFormat FORMAT = NumberFormat
			.getIntegerInstance(Util.DUTCH_LOCALE);
 
    @Override
    public int doStartTag() throws JspException {
 
        try {
            //Get the writer object for output.
            JspWriter out = pageContext.getOut();
 
            //Perform substr operation on string.
            out.println(FORMAT.format(Integer.parseInt(number)));
 
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}