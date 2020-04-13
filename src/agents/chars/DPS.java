/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.behaviours.CyclicBehaviour;
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
public class Tank extends Char {

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            setMAX_HP(Integer.parseInt((String) args[0]));
            setCURRENT_HP(Integer.parseInt((String) args[0]));
        }

        System.out.println("Tank " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");

        addBehaviour(new TickerBehaviour(this, 3000) {

            @Override
            protected void onTick() {
                setCURRENT_HP(getCURRENT_HP() - 100);
                System.out.println(myAgent.getName() + " losing hp " + getCURRENT_HP());

                if (getCURRENT_HP() <= getMAX_HP() / 2) {
                    DFAgentDescription healsearch = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("heal");
                    healsearch.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, healsearch);
                        if (result.length != 0) {
                            System.out.println(result[0].getName().getLocalName() + " is a healer");
                            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                            msg.addReceiver(result[0].getName());
                            msg.setContent("heal");
                            myAgent.send(msg);
                        }

                    } catch (FIPAException ex) {
                        Logger.getLogger(Tank.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (getCURRENT_HP() <= 0) {
                    doDelete();
                }
            }
        });

        addBehaviour(new CyclicBehaviour(this) {

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    String content = msg.getContent();
                    if (msg.getPerformative() == ACLMessage.INFORM) {
                        System.out.println("healing " + content + " points");
                        setCURRENT_HP(getCURRENT_HP() + Integer.parseInt(content));
                        System.out.println(myAgent.getName() + " " + getCURRENT_HP() + " HP");
                    } else {
                        block();
                    }

                }
            }
        });
    }

    @Override
    public void doDelete() {
        super.doDelete();
        System.out.println("Tank " + getAID().getName() + " died");
    }
}
