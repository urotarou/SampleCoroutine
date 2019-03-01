package lab.uro.kitori.samplecoroutine.sample

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class Sample {
    // 独自にCoroutineScopeを作って処理する
    suspend fun sample1() {
        CoroutineScope(Dispatchers.Default).launch {
            Timber.d("!!! sample 1: thread= ${Thread.currentThread().name}")

            try {
                Timber.d("!!! sample 1 wait...")
                delay(10_000)
                Timber.d("!!! sample 1 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 1 cancel: $exception")
            }
        }
    }

    // 独自にGlobalScopeを使って処理する
    suspend fun sample2() {
        GlobalScope.launch {
            Timber.d("!!! sample 2: thread= ${Thread.currentThread().name}")

            try {
                Timber.d("!!! sample 2 wait...")
                delay(10_000)
                Timber.d("!!! sample 2 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 2 cancel: $exception")
            }
        }
    }

    // 上位のCoroutineContextを利用しつつ動作場所を切り替えて処理する その1
    suspend fun sample3() {
        CoroutineScope(coroutineContext + Dispatchers.Default).launch {
            Timber.d("!!! sample 3: thread= ${Thread.currentThread().name}")

            try {
                Timber.d("!!! sample 3 wait...")
                delay(10_000)
                Timber.d("!!! sample 3 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 3 cancel: $exception")
            }
        }
    }

    // 上位のCoroutineContextを利用しつつ動作場所を切り替えて処理する その2
    suspend fun sample4() {
        GlobalScope.launch(coroutineContext + Dispatchers.IO) {
            Timber.d("!!! sample 4: thread= ${Thread.currentThread().name}")

            try {
                Timber.d("!!! sample 4 wait...")
                delay(10_000)
                Timber.d("!!! sample 4 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 4 cancel: $exception")
            }
        }
    }

    // 上位のCoroutineContextを利用しつつ動作場所を切り替...えようとしたけど切り替わってない
    suspend fun sample5() {
        GlobalScope.launch(Dispatchers.Default + coroutineContext) {
            Timber.d("!!! sample 5: thread= ${Thread.currentThread().name}")

            try {
                Timber.d("!!! sample 5 wait...")
                delay(10_000)
                Timber.d("!!! sample 5 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 5 cancel: $exception")
            }
        }
    }

    // 現在のCoroutineScopeで処理する
    suspend fun sample6() {
        coroutineScope {
            Timber.d("!!! sample 6: thread= ${Thread.currentThread().name}")

            try {
                Timber.d("!!! sample 6 wait...")
                // !!! 親そのものなので親が止まる !!!
                delay(10_000)
                Timber.d("!!! sample 6 end")
            } catch (exception: CancellationException) {
                Timber.d("!!! sample 6 cancel: $exception")
            }
        }
    }
}
