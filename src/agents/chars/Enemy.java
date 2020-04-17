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
public class Enemy extends Char {

    private final int DAMAGE = 100;
    private final int atkCooldown = 5000;
    private final int HP = 800;
    private final int CRITICAL_DAMAGE = (int) (DAMAGE * 1.5);

    @Override
    protected void setup() {
        super.setup();
        configureHP();

        System.out.println("Enemy " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription teamService = new ServiceDescription();
        teamService.setType("badguys");
        teamService.setName("Team");
        dfd.addServices(teamService);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException ex) {
            Logger.getLogger(Enemy.class.getName()).log(Level.SEVERE, null, ex);
        }

        DFAgentDescription teamSearch = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("hero");
        teamSearch.addServices(sd);

        addBehaviour(new TickerBehaviour(this, atkCooldown) {
            @Override
            protected void onTick() {
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, teamSearch);
                    if (result.length > 0) {
                        for (DFAgentDescription a : result) {
                            AID name = a.getName();
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(name);
                            int ATK = DAMAGE;
                            if (Math.random() <= 0.3) {
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
    }

    private void configureHP() {
        setMAX_HP(HP);
        setCURRENT_HP(HP);
    }
}
