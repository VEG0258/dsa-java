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
package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteHWExtra extends Autocomplete<List<AutocompleteHWExtra.Candidate>> {
    private long accessCounter;
    public AutocompleteHWExtra(String dict_file, int max) {
        super(dict_file, max);
        accessCounter = 0;
    }

    protected static class Candidate implements Comparable<Candidate> {
        String word;
        int frequency;
        long lastAccessOrder;

        public Candidate(String word) {
            this.word = word;
            this.frequency = 0;
            this.lastAccessOrder = 0;
        }

        @Override
        public int compareTo(Candidate input) {
            int freqCompare = Integer.compare(input.frequency, this.frequency); // Sort by frequency in descending order
            if (freqCompare != 0) {
                return freqCompare;
            }
            // If frequency is same, sort by recency in descending order
            return Long.compare(input.lastAccessOrder, this.lastAccessOrder); // Replace lastAccessTime with lastAccessOrder
        }
    }

    @Override
    public List<String> getCandidates(String prefix) {
        return BFS(prefix.trim());
    }

    public List<String> BFS(String prefix){
        Deque<TrieNode<List<Candidate>>> queue = new ArrayDeque<>();
        TrieNode<List<Candidate>> root = prefixNode(prefix);
        List<String> returnValue = new ArrayList<>();
        for(int i = 0; i < root.getValue().size(); i++){
            returnValue.add(root.getValue().get(i).word);
        }
        queue.add(root);
        while(queue.size() != 0 && returnValue.size() < getMax()){
            TrieNode<List<Candidate>> key = queue.poll();
            TreeMap<Character, TrieNode<List<Candidate>>> chidrentreeMap = new TreeMap<>(key.getChildrenMap());
            queue.addAll(chidrentreeMap.values());
            if(key.isEndState()){
                String output = trackBack(key);
                boolean isexist = false;
                for (String s : returnValue) {
                    if (s.equals(output)) {
                        isexist = true;
                        break;
                    }
                }
                if(!isexist){
                    returnValue.add(output);
                }
            }
        }
        return(returnValue);
    }

    public TrieNode<List<Candidate>> prefixNode(String prefix) {
        TrieNode<List<Candidate>> current = find(prefix);
        if(current == null){
            put(prefix, new ArrayList<>());
            current = find(prefix);
            current.setEndState(false);
        }
        List<Candidate> given = current.getValue();
        if(given == null){
            ArrayList<Candidate> answer = new ArrayList<>();
            current.setValue(answer);
        } else {
            if(given.size() > getMax()){
                int j = given.size() - 1;
                while(j > getMax() - 1){
                    given.remove(j);
                    j--;
                }
            }
        }

        return current;
    }

    public String trackBack(TrieNode<List<Candidate>> child){
        TrieNode<List<Candidate>> current = child;
        String result = "";
        while(current.getParent() != null){
            result = current.getKey() + result;
            current = current.getParent();
        }
        return result;
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {

        prefix = prefix.trim();
        candidate = candidate.trim();

        TrieNode<List<Candidate>> current = find(candidate);
        //put candidate into tries
        if(current == null){
            //if pass does not exist in tries
            put(candidate,null);
        } else if(current.isEndState() == false){
            //if pass exist in tries not as a word
            current.setEndState(true);
        }
        TrieNode<List<Candidate>> root = prefixNode(prefix);
        List<Candidate> given = root.getValue();
        boolean ifexist = false;
        int i = 0;
        while(i < given.size()){
            if(given.get(i).word.equals(candidate)){
                ifexist = true;
                break;
            }
            i++;
        }
        accessCounter++; // Increment the access counter when a candidate is picked

        if (ifexist) {
            given.get(i).frequency++;
            given.get(i).lastAccessOrder = accessCounter; // Update the lastAccessOrder with the current access counter value
        } else {
            Candidate newpick = new Candidate(candidate);
            newpick.frequency++;
            newpick.lastAccessOrder = accessCounter; // Set the lastAccessOrder with the current access counter value
            // add new candidate into value of root and sort it
            given.add(newpick);
        }
        Collections.sort(given);

        for(int j = 0; j < given.size(); j++){
            System.out.print("round - " + j + ": ");
            System.out.print("word: " + given.get(j).word + "; ");
            System.out.print("frequencey: " + given.get(j).frequency + "; ");
            System.out.print("time: " + given.get(j).lastAccessOrder + "; ");
            System.out.println("");
        }
    }
}