/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
public class AgentUpdater extends Agent {

    @Override
    protected void setup() {

        DFAgentDescription heroesSearch = new DFAgentDescription();
        DFAgentDescription enemiesSearch = new DFAgentDescription();
        ServiceDescription heroesSd = new ServiceDescription();
        ServiceDescription enemiesSd = new ServiceDescription();

        heroesSd.setType("hero");
        enemiesSd.setType("badguys");

        addBehaviour(new TickerBehaviour(this, 2000) {

            @Override
            protected void onTick() {
                try {
                    DFAgentDescription[] hResult = DFService.search(myAgent, heroesSearch);
                    DFAgentDescription[] eResult = DFService.search(myAgent, enemiesSearch);

                    if (hResult.length > 0) {
                        for (DFAgentDescription a : hResult) {
                            AID name = a.getName();
                            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                            msg.addReceiver(name);
                            msg.setContent("status");
                            myAgent.send(msg);
                        }
                    }

                    if (eResult.length > 0) {
                        for (DFAgentDescription a : eResult) {
                            AID name = a.getName();
                            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                            msg.addReceiver(name);
                            msg.setContent("status");
                            myAgent.send(msg);
                        }
                    }

                } catch (FIPAException ex) {
                    Logger.getLogger(AgentUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    if (msg.getContent().startsWith("description")) {
                        updateJson(getParams(msg.getContent()));
                    } else {
                        block();
                    }
                }
            }
        });

    }

    private void updateJson(String[] stats) {
        try {
            Reader reader = new FileReader("chars.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray arr = (JSONArray) obj;
            for (int i = 0; i < arr.size(); i++) {
                JSONObject chara = (JSONObject) arr.get(i);
                if (chara.get("name").equals(stats[1])) {
                    chara.put("name", stats[1]);
                    chara.put("type", stats[2]);
                    chara.put("maxHp", Integer.parseInt(stats[3]));
                    chara.put("hp", Integer.parseInt(stats[4]));
                }
            }

            FileWriter file = new FileWriter("chars.json");
            file.write(arr.toJSONString());
            file.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getParams(String msg) {
        return msg.split(",");
    }
}
