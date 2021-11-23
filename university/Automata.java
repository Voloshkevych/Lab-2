package university;

import java.io.*;
import java.util.*;


class Automata {

    private int n_a;
    //private Set<Character> alphabet = new TreeSet<>();
    private int n_w;
    private int start_state;
    private int n_f;
    private Set<Integer> final_states = new TreeSet<>();
    private Map<Integer, Map<Character, Set<Integer>>> transition_function = new TreeMap<>();
    private Map<Integer, String> labels = new TreeMap<>();

    boolean read_from_file(String fname) {

        try {
            File input = new File(fname);
            Scanner in = new Scanner(input);
            n_a = in.nextInt();
            n_w = in.nextInt();
            start_state = in.nextInt();
            n_f = in.nextInt();
            if (n_f < 1)
                return false;
            for (int i = 0; i < n_f; i++) {
                int state = in.nextInt();
                if (state < 0 || state > n_w - 1)
                    return false;
                final_states.add(state);
            }
            while (in.hasNext()) {
                int start = in.nextInt();
                if (start < 0 || start > n_w - 1)
                    return false;
                String Sym = in.next();
                if (Sym.length() > 1)
                    return false;
                char sym = Sym.charAt(0);
                if (sym < 'a' || sym > (char)('a' + n_a - 1))
                    return false;
                int end = in.nextInt();
                if (end < 0 || end > n_w - 1)
                    return false;
                transition_function.putIfAbsent(start, new TreeMap<>());
                transition_function.get(start).putIfAbsent(sym, new TreeSet<>());
                transition_function.get(start).get(sym).add(end);
            }
//            for (int i = 0; i < n_a; i++) {
//                alphabet.add((char)('a' + i));
//            }
            for (int i = 0; i < n_w; i++) {
                //states.add(i);
                labels.put(i, String.valueOf(i));
            }
            return true;
        }
        catch (FileNotFoundException | InputMismatchException e){
            e.printStackTrace();
            return false;
        }
    }

    private void print_state(int state, boolean label) {
        if (!label)
            System.out.print(state);
        else {
            System.out.print('[');
            System.out.print(labels.get(state));
            System.out.print(']');
        }
    }

    void print(boolean label) {
        System.out.print("Letters in alphabet: ");
        System.out.println(n_a);
//        for (char letter : alphabet) {
////            System.out.print(letter);
////            System.out.print(' ');
////        }
        System.out.print("Alphabet: ");
        for (int i = 0; i < n_a; i++) {
            System.out.print((char)('a' + i));
            System.out.print(' ');
        }
        System.out.println();

        System.out.print("Total number of states: ");
        System.out.println(n_w);
//        for (int state : states) {
//            System.out.print(state);
//            System.out.print(' ');
//        }
        System.out.print("States: ");
        for (int i = 0; i < n_w; i++) {
            print_state(i, label);
            System.out.print(' ');
        }
        System.out.println();

        System.out.print("Starting state: ");
        System.out.println(start_state);

        System.out.print("Number of final states : ");
        System.out.print(n_f);
        System.out.print("; final states: ");
        for (int state : final_states) {
            print_state(state, label);
            System.out.print(' ');
        }
        System.out.println();
        System.out.println("Transitions:");

        for (Map.Entry<Integer, Map<Character, Set<Integer>>> edge : transition_function.entrySet()) {
            int start = edge.getKey();
            for (Map.Entry<Character, Set<Integer>> half_edge: edge.getValue().entrySet()) {
                char sym = half_edge.getKey();
                for (int end : half_edge.getValue()) {
                    print_state(start, label);
                    System.out.print(' ');
                    System.out.print(sym);
                    System.out.print(' ');
                    print_state(end, label);
                    System.out.println();
                }
            }
        }
    }

    private void set_n_a(int n) {
        n_a = n;
    }

    private void set_n_w(int n) {
        n_w = n;
    }

    private void set_start_state(int n) {
        start_state = n;
    }

