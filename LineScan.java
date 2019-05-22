/*
 * Teljesen mezei megoldás, a Java speciális adottságait nem használva
 * 
 */

public class LineScan {
    static int n = 5;
    static int szigetCnt = 0;
    static int[][] tomb = new int[n][n]; 
    static int[] szigetElemSzam = new int[n*n];
    static int[] szigetCellaErtek = new int[n*n];
    static int[] talaltSor = new int[n*n];
    static int[] talaltOszlop = new int[n*n];
    static int[] talaltSzigetSzam = new int[n*n];
    
    static int[] elozoSorSzigetSzam = new int[n];
    static int[] jelenSorSzigetSzam = new int[n];
    static int logPozicio = 0;

    static void log(int i, int j, int szigetSzama) {
        talaltSor[logPozicio] = i;        
        talaltOszlop[logPozicio] = j;
        talaltSzigetSzam[logPozicio] = szigetSzama;
        logPozicio++;
        szigetElemSzam[szigetSzama]++;        
        szigetCellaErtek[szigetSzama] = tomb[i][j]; // Itt kenyelmes        
    }

    static void statuszKiir() {
        for (int j=0; j<n; j++) 
            System.out.print(String.format("%3d ", elozoSorSzigetSzam[j]));
            
        System.out.println();
        
        for (int j=0; j<n; j++) 
            System.out.print(String.format("%3d ", jelenSorSzigetSzam[j]));

        System.out.println();
        System.out.println();
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

        // Elokeszites
        for (int j=0; j<n; j++) {
            elozoSorSzigetSzam[j] = -1;
            jelenSorSzigetSzam[j] = -1;
        }

        // Fo feldolgozo ciklus
        for (int i=0; i<n; i++) {

            // statuszKiir();
        
            // Sor update az elozo sor alapjan
            for (int j=0; j<n; j++) {
                elozoSorSzigetSzam[j] = jelenSorSzigetSzam[j];
                jelenSorSzigetSzam[j] = -1;

                if (i>0)
                    if (tomb[i][j] == tomb[i-1][j]) {
                        jelenSorSzigetSzam[j] = elozoSorSzigetSzam[j];
                        log(i,j,jelenSorSzigetSzam[j]);
                    }
            }

            //statuszKiir();
            
            // Sor scan, elso elem 
            if (jelenSorSzigetSzam[0] == -1) {
                jelenSorSzigetSzam[0]= szigetCnt;
                log(i,0,szigetCnt);
                szigetCnt++;
            }

            // Sor scan, tobbi elem
            for (int j=1; j<n; j++)
                if (tomb[i][j] == tomb[i][j-1]) {
                    if (jelenSorSzigetSzam[j] == -1) {
                        jelenSorSzigetSzam[j]=jelenSorSzigetSzam[j-1];
                        log(i,j,jelenSorSzigetSzam[j]);
                    } else {
                        if (jelenSorSzigetSzam[j]!=jelenSorSzigetSzam[j-1]) {
                            
                            //System.out.println("Utkozes! @("+i+","+j+") " + jelenSorSzigetSzam[j-1] +" " + jelenSorSzigetSzam[j]);
                            
                            // Ekvivalencia osztályok kezelése
                            // jelenSorSzigetSzam[j-1] es jelenSorSzigetSzam[j] különböző, a cellaertekek azonosak
                            // A logban az egyik szigetSzam fajtat at kell cimkezni
                            for (int k=logPozicio-1; k>=0; k--) {
                                if (talaltSzigetSzam[k]==jelenSorSzigetSzam[j-1])
                                    talaltSzigetSzam[k]=jelenSorSzigetSzam[j];
                            }
                            
                            // Es kell egy kis konyveles is :-)
                            szigetElemSzam[jelenSorSzigetSzam[j]]   += szigetElemSzam[jelenSorSzigetSzam[j-1]];
                            szigetElemSzam[jelenSorSzigetSzam[j-1]]  = 0;
                        }
                        
                    } 
                } else {
                    if (jelenSorSzigetSzam[j] == -1) { 
                        jelenSorSzigetSzam[j]= szigetCnt;
                        log(i,j,szigetCnt);
                        szigetCnt++;                        
                    }
                }
                    
        }

        // statuszKiir();
        
        // Eredmeny (es ellenorzes)
        for (int i=0; i< szigetCnt; i++) {
            int szamlalo = 0;
            if (szigetElemSzam[i] >= 3) {
                System.out.println("Sziget: "+i+", cella erteke: "+szigetCellaErtek[i]+", elemek szama: "+szigetElemSzam[i]);
                for (int j=0; j<n*n;j++) {
                    if (talaltSzigetSzam[j] == i) {
                        System.out.println("   sor:"+(talaltSor[j]+1)+", oszlop:"+(talaltOszlop[j]+1));
                        szamlalo++;
                    }
                }
                if (szamlalo != szigetElemSzam[i])
                    throw new RuntimeException("szigetElemSzam hiba!");
            }
        }

        // Ellenorzes 1
        
        int sum = 0;
        
        for (int i=0; i< szigetCnt; i++)
            sum += szigetElemSzam[i];
        
        if (sum != n*n)
            throw new RuntimeException("sum(szigetElemSzam) != n*n !");
        
        // Ellenorzes 2 
        
        for (int i=0; i<n; i++) 
            for (int j=0; j<n; j++)
                szigetElemSzam[tomb[i][j]]--;

        sum = 0;
        
        for (int i=0; i< szigetCnt; i++)
            sum += szigetElemSzam[i];
            
        if (sum != 0)
            throw new RuntimeException("sum(minuszolt szigetElemSzam) != 0 !");
        
        // Ellenorzes 3
        if (logPozicio != n*n)
            throw new RuntimeException("logPozicio != n*n !");
        
        // Ellenorzes 4
        for (int i=0; i<n*n; i++) 
                if (tomb[talaltSor[i]][talaltOszlop[i]] == -1) 
                    throw new RuntimeException("Ervenytelen bejegyzes a log-ban!");
                else 
                    tomb[talaltSor[i]][talaltOszlop[i]] = -1;
                
    }
    
}
