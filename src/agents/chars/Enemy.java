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
public class Enemy extends Char {

    @Override
    protected void setup() {
        System.out.println("Enemy " + getAID().getName() + " is ready");
    }
}
