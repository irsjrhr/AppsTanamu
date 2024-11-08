package com.bangkit.ch2_ps178_android.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.ch2_ps178_android.R
import com.bangkit.ch2_ps178_android.data.dataclass.MainAdapterRow
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView

class AdapterStatis(private val itemClickListener: (MainAdapterRow, CardView) -> Unit) :
    RecyclerView.Adapter<AdapterStatis.ViewHolder>() {

    var itemList : List<MainAdapterRow> = emptyList()


    // Inner class ViewHolder
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul: TextView = itemView.findViewById(R.id.judul)
        val img_story: ImageView = itemView.findViewById(R.id.gambar)

        init {
            itemView.setOnClickListener {
                // Tangani klik item di sini
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val data = itemList[position]
                    val card_story = itemView.findViewById<CardView>(R.id.card_story)
                    itemClickListener(data, card_story)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_adapter, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Menetapkan aturan UI, dan di terapkan pada object Holder di method onBindViewHolder
        val img: ShapeableImageView = itemView.findViewById(R.id.gambar)
        val judul: TextView = itemView.findViewById(R.id.judul)
        val jarak: TextView = itemView.findViewById(R.id.jarak)
        val harga: TextView = itemView.findViewById(R.id.harga)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Bind atau isi datanya ke elemen yang sudah ditetapkan
        //Datanya itu yang diterima dari yang ditetapkan
        //Holder itu nilainya yang ditetapkan di Viewholder

        //Mengambil nilai dari row data yang dikirimkan sesuai dengan data class yang ditetapkan
        val data = itemList[position]
        val img_data = "https://api.ayo.co.id/image/venue/165589181882791.image_cropper_97735C2B-26B5-421C-9E4A-7128A7112E3A-37880-000006F883353C2A_large.jpg"
        val judul_data = data.name
        val jarak : String = data.rating
        val harga : String = data.price

        val card_story = holder.itemView.findViewById<CardView>(R.id.card_story)

        Glide.with(holder.itemView)
            .load(img_data)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Atur ke DiskCacheStrategy.NONE jika Anda tidak ingin menyimpan cache
            .into(holder.img)
        holder.judul.text = judul_data
        holder.jarak.text = jarak
        holder.harga.text = harga


        // Dalam Activity atau Fragment
        // Set sudut (corner) secara dinamis
//        img.shapeAppearanceModel = roundedImageView.shapeAppearanceModel
//            .toBuilder()
//            .setAllCorners(CornerFamily.ROUNDED, 16.dpToPx())
//            .build()

        //Melakukan event setia item diklik
        // Menangani klik item

        //Menambahkan event ketika item pada recyleview di klik, fungsi saat event terjadi ditambahkan dia activity pada recycle view di inisiasi
        holder.itemView.setOnClickListener {
            itemClickListener(data, card_story) //Yang akan dibalikin untuk jadi argumen pada activity
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun submitList(newItems: List<MainAdapterRow>) {
        itemList = newItems
        notifyDataSetChanged()
    }

}