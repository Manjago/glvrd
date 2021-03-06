/*
 * EndsWith.java
 *
 * Created on September 27, 2006, 10:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.xpath.function;

import javax.xml.xpath.XPathFunctionException;
import java.util.List;

/**
 *
 * @author richardallenbair
 */
public class UpperCase extends AbstractFunction {
    
    /** Creates a new instance of EndsWith */
    public UpperCase() {
        super("upper-case", 1);
    }

    public Object evaluate(List args) throws XPathFunctionException {
        return getStringParam(args.get(0)).toUpperCase();
    }
}
