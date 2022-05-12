package com.hp;

import java.util.Iterator;
import java.util.List;


/**
 * Testfile for elementary cycle search.
 *
 * @author Frank Meyer
 *
 */
public class TestCycles {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Integer nodes[] = new Integer[13];
        boolean adjMatrix[][] = new boolean[13][13];

        for (int i = 0; i < 13; i++) {
            nodes[i] = i;
        }

        adjMatrix[3][4] = true;
        //adjMatrix[4][3] = true;
        adjMatrix[4][5] = true;
        //adjMatrix[5][4] = true;

        adjMatrix[1][2] = true;//
        adjMatrix[2][1] = true;
        adjMatrix[1][6] = true;
        adjMatrix[6][1] = true;//

        adjMatrix[12][7] = true;//
        adjMatrix[7][12] = true;
        adjMatrix[11][12] = true;//
        adjMatrix[12][11] = true;

        adjMatrix[8][9] = true;
        //adjMatrix[9][8] = true;
        adjMatrix[9][10] = true;
        //adjMatrix[10][9] = true;


        adjMatrix[2][3] = true;
        //adjMatrix[3][2] = true;
        //adjMatrix[2][11] = true;
        adjMatrix[11][2] = true;
        adjMatrix[10][11] = true;
        //adjMatrix[11][10] = true;

        adjMatrix[5][6] = true;
        //adjMatrix[6][5] = true;
        adjMatrix[6][7] = true;
        //adjMatrix[7][6] = true;
        adjMatrix[7][8] = true;
        //adjMatrix[8][7] = true;


        //other graph examples:
		/*adjMatrix[0][1] = true;
		adjMatrix[1][2] = true;
		adjMatrix[2][0] = true;
		adjMatrix[2][4] = true;
		adjMatrix[1][3] = true;
		adjMatrix[3][6] = true;
		adjMatrix[6][5] = true;
		adjMatrix[5][3] = true;
		adjMatrix[6][7] = true;
		adjMatrix[7][8] = true;
		adjMatrix[7][9] = true;
		adjMatrix[9][6] = true;*/
        /*adjMatrix[0][1] = true;
        adjMatrix[1][2] = true;
        adjMatrix[2][0] = true; adjMatrix[2][6] = true;
        adjMatrix[3][4] = true;
        adjMatrix[4][5] = true; adjMatrix[4][6] = true;
        adjMatrix[5][3] = true;
        adjMatrix[6][7] = true;
        adjMatrix[7][8] = true;
        adjMatrix[8][6] = true;

        adjMatrix[6][1] = true;*/

        ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(adjMatrix, nodes);
        List cycles = ecs.getElementaryCycles();


        for (Iterator<List<Integer>> iter = cycles.listIterator(); iter.hasNext(); ) {
            List a = iter.next();
            if (a.size() <=2) {
                iter.remove();
            }
        }

        boolean index[] = new boolean[cycles.size()];
        for (int i = 0; i < cycles.size(); i++) {
            List cycle = (List) cycles.get(i);
            index[i] = true;
            for (int j = 0; j < cycle.size(); j++) {
                Integer node = (Integer) cycle.get(j);
                if (j < cycle.size() - 1) {
                    System.out.print(node + " -> ");
                } else {
                    System.out.print(node);
                }
            }
            System.out.print("\n");
        }

        Integer cycles_arr[][] = new Integer[cycles.size()][];
        for(int i = 0; i < cycles.size();i++){
            List cycle = (List) cycles.get(i);
            cycles_arr[i] = new Integer[cycle.size()];
            for (int j = 0; j < cycle.size(); j++) {
                cycles_arr[i][j] = (Integer) cycle.get(j);
                //System.out.println("cycles_arr[i][j] = " + cycles_arr[i][j]);
            }
            //System.out.println();
        }

        //call seg_solve with p = first element and r = last element in cycles recursively
        Integer p = 0;
        Integer r = cycles.size()-1;
        segSolve(cycles, p, r);

