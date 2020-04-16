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
public class Tank extends Hero {

    private final int DAMAGE = 25;
    private final int atkCooldown = 5000;
    private final int armorCooldown = 50000;
    private final int MAX_ARMOR = 200;
    private final int HP = 500;
    
    @Override
    protected void setup() {
        super.setup();
        configureHP();
       
        System.out.println("Tank " + getAID().getName() + " is ready with "
                + getCURRENT_HP() + " HP and " + getCURRENT_ARMOR_HP() + " Armor");

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
                    Logger.getLogger(Tank.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        addBehaviour(new TickerBehaviour(this, armorCooldown) {
            @Override
            protected void onTick() {
                setCURRENT_ARMOR_HP(ARMOR_HP);
            }
        });
    }

    private void configureHP() {
        setMAX_HP(HP);
        setCURRENT_HP(HP);
        setARMOR_HP(MAX_ARMOR);
        setCURRENT_ARMOR_HP(ARMOR_HP);
    }
}
