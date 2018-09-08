package lab.uro.kitori.samplecoroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.android.UI
import lab.uro.kitori.samplecoroutine.databinding.ActivityMainBinding
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    companion object {
        private const val COUNT_DOWN_INIT_VAL = 3
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.cancel.setOnClickListener {
            onClickCancel()
        }
        binding.start.setOnClickListener {
            onClickStart()
        }

        startCoroutine()
    }

    private fun startCoroutine() {
        setText("")

        if (::job.isInitialized && job.isActive) job.cancel()

        job = launch(UI) {
            try {
                var count = COUNT_DOWN_INIT_VAL
                while (true) {
                    setText(count.toString())
                    count -= countDown().await()

                    if (count == 0) break
                }
                setText("Hello Coroutine!!")
            } catch (e: CancellationException) {
                setText("cancel...")
            } catch (e: Exception) {
                setText("error...")
            }
        }
    }

    private suspend fun countDown() = async(coroutineContext) {
        delay(1000L)
        1
    }

    private fun onClickCancel() {
        if (job.isActive) job.cancel()
    }

    private fun onClickStart() {
        startCoroutine()
    }

    private fun setText(text: String) {
        binding.result.text = text
    }
}