        //output the state of chessboard (REFER TO GRAPH for pos) and total number of moves
        System.out.println("***\nFINAL KNIGHTS' POSITIONS: ");
        //graph:
        //for (int i = 0; i < knights_pos.length; i++) System.out.println("pos[" + (i + 1) + "] =" + knights_pos[i]);
        //cb:
        System.out.println(knights_pos[2] + "  " + knights_pos[11] + "  " + knights_pos[4] + "\n" +
                knights_pos[5] + "  " + knights_pos[8] + "  " + knights_pos[1] + "\n" +
                knights_pos[10] + "  " + knights_pos[3] + "  " + knights_pos[6] + "\n" +
                knights_pos[7] + "  " + knights_pos[0] + "  " + knights_pos[9]);
        System.out.println("\nTOTAL NUMBER OF MOVES = " + moves);

    }

    static Integer moves = 0;
    static String knights_pos[] = {"white", "n", "black", "n", "black", "n", "n", "white", "n", "white", "n", "black"};
    static String target_pos[] = {"black", "n", "white", "n", "white", "n", "n", "black", "n", "black", "n", "white"};
    public static void segSolve(List cycles, Integer p, Integer r){
        System.out.println("r = " + r + "p = " + p);
        if(p < r){
            //segSolve(cycles, r, r);
            segSolve(cycles, p, r-1);
        }
        //if(p == r){
            //check if cycle is valid to solve
            // (valid if no of black knights and white knights is the same)
            List cycle = (List) cycles.get(r);
            Integer w_count = 0, b_count = 0;
            boolean valid = false;
            for(int i = 0; i < cycle.size(); i++){
                int node = (int) cycle.get(i);
                if(knights_pos[node - 1] == "white"){
                    w_count++;
                }
                if(knights_pos[node - 1] == "black"){
                    b_count++;
                }
            }

            if(w_count == b_count) valid = true;
            if(!valid) System.out.println("not valid, skipped");
            //solve the cycle
            // (move each knight one space forward if next space null until its target is reached)
            if(valid){
                System.out.println("cycle [" + p + "] is to be solved.");
                while(!target_reached(cycle, target_pos)){
                    for(int i = 0; i < cycle.size(); i++){
                        int node = (int) cycle.get(i);
                        int k = node - 1;
                        int next_k;
                        int it;
                        if(i == cycle.size() - 1) {
                            it = 0;
                            next_k = (int) cycle.get(0) - 1;
                            //System.out.println("at the end of cycle........");
                        }
                        else {
                            it = i + 1;
                            next_k = (int) cycle.get(i + 1) - 1;
                        }
                        if((knights_pos[k].equals("white") || knights_pos[k].equals("black"))
                                && knights_pos[next_k].equals("n")){
                            System.out.println("Moving knights[" + k + "] = " + knights_pos[k] +
                                     " to next_k = " + next_k);
                            knights_pos[next_k] = knights_pos[k];
                            knights_pos[k] = "n";
                            moves++;
                            i++; //after moving the knight forward once, we jump over it to the spot after it
                            //System.out.println("moves +1");
                            System.out.print("knights_pos array is now: ");
                            for (int t = 0; t < knights_pos.length; t++) System.out.print(knights_pos[t] +" ");
                            System.out.println('\n');
                        }
                    }
                }
            }

        //}
    }

    public static boolean target_reached(List cycle, String[] target_pos){
        for(int i = 0; i < cycle.size(); i++){
            if(knights_pos[(int) cycle.get(i) - 1] != target_pos[(int) cycle.get(i) - 1]){
                return false;
            }
        }
        //System.out.println("target is reached!! knights_pos[]: ");
        //for(int i = 0; i < knights_pos.length; i++) System.out.print(knights_pos[i] + " ");
        //System.out.println();
        return true;
    }


/* cycles ref

Node 1 -> Node 2 -> Node 3 -> Node 4 -> Node 5 -> Node 6
Node 1 -> Node 6 -> Node 7 -> Node 8 -> Node 9 -> Node 10 -> Node 11 -> Node 2
Node 1 -> Node 6 -> Node 7 -> Node 12 -> Node 11 -> Node 2
Node 2 -> Node 3 -> Node 4 -> Node 5 -> Node 6 -> Node 7 -> Node 8 -> Node 9 -> Node 10 -> Node 11
Node 2 -> Node 3 -> Node 4 -> Node 5 -> Node 6 -> Node 7 -> Node 12 -> Node 11
Node 7 -> Node 8 -> Node 9 -> Node 10 -> Node 11 -> Node 12
*/

}
