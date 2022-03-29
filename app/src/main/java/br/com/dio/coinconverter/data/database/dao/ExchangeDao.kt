package br.com.dio.coinconverter.data.database.dao

import android.view.MenuItem
import androidx.room.*
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import kotlinx.coroutines.flow.Flow
import okhttp3.internal.connection.Exchange
import retrofit2.http.DELETE

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM tb_exchange")
    fun findAll(): Flow<List<ExchangeResponseValue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: ExchangeResponseValue)

    /*@Query("DELETE FROM tb_exchange WHERE id = :id")
    fun delete(id: Long)*/

    @Delete
    fun delete(exchange: ExchangeResponseValue)
}