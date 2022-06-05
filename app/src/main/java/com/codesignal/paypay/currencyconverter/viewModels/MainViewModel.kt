package com.codesignal.paypay.currencyconverter.viewModels

import androidx.lifecycle.ViewModel
import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel()  {

    // TODO: Will be implemented later
}