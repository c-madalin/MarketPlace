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
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var firstNameEditText: TextInputEditText
    private lateinit var lastNameEditText: TextInputEditText
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
        return inflater.inflate(R.layout.fragment_register2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstNameLayout = view.findViewById(R.id.firstNameLayout)
        lastNameLayout = view.findViewById(R.id.lastNameLayout)
        firstNameEditText = view.findViewById(R.id.Reg2FirstName)
        lastNameEditText = view.findViewById(R.id.Reg2LastName)
        registerButton = view.findViewById(R.id.doRegister)
        errorTextView = view.findViewById(R.id.register2ErrorText)

        setupRegisterButton()
        setupBackButton()
    }

    private fun setupRegisterButton() {
        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()

            errorTextView.text = ""
            errorTextView.visibility = View.GONE
            firstNameLayout.error = null
            lastNameLayout.error = null

            when {
                firstName.isEmpty() -> {
                    firstNameLayout.error = "First name cannot be empty"
                    firstNameEditText.requestFocus()
                }
                firstName.length < 2 -> {
                    firstNameLayout.error = "First name must be at least 2 characters"
                    firstNameEditText.requestFocus()
                }
                !firstName.all { it.isLetter() || it.isWhitespace() } -> {
                    firstNameLayout.error = "First name can only contain letters and spaces"
                    firstNameEditText.requestFocus()
                }
                lastName.isEmpty() -> {
                    lastNameLayout.error = "Last name cannot be empty"
                    lastNameEditText.requestFocus()
                }
                lastName.length < 2 -> {
                    lastNameLayout.error = "Last name must be at least 2 characters"
                    lastNameEditText.requestFocus()
                }
                !lastName.all { it.isLetter() || it.isWhitespace() } -> {
                    lastNameLayout.error = "Last name can only contain letters and spaces"
                    lastNameEditText.requestFocus()
                }
                else -> {
                    attemptRegistration(firstName, lastName)
                }
            }
        }
    }

    private fun setupBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_register2Fragment_to_loginFragment)
        }
    }

    private fun attemptRegistration(firstName: String, lastName: String) {
        registerButton.isEnabled = false
        errorTextView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val userExists = withContext(Dispatchers.IO) {
                    DatabaseInstance.Access.userDAO()
                        .CheckIfUserExists(CurrentUser.Data.Email) != null
                }

                if (!userExists) {
                    CurrentUser.Data.FirstName = firstName
                    CurrentUser.Data.LastName = lastName

                    withContext(Dispatchers.IO) {
                        val userID = DatabaseInstance.Access.userDAO().InsertUser(CurrentUser.Data.toUserEntity())
                        CurrentUser.Data.UserID = userID
                    }

                    findNavController().setGraph(R.navigation.application_graph)
                } else {
                    showError("An account with this email already exists")
                    registerButton.isEnabled = true
                }
            } catch (e: Exception) {
                showError("An error occurred. Please try again.")
                registerButton.isEnabled = true
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