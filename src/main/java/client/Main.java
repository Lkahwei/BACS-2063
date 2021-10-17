/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.FileNotFoundException;



public class Main {
    
    public static void main(String[] args) throws FileNotFoundException {
        StaffModule staffModule = new StaffModule();
        System.out.println("        +-------------------------------------------+");
        System.out.println("        |             Welcome to TAR UC             |");
        System.out.println("        |                KEDAI RUNCIT               |");
        System.out.println("        |                 POS SYSTEM                |");
        System.out.println("        +-------------------------------------------+");
        staffModule.loginMain();
    }

}
