
package edu.emory.cs.trie.autocomplete;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AutocompleteTest {
    static class Eval {
        int correct = 0;
        int total = 0;
    }

    @Test
    public void test() {
        final String dict_file = "src/main/resources/dict.txt";
        final int max = 20;

        Autocomplete<?> ac = new AutocompleteHW(dict_file, max);
        Eval eval = new Eval();
        testAutocomplete(ac, eval);
    }

    private void testAutocomplete(Autocomplete<?> ac, Eval eval) {
        String prefix;
        List<String> expected;

        prefix = "sh";
        expected = List.of();
        testGetCandidates(ac, eval, prefix, expected);

        prefix = "sh";
        expected = List.of("");
        ac.pickCandidate(prefix, "shcd");
        testGetCandidates(ac, eval, prefix, expected);

        prefix = "shcd";
        expected = List.of("");
//        ac.pickCandidate(prefix, "she");
//        ac.pickCandidate(prefix, "12334");
//        ac.pickCandidate(prefix, "098776");
        testGetCandidates(ac, eval, prefix, expected);

        prefix = "shcd";
        expected = List.of("");
        ac.pickCandidate(prefix, "abc");
        testGetCandidates(ac, eval, prefix, expected);


        prefix = "sh";
        expected = List.of();
        testGetCandidates(ac, eval, prefix, expected);

        System.out.printf("Score: %d/%d\n", eval.correct, eval.total);
    }

    private void testGetCandidates(Autocomplete<?> ac, Eval eval, String prefix, List<String> expected) {
        String log = String.format("%2d: ", eval.total);
        eval.total++;

        try {
            List<String> candidates = ac.getCandidates(prefix);

            if (expected.equals(candidates)) {
                eval.correct++;
                log += "PASS";
            }
            else
                log += String.format("FAIL -> expected = %s, returned = %s", expected, candidates);
        }
        catch (Exception e) {
            log += "ERROR";
        }

        System.out.println(log);
    }
}