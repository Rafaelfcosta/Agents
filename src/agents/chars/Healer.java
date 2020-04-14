/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.behaviours.CyclicBehaviour;
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
public class Healer extends Char {

    private final int HEAL_AMOUT = 150;
    private final int healCooldown = 4000;
    private Long lastHeal = System.currentTimeMillis();

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            setMAX_HP(Integer.parseInt((String) args[0]));
            setCURRENT_HP(Integer.parseInt((String) args[0]));
        }

        System.out.println("Healer " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription teamService = new ServiceDescription();
        teamService.setType("hero");
        teamService.setName("Team");

        ServiceDescription healingService = new ServiceDescription();
        healingService.setType("heal");
        healingService.setName("Healing");

        dfd.addServices(healingService);
        dfd.addServices(teamService);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
        
        DFAgentDescription healsearch = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("heal");
        healsearch.addServices(sd);

        addBehaviour(new CyclicBehaviour(this) {
            
            

            @Override
            public void action() {
                
                if (getCURRENT_HP() <= getMAX_HP() / 2) {

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, healsearch);
                        if (result.length != 0) {
                            
                            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                            msg.addReceiver(result[0].getName());
                            msg.setContent("needheal");
                            myAgent.send(msg);
                        }

                    } catch (FIPAException ex) {
                        Logger.getLogger(Tank.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if (getCURRENT_HP() <= 0) {
                    doDelete();
                }

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
                    } else if (msg.getContent().startsWith("healing")) {
                        System.out.println("healing " + getParams(msg.getContent())[1] + " points");
                        heal(Integer.parseInt(getParams(msg.getContent())[1]));
                        System.out.println(myAgent.getName() + " " + getCURRENT_HP() + " HP");
                    } else if (msg.getContent().startsWith("damage")) {
                        System.out.println(getAID().getLocalName() + " taking damage from " + msg.getSender().getLocalName() + getParams(msg.getContent())[1] + " points");
                        damage(Integer.parseInt(getParams(msg.getContent())[1]));
                        System.out.println(myAgent.getName() + " " + getCURRENT_HP() + " HP");
                    } else {
                        block();
                    }
                }
            }
        }
        );
    }

    @Override

    protected void takeDown() {
        super.takeDown();
        System.out.println("Healer " + getAID().getName() + " leaving");
    }

}
