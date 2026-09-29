package com.paisede.app.data.repository

import com.paisede.app.data.db.AppDatabase
import com.paisede.app.data.db.entities.SettlementEntity
import com.paisede.app.domain.model.SettlementTransaction
import com.paisede.app.domain.repository.SettlementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class SettlementRepositoryImpl(
    private val database: AppDatabase
) : SettlementRepository {

    private val settlementDao = database.settlementDao()

    override fun getSettlements(groupId: String): Flow<List<SettlementTransaction>> {
        return settlementDao.getSettlementsForGroup(groupId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getSettlementsSync(groupId: String): List<SettlementTransaction> =
        withContext(Dispatchers.IO) {
            settlementDao.getSettlementsForGroupSync(groupId).map { it.toDomain() }
        }

    override suspend fun recordSettlement(
        groupId: String,
        fromUserId: String,
        toUserId: String,
        amountPaise: Long
    ) = withContext(Dispatchers.IO) {
        require(amountPaise > 0L) { "Settlement amount must be positive" }
        require(fromUserId != toUserId) { "Cannot settle with oneself" }

        val entity = SettlementEntity(
            id = UUID.randomUUID().toString(),
            groupId = groupId,
            fromUserId = fromUserId,
            toUserId = toUserId,
            amountPaise = amountPaise,
            createdAt = System.currentTimeMillis()
        )
        settlementDao.insertSettlement(entity)
    }

    private fun SettlementEntity.toDomain(): SettlementTransaction = SettlementTransaction(
        fromUserId = fromUserId,
        toUserId = toUserId,
        amountPaise = amountPaise
    )
}
