public class UniqueSubstringFinder {
    private static int getTrackerArrayIndex(char[] stringArr, int ind) {
        return stringArr[ind] - 'a';
    }

    public static String longestUniqueSubstring(String s) {
        char[] arr = s.toCharArray();
        boolean[] charAppearanceTracker = new boolean[26];
        int l = 0, maxLength = 0;
        String res = s;

        for (int i = 0; i < arr.length; i++) {
            int ind = getTrackerArrayIndex(arr, i);

            if (charAppearanceTracker[ind]) {
                int currentLength = i - l;
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    res = s.substring(l, i);
                }

                while (arr[l] != arr[i]) {
                    ind = getTrackerArrayIndex(arr, l);
                    charAppearanceTracker[ind] = false;
                    l++;
                }
                l++;
            }
            else {
                charAppearanceTracker[ind] = true;
            }
        }

        return res;
    }
}
