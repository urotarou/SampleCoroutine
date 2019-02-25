package lab.uro.kitori.samplecoroutine.sample

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * GlobalScopeで処理する
 */
class Sample3Class {
    suspend fun sample() {
        GlobalScope.launch {
            try {
                Timber.d("!!! sample 3 start")
                delay(10_000)
                Timber.d("!!! sample 3 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 3 cancel: $exception")
            }
        }
    }
}