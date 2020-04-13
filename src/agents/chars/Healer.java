/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author rafae
 */
public class Healer extends Char {
    
    private final int HEAL_AMOUT = 150;
    
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
        ServiceDescription sd = new ServiceDescription();
        sd.setType("heal");
        sd.setName("Healing");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }

        addBehaviour(new TickerBehaviour(this, 5000) {

            @Override
            protected void onTick() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    ACLMessage reply = msg.createReply();
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        System.out.println(msg.getSender().getName() + " needs healing");
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(Integer.toString(HEAL_AMOUT));
                        myAgent.send(reply);
                    } else {
                        block();
                    }

                }
            }
        });
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("Healer " + getAID().getName() + " leaving");
    }

}
