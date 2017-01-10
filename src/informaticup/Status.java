/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package informaticup;

/**
 *
 * 
 */
public class Status {

    public static String statustext(String statusmeldung) {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getResourceMap(Status.class);
        return resourceMap.getString(statusmeldung);

    }
}
