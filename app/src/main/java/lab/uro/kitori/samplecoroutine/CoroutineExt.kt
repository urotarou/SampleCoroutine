package lab.uro.kitori.samplecoroutine

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

private class CoroutineHandlers {
    lateinit var cancelBlock: (scope: CoroutineScope, e: CancellationException) -> Unit
    lateinit var errorBlock: (scope: CoroutineScope, e: Exception) -> Unit
    lateinit var cleanUpBlock: (scope: CoroutineScope) -> Unit

    fun isCancelBlockInitialized() = ::cancelBlock.isInitialized
    fun isErrorBlockInitialized() = ::errorBlock.isInitialized
    fun isCleanUpBlockInitialized() = ::cleanUpBlock.isInitialized
}

private val coroutineHandlersArray = arrayListOf<CoroutineHandlers>()

fun launchExt(block: suspend (coroutineScope: CoroutineScope) -> Unit): Job {
    val handleBlocks = CoroutineHandlers()
    coroutineHandlersArray.add(handleBlocks)

    return launch(UI) {
        try {
            block(this)
        } catch (e: CancellationException) {
            if (handleBlocks.isCancelBlockInitialized()) handleBlocks.cancelBlock(this, e)
        } catch (e: Exception) {
            if (handleBlocks.isErrorBlockInitialized()) handleBlocks.errorBlock(this, e)
        } finally {
            coroutineHandlersArray.remove(handleBlocks)
            if (handleBlocks.isCleanUpBlockInitialized()) handleBlocks.cleanUpBlock(this)
        }
    }
}

infix fun Job.cancel(block: (scope: CoroutineScope, e: CancellationException) -> Unit): Job {
    val position = coroutineHandlersArray.lastIndex
    if (position >= 0) coroutineHandlersArray[position].cancelBlock = block
    return this
}

infix fun Job.error(block: (scope: CoroutineScope, e: Exception) -> Unit): Job {
    val position = coroutineHandlersArray.lastIndex
    if (position >= 0) coroutineHandlersArray[position].errorBlock = block
    return this
}

infix fun Job.cleanUp(block: (scope: CoroutineScope) -> Unit): Job {
    val position = coroutineHandlersArray.lastIndex
    if (position >= 0) coroutineHandlersArray[position].cleanUpBlock = block
    return this
}
