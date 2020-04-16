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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafae
 */
public class Dps extends Hero {
    private final int DAMAGE = 75;
    private final int atkCooldown = 3000;
    
    @Override
    protected void setup() {
        super.setup();
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
                            msg.setContent("damage," + Integer.toString(DAMAGE));
                            myAgent.send(msg);
                        }
                    }
                } catch (FIPAException ex) {
                    Logger.getLogger(Dps.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
