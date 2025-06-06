package com.example.reuseit.LoginAndRegistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import com.google.android.material.snackbar.Snackbar
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

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var errorTextView: TextView

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

        emailEditText = view.findViewById(R.id.LoginEmail)
        passwordEditText = view.findViewById(R.id.LoginPassword)
        loginButton = view.findViewById(R.id.doLogin)
        registerButton = view.findViewById(R.id.goToR1)
        errorTextView = view.findViewById(R.id.loginErrorText)

        setupLoginButton()
        setupRegisterButton()
        setupBackButton()
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            errorTextView.text = ""
            errorTextView.visibility = View.GONE

            when {
                email.isEmpty() -> {
                    showError("Email cannot be empty")
                    emailEditText.requestFocus()
                }
                !CurrentUser.Data.validateEmail() -> {
                    showError("Please enter a valid email address")
                    emailEditText.requestFocus()
                }
                password.isEmpty() -> {
                    showError("Password cannot be empty")
                    passwordEditText.requestFocus()
                }
                else -> {
                    attemptLogin(email, password)
                }
            }
        }
    }

    private fun setupRegisterButton() {
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val action = LoginFragmentDirections.actionLoginFragmentToRegister1Fragment(email, password)
            view?.findNavController()?.navigate(action)
        }
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun attemptLogin(email: String, password: String) {
        loginButton.isEnabled = false
        errorTextView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val userId = withContext(Dispatchers.IO) {
                    DatabaseInstance.Access.userDAO().GetUserID(
                        email = email,
                        password = com.example.reuseit.Application.Utils.ValidationUtils.hashPassword(password)
                    )
                }

                if (userId != null) {
                    CurrentUser.Data.UserID = userId
                    CurrentUser.Data.Email = email
                    findNavController().setGraph(R.navigation.application_graph)
                } else {
                    showError("Invalid email or password")
                    loginButton.isEnabled = true
                }
            } catch (e: Exception) {
                showError("An error occurred. Please try again.")
                loginButton.isEnabled = true
            }
        }
    }

    private fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
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