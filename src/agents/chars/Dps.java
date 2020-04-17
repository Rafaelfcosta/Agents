/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author rafae
 */
public class Dps extends Hero {
    private final int DAMAGE = 75;
    private final int atkCooldown = 3000;
    private final int HP = 200;
    private final int CRITICAL_DAMAGE = (int) (DAMAGE * 1.5);
    
    @Override
    protected void setup() {
        super.setup();
        configureHP();
        System.out.println("Dps " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");
     
        addBehaviour(new TickerBehaviour(this, atkCooldown) {
            @Override
            protected void onTick() {
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, enemySearch);
                    if (result.length > 0) {
                        for (DFAgentDescription a : result) {
                            AID name = a.getName();
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(name);
                            int ATK = DAMAGE;
                            if(Math.random() <= 0.3){
                               ATK = CRITICAL_DAMAGE;
                            }
                            msg.setContent("damage," + Integer.toString(ATK));
                            myAgent.send(msg);
                        }
                    }
                } catch (FIPAException ex) {
                    Logger.getLogger(Dps.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        addBehaviour(new TickerBehaviour(this, 2000) {
            
            @Override
            protected void onTick() {
                updateJson();
            }
        });
    }
    
    private void updateJson() {
        try {
            Reader reader = new FileReader("chars.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray arr = (JSONArray) obj;
            for (int i = 0; i < arr.size(); i++) {
                JSONObject chara = (JSONObject) arr.get(i);
                if (chara.get("name").equals(this.getLocalName())) {
                    chara.put("maxHp", getMAX_HP());
                    chara.put("hp", getCURRENT_HP());
                    chara.put("type", "dps");
                    
                    
                }
            }
                        
            FileWriter file = new FileWriter("chars.json");
            file.write(arr.toJSONString());
            file.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
        updateJson();
    }
    
    private void configureHP() {
        setMAX_HP(HP);
        setCURRENT_HP(HP);
    }
}
