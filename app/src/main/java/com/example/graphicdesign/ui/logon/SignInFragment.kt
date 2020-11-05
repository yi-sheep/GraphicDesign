package com.example.graphicdesign.ui.logon

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.graphicdesign.R
import com.example.graphicdesign.logic.network.LogonService
import com.example.graphicdesign.logic.network.ServiceCreator
import kotlinx.android.synthetic.main.load_more_item.*
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 返回键
        signin_back_img.setOnClickListener {
            activity?.finish()
        }
        // 前往注册
        register_tv.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInFragment_to_registerFragment)
        }
        signin_tv.setOnClickListener {
            val account = account_edit.text.toString()
            val password = password_edit.text.toString()
            Log.d("my",account+password)
            viewModel.logon(account, password)
        }
        viewModel.loginStatusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                LoginStatus.LOGIN_SUCCESSFUL->{
                    activity?.finish()
                }
                LoginStatus.LOGIN_FAILED->{
                    Toast.makeText(context,context?.getString(R.string.sign_in_failed),Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
