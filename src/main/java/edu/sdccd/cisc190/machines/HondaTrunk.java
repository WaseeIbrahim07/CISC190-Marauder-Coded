package edu.sdccd.cisc190.machines;

public class HondaTrunk extends Slot {
    public HondaTrunk() {
        super(new String[]{"🚗", "🛻", "🚕"}, 1000, 1, 1.5);
    }

    //honda trunk will have one match wins a tenth of full match
    @Override
    public int evaluateWinCondition(String[] arr) {
        if (arr[0].equals(arr[1]) && arr[1].equals(arr[2])) {
            return 3; // Full match
        } else if (arr[0].equals(arr[1]) || arr[1].equals(arr[2]) || arr[0].equals(arr[2])) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public int calculatePayout(int moneyAmount, String[] spunRow, int bet) {
        int winningCondition = evaluateWinCondition(spunRow);
        return switch (winningCondition) {
            case 0 -> // No match
                    moneyAmount - bet;
            case 2 ->
                    (int) (moneyAmount + Math.floor(bet * returnAmt * 0.1));
            case 3 -> // Three-symbol match
                    (int) (moneyAmount + Math.floor(bet * returnAmt));
            default -> moneyAmount;
        };
    }

}
