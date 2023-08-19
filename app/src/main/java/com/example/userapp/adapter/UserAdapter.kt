package com.example.userapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.databinding.ItemUserBinding
import com.example.userapp.model.UserData

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<UserData>()
  //  private val filteredUsers = mutableListOf<UserData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    fun submitData(data: List<UserData>) {
        users.clear()
        users.addAll(data)
        notifyDataSetChanged()
    }

//    fun filterData(filterText: String) {
//        filteredUsers.clear()
//        filteredUsers.addAll(users.filter {
//            it.name.contains(filterText, ignoreCase = true) ||
//                    it.age.contains(filterText, ignoreCase = true) ||
//                    it.city.contains(filterText, ignoreCase = true)
//        })
//        notifyDataSetChanged()
//    }


    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: UserData) {
            binding.tvUser.text = data.name
            binding.tvAge.text = data.age
            binding.tvCity.text = data.city
        }
    }
}
