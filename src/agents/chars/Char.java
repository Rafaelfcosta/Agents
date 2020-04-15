/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.Agent;
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
public abstract class Char extends Agent {

    private int MAX_HP;
    private int CURRENT_HP;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            setMAX_HP(Integer.parseInt((String) args[0]));
            setCURRENT_HP(Integer.parseInt((String) args[0]));
        }

        addBehaviour(new CyclicBehaviour(this) {

            @Override
            public void action() {
                if (getCURRENT_HP() <= 0) {
                    doDelete();
                }
                
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    if (msg.getContent().startsWith("healing")) {
                        System.out.println(myAgent.getLocalName() + " healing " + getParams(msg.getContent())[1] + " points");
                        heal(Integer.parseInt(getParams(msg.getContent())[1]));
                        System.out.println(myAgent.getName() + " " + getCURRENT_HP() + " HP");
                    } else if (msg.getContent().startsWith("damage")) {
                        System.out.println(getAID().getLocalName() +" taking damage from " + msg.getSender().getLocalName() + " by " + getParams(msg.getContent())[1] + " points");
                        damage(Integer.parseInt(getParams(msg.getContent())[1]));
                        System.out.println(myAgent.getName() + " " + getCURRENT_HP() + " HP");
                    } else {
                        block();
                    }
                }
            }
        });
    }

    public int getMAX_HP() {
        return MAX_HP;
    }

    public void setMAX_HP(int MAX_HP) {
        this.MAX_HP = MAX_HP;
    }

    public int getCURRENT_HP() {
        return CURRENT_HP;
    }

    public void setCURRENT_HP(int CURRENT_HP) {
        this.CURRENT_HP = CURRENT_HP;
    }

    public void heal(int healSize) {
        int newHp = getCURRENT_HP() + healSize;
        if (newHp > getMAX_HP()) {
            setCURRENT_HP(getMAX_HP());
        } else {
            setCURRENT_HP(newHp);
        }
    }
    
    public void damage(int damageSize) {
        int newHp = getCURRENT_HP() - damageSize;
        if (newHp < 0) {
            setCURRENT_HP(0);
        } else {
            setCURRENT_HP(newHp);
        }
    }
    
    @Override
    public void doDelete() {
        super.doDelete();
        System.out.println(getAID().getName() + " died");
        
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        try {
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(Char.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
        
    public String[] getParams(String msg){
        return msg.split(","); 
    }
    
    
}