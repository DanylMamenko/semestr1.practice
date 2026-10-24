package practice05;
public class Task4DotaMatchStats {
    static void main(String[] args) {
        String[] heroes = {"Pudge", "Invoker", "Juggernaut", "Crystal Maiden", "Earthshaker"};
        int[] kills   = {8,  12, 10, 2,  4};
        int[] deaths  = {6,  3,  2,  9,  5};
        int[] assists = {14, 9,  8,  18, 16};
        int[] netWorth = {14500, 21200, 24800, 9800, 12300};
        String[] title = {"Герой", "K", "D", "A", "Net Worth", "KDA Ratio", "Kill Participation"};
        double[] kdaRatios = new double[5];
        double[] killPartipication = new double[5];

        int totalTeamKills = 0;
        for (int i = 0; i < kills.length; i++){                     //TOTALKILLS
            totalTeamKills += kills[i];
        };

        for (int i = 0; i < kdaRatios.length; i++ ){                //KDA
            double totalKA = kills[i] + assists[i];
            kdaRatios[i] = totalKA / Math.max(1, deaths[i]);
        };

        for (int i = 0; i < killPartipication.length; i++){         //KPA
            double totalKA = kills[i] + assists[i];
            killPartipication[i] = (totalKA / totalTeamKills) * 100;
        };
        int totalNetWorth = 0;
        for (int i = 0; i < netWorth.length; i++){
            totalNetWorth += netWorth[i];
        };
        double MVP = kdaRatios[0];                                   //MVP
        int mvpIndex = 0;
        for (int i = 0; i < kdaRatios.length; i++){
            if (MVP < kdaRatios[i]){
                MVP = kdaRatios[i];
                mvpIndex = i;
            };
        };

        System.out.println("""
                ========================================================================
                                       🏆 DOTA 2: POST-MATCH REPORT
                ========================================================================""");
        System.out.printf("%-20s | %s / %s / %s  | %s | %s | %s",title[0],title[1],title[2],title[3],title[4],title[5],title[6]);
        System.out.println("\n------------------------------------------------------------------------");
        for (int i = 0; i < heroes.length; i++) {
            System.out.printf("%-20s | %d / %d / %d | %-8d  | %-9.2f | %.2f %% %n", heroes[i], kills[i], deaths[i], assists[i], netWorth[i], kdaRatios[i],killPartipication[i]);
        };
        System.out.println("Загальна кількість вбивств команди: " + totalTeamKills);
        System.out.printf("Сумарний Net Worth команди: \t\t%d золота%n", totalNetWorth);
        System.out.printf("⭐ НАЙКРАЩИЙ ГРАВЕЦЬ МАТЧУ (MVP): \t%s (KDA: %.2f)%n", heroes[mvpIndex], MVP);
    }
}
