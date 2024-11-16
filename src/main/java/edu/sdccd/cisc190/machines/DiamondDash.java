package edu.sdccd.cisc190.machines;

import edu.sdccd.cisc190.machines.Slot;

public class DiamondDash extends Slot {
    @Override
    public void initializeSymbols() {
        symbols = new String[]{"💍", "💠", "💎"};
    }



    public DiamondDash() {
        returnAmt = 10;
    }
}