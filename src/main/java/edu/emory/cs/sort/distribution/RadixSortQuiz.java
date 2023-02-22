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
package edu.emory.cs.sort.distribution;

import java.util.Deque;
import java.util.function.Function;
import java.util.Arrays;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class RadixSortQuiz extends RadixSort {

    @Override
    public void sort(Integer[] array, int beginIndex, int endIndex) {
        sort(array, beginIndex, endIndex, 0);
    }

    public void sort(Integer[] array, int beginIndex, int endIndex, int digits){

        int maxBit = getMaxBit(array, beginIndex, endIndex);
        if(endIndex - beginIndex <= 1){
            return;
        }
        int div = (int)Math.pow(10, maxBit - 1 - digits);
        if(div == 0){
            return;
        }
        sort(array, beginIndex, endIndex, key -> (key / div) % 10);
        if(div == 1) {
            return;
        }
        int i = beginIndex;
        int j = i + 1;
        while(j <= endIndex){
            if(j == endIndex || (array[i]/div) % 10 != (array[j]/div) % 10){
                if(array[i]/div == 0) sort(array, i, j, digits);
                else sort(array, i, j, digits + 1);
                i = j;
            }
            j++;
        }
    }
}