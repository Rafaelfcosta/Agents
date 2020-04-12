/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents.chars;

/**
 *
 * @author rafae
 */
public class Healer extends Char{

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            setMAX_HP(Integer.parseInt((String)args[0]));
            setCURRENT_HP(Integer.parseInt((String)args[0]));
            System.out.println(args[0]);
        }
        
        System.out.println("Healer " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("Healer " + getAID().getName() + " leaving");
    }
    
}
