package lab2;

import java.util.List;

/**
 * The frequency analysis attack on the Variant 1 cryptogram, step by step.
 * Each step is a group of substitutions "CIPHER=plain" together with the reasoning behind it.
 */
public class VariantOneAttack {

    public record Step(String substitutions, String reasoning) {}

    public static final List<Step> STEPS = List.of(
        new Step("V=e W=t",
            "The most frequent ciphertext letters are V (12.74%) and W (10.75%). In English the most frequent "
            + "letters are E (12.70%) and T (9.06%), so we assume V -> e and W -> t."),
        new Step("Q=h",
            "The 3-letter word 'tQe' appears 55 times. The most common English word is 'the', so Q -> h. "
            + "'th' and 'he' are also the two most frequent English digraphs."),
        new Step("T=a",
            "'T' appears 15 times as a word on its own. The only one-letter English words are 'a' and 'I'; "
            + "T is also the 6th most frequent letter (6.93%), close to A (8.17%), so T -> a."),
        new Step("N=o C=f",
            "'tN' (15 times) can only be 'to', so N -> o (7.99% vs 7.51% for O). The most frequent "
            + "two-letter word 'oC' (27 times) then reads as 'of', so C -> f."),
        new Step("G=n O=d",
            "'aGO' appears 6 times: the most frequent 3-letter word after 'the' is 'and', so G -> n "
            + "and O -> d. G is 7.64% and N is 6.75% in English."),
        new Step("X=i",
            "'Xn' appears 19 times and 'Xt' 6 times: 'in' and 'it'. X has 7.71%, very close to I (6.97%)."),
        new Step("I=r P=s R=w",
            "'foI' is 'for', so I -> r. 'hiP' (his) gives P -> s and then the frequent word 'Ras' (6 times) "
            + "can only be 'was', so R -> w."),
        new Step("H=c F=y U=p S=l J=g",
            "In 'the reHorded historF of HrFUtoSoJF' the 10-letter word 'HrFUtoSoJF' fits only 'cryptology' "
            + "(and the text is about its history), giving H -> c, F -> y, U -> p, S -> l and J -> g."),
        new Step("A=b D=u Z=m",
            "'ADt' (3 times) is 'but', so A -> b and D -> u. 'ZaDy' / 'Zany' reads 'many', so Z -> m."),
        new Step("K=v L=k Y=x B=q E=j",
            "Remaining rare letters from context: 'deKeloped' -> 'developed' (K -> v), 'Lnows' -> 'knows' "
            + "(L -> k), 'teYt' -> 'text' (Y -> x), 'techniBue' -> 'technique' (B -> q), 'Eust' -> 'just' (E -> j).")
    );

    /**
     * Applies a step's substitutions to the key.
     */
    public static void apply(Step step, SubstitutionKey key) {
        for (String pair : step.substitutions().split("\\s+")) {
            key.set(pair.charAt(0), pair.charAt(2));
        }
    }
}
