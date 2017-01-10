/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package informaticup;

import javax.swing.JOptionPane;

/**
 *
 * 
 */
public class Nachrichten {
    public static void WarnungAusgeben(String nachrichtenID)
    {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getResourceMap(Nachrichten.class);
        String nachricht = resourceMap.getString(nachrichtenID);
        
        JOptionPane.showMessageDialog(null, nachricht, "informatiCup: ATM", JOptionPane.WARNING_MESSAGE);
    }
}
