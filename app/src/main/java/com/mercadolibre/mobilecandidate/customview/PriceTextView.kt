package com.mercadolibre.mobilecandidate.customview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import com.mercadolibre.mobilecandidate.R
import kotlinx.android.synthetic.main.view_price_text_view.view.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class PriceTextView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private var integerValue = "0"
    private var decimalValue = "0"
    private var unit = ""
    private var textSize = 0
    private var textColor = 0

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_price_text_view, this)
        val attributes = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.PriceTextView
        )
        val totalValue = attributes.getString(R.styleable.PriceTextView_android_value) ?: "0.0"
        setValues(totalValue)
        unit = attributes.getString(R.styleable.PriceTextView_unit) ?: ""
        textSize = attributes.getDimensionPixelSize(R.styleable.PriceTextView_android_textSize, 0)
        textColor = attributes.getColor(
            R.styleable.PriceTextView_android_textColor,
            ContextCompat.getColor(context, R.color.text)
        )
        attributes.recycle()
    }

    private fun setValues(totalValue: String?) {
        try {
            val splatValues = totalValue?.replace(",", ".")?.split(".") ?: "0.0".split(".")
            integerValue = splatValues[0]
            if (splatValues.size == 1) return
            decimalValue = splatValues[1]
            if (decimalValue.length == 1) decimalValue += "0"
            if (decimalValue.length > 2) decimalValue = decimalValue.substring(0, 2)
        } catch (e: Exception) {
            integerValue = "0"
            decimalValue = "0"
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setIntegerValue()
        setDecimalValue()
        setUnit()
    }

    private fun setIntegerValue() {
        tv_integer_value_price.text = integerValue
        tv_integer_value_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        tv_integer_value_price.setTextColor(textColor)
        tv_integer_value_price.visibility = if (integerValue == "0") View.GONE else View.VISIBLE
    }

    private fun setDecimalValue() {
        tv_decimal_value_price.text = decimalValue
        tv_decimal_value_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * 0.75f)
        tv_decimal_value_price.setTextColor(textColor)
        tv_decimal_value_price.visibility = if (decimalValue == "0" || decimalValue == "00") View.GONE else View.VISIBLE
    }

    private fun setUnit() {
        tv_unit_price.text = unit
        tv_unit_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        tv_unit_price.setTextColor(textColor)
    }

    fun setValue(value: String?) {
        setValues(value)
        setIntegerValue()
        setDecimalValue()
    }

    fun setValue(value: Double?) {
        setValue(value?.toString() ?: "0.0")
    }

    fun setUnit(unit: String?) {
        this.unit = unit ?: ""
        setUnit()
    }
}