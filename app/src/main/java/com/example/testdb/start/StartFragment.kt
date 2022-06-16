package com.example.testdb.start

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.testdb.R
import com.example.testdb.databinding.FragmentStartBinding
import com.example.testdb.modelsDb.WorkerDb
import com.example.testdb.utils.*
import kotlin.random.Random

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var viewModel: StartFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      AppPreference.setInitUser(false, "", "")
        binding = FragmentStartBinding.inflate(layoutInflater,container,false)
        initViewModel()
        checkIfLoggedIn()
        initListeners()
        return binding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(StartFragmentViewModel::class.java)
    }

    private fun initListeners(){
        setFragmentResultListener(AuthorizationFragment.REQUEST_KEY) { key, bundle ->
            authPrepare()
            viewModel.authorize(TYPE_FIREBASE, {
                findNavController().navigate(R.id.action_about3_to_second)
            },
                {
                    authWrong()
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                })
        }

        setFragmentResultListener(RegistrationFragment.REQUEST_KEY) { key, bundle ->
            authPrepare()
            viewModel.register(
                TYPE_FIREBASE, {
                    findNavController().navigate(R.id.action_about3_to_second)
                },
                {
                    authWrong()
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                })
        }

        binding.developers.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment4_to_developerInfoFragment)
        }

        binding.btnFirebaseReg.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment4_to_registrationFragment)
        }

        binding.btnFirebaseAuth.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment4_to_authorizationFragment)
        }
    }

    private fun authPrepare(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnFirebaseReg.visibility = View.INVISIBLE
        binding.btnFirebaseAuth.visibility = View.INVISIBLE
        binding.developers.visibility = View.INVISIBLE
        binding.titleFbRegistr .visibility = View.INVISIBLE
        val rand = Random.nextInt(0,4)
        binding.cite.text = resources.getStringArray(R.array.cites)[rand]
        binding.author.text = resources.getStringArray(R.array.authors)[rand]
        binding.cite.visibility = View.VISIBLE
        binding.author.visibility = View.VISIBLE
    }

    private fun authWrong(){
        binding.progressBar.visibility = View.GONE
        binding.btnFirebaseReg.visibility = View.VISIBLE
        binding.btnFirebaseAuth.visibility = View.VISIBLE
        binding.developers.visibility = View.VISIBLE
        binding.titleFbRegistr .visibility = View.VISIBLE
        binding.cite.visibility = View.GONE
        binding.author.visibility = View.GONE
    }

    private fun checkIfLoggedIn(){
        if (AppPreference.getInitUser()){
            authPrepare()
            USER = WorkerDb(email = AppPreference.getUserEmail(), password = AppPreference.getUserPass())
            viewModel.authorize(TYPE_FIREBASE, {
                findNavController().navigate(R.id.action_about3_to_second)
            },
                {
                    authWrong()
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                })
        }
    }
}