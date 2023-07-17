package com.developer.cory.Model

import java.text.NumberFormat
import java.util.Locale

class FormatCurrency {
  companion object{
      val lc = Locale("vi","VN")
      val numberFormat = NumberFormat.getCurrencyInstance(lc)
  }
}