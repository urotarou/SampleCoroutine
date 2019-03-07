package lab.uro.kitori.samplecoroutine.sample

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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

    // 並行処理 その1
    suspend fun sample7() {
        CoroutineScope(coroutineContext + Dispatchers.Default).launch {
            runCatching {
                Timber.d("!!! sample 7: thread= ${Thread.currentThread().name}")

                val deferred1 = async {
                    Timber.d("!!! async 7-1 start")
                    delay(10_000)
                    Timber.d("!!! async 7-1 end")
                    1
                }
                val deferred2 = async {
                    Timber.d("!!! async 7-2 start")
                    delay(1_000)
                    Timber.d("!!! async 7-2 end")
                    2
                }
                val deferred3 = async {
                    Timber.d("!!! async 7-3 start")
                    delay(5_000)
                    Timber.d("!!! async 7-3 end")
                    4
                }
                delay(3_000)
                Timber.d("!!! sample 7 wait...")
                deferred1.await() + deferred2.await() + deferred3.await()
            }.fold({
                Timber.d("!!! sample 7 end: $it")
            }, {
                Timber.d("!!! sample 7 cancel: $it")
            })
        }
    }

    // 並行処理...できてない、これは直列 その2
    suspend fun sample8() {
        CoroutineScope(coroutineContext + Dispatchers.Default).launch {
            runCatching {
                Timber.d("!!! sample 8: thread= ${Thread.currentThread().name}")

                Timber.d("!!! sample 8 wait...")
                val value1 = async {
                    Timber.d("!!! async 8-1 start")
                    delay(10_000)
                    Timber.d("!!! async 8-1 end")
                    1
                }.await()
                val value2 = async {
                    Timber.d("!!! async 8-2 start")
                    delay(1_000)
                    Timber.d("!!! async 8-2 end")
                    2
                }.await()
                val value3 = async {
                    Timber.d("!!! async 8-3 start")
                    delay(5_000)
                    Timber.d("!!! async 8-3 end")
                    4
                }.await()
                delay(3_000)
                value1 + value2 + value3
            }.fold({
                Timber.d("!!! sample 8 end: $it")
            }, {
                Timber.d("!!! sample 8 cancel: $it")
            })
        }
    }
}
