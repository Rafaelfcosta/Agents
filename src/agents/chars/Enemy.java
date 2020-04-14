/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.AID;
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
public class Enemy extends Char {

    private final int DAMAGE = 50;
    private final int atkCooldown = 5000;

    @Override
    protected void setup() {

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            setMAX_HP(Integer.parseInt((String) args[0]));
            setCURRENT_HP(Integer.parseInt((String) args[0]));
        }

        System.out.println("Enemy " + getAID().getName() + " is ready");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription teamService = new ServiceDescription();
        teamService.setType("badguys");
        teamService.setName("Team");
        dfd.addServices(teamService);

        DFAgentDescription teamSearch = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("hero");
        teamSearch.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            Logger.getLogger(Enemy.class.getName()).log(Level.SEVERE, null, ex);
        }

        addBehaviour(new TickerBehaviour(this, atkCooldown) {

            @Override
            protected void onTick() {

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, teamSearch);
                    if (result.length > 0) {
                        for (DFAgentDescription a : result) {
                            AID name = a.getName();
                            System.out.println(name.getLocalName());

                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(name);
                            msg.setContent("damage," + Integer.toString(DAMAGE));
                            myAgent.send(msg);
                        }
                    }

                } catch (FIPAException ex) {
                    Logger.getLogger(Dps.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (getCURRENT_HP() <= 0) {
                    doDelete();
                }
            }
        }
        );

        addBehaviour(
                new CyclicBehaviour(this) {

                    @Override
                    public void action() {
                        ACLMessage msg = myAgent.receive();
                        if (msg != null) {
                            if (msg.getContent().startsWith("healing")) {
                                System.out.println("healing " + getParams(msg.getContent())[1] + " points");
                                heal(Integer.parseInt(getParams(msg.getContent())[1]));
                                System.out.println(myAgent.getName() + " " + getCURRENT_HP() + " HP");
                            } else if (msg.getContent().startsWith("damage")) {
                                System.out.println(getAID().getLocalName() +" taking damage from " + msg.getSender().getLocalName() + getParams(msg.getContent())[1] + " points");
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
}
