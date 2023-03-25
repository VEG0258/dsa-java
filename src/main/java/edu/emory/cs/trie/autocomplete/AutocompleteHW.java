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
import edu.emory.cs.graph.Edge;
import edu.emory.cs.trie.TrieNode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.io.*;
import java.util.*;
import java.lang.*;



/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AutocompleteHW extends Autocomplete<List<String>> {
    public AutocompleteHW(String dict_file, int max) {
        super(dict_file, max);
    }

    @Override
    public List<String> getCandidates(String prefix) {
        return BFS(prefix.trim());
    }

    public List<String> BFS(String prefix){
        Deque<TrieNode<List<String>>> queue = new ArrayDeque<>();
        TrieNode<List<String>> root = prefixNode(prefix);
        List<String> given = root.getValue();
        queue.add(root);
        while(given.size() < getMax() && queue.size() != 0){
            TrieNode<List<String>> key = queue.poll();
            TreeMap<Character, TrieNode<List<String>>> chidrentreeMap = new TreeMap<>(key.getChildrenMap());
            queue.addAll(chidrentreeMap.values());
            if(key.isEndState()){
                String output = trackBack(key);
                boolean isexist = false;
                for (String s : given) {
                    if (s.equals(output)) {
                        isexist = true;
                        break;
                    }
                }
                if(!isexist){
                    given.add(output);
                }
            }
        }
        root.setValue(given);
        return given;
    }

    public TrieNode<List<String>> prefixNode(String prefix) {
        TrieNode<List<String>> current = find(prefix);
        if(current == null){
            put(prefix, new ArrayList<>());
            current = find(prefix);
            current.setEndState(false);
        }
        List<String> given = current.getValue();
        if(given == null){
            ArrayList<String> answer = new ArrayList<>();
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

    public String trackBack(TrieNode<List<String>> child){
        TrieNode<List<String>> current = child;
        String result = "";
        while(current.getParent() != null){
            result = current.getKey() + result;
            current = current.getParent();
        }
        return result;
    }


    @Override
    public void pickCandidate(String prefix, String candidate) {
        TrieNode<List<String>> current = find(candidate);
        //put candidate into tries
        if(current == null){
            //if pass does not exist in tries
            put(candidate,null);
        } else if(current.isEndState() == false){
            //if pass exist in tries not as a word
            current.setEndState(true);
        }

        prefix = prefix.trim();
        candidate = candidate.trim();
        Deque<TrieNode<List<String>>> queue = new ArrayDeque<>();
        TrieNode<List<String>> root = prefixNode(prefix);
        List<String> given = root.getValue();
        queue.add(root);
        while(given.size() < getMax() && queue.size() != 0){
            TrieNode<List<String>> key = queue.poll();
            TreeMap<Character, TrieNode<List<String>>> chidrentreeMap = new TreeMap<>(key.getChildrenMap());
            queue.addAll(chidrentreeMap.values());
            if(key.isEndState()){
                String output = trackBack(key);
                boolean isexist = false;
                for (String s : given) {
                    if (s.equals(output)) {
                        isexist = true;
                        break;
                    }
                }
                if(!isexist){
                    if(output.equals(candidate)){
                        given.add(0, output);
                    } else {
                        given.add(output);
                    }
                }
            }
        }
        //if picked a things that never exist in the dictionary
        //should we also add that new word to tries?
        boolean candidateexist = false;
        for(int i = 0; i < given.size(); i++){
            if(given.get(i).equals(candidate)){
                candidateexist = true;
                if(i > 0){
                    // If candidate is not already at the beginning of the list and it is the most recently picked word, move it to the beginning
                    String first = given.get(0);
                    if(candidate.compareTo(first) > first.compareTo(candidate)) {
                        given.remove(i);
                        given.add(0, candidate);
                    }
                }
                break;
            }
        }
        if(candidateexist == false && given.size() != 0){
            given.remove(given.size() - 1);
            given.add(0, candidate);
            candidateexist = true;
        }
        if(candidateexist == false && given.size() == 0){
            given.add(0, candidate);
        }
        root.setValue(given);
    }
}