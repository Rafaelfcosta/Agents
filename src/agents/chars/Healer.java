/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rafae
 */
public class Healer extends Hero {

    private final int HEAL_AMOUT = 150;
    private final int healCooldown = 4000;
    private Long lastHeal = System.currentTimeMillis();

    @Override
    protected void setup() {
        super.setup();
        
        try {
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(Healer.class.getName()).log(Level.SEVERE, null, ex);
        }

        ServiceDescription healingService = new ServiceDescription();
        healingService.setType("heal");
        healingService.setName("Healing");
        dfd.addServices(healingService);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("Healer " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {

                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    ACLMessage reply = msg.createReply();
                    if (msg.getContent().startsWith("needheal")) {
                        if (System.currentTimeMillis() - lastHeal >= healCooldown) {
                            System.out.println(msg.getSender().getName() + " needs healing");
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("healing," + Integer.toString(HEAL_AMOUT));
                            myAgent.send(reply);
                            lastHeal = System.currentTimeMillis();
                        }
                    } else {
                        block();
                    }
                }
            }
        }
        );
    }
}
