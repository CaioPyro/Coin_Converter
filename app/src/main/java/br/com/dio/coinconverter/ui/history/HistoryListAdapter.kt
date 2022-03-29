package br.com.dio.coinconverter.ui.history

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.coinconverter.core.extensions.formatCurrency
import br.com.dio.coinconverter.data.database.AppDatabase
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import br.com.dio.coinconverter.data.repository.conversionRepository
import br.com.dio.coinconverter.databinding.ItemHistoryBinding
import br.com.dio.coinconverter.presentation.HistoryViewModel
import br.com.dio.coinconverter.ui.main.MainActivity
import kotlinx.coroutines.currentCoroutineContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeId
import kotlin.coroutines.coroutineContext
import android.content.Intent as Intent1

class HistoryListAdapter : ListAdapter<ExchangeResponseValue, HistoryListAdapter.ViewHolder>(DiffCallback()) {

    lateinit var btnDelete : ImageButton
    private lateinit var view: HistoryViewModel
    private lateinit var bind: ItemHistoryBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        bind = binding
        btnDelete = binding.tvDelete
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        bindListeners(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExchangeResponseValue) {
            binding.tvName.text = item.name

            val coinIn = Coin.getByName(item.codein)
            binding.tvValue.text = item.bid.formatCurrency(coinIn.locale)

            binding.tvDate.text = item.date

            val coin = Coin.getByName(item.code)
            binding.tvEntry.text = item.entry.formatCurrency(coin.locale)
        }
    }

    private fun bindListeners(binding: ExchangeResponseValue){
        bind.tvDelete.setOnClickListener{
            var builder = AlertDialog.Builder(it.context)
            builder.setPositiveButton("Sim") {_, _ ->
                view.deleteConversion(binding)
            }
            builder.setNegativeButton("Cancelar") {_, _ ->}
            builder.setTitle("Excluir?")
            builder.setMessage("Tem certeza que deseja excluir essa conversão?")
            builder.create().show()
        }
    }

    fun setupViewModel(vm: HistoryViewModel){
        view = vm
    }
}

class DiffCallback : DiffUtil.ItemCallback<ExchangeResponseValue>() {
    override fun areItemsTheSame(oldItem: ExchangeResponseValue, newItem: ExchangeResponseValue) = oldItem == newItem
    override fun areContentsTheSame(oldItem: ExchangeResponseValue, newItem: ExchangeResponseValue) = oldItem.id == newItem.id
}