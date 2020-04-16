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
public class Hero extends Char {

    public DFAgentDescription dfd = new DFAgentDescription();
    public DFAgentDescription enemySearch = new DFAgentDescription();
    
    @Override
    protected void setup() {
        super.setup();

        dfd.setName(getAID());
        ServiceDescription teamService = new ServiceDescription();
        teamService.setType("hero");
        teamService.setName("Team");
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

        ServiceDescription esd = new ServiceDescription();
        esd.setType("badguys");
        enemySearch.addServices(esd);

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
                        Logger.getLogger(Hero.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
}
