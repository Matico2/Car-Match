package com.example.carmatch.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carmatch.view.fragments.FavVehiclesFragment
import com.example.carmatch.view.fragments.AdVehiclesFragment
import com.example.carmatch.view.fragments.UserChatFragment

class ViewPagerAdapter(
    
    private val  tabs: List<String>,
    fragmentManeger:FragmentManager,
    lifecicle: Lifecycle
): FragmentStateAdapter (fragmentManeger, lifecicle){
    
    // retorna a quantidade de abas na activity
    override fun getItemCount(): Int {
         return tabs.size
    }
    
    //cria a aba selecionada
    override fun createFragment(position: Int): Fragment {
        when(position){
            1 -> return FavVehiclesFragment()
            2 -> return UserChatFragment()
    }
        return AdVehiclesFragment()
   }
}
