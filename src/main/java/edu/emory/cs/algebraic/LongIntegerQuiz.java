package edu.emory.cs.algebraic;

import java.security.InvalidParameterException;
import java.util.Arrays;

/** @author Jinho D. Choi */
public class LongIntegerQuiz extends LongInteger {
    public LongIntegerQuiz(LongInteger n) { super(n); }

    public LongIntegerQuiz(String n) { super(n); }

    @Override
    protected void addDifferentSign(LongInteger n) {
        int m = Math.max(digits.length, n.digits.length);
        byte[] result = new byte[m];// not m + 1 because it is subtution
        //when digit is larger than n.digits
        if(digits.length > n.digits.length){
            System.arraycopy(digits, 0, result, 0, digits.length);

            for(int i = 0; i < n.digits.length; i++){
                if(result[i] < n.digits[i]){
                    result[i] += 10;
                    result[i + 1] -= 1;
                }
                result[i] -= n.digits[i];
            }
        } else if (digits.length < n.digits.length){ //when n.digits is larger than digits
            System.arraycopy(n.digits, 0, result, 0, n.digits.length);

            for(int i = 0; i < digits.length; i++){
                if(result [i] < digits[i]){
                    result[i] += 10;
                    result[i + 1] -= 1;
                }
                result[i] -= digits[i];
            }
        } else {
            byte[] smaller = new byte[m];
            for(int i = 0; i < digits.length; i++){
                if(digits[i] > n.digits[i]){
                    System.arraycopy(digits, 0, result, 0, digits.length);
                    System.arraycopy(n.digits, 0, smaller, 0, n.digits.length);
                    break;
                } else if (digits[i] < n.digits[i]){
                    System.arraycopy(n.digits, 0, result, 0, n.digits.length);
                    System.arraycopy(digits, 0, smaller, 0, digits.length);
                    break;
                } else {
                    System.arraycopy(digits, 0, result, 0, digits.length);
                    System.arraycopy(n.digits, 0, smaller, 0, n.digits.length);
                }
            }
            for(int j = 0; j < smaller.length; j++){
                if(result [j] < smaller[j]){
                    result[j] += 10;
                    result[j + 1] -= 1;
                }
                result[j] -= smaller[j];
            }
        }
        digits = result[m] == 0 ? Arrays.copyOf(result, m) : result;
    }
}
