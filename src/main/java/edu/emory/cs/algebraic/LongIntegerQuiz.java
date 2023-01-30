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
        byte[] result = new byte[m + 1];
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

            this.flipSign();

        } else {
            if(this.compareAbs(n) >= 0){ ////when digit is larger than n.digits
                System.arraycopy(digits, 0, result, 0, digits.length);

                for(int i = 0; i < n.digits.length; i++){
                    if(result[i] < n.digits[i]){
                        result[i] += 10;
                        result[i + 1] -= 1;
                    }
                    result[i] -= n.digits[i];
                }
            } else {
                System.arraycopy(n.digits, 0, result, 0, n.digits.length);

                for(int i = 0; i < digits.length; i++){
                    if(result [i] < digits[i]){
                        result[i] += 10;
                        result[i + 1] -= 1;
                    }
                    result[i] -= digits[i];
                }

                this.flipSign();
            }
        }
        int count = 0;
        for(int i = m; i > 0; i--){
            if(result [i] == 0) {
                count += 1;
            }
        }
        digits = Arrays.copyOf(result, m - count + 1);
    }
}

