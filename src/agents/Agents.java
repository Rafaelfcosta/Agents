/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import jade.Boot;
import jade.core.Agent;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        String[] per = {"Mercy","Rein", "Genji", "EnderDragon"};
        JSONArray characters = new JSONArray();
        
        for (String p : per) {
            JSONObject jsonChar = new JSONObject();
            jsonChar.put("name", p);
            jsonChar.put("type", "undefined");
            jsonChar.put("maxHp", 0);
            jsonChar.put("hp", 0);
            characters.add(jsonChar);
        }
        
        try (FileWriter file = new FileWriter("chars.json")) {
 
            file.write(characters.toJSONString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String[] params = new String[2];
        params[0] = "-gui";
        params[1]
                = "Mercy:agents.chars.Healer; "
                + "Rein:agents.chars.Tank; "
                + "Genji:agents.chars.Dps;"
                + "EnderDragon:agents.chars.Enemy;"
                + "Updater:agents.chars.AgentUpdater;";
        Boot.main(params);
        
        
    }
}
