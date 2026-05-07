package ca.qc.cegep.g30.snake

import ca.qc.cegep.g30.snake.Snake.DoublyLinkedList.DLNode

class Snake {
    private val snake = DoublyLinkedList()
    var snakeDirection: Direction = Direction.UP

    init {
        snake.initializeSnake()
    }

    private class DoublyLinkedList : Iterable<DLNode> {
        private var head: DLNode? = null
        private var tail: DLNode? = null
        private var length: Int = 0

        data class DLNode(var position: Pair<Int, Int>, var next: DLNode? = null, var prev: DLNode? = null)

        override fun iterator(): Iterator<DLNode> {
            return object : Iterator<DLNode> {
                private var current: DLNode? = head

                override fun hasNext(): Boolean = current != null
                override fun next(): DLNode {
                    val node = current ?: throw NoSuchElementException()
                    current = current?.next
                    return node
                }
            }
        }

        fun initializeSnake() {
            head = DLNode(Pair(20, 20))
            length++
            tail = head
            append(Pair(20, 21))
            append(Pair(20, 22))
        }

        fun size(): Int = length

        fun append(pos: Pair<Int, Int>) {
            val newNode = DLNode(pos)
            tail?.next = newNode
            newNode.prev = tail
            tail = newNode
            length++
        }

        fun prepend(pos: Pair<Int, Int>) {
            val newNode = DLNode(pos)
            newNode.next = head
            head?.prev = newNode
            head = newNode
            length++
        }

        fun cutTail() {
            val newTail = tail?.prev
            newTail?.next = null
            tail = newTail
            length--
        }

        fun getHeadPos(): Pair<Int, Int> = head!!.position
    }

    fun move(dir: Direction) {
        val currentHead = snake.getHeadPos()

        val newHead = when (dir) {
            Direction.UP -> Pair(currentHead.first, currentHead.second - 1)
            Direction.DOWN -> Pair(currentHead.first, currentHead.second + 1)
            Direction.LEFT -> Pair(currentHead.first - 1, currentHead.second)
            Direction.RIGHT -> Pair(currentHead.first + 1, currentHead.second)
        }
        snake.prepend(newHead)
        snake.cutTail()

    }

    fun eats() {
        val currentHead = snake.getHeadPos()
        snake.prepend(currentHead)
    }

    fun getBody(): Iterable<Pair<Int, Int>> = snake.map { it.position }
    fun getHead(): Pair<Int, Int> = snake.getHeadPos()
    fun getTheSnakeDirection(): Direction = snakeDirection
    fun setTheSnakeDirection(direction: Direction) {
        snakeDirection = direction
    }
}


enum class Direction {
    UP, DOWN, LEFT, RIGHT
}