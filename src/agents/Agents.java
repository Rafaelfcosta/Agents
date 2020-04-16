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
        String[] params = new String[2];
        params[0] = "-gui";
        params[1] = "Mercy:agents.chars.Healer(100); Rein:agents.chars.Tank(500); Hilha:agents.chars.Enemy(5000); Genji:agents.chars.Dps(100)";
        Boot.main(params);
    }
}
