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
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafae
 */
public class Tank extends Hero {
    private final int DAMAGE = 25;
    private final int atkCooldown = 5000;
    private final int armorCooldown = 25000;
    private int ARMOR_HP = 200;
    private int CURRENT_ARMOR_HP;
    @Override
    protected void setup() {
        super.setup();
        System.out.println("Tank " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");

        ServiceDescription sd = new ServiceDescription();
        sd.setType("badguys");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
        
          addBehaviour(new TickerBehaviour(this, armorCooldown) {
            @Override
            protected void onTick() {
                 this.CURRENT_ARMOR_HP = ARMOR_HP;
            }
          
          }
        addBehaviour(new TickerBehaviour(this, atkCooldown) {
            @Override
            protected void onTick() {
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, dfd);
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
