package com.example.userapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userapp.model.UserData
import com.example.userapp.util.SingleLiveEvent
import com.example.userapp.util.SortOption
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _messageChannel = SingleLiveEvent<String>()
    val messageChannel: LiveData<String> get() = _messageChannel

    var openBottomSheet=MutableLiveData<Boolean>()

    private val _userData: MutableLiveData<RealmResults<UserData>> = MutableLiveData()
    val userData: LiveData<RealmResults<UserData>> get() = _userData

    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }


    init {
        getUserData()
    }


    private fun getUserData() {
        _userData.value = realm.where(UserData::class.java).findAll()
    }

    fun addUserData(data: UserData) {
        val newData = UserData().apply {
            name = data.name
            age = data.age
            city = data.city
        }
        viewModelScope.launch {
            try {
                realm.executeTransaction { realm ->
                    realm.insertOrUpdate(newData)
                }
                _messageChannel.value = "User data added successfully"
                getUserData()
            } catch (e: Exception) {
                _messageChannel.value = "Failed to add data to the database"
            }
        }
    }

    fun filterAndSortData(filterText: String?, sortBy: SortOption) {
        realm.executeTransaction { realm ->
            val query = realm.where(UserData::class.java)

            // Apply filtering if filterText is provided
            if (!filterText.isNullOrEmpty()) {
                query.beginGroup()
                    .contains("name", filterText, Case.INSENSITIVE)
                    .or()
                    .contains("age", filterText, Case.INSENSITIVE)
                    .or()
                    .contains("city", filterText, Case.INSENSITIVE)
                    .endGroup()
            }

            // Apply sorting based on the selected SortOption
            when (sortBy) {
                SortOption.NAME -> query.sort("name", Sort.ASCENDING)
                SortOption.AGE -> query.sort("age", Sort.ASCENDING)
                SortOption.CITY -> query.sort("city", Sort.ASCENDING)
            }

            _userData.value = query.findAll()
        }
    }

    fun openAddUserBottomSheet() {
        openBottomSheet.value =true
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }
}
