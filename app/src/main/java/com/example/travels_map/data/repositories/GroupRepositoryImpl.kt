package com.example.travels_map.data.repositories

import android.content.Context
import com.example.travels_map.R
import com.example.travels_map.data.mappers.IEntityMapper
import com.example.travels_map.domain.entities.Group
import com.example.travels_map.domain.entities.User
import com.example.travels_map.domain.repositories.IGroupRepository
import com.example.travels_map.domain.repositories.IUserRepository
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.coroutines.getById
import com.parse.coroutines.suspendFind
import com.parse.coroutines.suspendSave
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val context: Context,
    private val userRepository: IUserRepository,
    private val parseObjectToGroupMapper: IEntityMapper<ParseObject, Group>,
    private val parseObjectToUserMapper: IEntityMapper<ParseObject, User>,
) : IGroupRepository {

    private val _groupFlow = MutableSharedFlow<Result<Group?>>(1, 1, BufferOverflow.DROP_OLDEST)

    override val groupFlow = _groupFlow.asSharedFlow()

    private val _participantsLocationFlow = MutableSharedFlow<Result<List<User>>>(0, 1, BufferOverflow.DROP_OLDEST)

    override val participantsLocationFlow: SharedFlow<Result<List<User>>> = _participantsLocationFlow.asSharedFlow()

    override suspend fun load() {
        val result = runCatching {
            val currentUser = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            val user = with(ParseQuery<ParseUser>(User.CLASS_NAME)) {
                whereEqualTo(User.KEY_OBJECT_ID, currentUser.objectId)
                return@with first
            }

            val selectedGroupId = user.getString(User.KEY_SELECTED_GROUP_ID) ?: throw Exception()

            val group = ParseQuery<ParseObject>(Group.CLASS_NAME).getById(selectedGroupId)

            return@runCatching parseObjectToGroupMapper.mapEntity(group)
        }

        _groupFlow.emit(result)
    }

    override suspend fun loadAll(): Result<List<Group>> {
        return runCatching {
            val currentUser = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            val user = with(ParseQuery<ParseUser>(User.CLASS_NAME)) {
                whereEqualTo(User.KEY_OBJECT_ID, currentUser.objectId)
                return@with first
            }

            val groupQuery = ParseQuery<ParseObject>(Group.CLASS_NAME).apply {
                whereEqualTo(Group.KEY_PARTICIPANTS, user)

                user.getString(User.KEY_SELECTED_GROUP_ID)?.let { selectedGroupId ->
                    whereNotEqualTo(Group.KEY_OBJECT_ID, selectedGroupId)
                }
            }

            return@runCatching groupQuery.suspendFind().map { group ->
                parseObjectToGroupMapper.mapEntity(group)
            }
        }
    }

    override suspend fun select(group: Group) {
        val result = runCatching {
            val currentUser = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            currentUser.put(User.KEY_SELECTED_GROUP_ID, group.id)
            currentUser.suspendSave()

            requestParticipantsLocation()

            return@runCatching group
        }

        _groupFlow.emit(result)
    }

    override suspend fun join(query: String) {
        val result = runCatching {
            val currentUser = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            val user = with(ParseQuery<ParseUser>(User.CLASS_NAME)) {
                whereEqualTo(User.KEY_OBJECT_ID, currentUser.objectId)
                return@with first
            }

            val splitQuery = query.split('#')

            val groupQuery = ParseQuery<ParseObject>(Group.CLASS_NAME).apply {
                whereEqualTo(Group.KEY_NAME, splitQuery[0])
                whereEqualTo(Group.KEY_OBJECT_ID, splitQuery[1])
                whereNotEqualTo(Group.KEY_PARTICIPANTS, user)
            }

            val groupList = groupQuery.suspendFind()

            if (groupList.isEmpty()) throw Exception()

            val group = groupList.first().apply {
                getRelation<ParseUser>(Group.KEY_PARTICIPANTS).add(user)
                suspendSave()
            }

            user.put(User.KEY_SELECTED_GROUP_ID, group.objectId)
            user.suspendSave()

            requestParticipantsLocation()

            return@runCatching parseObjectToGroupMapper.mapEntity(group)
        }

        _groupFlow.emit(result)
    }

    override suspend fun leave() {
        runCatching {
            val currentUser = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            val user = with(ParseQuery<ParseUser>(User.CLASS_NAME)) {
                whereEqualTo(User.KEY_OBJECT_ID, currentUser.objectId)
                return@with first
            }

            val selectedGroupId = user.getString(User.KEY_SELECTED_GROUP_ID) ?: throw Exception()

            val group = with(ParseQuery<ParseObject>(Group.CLASS_NAME)) {
                whereEqualTo(Group.KEY_OBJECT_ID, selectedGroupId)
                return@with first
            }

            group.getRelation<ParseUser>(Group.KEY_PARTICIPANTS).remove(user)
            group.suspendSave()

            user.put(User.KEY_SELECTED_GROUP_ID, String())
            user.suspendSave()

            requestParticipantsLocation()

            _groupFlow.emit(Result.success(null))
        }
    }

    override suspend fun loadKey(): Result<String> {
        return runCatching {
            return@runCatching groupFlow.first().getOrThrow().let { group ->
                context.getString(R.string.add_participant_group_key, group?.name, group?.id)
            }
        }
    }

    override suspend fun create(name: String) {
        runCatching {
            val user = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            val group = ParseObject(Group.CLASS_NAME).apply {
                put(Group.KEY_NAME, name)
                getRelation<ParseUser>(Group.KEY_PARTICIPANTS).add(user)
                suspendSave()
            }

            _groupFlow.emit(Result.success(parseObjectToGroupMapper.mapEntity(group)))

            user.put(User.KEY_SELECTED_GROUP_ID, group.objectId)
            user.suspendSave()

            requestParticipantsLocation()
        }
    }

    override suspend fun requestParticipantsLocation() {
        val result = runCatching {
            val currentUser = userRepository.getCurrentParseUserSafely() ?: throw Exception()

            val user = with(ParseQuery<ParseUser>(User.CLASS_NAME)) {
                whereEqualTo(User.KEY_OBJECT_ID, currentUser.objectId)
                return@with first
            }

            val selectedGroupId = user.getString(User.KEY_SELECTED_GROUP_ID) ?: throw Exception()

            val group = ParseQuery<ParseObject>(Group.CLASS_NAME).getById(selectedGroupId)

            val participantList = with(ParseQuery<ParseObject>(Group.CLASS_NAME).getById(group.objectId)) {
                return@with getRelation<ParseUser>(Group.KEY_PARTICIPANTS).query.suspendFind()
            }

            return@runCatching participantList.map { parseObjectToUserMapper.mapEntity(it) }
        }

        _participantsLocationFlow.emit(result)
    }
}