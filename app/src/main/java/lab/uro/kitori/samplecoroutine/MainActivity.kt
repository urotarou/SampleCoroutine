package lab.uro.kitori.samplecoroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import lab.uro.kitori.samplecoroutine.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.cancelButton.setOnClickListener {
            viewModel.cancel()
            binding.resultTextView.text = "cancel!!"
        }
        binding.cancelChildrenButton.setOnClickListener {
            viewModel.cancelChildren()
            binding.resultTextView.text = "cancelChildren!!"
        }

        viewModel.start()
        binding.resultTextView.text = "start..."
    }
}
