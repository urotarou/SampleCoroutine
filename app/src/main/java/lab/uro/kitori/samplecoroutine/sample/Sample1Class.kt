package lab.uro.kitori.samplecoroutine.sample

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 上位からCoroutineScopeをもらって処理する
 */
class Sample1Class(
        private val coroutineScope: CoroutineScope
) {
    suspend fun sample() {
        coroutineScope.launch {
            try {
                Timber.d("!!! sample 1 start")
                delay(10_000)
                Timber.d("!!! sample 1 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 1 cancel: $exception")
            }
        }
    }
}
