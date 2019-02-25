package lab.uro.kitori.samplecoroutine.sample

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 独自にCoroutineScopeを作って処理する
 */
class Sample2Class {
    suspend fun sample() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                Timber.d("!!! sample 2 start")
                delay(10_000)
                Timber.d("!!! sample 2 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 2 cancel: $exception")
            }
        }
    }
}
