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
package edu.emory.cs.graph.span;

import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;
import edu.emory.cs.graph.span.MSTKruskal;
import edu.emory.cs.set.DisjointSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayDeque;
import java.util.stream.Collectors;


/** @author Jinho D. Choi */
public class MSTAllHW implements MSTAll {

    //pre: kruskal to find the MST
    //1: did the edges cover all the vertices
    //2: is there cycles in these edges
    //3: is the total weight equal to the MST weight
    //4: get all combination of (vertices - 1) edges and see if any of them pass the 3 test above

    public List<SpanningTree> getMinimumSpanningTrees(Graph graph) {
        SpanningTree firstTree = new MSTKruskal().getMinimumSpanningTree(graph);
        double firstTreeWeight = firstTree.getTotalWeight();
        List<Edge> edges = graph.getAllEdges().stream()
                .filter(e -> e.getSource() < e.getTarget())
                .collect(Collectors.toList());
        List<SpanningTree> result = new ArrayList<>();
        generateCombinations(graph, firstTreeWeight, edges, new ArrayList<>(), 0, result);
        return result;
    }


    public void generateCombinations(Graph graph, double firstTreeWeight, List<Edge> edges, List<Edge> current, int start, List<SpanningTree> result) {
        if (current.size() == graph.size() - 1) {
            if (//coverAllVertices(current, graph) &&
                    !containCycles(current, graph) &&
                    equaltotalweight(current, firstTreeWeight)) {
                SpanningTree tree = new SpanningTree();
                current.forEach(tree::addEdge);
                result.add(tree);
            }
            return;
        }

        for (int i = start; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            current.add(edge);
            generateCombinations(graph, firstTreeWeight, edges, current, i + 1, result);
            current.remove(current.size() - 1);
        }
    }

//    public boolean coverAllVertices(List<Edge> input, Graph graph){
//        Set<Integer> visited = new HashSet<>();
//        for(Edge edge : input){
//            visited.add(edge.getTarget());
//            visited.add(edge.getSource());
//        }
//        return visited.size() == graph.size();
//    }

    public boolean containCycles(List<Edge> input, Graph graph){
        List<Edge> temp = input;
        DisjointSet forest = new DisjointSet(graph.size());
        int i = 0;
        while (i < temp.size()) {
            Edge edge = temp.get(i);
            if (forest.inSameSet(edge.getTarget(), edge.getSource())) {
                return true;
            }
            forest.union(edge.getTarget(), edge.getSource());
            i++;
        }
        return false;
    }

    public boolean equaltotalweight(List<Edge> input, double firstreeweight){
        double result = 0;
        for(Edge edge : input){
            result += edge.getWeight();
        }
        return result == firstreeweight;
    }
}