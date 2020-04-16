/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import jade.Boot;
import jade.core.Agent;

/**
 *
 * @author rafae
 */
public class Agents extends Agent {

    /**
     * ;
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//        String dps = "";
//        for (int i = 0; i < 10; i++) {
//            dps += "DPS" + i + ":agents.chars.Dps;";
//        };

        String[] params = new String[2];
        params[0] = "-gui";
        params[1]
                = "Mercy:agents.chars.Healer; "
                + "Rein:agents.chars.Tank; "
                + "Genji:agents.chars.Dps;"
                + "EnderDragon:agents.chars.Enemy;";
        Boot.main(params);
    }
}
