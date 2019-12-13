import java.util.ArrayList;
import java.util.HashMap;

/**
 * Realization of arihtmetic encoding algorithm, see https://habr.com/ru/post/130531/
 */
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: jar <string_to_be_encoded>");
        }
        System.out.println("Read line: '" + args[0] + "'");

        String text = args[0];
        char[] charArray = text.toCharArray();
        final HashMap<Character, Integer> charFrequency = new HashMap<Character, Integer>();
        for (char c : charArray) {
            if (charFrequency.containsKey(c)) {
                charFrequency.put(c, charFrequency.get(c) + 1);
            } else {
                charFrequency.put(c, 1);
            }
        }

        final HashMap<Character, Double> codesTable = new HashMap<>(charFrequency.size());
        charFrequency.forEach((k, v) -> codesTable.put(k, v.doubleValue() / text.length()));

        ArrayList<Character> symbolsSeq = new ArrayList<>(codesTable.keySet());

        double[] firstSegmentation = new double[codesTable.size()];
        firstSegmentation[0] = codesTable.get(symbolsSeq.get(0));
        for (int i = 1; i < codesTable.size(); ++i) {
            firstSegmentation[i] = firstSegmentation[i - 1] + codesTable.get(symbolsSeq.get(i));
        }

        // for debug purpose
        System.out.println("Interation: ");
        for (int i = 0; i < codesTable.size(); ++i) {
            System.out.print("[ " + symbolsSeq.get(i) + " ] : " + firstSegmentation[i] + "  |  ");
        }
        System.out.println();

        for (int k = 0; k < charArray.length - 1; ++k) {
            //find letter and set a code for it (left border)
            int letterIndex = symbolsSeq.indexOf(charArray[k]);
            double lowBorder = letterIndex == 0 ? 0 : firstSegmentation[letterIndex - 1];
            double highBorder = firstSegmentation[letterIndex];

            double[] secondSegmentation = new double[codesTable.size()];
            for (int i = 0; i < codesTable.size(); ++i) {
                secondSegmentation[i] = lowBorder + (highBorder - lowBorder) * firstSegmentation[i];
            }
            firstSegmentation = secondSegmentation;

            // for debug purpose
//            System.out.println("Interation for '" + charArray[k] + "' : ");
//            for (int i = 0; i < codesTable.size(); ++i) {
//                System.out.print("[ " + symbolsSeq.get(i) + " ] : " + firstSegmentation[i] + "  |  ");
//            }
//            System.out.println();
        }
        int lastSymbolIndex = symbolsSeq.indexOf(charArray[charArray.length - 1]);
        double stringCode = lastSymbolIndex == 0 ? 0 : firstSegmentation[lastSymbolIndex - 1];
        System.out.println("Result : " + stringCode);

        //TODO: https://www.geeksforgeeks.org/ieee-standard-754-floating-point-numbers/
    }
}
