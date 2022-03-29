package br.com.dio.coinconverter.ui.history

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.dio.coinconverter.R
import br.com.dio.coinconverter.core.extensions.createDialog
import br.com.dio.coinconverter.core.extensions.createProgressDialog
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import br.com.dio.coinconverter.databinding.ActivityHistoryBinding
import br.com.dio.coinconverter.databinding.ItemHistoryBinding
import br.com.dio.coinconverter.presentation.HistoryViewModel
import kotlinx.coroutines.NonCancellable.cancel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.E
import br.com.dio.coinconverter.ui.history.HistoryActivity as HistoryHistoryActivity

class HistoryActivity : AppCompatActivity() {

    private val adapter by lazy { HistoryListAdapter() }
    private val dialog by lazy { createProgressDialog() }
    private val viewModel by viewModel<HistoryViewModel>()
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = window
        window.setBackgroundDrawableResource(R.drawable.gradient_toolbar)
        setContentView(binding.root)

        adapter.setupViewModel(viewModel)

        binding.rvHistory.adapter = adapter
        binding.rvHistory.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        )

        bindListeners()
        bindObserve()

        lifecycle.addObserver(viewModel)
    }

    private fun bindObserve() {
        viewModel.state.observe(this) {
            when (it) {
                HistoryViewModel.State.Loading -> dialog.show()
                is HistoryViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.error.message)
                    }.show()
                }
                is HistoryViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(it.list)
                }
            }
        }
    }


    private fun bindListeners(){
        binding.imageButton.setOnClickListener {
            finish()
        }
    }
}