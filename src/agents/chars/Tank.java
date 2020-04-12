/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;

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
            System.out.println(args[0]);
        }

        System.out.println("Tank " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");

        addBehaviour(new TickerBehaviour(this, 2000) {

            @Override
            protected void onTick() {
                setCURRENT_HP(getCURRENT_HP() - 100);
                System.out.println("losing hp " + getCURRENT_HP());
                if(getCURRENT_HP() <= 0){
                    doDelete();
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
