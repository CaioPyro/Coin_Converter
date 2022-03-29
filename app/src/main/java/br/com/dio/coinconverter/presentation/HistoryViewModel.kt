package br.com.dio.coinconverter.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dio.coinconverter.data.database.AppDatabase
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import br.com.dio.coinconverter.data.repository.CoinRepositoryImpl
import br.com.dio.coinconverter.data.repository.conversionRepository
import br.com.dio.coinconverter.domain.DeleteExchangeUseCase
import br.com.dio.coinconverter.domain.ListExchangeUseCase
import br.com.dio.coinconverter.ui.history.HistoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val listExchangeUseCase: ListExchangeUseCase,
    appDatabase: AppDatabase
) : ViewModel(), LifecycleObserver {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state
    private val repository: conversionRepository

    init {
        val conversionDao = appDatabase.exchangeDao()
        repository = conversionRepository(conversionDao)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun getExchanges() {
        viewModelScope.launch {
            listExchangeUseCase()
                .flowOn(Dispatchers.Main)
                .onStart {
                    _state.value = State.Loading
                }
                .catch {
                    _state.value = State.Error(it)
                }
                .collect {
                    _state.value = State.Success(it)
                }
        }
    }

    fun deleteConversion(exchange: ExchangeResponseValue){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteConversion(exchange)
        }
    }

    sealed class State {
        object Loading : State()
        object Deleted : State()

        data class Success(val list: List<ExchangeResponseValue>) : State()
        data class Error(val error: Throwable) : State()
    }
}