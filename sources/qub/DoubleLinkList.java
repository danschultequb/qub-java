package qub;

public class DoubleLinkList<T> implements List<T>
{
    private DoubleLinkNode<T> head;
    private DoubleLinkNode<T> tail;

    public DoubleLinkList()
    {
        head = null;
        tail = null;
    }

    @Override
    public DoubleLinkList<T> add(T value)
    {
        final DoubleLinkNode<T> nodeToAdd = new DoubleLinkNode<>(value);
        if (head == null)
        {
            head = nodeToAdd;
            tail = nodeToAdd;
        }
        else
        {
            nodeToAdd.setPrevious(tail);
            tail.setNext(nodeToAdd);
            tail = nodeToAdd;
        }
        return this;
    }

    @Override
    public DoubleLinkList<T> insert(int insertIndex, T value)
    {
        PreCondition.assertBetween(0, insertIndex, this.getCount(), "insertIndex");

        if (insertIndex == this.getCount())
        {
            this.add(value);
        }
        else
        {
            final DoubleLinkNode<T> nodeToInsert = new DoubleLinkNode<>(value);

            if (insertIndex == 0)
            {
                nodeToInsert.setNext(head);
                head.setPrevious(nodeToInsert);
                head = nodeToInsert;
            }
            else
            {
                DoubleLinkNode<T> currentNode = head;
                for (int currentNodeIndex = 0; currentNodeIndex < insertIndex; ++currentNodeIndex)
                {
                    currentNode = currentNode.getNext();
                }

                currentNode.getPrevious().setNext(nodeToInsert);
                nodeToInsert.setPrevious(currentNode.getPrevious());

                currentNode.setPrevious(nodeToInsert);
                nodeToInsert.setNext(currentNode);
            }
        }

        return this;
    }

    private DoubleLinkNode<T> getNode(int index)
    {
        PreCondition.assertIndexAccess(index, getCount(), "index");

        DoubleLinkNode<T> result = head;
        for (int i = 0; i < index; ++i)
        {
            result = result.getNext();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    @Override
    public DoubleLinkList<T> set(int index, T value)
    {
        PreCondition.assertIndexAccess(index, getCount());

        this.getNode(index).setValue(value);

        return this;
    }

    @Override
    public T removeAt(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

        final DoubleLinkNode<T> nodeToRemove = getNode(index);
        final T result = nodeToRemove.getValue();

        final DoubleLinkNode<T> previousNode = nodeToRemove.getPrevious();
        final DoubleLinkNode<T> nextNode = nodeToRemove.getNext();

        if (previousNode == null)
        {
            head = nextNode;
        }
        else
        {
            previousNode.setNext(nextNode);
        }

        if (nextNode == null)
        {
            tail = previousNode;
        }
        else
        {
            nextNode.setPrevious(previousNode);
        }

        nodeToRemove.setPrevious(null);
        nodeToRemove.setNext(null);
        nodeToRemove.setValue(null);

        return result;
    }

    @Override
    public T removeFirst(Function1<T, Boolean> condition)
    {
        PreCondition.assertNotNull(condition, "condition");

        T result = null;

        DoubleLinkNode<T> node = head;
        while (node != null)
        {
            final T value = node.getValue();
            final DoubleLinkNode<T> nextNode = node.getNext();
            if (condition.run(value))
            {
                result = value;

                final DoubleLinkNode<T> previousNode = node.getPrevious();

                if (previousNode == null)
                {
                    head = nextNode;
                }
                else
                {
                    previousNode.setNext(nextNode);
                }

                if (nextNode == null)
                {
                    tail = previousNode;
                }
                else
                {
                    nextNode.setPrevious(previousNode);
                }

                node.setPrevious(null);
                node.setNext(null);
                node.setValue(null);
                break;
            }
            else
            {
                node = node.getNext();
            }
        }

        return result;
    }

    @Override
    public DoubleLinkList<T> clear()
    {
        DoubleLinkNode<T> node = head;
        while (node != null)
        {
            final DoubleLinkNode<T> nextNode = node.getNext();
            node.setPrevious(null);
            node.setNext(null);
            node.setValue(null);
            node = nextNode;
        }

        head = null;
        tail = null;

        return this;
    }

    @Override
    public T get(int index)
    {
        PreCondition.assertIndexAccess(index, getCount());

        return getNode(index).getValue();
    }

    @Override
    public Iterator<T> iterate()
    {
        return head == null ? new EmptyIterator<T>() : head.iterate();
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
