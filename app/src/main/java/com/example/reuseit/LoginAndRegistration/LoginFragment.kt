package com.example.reuseit.LoginAndRegistration

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.Application.Models.UserModel
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit
import com.example.reuseit.Utils.ValidationUtils


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
    private lateinit var checkbox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

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

        val container = view.findViewById<View>(R.id.loginFragment)
        container.visibility = View.INVISIBLE


        sharedPreferences = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        viewLifecycleOwner.lifecycleScope.launch {
            val shouldNavigate = loginUserIfCheckWasEnabled()
            if (!shouldNavigate) {
                container.visibility = View.VISIBLE
            }
        }


        emailEditText = view.findViewById(R.id.LoginEmail)
        passwordEditText = view.findViewById(R.id.LoginPassword)
        loginButton = view.findViewById(R.id.doLogin)
        registerButton = view.findViewById(R.id.goToR1)
        errorTextView = view.findViewById(R.id.loginErrorText)
        checkbox = view.findViewById(R.id.keepSignedInCheckBox)


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
                        password = ValidationUtils.hashPassword(password)
                    )
                }

                if (userId != null) {
                    val userModel: UserModel = withContext(Dispatchers.IO) {
                        DatabaseInstance.Access.userDAO().GetUserById(userId)
                            ?.toUserModel() as UserModel
                    }

                    CurrentUser.Data.UserID = userModel.UserID
                    CurrentUser.Data.Email = userModel.Email
                    CurrentUser.Data.Password = userModel.Password
                    CurrentUser.Data.FirstName = userModel.FirstName
                    CurrentUser.Data.LastName = userModel.LastName
                    keepUserLogged()

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
        //Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun keepUserLogged(){
        if (checkbox.isChecked)
        {
            sharedPreferences.edit() {
                putLong("last_login_time", System.currentTimeMillis())
                putLong("user_id", CurrentUser.Data.UserID)
            }
        }
    }

    private suspend fun loginUserIfCheckWasEnabled(): Boolean {
        val savedTime = sharedPreferences.getLong("last_login_time", 0L)
        if ((System.currentTimeMillis() - savedTime) < 5 * 60 * 1000) {
            val userEntity: UserModel? =
                DatabaseInstance.Access.userDAO().GetUserById(sharedPreferences.getLong("user_id", -404L))?.toUserModel()

            if (userEntity != null) {
                CurrentUser.Data.UserID = userEntity.UserID
                CurrentUser.Data.Email = userEntity.Email
                CurrentUser.Data.Password = userEntity.Password
                CurrentUser.Data.FirstName = userEntity.FirstName
                CurrentUser.Data.LastName = userEntity.LastName

                findNavController().setGraph(R.navigation.application_graph)
                return true
            }
        }
        return false
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


