package lab.uro.kitori.samplecoroutine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lab.uro.kitori.samplecoroutine.sample.Sample1Class
import lab.uro.kitori.samplecoroutine.sample.Sample2Class
import lab.uro.kitori.samplecoroutine.sample.Sample3Class
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

// 実際に使うときはこんな感じと思われ
class MainViewModel(
        app: Application
) : AndroidViewModel(app), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Default + job
    private val coroutineScope = CoroutineScope(coroutineContext)

    fun start() {
        coroutineScope.launch {
            try {
                Timber.d("!!! start...")
                val sample1 = Sample1Class(coroutineScope)
                val sample2 = Sample2Class()
                val sample3 = Sample3Class()

                sample1.sample()
                sample2.sample()
                sample3.sample()

                Timber.d("!!! ...wait")
                delay(10_000)
                Timber.d("!!! ...end")
            } catch (exception: CancellationException) {
                Timber.d("!!! cancel parent: $exception")
            }
        }
    }

    fun cancel() {
        job.cancel()
    }

    fun cancelChildren() {
        job.cancelChildren()
    }
}
