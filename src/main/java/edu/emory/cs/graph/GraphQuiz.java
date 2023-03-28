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
package edu.emory.cs.graph;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/** @author Jinho D. Choi */
public class GraphQuiz extends Graph {
    public GraphQuiz(int size) { super(size); }
    public GraphQuiz(Graph g) { super(g); }

    private int res = 0;

    private final List<Integer> list = new ArrayList<>();

    private List<Deque<Edge>> outgoingEdges;

    /** @return the total number of cycles in this graph. */
    public int numberOfCycles() {
        outgoingEdges = getOutgoingEdges();
        for (int idx = 0; idx < size(); ++idx) {
            Iterator<Deque<Edge>> iterator = outgoingEdges.iterator();
            label33: while (iterator.hasNext()) {
                Deque<Edge> edges = iterator.next();
                Iterator<Edge> edgeIterator = edges.iterator();
                while (true) {
                    Edge edge;
                    do {
                        if (!edgeIterator.hasNext()) {
                            continue label33;
                        }
                        edge = edgeIterator.next();
                    } while (edge.getSource() >= idx && edge.getTarget() >= idx);

                    edges.remove(edge);
                }
            }
            findCycle(idx, idx);
        }

        System.out.println(res);
        return res;
    }

    private void findCycle(int start, int idx) {
        list.add(idx);
        Deque<Edge> edges = outgoingEdges.get(idx);
        for (Edge edge : edges) {
            if (start == edge.getTarget()) {
                ++res;
            } else if (!list.contains(edge.getTarget())) {
                findCycle(start, edge.getTarget());
            }
        }
        list.remove(list.size() - 1);
    }
}