import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.airbaggie.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InputBottomSheetDialogFragment : BottomSheetDialogFragment() {

    interface OnTextSubmitListener {
        fun onTextSubmit(inputText: String)
    }

    var onTextSubmitListener: OnTextSubmitListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputEditText: EditText = view.findViewById(R.id.inputEditText)
        val submitButton: Button = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val inputText = inputEditText.text.toString()
            if (inputText.isNotBlank()) {
                onTextSubmitListener?.onTextSubmit(inputText)
                dismiss()
            } else {
                Toast.makeText(context, "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
