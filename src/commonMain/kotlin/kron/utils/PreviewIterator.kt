package kron.utils


/**
 * 可以预览下一个元素的 [Iterator]
 * @author ForteScarlet
 */

class PreviewIterator<T>(private val delegate: Iterator<T>) : Iterator<T> {
    @Suppress("MemberVisibilityCanBePrivate")
    var preNext: T? = null
    private set

    init {
        nextView()
    }

    private fun nextView() {
        preNext = if (delegate.hasNext()) delegate.next() else null
    }

    override fun hasNext(): Boolean {
        return preNext != null
    }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("No more element.")
        }
        return preNext!!.also {
            nextView()
        }
    }
}


fun <T> Iterator<T>.asPreview(): PreviewIterator<T> = PreviewIterator(this)
