package hr.bm.scanandsave.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.media.Image
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.TintTypedArray
import androidx.constraintlayout.widget.ConstraintLayout
import hr.bm.scanandsave.R
import hr.bm.scanandsave.ui.activities.main.RegexInputFilter
import java.lang.StringBuilder

class CustomInputLayout : LinearLayout, View.OnFocusChangeListener {

    companion object {
        private const val EDITTEXT_TAG = "editText"
        private const val ACTIONICON_TAG = "actionIcon"
    }

    private var mInputFrame : FrameLayout
    private var mInfoFrame : FrameLayout
    private var mHintEnabled : Boolean
    private var mHintAnimationEnabled : Boolean
    private var mDefaultTextColor : ColorStateList? = null
    private var mFocusedTextColor : ColorStateList? = null

    private var mErrorTextAppearance : Int
    private var mNoteTextAppearance : Int

    private var mErrorEnabled : Boolean = false
    private var mNoteEnabled : Boolean = false

    private var mEditText : EditText? = null
    private var mActionIcon : ImageView? = null

    private var mErrorView: TextView? = null

    private var mIsDecimalField: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        setAddStatesFromChildren(true)
        clipChildren = false
        clipToPadding = false
        orientation = VERTICAL

        mInputFrame = FrameLayout(context)
        mInputFrame.setAddStatesFromChildren(true)
        addView(mInputFrame)

        mInfoFrame = FrameLayout(context)
        mInfoFrame.setAddStatesFromChildren(true)
        addView(mInfoFrame)

        val tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attrs,
            com.google.android.material.R.styleable.TextInputLayout,
            defStyleAttr, com.google.android.material.R.style.Widget_Design_TextInputLayout)

        mHintEnabled = tintTypedArray.getBoolean(com.google.android.material.R.styleable.TextInputLayout_hintEnabled, true)
        mHintAnimationEnabled = tintTypedArray.getBoolean(com.google.android.material.R.styleable.TextInputLayout_hintAnimationEnabled, true)
        if (tintTypedArray.hasValue(com.google.android.material.R.styleable.TextInputLayout_android_textColorHint)) {
            mDefaultTextColor =
                tintTypedArray.getColorStateList(com.google.android.material.R.styleable.TextInputLayout_android_textColorHint)
            mFocusedTextColor = mDefaultTextColor
        }

        mErrorTextAppearance = tintTypedArray.getResourceId(com.google.android.material.R.styleable.TextInputLayout_errorTextAppearance, 0)
        val errorEnabled = tintTypedArray.getBoolean(com.google.android.material.R.styleable.TextInputLayout_errorEnabled, false)
        mNoteTextAppearance = tintTypedArray.getResourceId(com.google.android.material.R.styleable.TextInputLayout_helperTextTextAppearance, 0)
        val noteEnabled = tintTypedArray.getBoolean(com.google.android.material.R.styleable.TextInputLayout_helperTextEnabled, false)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomInputLayout)
        if (a.hasValue(R.styleable.CustomInputLayout_decimalField))
            mIsDecimalField = a.getBoolean(R.styleable.CustomInputLayout_decimalField, false)

        tintTypedArray.recycle()
        a.recycle()

        setErrorView(errorEnabled)
        setNoteView(noteEnabled)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is ConstraintLayout) {
            setEditText(child.findViewWithTag(EDITTEXT_TAG))
            setActionIcon(child.findViewWithTag(ACTIONICON_TAG))
            mInputFrame.addView(child)
        } else if (child is EditText && mInputFrame.childCount == 0) {
            setEditText(child)
            mInputFrame.addView(child)
        } else {
            super.addView(child, index, params)
        }
    }

    fun setRequired() {
        if (mEditText != null) {
            val builder = StringBuilder()
            builder.append(mEditText!!.hint)
            builder.append("*")
            mEditText!!.hint = builder.toString()
        }
    }

    fun showError(error: String) {
        if (error.isEmpty())
            hideError()

        if (mErrorEnabled) {
            mErrorView?.visibility = View.VISIBLE
            mErrorView?.text = error
        }
    }

    private fun hideError() {
        if (mErrorEnabled) {
            mErrorView?.visibility = View.GONE
        }
    }

    private fun setEditText(editText : EditText) {
        mEditText = editText
        mEditText!!.onFocusChangeListener = this

        if (mIsDecimalField) {
            mEditText!!.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            mEditText!!.filters = arrayOf(RegexInputFilter("^([0-9]+([.,])?[0-9]*)".toRegex()))
        }
    }

    private fun setActionIcon(actionIcon : ImageView) {
        mActionIcon = actionIcon
    }

    private fun setErrorView(errorEnabled: Boolean) {
        if (mErrorEnabled == errorEnabled)
            return

        mErrorView = TextView(context)
        mErrorView!!.setTextAppearance(mErrorTextAppearance)
        mErrorView!!.visibility = View.GONE
        mInfoFrame.addView(mErrorView)

        mErrorEnabled = errorEnabled
    }

    private fun setNoteView(noteEnabled: Boolean) {
        if (mNoteEnabled == noteEnabled)
            return

        val view = TextView(context)
        view.setTextAppearance(mNoteTextAppearance)
        mInfoFrame.addView(view)

        mNoteEnabled = noteEnabled
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            mEditText?.elevation = 10F
            mEditText?.translationZ = -5F
        } else {
            mEditText?.elevation = 0F
            mEditText?.translationZ = 0F
        }
    }
}