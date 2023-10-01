package com.example.catapiapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.catapiapp.databinding.FragmentDisplayBinding
import com.squareup.picasso.Picasso


class DisplayFragment : Fragment() {

    private lateinit var binding : FragmentDisplayBinding
    private val viewModel: MainActivity.CatViewModel by activityViewModels()
    var activityCallback : DisplayListener? = null

    interface DisplayListener{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            activityCallback = context as DisplayListener
        }
        catch (e : ClassCastException){
            throw java.lang.ClassCastException(context.toString() + "must implement DisplayListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDisplayBinding.inflate(inflater, container, false)
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { cat->
            if(cat.has("reference_image_id"))
            {
                val url = "https://cdn2.thecatapi.com/images/"+cat.getString("reference_image_id")+".jpg"
                Picasso.get().load(url).into(binding.catImage)
            }
            if(cat.has("name"))
            {
                binding.catName.text="Name: "+cat.getString("name")
                binding.catWeight.text="Weight: "+cat.getJSONObject("weight").getString("imperial")+" lbs"
                binding.catLifeSpan.text="Life span: "+cat.getString("life_span")+" years"
                binding.catHeight.text="Origin: "+cat.getString("origin")
                binding.catTemperament.text="Temperament: " + cat.getString("temperament")
                binding.catDescription.text="Description: "+cat.getString("description")

            }
        })
        return binding.root
    }

}