package com.example.reuseit.LoginAndRegistration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.doLogin).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.LoginEmail).text.toString()
            val password = view.findViewById<EditText>(R.id.LoginPassword).text.toString()

            if (email != "" && password != "") {
                CurrentUser.Data.Email = email
                CurrentUser.Data.Password = password

                viewLifecycleOwner.lifecycleScope.launch {
                    val userId = withContext(Dispatchers.IO) {
                        DatabaseInstance.Access.userDAO().GetUserID(
                            email = CurrentUser.Data.Email,
                            password = CurrentUser.Data.hashPassword()
                        )
                    }

                    if (userId != -1) {
                        findNavController().setGraph(R.navigation.application_graph)
                        Log.d("Database", userId.toString() + email.toString() + password.toString())
                    } else {
                        Log.d("Database", userId.toString() + email.toString() + password.toString())
                    }
                }
            }
        }



        view.findViewById<Button>(R.id.goToR1).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.LoginEmail).text.toString()
            val password = view.findViewById<EditText>(R.id.LoginPassword).text.toString()
            val action = LoginFragmentDirections.actionLoginFragmentToRegister1Fragment(email, password)
            view.findNavController().navigate(action)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finish()
        }
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}