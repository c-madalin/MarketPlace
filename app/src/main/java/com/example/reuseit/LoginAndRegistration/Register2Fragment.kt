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
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register2Fragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.doRegister).setOnClickListener {
            val firstName = view.findViewById<EditText>(R.id.Reg2FirstName).text.toString()
            val lastName = view.findViewById<EditText>(R.id.Reg2LastName).text.toString()

            if (firstName != "" && lastName != "") {
                CurrentUser.Data.FirstName = firstName
                CurrentUser.Data.LastName = lastName

                viewLifecycleOwner.lifecycleScope.launch {
                    val userExists = withContext(Dispatchers.IO) {
                        DatabaseInstance.Access.userDAO().CheckIfUserExists(CurrentUser.Data.Email) != 0
                    }

                    if (userExists) {
                        Log.d("Database", "User already exists")
                    } else {
                        withContext(Dispatchers.IO) {
                            DatabaseInstance.Access.userDAO().InsertUser(CurrentUser.Data.toUserEntity())
                            Log.d("Database", "User inserted")
                        }
                        findNavController().setGraph(R.navigation.application_graph)
                    }
                }
            }
        }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                findNavController().navigate(R.id.action_register2Fragment_to_loginFragment)
            }
        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}