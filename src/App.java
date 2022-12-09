import java.util.Random;

public class App {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefense;
    public static int[] heroesHealth = { 270, 260, 250 };
    public static int[] heroesDamage = { 20, 15, 10 };
    public static String[] heroesAttackType = { "Physical", "Magical", "Kinetic" };
    public static int roundNumber;
    public static String criticalMessage;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefense();
        bossHits();
        heroesHit();
        printStatistics();
    }

    public static void chooseBossDefense() {
        Random random = new Random();
        int randIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefense = heroesAttackType[randIndex];
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i] == bossDefense) {
                    Random random = new Random();
                    int coif = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coif;
                    criticalMessage = "Critical damage: " + damage;
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defense: " +
                (bossDefense == null ? "No defense" : bossDefense));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
        if (criticalMessage != null) {
            System.out.println(">>> " + criticalMessage);
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }
}
