package com.example.catapiapp

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.catapiapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SpinnerFragment.SpinnerListener, DisplayFragment.DisplayListener{

    private lateinit var binding: ActivityMainBinding

    class CatViewModel : ViewModel() {
        private val mutableSelectedItem = MutableLiveData<JSONObject>()
        val selectedItem: LiveData<JSONObject> get() = mutableSelectedItem

        fun selectItem(item: JSONObject) {
            mutableSelectedItem.value = item
        }
    }

    private val viewModel: CatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.selectedItem.observe(this, Observer { item->
            Log.i("Seleted",item.toString())
        })
    }
}