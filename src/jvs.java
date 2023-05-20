import main.gme;

public class jvs {
    private static gme advent;
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("there are not enough arguments suplied to the program\n  args: [advent.dta, save||\"*new\"]");
            System.exit(0);
        }

        advent = new gme();
        advent.load(args[0], args[1]);//"src/resources/adventure/adventest.dta","");
        advent.iterate();
    }
}