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
package edu.emory.cs.tree.balanced;

import edu.emory.cs.tree.BinaryNode;

/** @author Jinho D. Choi */
public class BalancedBinarySearchTreeQuiz<T extends Comparable<T>> extends AbstractBalancedBinarySearchTree<T, BinaryNode<T>> {
    @Override
    public BinaryNode<T> createNode(T key) {
        return new BinaryNode<>(key);
    }

    @Override
    protected void balance(BinaryNode<T> node) {
        if(node == null){
            return;
        } else {
            BinaryNode<T> parent = node.getParent();
            BinaryNode<T> grand = node.getGrandParent();
            BinaryNode<T> uncle = node.getUncle();
            if (parent != null && grand != null && uncle != null) {
                boolean con1 = (parent.hasLeftChild() && !parent.hasRightChild()) || (!parent.hasLeftChild() && parent.hasRightChild());
                boolean con2 = grand.isRightChild(parent);
                boolean con3 = (uncle.hasLeftChild() && !uncle.hasRightChild()) || (!uncle.hasLeftChild() && uncle.hasRightChild());
                if (con1 == true && con2 == true && con3 == true) {
                    if (uncle.hasLeftChild() == true) {
                        if (parent.hasLeftChild() == true) {
                            rotateRight(parent);
                        }
                        rotateLeft(grand);
                        rotateRight(grand);
                    } else {
                        if (parent.hasLeftChild()) {
                            rotateRight(parent);
                        }
                        rotateLeft(uncle);
                        rotateLeft(grand);
                        rotateRight(grand);
                    }
                }
            }
            balance(root.getParent());
        }
    }
}