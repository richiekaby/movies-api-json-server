package net.larntech.movies.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.larntech.movies.databinding.LayoutRateUsBinding

class RateUsBottomDialog(var ratingInterface: RatingInterface, var originalRate: String?) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutRateUsBinding

    interface RatingInterface {
        fun newRating(rateValue: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutRateUsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
//        binding.edRating.setText(originalRate)
    }

    private fun clickListener(){

        binding.btnNotNow.setOnClickListener {
            dismiss()
        }

        binding.btnSubmit.setOnClickListener {
            if(binding.edRating.text != null && binding.edRating.text.toString().isNotEmpty()) {
                val rating = Integer.parseInt(binding.edRating.text.toString())
                if(rating > 10){
                    showMessage(" Rating show be <= 10")
                }else {
                    doRating()
                }
            }else{
                showMessage("Rating required")
            }
        }

    }

    private fun showMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    private fun doRating(){
        ratingInterface.newRating(Integer.parseInt(binding.edRating.text.toString()))
        dismiss()
    }

}