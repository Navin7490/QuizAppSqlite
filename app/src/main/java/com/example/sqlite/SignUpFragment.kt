package com.example.sqlite

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sqlite.databinding.FragmentSignUpBinding
import com.example.sqlite.model.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SignUpFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentSignUpBinding
    private lateinit var dbHelper:SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        dbHelper = SQLiteHelper(requireContext())


        binding.btnCreate.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_FIRST_NAME,binding.etFirstName.text.toString().trim())
                put(COLUMN_LAST_NAME,binding.etLastName.text.toString().trim())
                put(COLUMN_MOBILE,binding.etMobileNumber.text.toString().trim())
                put(COLUMN_EMAIL,binding.etEmail.text.toString().trim())
                put(Column_PASSWORD,binding.etPassword.text.toString().trim())
            }

            if (checkValidation()){
                db.insert(TABLE_NAME,null,values)
                Toast.makeText(requireContext(),"Data inserted",Toast.LENGTH_SHORT).show()

            }

        }
        return binding.root
    }

    private fun checkValidation():Boolean{
        var flag = true
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val mobileNumber = binding.etMobileNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password =binding.etPassword.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || mobileNumber.isEmpty()  || email.isEmpty() || password.isEmpty()){
            flag = false
            Toast.makeText(requireContext(),"All Field Required",Toast.LENGTH_SHORT).show()
        }else{
            flag = true
        }

        return  flag
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}