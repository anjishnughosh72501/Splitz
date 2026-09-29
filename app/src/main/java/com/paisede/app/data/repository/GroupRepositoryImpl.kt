package com.paisede.app.data.repository

import com.paisede.app.data.db.AppDatabase
import com.paisede.app.data.db.entities.GroupEntity
import com.paisede.app.data.db.entities.MemberEntity
import com.paisede.app.domain.model.Group
import com.paisede.app.domain.model.Member
import com.paisede.app.domain.repository.GroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class GroupRepositoryImpl(
    private val database: AppDatabase
) : GroupRepository {

    private val groupDao = database.groupDao()
    private val memberDao = database.memberDao()

    override fun getAllGroups(): Flow<List<Group>> {
        return groupDao.getAllGroups().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getGroupById(groupId: String): Group? = withContext(Dispatchers.IO) {
        groupDao.getGroupById(groupId)?.toDomain()
    }

    override suspend fun createGroup(name: String, memberNames: List<String>): Group = withContext(Dispatchers.IO) {
        val groupId = UUID.randomUUID().toString()
        val createdAt = System.currentTimeMillis()
        val groupEntity = GroupEntity(
            id = groupId,
            name = name.trim(),
            createdAt = createdAt
        )

        val members = memberNames.map { memberName ->
            MemberEntity(
                id = UUID.randomUUID().toString(),
                groupId = groupId,
                name = memberName.trim(),
                createdAt = createdAt
            )
        }

        database.runInTransaction {
            kotlinx.coroutines.runBlocking {
                groupDao.insertGroup(groupEntity)
                memberDao.insertMembers(members)
            }
        }

        groupEntity.toDomain()
    }

    override suspend fun deleteGroup(groupId: String) = withContext(Dispatchers.IO) {
        val group = groupDao.getGroupById(groupId)
        if (group != null) {
            groupDao.deleteGroup(group)
        }
    }

    override fun getMembers(groupId: String): Flow<List<Member>> {
        return memberDao.getMembersForGroup(groupId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMembersSync(groupId: String): List<Member> = withContext(Dispatchers.IO) {
        memberDao.getMembersForGroupSync(groupId).map { it.toDomain() }
    }

    override suspend fun addMember(groupId: String, name: String): Member = withContext(Dispatchers.IO) {
        val member = MemberEntity(
            id = UUID.randomUUID().toString(),
            groupId = groupId,
            name = name.trim(),
            createdAt = System.currentTimeMillis()
        )
        memberDao.insertMember(member)
        member.toDomain()
    }

    private fun GroupEntity.toDomain(): Group = Group(
        id = id,
        name = name,
        createdAt = createdAt,
        currencyCode = currencyCode
    )

    private fun MemberEntity.toDomain(): Member = Member(
        id = id,
        groupId = groupId,
        name = name,
        createdAt = createdAt
    )
}
