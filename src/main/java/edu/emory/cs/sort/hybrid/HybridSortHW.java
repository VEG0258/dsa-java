/*
 * Copyright 2020 Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.cs.sort.hybrid;
import edu.emory.cs.sort.AbstractSort;
import edu.emory.cs.sort.comparison.ShellSortKnuth;
import edu.emory.cs.sort.divide_conquer.IntroSort;

import java.lang.reflect.Array;
import java.util.*;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HybridSortHW<T extends Comparable<T>> implements HybridSort<T> {

    private AbstractSort<T> engine1 = new ShellSortKnuth<>();
    private IntroSort<T> engine2 = new IntroSort<>(new ShellSortKnuth<T>());

    @Override
    public T[] sort(T[][] input) {
        for(int i = 0; i < input.length; i++){
            if(input[i].length <= 1){
                continue;
            }
            int count = 0;
            int same = 0;
            for(int j = 0; j < input[i].length - 1; j++) {
                int z = j + 1;
                if (input[i][j].compareTo(input[i][z]) < 0) {
                    count++;
                } else if (input[i][j].compareTo(input[i][z]) > 0) {
                    count--;
                } else {
                    same++;
                }
            }
            double ratio = count * 1.0 / (input[i].length - 1 - same);
            //ascending & Partially asscending
            if(ratio > 0.5){
                if(ratio == 1){
                    //null
                } else {
                    engine1.sort(input[i]);
                }
            }else if(ratio < -0.5){ //descending & Partially descending
                Collections.reverse(Arrays.asList(input[i]));
                if(ratio == -1){
                    //null
                } else {
                    engine1.sort(input[i]);
                }
            } else {//random
                engine2.sort(input[i]);
            }
        }
        return mergeSort(input);
    }

    @SuppressWarnings("unchecked")
    private T[] mergeSort(T[][] input){
        Class<?> classtype = input[0][0].getClass();

        Queue<T[]> result = new ArrayDeque<>(Arrays.asList(input));

        while(result.size() > 1){
            T[] sub1 = result.poll();
            T[] sub2 = result.poll();
            T[] sub3 = merge(sub1, sub2, classtype);
            result.add(sub3);
        }

        return result.poll();

    }

    private T[] merge(T[] input1, T[] input2, Class<?> classtype) {
        int length1 = input1.length;
        int length2 = input2.length;
        T[] result =(T[]) Array.newInstance(classtype, length1 + length2);
        int i = 0, j = 0, z = 0;
        while(i < length1 && j < length2){
            if(input1[i].compareTo(input2[j]) >= 0){
                result[z] = input2[j];
                j++;
                z++;
            } else {
                result[z] = input1[i];
                i++;
                z++;
            }
        }
        while(i < length1){
            result[z] = input1[i];
            i++;
            z++;
        }
        while(j < length2){
            result[z] = input2[j];
            j++;
            z++;
        }
        return result;
    }
}