package com.example.sqlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.sqlite.databinding.FragmentStartQuizBinding


class StartQuizFragment : Fragment() {
     lateinit var binding:FragmentStartQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartQuizBinding.inflate(inflater,container,false)
        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.quizFragment)
        }
        return binding.root
    }

}