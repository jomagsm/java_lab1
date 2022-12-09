import java.util.Arrays;
import java.util.Random;

public class App {
    public static int bossHealth = 1500;
    public static int bossDamage = 50;
    public static boolean bossIsStunned = false;
    public static String bossDefense;
    public static int[] heroesHealth = { 270, 260, 250, 250, 500, 200, 150, 200 };
    public static int[] heroesDamage = { 20, 15, 10, 0, 5, 10, 0, 0 };
    public static String[] heroesAttackType = { "Physical", "Magical", "Kinetic", "Medical", "Golem", "Lucky",
            "Berserk", "Thor" };
    public static int roundNumber;

    public static int medicalTreatment = 20;
    public static int golemIndex = Arrays.asList(heroesAttackType).indexOf("Golem");

    public static int berserkBlockedDamage = 0;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        System.out.println(" ------------ ROUND " + roundNumber + " ------------");
        chooseBossDefense();
        bossHits();
        heroesHit();
        printHeroesHealths();
    }

    public static void chooseBossDefense() {
        Random random = new Random();
        int randIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefense = heroesAttackType[randIndex];
        System.out.println('\u26E8' + " Boss chooses defense " + bossDefense + " " + '\u26E8');
    }

    public static void bossHits() {
        if (!bossIsStunned) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0) {
                    System.out.println(" => => => BOSS ATTACKS " + heroesAttackType[i] + " <= <= <=");
                    int newBossDamage = bossDamage;
                    if (heroesHealth[golemIndex] > 0) {
                        int golemDefense = (int) (bossDamage * 0.2);
                        newBossDamage = bossDamage - golemDefense;
                        heroesHealth[golemIndex] = heroesHealth[golemIndex] - golemDefense;
                        System.out.println('\u229D' + " The golem took the hit " + '\u229D');
                        if (heroesHealth[golemIndex] < 1 && i == golemIndex) {
                            heroesHealth[golemIndex] = 0;
                            continue;
                        }
                    }
                    if (heroesAttackType[i] == "Lucky") {
                        if (getRandomBoolean()) {
                            System.out.println("Lucky dodged");
                            continue;
                        }
                    }
                    if (heroesAttackType[i] == "Berserk") {
                        Random random = new Random();
                        berserkBlockedDamage = random.nextInt(newBossDamage - 10) + 1;
                        newBossDamage -= berserkBlockedDamage;
                    }
                    if (heroesHealth[i] - newBossDamage < 0) {
                        heroesHealth[i] = 0;
                        System.out.println("+++ " + heroesAttackType[i] + "DEAD +++");
                    } else {
                        heroesHealth[i] = heroesHealth[i] - newBossDamage;
                        System.out.println("*** " + heroesAttackType[i] + "  HEALTH " + heroesHealth[i] + " ***");
                    }
                }
            }
        } else {
            System.out.println("^^^ Stunned boss skips a turn ^^^");
        }
        bossIsStunned = false;
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                if (heroesAttackType[i] == "Medical") {
                    medicStep();
                    continue;
                }
                if (heroesAttackType[i] == "Thor") {
                    bossIsStunned = getRandomBoolean();
                    if (bossIsStunned) {
                        System.out.println('\u2728' + " Thor stunned Boos " + '\u2728');
                    }
                    continue;
                }
                if (heroesAttackType[i] == "Berserk") {
                    berserkHit();
                    continue;
                }
                int damage = heroesDamage[i];
                System.out
                        .println('\u21AF' + " HERO " + heroesAttackType[i] + " ATTACKS " + '\u21AF');
                if (heroesAttackType[i] == bossDefense) {
                    Random random = new Random();
                    int coif = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    damage = heroesDamage[i] * coif;
                    System.out.println(heroesAttackType[i] + " Commits a critical hit: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
                printBoosHealth();
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

    public static void medicStep() {
        boolean flag = false;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] < 100 && heroesAttackType[i] != "Medical") {
                flag = true;
            }
        }
        while (flag) {
            Random random = new Random();
            int randIndex = random.nextInt(heroesAttackType.length);
            if (heroesHealth[randIndex] > 0 && heroesHealth[randIndex] < 100
                    && heroesAttackType[randIndex] != "Medical") {
                int tempHealth = heroesHealth[randIndex];
                heroesHealth[randIndex] = heroesHealth[randIndex] + medicalTreatment;
                flag = false;
                System.out
                        .println("++++++ The medic cured " + heroesAttackType[randIndex] + " OLD HEALTH: " + tempHealth
                                + " NEW HEALTH " + heroesHealth[randIndex] + "++++++");
            }
        }
    }

    public static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static void printBoosHealth() {
        System.out.println("*** BOSS HEALTH " + bossHealth + " ***");
    }

    public static void printHeroesHealths() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                System.out
                        .println('\u2295' + " " + heroesAttackType[i] + " HEALTH " + heroesHealth[i] + " " + '\u2295');
            } else {
                System.out
                        .println('\u2296' + " " + heroesAttackType[i] + " DEAD " + '\u2296');
            }
        }
    }

    public static void berserkHit() {
        if (bossHealth > 0) {
            if (bossDefense == "Berserk") {
                Random random = new Random();
                int coif = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                berserkBlockedDamage *= coif;
                System.out.println("Berserk Commits a critical hit: " + berserkBlockedDamage);
            }
            System.out
                    .println('\u21AF' + " HERO Berserk" + " ATTACKS " + '\u21AF');
            bossHealth = bossHealth - berserkBlockedDamage;
            if (bossHealth < 0) {
                bossHealth = 0;
            }
            printBoosHealth();
        }
        berserkBlockedDamage = 0;
    }
}
