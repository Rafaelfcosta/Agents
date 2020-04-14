/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.Agent;
import jade.domain.DFService;
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
        try {
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(Tank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String[] getParams(String msg){
        return msg.split(","); 
    }
}
