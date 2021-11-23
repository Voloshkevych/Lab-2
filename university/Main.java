package university;

public class Main {

    public static void main(String[] args) {
        String filename = "in.txt";
        Automata automaton = new Automata();
        if (!automaton.read_from_file(filename)) {
            System.out.println("Problems with input");
        }
        automaton.print(true);
        System.out.println();
        Automata det = automaton.determinate();
        det.print(true);
//        System.out.println(automaton.reads("aab"));
//        System.out.println(det.reads("aab"));

//        try {
//            File tests = new File("words.txt");
//            Scanner in = new Scanner(tests);
//            while (in.hasNext()) {
//                String word = in.next();
//                System.out.print(word);
//                System.out.print(' ');
//                System.out.print(automaton.reads(word));
//                System.out.print("   ");
//                System.out.print(det.reads(word));
//                System.out.print("      ");
//                System.out.println(automaton.reads(word) == det.reads(word));
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
