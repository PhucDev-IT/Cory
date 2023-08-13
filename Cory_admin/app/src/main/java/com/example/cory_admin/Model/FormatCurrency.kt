package com.example.cory_admin.Model

import java.text.NumberFormat
import java.util.Locale

class FormatCurrency {
  companion object{
      private val lc = Locale("vi","VN")
      val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(lc)
  }
}