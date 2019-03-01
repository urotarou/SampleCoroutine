package lab.uro.kitori.samplecoroutine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import lab.uro.kitori.samplecoroutine.sample.Sample
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainViewModel(
        app: Application
) : AndroidViewModel(app), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    fun start() {
        CoroutineScope(coroutineContext).launch {
            try {
                Timber.d("!!! start... thread= ${Thread.currentThread().name}")

                val sample = Sample()
                sample.sample1()
                sample.sample2()
                sample.sample3()
                sample.sample4()
                sample.sample5()
//                sample.sample6() // !!! コメント解除するとここでブロックされる !!!

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
