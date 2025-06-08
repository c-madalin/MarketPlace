package com.example.reuseit.LoginAndRegistration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import com.example.reuseit.Utils.ValidationUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var nextButton: Button
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
        return inflater.inflate(R.layout.fragment_register1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailLayout = view.findViewById(R.id.emailLayout)
        passwordLayout = view.findViewById(R.id.passwordLayout)
        confirmPasswordLayout = view.findViewById(R.id.confirmPasswordLayout)
        emailEditText = view.findViewById(R.id.Reg1Email)
        passwordEditText = view.findViewById(R.id.Reg1Password)
        confirmPasswordEditText = view.findViewById(R.id.Reg1ConfirmPassword)
        nextButton = view.findViewById(R.id.Reg1Next)
        errorTextView = view.findViewById(R.id.register1ErrorText)

        arguments?.let {
            emailEditText.setText(it.getString("email"))
            passwordEditText.setText(it.getString("password"))
        }

        setupNextButton()
        setupBackButton()
    }

    private fun setupNextButton() {
        nextButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            errorTextView.text = ""
            errorTextView.visibility = View.GONE
            emailLayout.error = null
            passwordLayout.error = null
            confirmPasswordLayout.error = null

            viewLifecycleOwner.lifecycleScope.launch {
                val userExists =
                    DatabaseInstance.Access.userDAO().CheckIfUserExists(email) != null

                when {
                    email.isEmpty() -> {
                        emailLayout.error = "Email cannot be empty"
                        emailEditText.requestFocus()
                    }
                    !ValidationUtils.isValidEmail(email) -> {
                        emailLayout.error = "Please enter a valid email address"
                        emailEditText.requestFocus()
                    }
                    userExists -> {
                        emailLayout.error = "There is already a user with that email address"
                        emailEditText.requestFocus()
                    }
                    password.isEmpty() -> {
                        passwordLayout.error = "Password cannot be empty"
                        passwordEditText.requestFocus()
                    }
                    !ValidationUtils.isValidPassword(password) -> {
                        passwordLayout.error = "Password must be at least 5 characters long and contain at least one number and one letter"
                        passwordEditText.requestFocus()
                    }
                    confirmPassword.isEmpty() -> {
                        confirmPasswordLayout.error = "Please confirm your password"
                        confirmPasswordEditText.requestFocus()
                    }
                    password != confirmPassword -> {
                        confirmPasswordLayout.error = "Passwords do not match"
                        confirmPasswordEditText.requestFocus()
                    }
                    else -> {
                        CurrentUser.Data.Email = email
                        CurrentUser.Data.Password = password
                        val action = Register1FragmentDirections.actionRegister1FragmentToRegister2Fragment(email)
                        view?.findNavController()?.navigate(action)
                    }
                }
            }
        }
    }


    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_register1Fragment_to_loginFragment)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}