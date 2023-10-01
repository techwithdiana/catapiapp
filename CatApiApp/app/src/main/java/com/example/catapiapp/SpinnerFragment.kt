package com.example.catapiapp

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.catapiapp.databinding.FragmentSpinnerBinding
import org.json.JSONArray
import org.json.JSONObject


class SpinnerFragment : Fragment() {

    private lateinit var binding : FragmentSpinnerBinding
    lateinit var dataAdapter: ArrayAdapter<String>
    var activityCallback : SpinnerFragment.SpinnerListener? = null
    private val viewModel: MainActivity.CatViewModel by activityViewModels()
    var catsArray = JSONArray()
    var catsNames = ArrayList<String>()
    interface SpinnerListener{
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            activityCallback = context as SpinnerListener
        }
        catch (e : ClassCastException){
            throw java.lang.ClassCastException(context.toString() + "must implement SpinnerListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSpinnerBinding.inflate(inflater, container, false)

        //Set the drop down items to be populated by the different breeds returned from the API
        dataAdapter = ArrayAdapter<String>(this.requireContext(), R.layout.simple_spinner_item, getCatBreedsArray())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.breedSpinner.adapter = dataAdapter
        binding.breedSpinner.onItemSelectedListener= object:OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.selectItem(catsArray.getJSONObject(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.selectItem(JSONObject())
            }

        }
        Log.i("Spinner","Create")

        return binding.root
    }


    // method to interact with API and return a JSON array
    private fun getCatBreeds() :JSONArray {
        var catUrl = "https://api.thecatapi.com/v1/breeds"

        val queue = Volley.newRequestQueue(this.context)


        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, catUrl,
            { response ->
                catsArray = JSONArray(response)
                //indices from 0 through catsArray.length()-1
                for(i in 0 until catsArray.length()) {
                    //${} is to interpolate the string /
                    // uses a string template
                    var theCat : JSONObject = catsArray.getJSONObject(i)
                    val name = theCat.getString("name")
                    catsNames.add(name)
                    //now get the properties we want:  name and description
//                    Log.i("MainActivity", "Cat name: ${theCat.getString("name")}")
//                    Log.i("MainActivity", "Cat description: ${theCat.getString("description")}")
                }//end for
                dataAdapter.clear()
                dataAdapter.addAll(catsNames)
                dataAdapter.notifyDataSetChanged()
            },
            {
//                Log.i("MainActivity", "That didn't work!")
            })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
        Log.i("Cats",catsArray.toString())
        return catsArray
    }//end printCatData

    //Method to convert the JSON array from the API to an ArrayList
    private fun getCatBreedsArray() : ArrayList<String>{
        val catsJSONArray : JSONArray = getCatBreeds()
        val catBreedsArray: ArrayList<String> = ArrayList()
        for (i in 0 until catsJSONArray.length()) {
            val actor = catsJSONArray.getJSONObject(i)
            val name = actor.getString("name")
            catBreedsArray.add(name)
        }
        return catBreedsArray
    }//end of getCatBreedsArray


}