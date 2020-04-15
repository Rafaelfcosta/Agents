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
public class Dps extends Hero {

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Dps " + getAID().getName() + " is ready with " + getCURRENT_HP() + " HP");
    }
}
