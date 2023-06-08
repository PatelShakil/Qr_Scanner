package com.resoluteai.qrscanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.resoluteai.qrscanner.R
import com.resoluteai.qrscanner.databinding.SampleQrCodeBinding
import com.resoluteai.qrscanner.models.QRCode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QRListAdapter(val list: ArrayList<QRCode>) : RecyclerView.Adapter<QRListAdapter.QRListViewHolder>() {

    class QRListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = SampleQrCodeBinding.bind(itemView)
        fun setData(data : QRCode,position: Int){
            binding.indexNo.text = (position + 1) .toString()
            binding.qrValue.text = data.data
            binding.sTime.text = convertLongToDate(data.time)
        }
        private fun convertLongToDate(timestamp: Long): String {
            val date = Date(timestamp)
            val format = SimpleDateFormat("hh:mm dd-MMM", Locale.getDefault())
            return format.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QRListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_qr_code, parent, false)
        return QRListViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QRListViewHolder, position: Int) {
        holder.setData(list[position],position)
    }
}