    private void set_finals(Set<Integer> fin) {
        final_states.clear();
        final_states.addAll(fin);
        n_f = fin.size();
    }

    private void set_trans(Map<Integer, Map<Character, Set<Integer>>> trans_f) {
        transition_function.clear();
        transition_function.putAll(trans_f);
    }

    private void set_labels(Map<Integer, String> new_labels) {
        labels.clear();
        labels.putAll(new_labels);
    }


    Automata determinate() {
        Automata dfa = new Automata();
        dfa.set_n_a(n_a);
        Map<Integer, Set<Integer>> new_states = new TreeMap<>();
        Map<Integer, Map<Character, Set<Integer>>> new_transition_function = new TreeMap<>();
        Stack<Set<Integer>> states_stack = new Stack<>();
        Set<Integer> state = new TreeSet<>();
        state.add(start_state);
        new_states.put(0, new TreeSet<>(state));
        dfa.set_start_state(0);
        states_stack.push(new TreeSet<>(state));
        int cur = 0;
        while (!states_stack.empty()) {
            state.clear();
            state.addAll(states_stack.pop());
            for (int i = 0; i < n_a; i++) {

                Set<Integer> new_state = new TreeSet<>();
                for (int s : state) {
                    if (transition_function.get(s) != null && transition_function.get(s).get((char) ('a' + i)) != null) {
                        new_state.addAll(transition_function.get(s).get((char) ('a' + i)));
                    }
                }

                if (!new_state.isEmpty() && !new_states.containsValue(new_state)) {
                    cur++;
                    new_states.put(cur, new TreeSet<>(new_state));
                    states_stack.push(new TreeSet<>(new_state));

                }

                if (!new_state.isEmpty()) {
                    int key = 0;
                    for (Map.Entry<Integer, Set<Integer>> entry : new_states.entrySet()) {
                        if (state.equals(entry.getValue())) {
                            key = entry.getKey();
                            break;
                        }
                    }
                    int key1 = 0;
                    for (Map.Entry<Integer, Set<Integer>> entry : new_states.entrySet()) {
                        if (new_state.equals(entry.getValue())) {
                            key1 = entry.getKey();
                            break;
                        }
                    }

                    new_transition_function.putIfAbsent(key, new TreeMap<>());
                    new_transition_function.get(key).putIfAbsent((char) ('a' + i), new TreeSet<>());
                    new_transition_function.get(key).get((char) ('a' + i)).add(key1);

                }
            }
        }

        dfa.set_n_w(new_states.size());

        Set<Integer> new_final_states = new TreeSet<>();
        for (int fin: final_states) {
            for (Map.Entry<Integer, Set<Integer>> nst: new_states.entrySet()) {
                if (nst.getValue().contains(fin)) {
                    new_final_states.add(nst.getKey());
                }
            }
        }
        dfa.set_finals(new_final_states);
        dfa.set_trans(new_transition_function);

        Map<Integer, String> new_labels = new TreeMap<>();
        for (int i = 0; i < new_states.size(); i++) {
            StringBuilder label = new StringBuilder();
            for (int old : new_states.get(i)) {
                label.append(old);
                label.append('+');
            }
            label.deleteCharAt(label.length() - 1);
            new_labels.put(i, label.toString());
        }
        dfa.set_labels(new_labels);

        return dfa;
    }

boolean reads(String word) {
        return backtrack(word, 0, start_state);
}

private boolean backtrack(String word, int num, int state) {
            if (num == word.length()) {
                return final_states.contains(state);
            }
            char c = word.charAt(num);
            if (c < 'a' || c > (char)('a' + n_a - 1)) {
                return false;
            }
            if (!transition_function.containsKey(state) || !transition_function.get(state).containsKey(c)) {
                return false;
            }
            else {
                Set<Integer> reachable = new TreeSet<>(transition_function.get(state).get(c));
                boolean r = false;
                for (int st: reachable) {
                    r = backtrack(word, num + 1, st) || r;
                }
                return r;
            }
    }

}
