///*
// * Copyright 2020 Emory University
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
package edu.emory.cs.graph.flow;

import edu.emory.cs.graph.Edge;
import edu.emory.cs.graph.Graph;
import edu.emory.cs.graph.Subgraph;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetworkFlowQuiz {
    Set<Subgraph> set = new HashSet<>();

    /**
     * Using depth-first traverse.
     *
     * @param graph a directed graph.
     * @param source the ource vertex.
     * @param target the target vertex.
     * @return a set of all augmenting paths between the specific source and target vertices in the graph.
     */
    public Set<Subgraph> getAugmentingPaths(Graph graph, int source, int target) {
        int tmp = target;
        target = source;
        source = tmp;
        List<Edge> first = graph.getIncomingEdges(tmp);
        boolean[] visited = new boolean[graph.size()];

        for (Edge edge : first) {
            visited[source] = true;
            Subgraph subgraph = new Subgraph();
            subgraph.addEdge(edge);
            dfs(subgraph, graph, edge, target, visited);
        }
        return set;
    }

    public void dfs(Subgraph subgraph, Graph graph, Edge e, int target, boolean[] visited) {
        List<Edge> edges;
        if (e.getSource() == target) {
            addSubgraphs(subgraph, target, visited);
        }

        edges = graph.getIncomingEdges(e.getSource());
        for (Edge edge : edges) {
            dealEachGraph(subgraph, graph, target, visited, edge);
        }

        visited[e.getSource()] = false;
    }

    private void addSubgraphs(Subgraph subgraph, int target, boolean[] visited) {
        List<Edge> edges;
        visited[target] = false;
        edges = subgraph.getEdges();
        Subgraph n = new Subgraph();
        for (Edge edge : edges) {
            n.addEdge(edge);
        }
        set.add(n);
    }

    private void dealEachGraph(Subgraph subgraph, Graph graph, int target, boolean[] visited, Edge edge) {
        if (!visited[edge.getSource()]) {
            visited[edge.getSource()] = true;
            subgraph.addEdge(edge);
            dfs(subgraph, graph, edge, target, visited);
            List<Edge> cur = subgraph.getEdges();
            cur.remove(edge);
            subgraph = new Subgraph();
            for (Edge ed : cur) {
                subgraph.addEdge(ed);
            }
        }
    }
}