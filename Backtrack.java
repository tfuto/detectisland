/*
 * Teljesen mezei megoldás, a Java speciális adottságait nem használva
 * 
 * Futási eredmény pl:
 * 
 *   7 7 4 5 7 
 *   8 1 7 6 1 
 *   8 8 8 8 3 
 *   5 7 8 1 5 
 *   4 9 7 4 9 
 *   Sziget: 4, cella erteke: 8, elemek szama: 6
 *      sor:2, oszlop:1
 *      sor:3, oszlop:1
 *      sor:3, oszlop:2
 *      sor:3, oszlop:3
 *      sor:4, oszlop:3
 *      sor:3, oszlop:4
 *   
 */

public class Backtrack {
	static int n = 5;
	static int szigetCnt = 0;
	static int[][] tomb = new int[n][n];
	static boolean[][] latogatott = new boolean[n][n];
	static int[] szigetElemSzam = new int[n*n];
	static int[] szigetCellaErtek = new int[n*n];
	static int[] talaltX = new int[n*n];
	static int[] talaltY = new int[n*n];
	static int[] talaltSzigetSzam = new int[n*n];
	static int logPozicio = 0;

	static void log(int i, int j, int szigetSzama, int cellaErtek) {
        talaltX[logPozicio] = i;		
        talaltY[logPozicio] = j;
        talaltSzigetSzam[logPozicio] = szigetSzama;
        logPozicio++;
		szigetElemSzam[szigetCnt]++;
	}

	static void szigetBejar(int i, int j, int cellaErtek) {
		if (i<0 || i>=n || j<0 || j>=n) return;
		if (latogatott[i][j] || tomb[i][j] != cellaErtek) return; 
		latogatott[i][j] = true;		
		log(i, j, szigetCnt, cellaErtek);
		szigetBejar(i-1,   j, cellaErtek);
		szigetBejar(i+1,   j, cellaErtek);		
		szigetBejar(  i, j-1, cellaErtek);		
		szigetBejar(  i, j+1, cellaErtek);
	}

	public static void main(String[] args) {
		int maxnum = 9;
		
		// Generalas
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				tomb[i][j] = (int) (Math.random()*maxnum) + 1;
			}
		}
		
		// Kiiras 
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				System.out.print(tomb[i][j] + " ");
			}
			System.out.println();
		}
		
		// Latogatatasok
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				if (!latogatott[i][j]) {
			        szigetCellaErtek[szigetCnt] = tomb[i][j];
					szigetBejar(i,j,tomb[i][j]);
					szigetCnt++;
				}
			}
		}
		
		// Eredmeny
		for (int i=0; i< szigetCnt; i++)
			if (szigetElemSzam[i] >= 3) {
				System.out.println("Sziget: "+i+", cella erteke: "+szigetCellaErtek[i]+", elemek szama: "+szigetElemSzam[i]);
				for (int j=0; j<n*n;j++) {
					if (talaltSzigetSzam[j] == i) {
						System.out.println("   sor:"+(talaltX[j]+1)+", oszlop:"+(talaltY[j]+1));						
					}
				}
			}

	}
	
}
