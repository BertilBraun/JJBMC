public class WrapUtils {


    /**
     * Given a non-null array s and a positive length, this
     * method replaces ' ' by '\n' characters such that the
     * length of each line never exceeps length characters.
     * However, it makes lines as long as possible.
     *
     * This method never throws an exception and always
     * terminates.
     */
    /*@ requires s != null && length > 0;
      @ ensures (\forall int i; i >= 0 && i < s.length; \old(s[i]) == s[i] || (\old(s[i]) == ' ' && s[i] == '\n'));
      @ ensures (\forall int i; 0 <= i && i < s.length;
      @             ((\forall int j; i <= j && j < i + length; j < s.length ==>  s[j] != '\n')
      @         ==> (\forall int j; i <= j && j < i + length; j < s.length ==> s[j] != ' ')));
      @ ensures (\forall int i; 0 <= i < s.length;
      @     (\forall int j; i < j < s.length;
      @     (\forall int k; j < k < s.length;
      @         (s[i] == '\n' && s[j] == '\n' && \old(s[j]) == ' ' && \old(s[k]) == ' ') ==> k - i >= length)));
      @
     */
    public static void wrapLines(char[] s, int length) {

        int lastChange = -1;
        int lastSpace = -1;



        /*@ loop_invariant s != null;
          @ loop_invariant -1 <= lastSpace && lastSpace < s.length;
          @ loop_invariant -1 <= lastChange && lastChange <= lastSpace;
          @ //loop_invariant lastSpace >= 0 ==> (\old(s)[lastSpace] == ' ' || \old(s)[lastSpace] == '\n');
          @ loop_invariant (\forall int i; 0 <= i && i < s.length;
          @    s[i] == \old(s[i]) || (\old(s[i]) == ' ' && s[i] == '\n'));
          @ loop_invariant lastSpace - lastChange < length ||
          @    (\forall int l; lastChange < l && l < lastSpace; s[l] != ' ');
          @ loop_invariant (\forall int i; 0 <= i && i < lastChange; (i < s.length - length) ==>
          @             ((\forall int j; i <= j && j < i + length; s[j] != '\n')
          @         ==> (\forall int j; i <= j && j < i + length; s[j] != ' ')));
          @ loop_invariant lastChange == -1 || s[lastChange] == '\n';
          @ loop_invariant (\forall int i; 0 <= i <= lastChange;
          @     (\forall int j; i < j <= lastChange;
          @     (\forall int k; j < k <= lastChange;
          @         (s[i] == '\n' && s[j] == '\n' && \old(s[j]) == ' ' && \old(s[k]) == ' ') ==> k - i >= length)));
          @ decreases s.length - lastSpace;
          @ loop_modifies s[*], lastSpace, lastChange;
          @*/
        while(true) {
            int nextSpace = indexOf(s, ' ', lastSpace + 1);
            if(nextSpace == -1) {
                if(s.length - lastChange >= length && lastSpace >= 0) {
                    s[lastSpace] = '\n';
                }
                return;
            }
            int nextNewLine = indexOf(s, '\n', lastSpace + 1);
            if(0 <= nextNewLine && nextNewLine < nextSpace) {
                if(nextNewLine - lastChange >= length && lastSpace >= 0) {
                    s[lastSpace] = '\n';
                }
                lastChange = lastSpace = nextNewLine;
            } else {
                if(nextSpace - lastChange >= length) {
                    if(lastChange == lastSpace) {
                        s[nextSpace] = '\n';
                        lastChange = lastSpace = nextSpace;
                    } else {
                        s[lastSpace] = '\n';
                        lastChange = lastSpace;
                    }
                }
                lastSpace = nextSpace;
            }
        }
    }

    /*@ requires s != null && from >= 0;
      @ ensures (\result == -1 && (\forall int i; i >= from && i < s.length; s[i] != c)) || (\result >= from && \result < s.length && s[\result] == c);
      @ ensures (\forall int i; i >= from && i < \result; s[i] != c);
    */
    private static int indexOf(char[] s, char c, int from) {
        for(int k = from; k < s.length; k++) {
            if(s[k] == c) {
                return k;
            }
        }
        return -1;
    }

    private static int indexBefore(char[] s, char c, int before) {
        for(int k = before; k > 0; k--) {
            if(s[k] == c) {
                return k;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        char[] a = "\n   \n".toCharArray();
        WrapUtils.wrapLines(a, 4);
        System.out.println(a);
        char[] a1 = "\n   \n".toCharArray();
        WrapUtils.wrapLines2(a1, 4);
        System.out.println(a1);
    }

    /*@ requires s != null && length > 0;
      @ ensures (\forall int i; i >= 0 && i < s.length; \old(s[i]) == s[i] || (\old(s[i]) == ' ' && s[i] == '\n'));
      @ ensures (\forall int i; -1 <= i && i < s.length; ((i == -1 || s[i] == '\n')  && i < s.length - length) ==>
      @             ((\forall int j; i < j && j <= i + length; j <= s.length ==>  s[j] != '\n')
      @         ==> (\forall int j; i < j && j <= i + length; j <= s.length ==> s[j] != ' ')));
      @ ensures (\forall int i; 0 <= i < s.length;
      @     (\forall int j; i < j < s.length;
      @     (\forall int k; j < k < s.length;
      @         (s[i] == '\n' && s[j] == '\n' && \old(s[j]) == ' ' && \old(s[k]) == ' ') ==> k - i >= length)));
      @*/
    public static void wrapLines2(char[] s, int length) {
        int lastBreak = -1;
        int nextBreak = lastBreak + length;
        int origBreak = indexOf(s, '\n', 0);
        while(nextBreak < s.length) {
            if(origBreak > lastBreak && origBreak <= nextBreak) {
                lastBreak = origBreak;
                nextBreak = lastBreak + length;
                origBreak = indexOf(s, '\n', lastBreak + 1);
            } else {
                int potBreak = indexBefore(s, ' ', nextBreak);
                if (potBreak > lastBreak) {
                    s[potBreak] = '\n';
                    lastBreak = potBreak;
                } else {
                    potBreak = indexOf(s, ' ', lastBreak + 1);
                    if (potBreak >= 0) {
                        s[potBreak] = '\n';
                        lastBreak = potBreak;
                    } else {
                        lastBreak = s.length;
                    }
                }
                nextBreak = lastBreak + length;
            }
        }
    }
}
